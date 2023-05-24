package kiul.tierblock.listeners;

import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.utils.enums.WoodType;

// only the coolest
public class FROGGEForagingListener implements Listener {

    // could just do it in a for loop, but it doesn't affect performance as it's one instance anyway
    private final Map<WoodType, Double> FORAGING_REWARD_BLOCKS = Map.of( 
        WoodType.OAK, Main.getXPReward("foraging.oak"), 
        WoodType.BIRCH, Main.getXPReward("foraging.birch"),
        WoodType.ACACIA, Main.getXPReward("foraging.acacia"),
        WoodType.DARK_OAK, Main.getXPReward("foraging.dark_oak"),
        WoodType.SPRUCE, Main.getXPReward("foraging.spruce"),
        WoodType.JUNGLE, Main.getXPReward("foraging.jungle"), 
        WoodType.CRIMSON, Main.getXPReward("foraging.crimson"),
        WoodType.WARPED, Main.getXPReward("foraging.warped")
    );

    @EventHandler
    public void blockBreakListener(BlockBreakEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());
        Block block = event.getBlock();
        WoodType type = WoodType.fromMaterial(block.getType());
        
		if(type == null) return; // not wood, stopping here.
        if(block.hasMetadata("used")) return; // player-placed block

        event.setDropItems(false); // just incase you can't mine it
        int blockLevelRequirement = Main.getGlobalLevelRequirement("foraging." + type.toString().toLowerCase());
        if(
            // We check if the broken block has a level requirement:
            blockLevelRequirement != 0
            // and also check if the player doesn't have the required level...
            && user.getLevel(SkillType.GLOBAL) < blockLevelRequirement)
        {
            user.sendActionBar("&cYou need to have &elevel " + blockLevelRequirement + " or above&c to break this!");
            return;
        }

        WoodType requiredType = WoodType.fromInt(type.toInt()-1);
		int levelUpRequirement = Main.getInstance().getConfig().getInt("foraging." + requiredType + ".level_up");
        
        // if isn't oak & hasn't reached the wood type, we tell him to fuck off.
        if(!(type == WoodType.OAK) && user.getCollectedWood(requiredType) < levelUpRequirement) {

            // C/C++ like-ish enums. for easier work...
            WoodType nextWoodType = WoodType.fromInt(type.toInt()+1); // WoodType + 1

            user.sendActionBar(
                String.format("&cYou need to forage &e%s &c%s to be able to mine %s!",
                    (levelUpRequirement - user.getCollectedWood(requiredType)), 
                    requiredType.formatName(),
                    nextWoodType.toString()
                )
            );
            
            return;
        }

        event.setDropItems(true);

        double reward = FORAGING_REWARD_BLOCKS.get(type);
        user.sendActionBar(
            String.format(
                "&eGlobal: &2+&a%sxp &8| &eForaging &2+&a1xp &8(&b%s&8)",
                reward,
                type.toString()
            )
        );

        user.addForaging(WoodType.fromMaterial(block.getType()), 1);
        user.addExperience(SkillType.GLOBAL, reward);
    }

    @EventHandler
    public void blockPlaceListener(BlockPlaceEvent event) {
        Block block = event.getBlock();

        if(WoodType.fromMaterial(block.getType()) == null) return; // fromMaterial() returns null if not wood., we stop here.

        block.setMetadata("used", new FixedMetadataValue(Main.getInstance(), "state"));
    }
    
}
