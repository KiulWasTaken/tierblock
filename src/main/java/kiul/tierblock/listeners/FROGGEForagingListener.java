package kiul.tierblock.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillManager;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.user.skill.impl.ForagingSkill;
import kiul.tierblock.utils.enums.WoodType;

// NAG ME TO FIX THIS BEFORE ACTUALLY IMPLEMENTING
public class FROGGEForagingListener implements Listener {

    public FROGGEForagingListener() {
        SkillManager.getInstance().registerSkill(new ForagingSkill());
    }

    @EventHandler
    public void blockBreakListener(BlockBreakEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());
        Block block = event.getBlock();
        WoodType type = WoodType.fromMaterial(block.getType());
        
		if(type == null) return; // not wood, stopping here.
        if(!user.hasIsland()) return;
        
		// CHECK IF PLAYER-PLACED!!!
		
        event.setDropItems(false); // just incase you can't mine it
        int blockLevelRequirement = Main.getGlobalLevelRequirement("foraging." + type.toString().toLowerCase());
        if(
            // We check if the broken block has a level requirement:
            blockLevelRequirement != 0
            // and also check if the player doesn't have the required level...
            && user.getGlobalLevel() < blockLevelRequirement)
        {
            user.sendActionBar("&cYou need island level &e" + blockLevelRequirement + " &cto collect this!");
            return;
        }

        WoodType requiredType = WoodType.values()[type.ordinal() - 1];
		int levelUpRequirement = (int)Main.getInstance().getConfig().getDouble("foraging." + requiredType + ".level_up");
        int collected = (int)user.getExperience(SkillType.FORAGING, type.isNether);
        // if isn't oak & hasn't reached the wood type, we tell him to fuck off.
        if(!(type == WoodType.OAK) && collected < levelUpRequirement) {

            user.sendActionBar(
                String.format("&cYou need to forage &e%s &c%s to be able to mine %s!",
                (levelUpRequirement - collected), 
                    requiredType.formatName(),
                    type.formatName()
                )
            );
            
            return;
        }

        event.setDropItems(true);

        
        
        user.sendActionBar(
            String.format(
                "&eGlobal: &2+&a%sxp &8| &eForaging &2+&a%sxp &8(&b%s&8)",
                user.addGlobalExperience(type.xpReward),
                user.addExperience(SkillType.FORAGING, 1.0, type.isNether),
                type.toString()
            )
        );
    }

}
