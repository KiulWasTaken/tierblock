package kiul.tierblock.listeners;

import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.utils.enums.CropType;

// literally copied my ForagingListener and replaced everything related to wood to crops except for placing plants
public class FarmingListener implements Listener{

    private final Map<CropType, Double> FARMING_REWARDS = Map.of(
        CropType.WHEAT, Main.getXPReward("farming.wheat"),
        CropType.BEETROOT, Main.getXPReward("farming.beetroot"),
        CropType.CARROT, Main.getXPReward("farming.carrot"),
        CropType.POTATO, Main.getXPReward("farming.potato"),
        CropType.SUGAR_CANE, Main.getXPReward("farming.sugar_cane"),
        CropType.MELON, Main.getXPReward("farming.melon"),
        CropType.PUMPKIN, Main.getXPReward("farming.pumpkin"),
        CropType.NETHER_WART, Main.getXPReward("farming.nether_wart"),
        CropType.CHORUS_FRUIT, Main.getXPReward("farming.chorus_fruit")
    ); 
    
    @EventHandler
    public void cropBreakListener(BlockBreakEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());
        Block block = event.getBlock();
        CropType type = CropType.fromMaterial(block.getType());
        
		if(type == null) return; // not a crop, stopping here.
		
		Ageable crop = (Ageable) block.getState().getBlockData();
		
		if(crop.getAge() < crop.getMaximumAge()) return; // plant not fully grown

        event.setDropItems(false); // just incase you can't mine it
        int blockLevelRequirement = Main.getGlobalLevelRequirement("farming." + type.toString().toLowerCase());

        if(
            // We check if the broken block has a level requirement:
            blockLevelRequirement != 0
            // and also check if the player doesn't have the required level...
            && user.getLevel(SkillType.GLOBAL) < blockLevelRequirement)
        {
            user.sendActionBar("&cYou need to have &elevel " + blockLevelRequirement + " or above&c to break this!");
            return;
        }

        /**
         * is the type of crop you need to break before you break this type...
         * if you were breaking beetroot (and not supposed to), this will be equal to WHEAT
         */
        CropType requiredType = CropType.fromInt(type.toInt()-1);

		int levelUpRequirement = Main.getInstance().getConfig().getInt("farming." + requiredType + ".level_up");
        
        // if isn't wheat & hasn't reached the crop type, we tell him to fuck off.
        if(!(type == CropType.WHEAT) && user.getCollectedCrops(requiredType) < levelUpRequirement) {

            // C/C++ like-ish enums. for easier work...
            CropType nextCropType = CropType.fromInt(type.toInt()+1); // CropType + 1

            user.sendActionBar(
                String.format("&cYou need to farm &e%s &c%s to be able to mine %s!",
                    (levelUpRequirement - user.getCollectedCrops(requiredType)), 
                    requiredType.formatName(),
                    nextCropType.formatName()
                )
            );
            
            return;
        }

        event.setDropItems(true);

        double reward = FARMING_REWARDS.get(type);

        user.sendActionBar(
            String.format(
                "&eGlobal: &2+&a%sxp &8| &eFarming &2+&a1xp &8(&b%s&8)",
                reward,
                type.formatName()
            )
        );

        user.addFarming(type, 1);
        user.addExperience(SkillType.FARMING, 1.0);
        user.addExperience(SkillType.GLOBAL, reward);
    }

}
