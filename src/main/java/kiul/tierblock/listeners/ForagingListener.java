package kiul.tierblock.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillManager;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.user.skill.impl.ForagingSkill;
import kiul.tierblock.utils.enums.WoodType;

public class ForagingListener implements Listener {

    public ForagingListener() {
        SkillManager.getInstance().registerSkill(new ForagingSkill());
    }

    @EventHandler
    public void blockBreakListener(BlockBreakEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());
        Block block = event.getBlock();
        WoodType type = WoodType.fromMaterial(block.getType());
        
        if(!user.hasIsland()) return;
		if(type == null) return; // not wood, stopping here.
        if(event.getBlock().hasMetadata("pp")) return; // player placed
		
        if(type.isNether && !user.getStats().getBoolean("foraging.nether.unlocked")) {
			user.sendActionBar("&cYou need to &emax foraging &cto unlock nether content!");
            return;
        }

        event.setDropItems(false); // just incase you can't mine it
        int blockLevelRequirement = Main.getGlobalLevelRequirement("foraging." + type.toString().toLowerCase());
        if(
            // We check if the broken block has a level requirement:
            blockLevelRequirement != 0
            // and also check if the player doesn't have the required level...
            && user.getGlobalLevel() < blockLevelRequirement)
        {
            user.sendActionBar("&cYou need &eisland level " + blockLevelRequirement + " &cto collect this!");
            return;
        }

        if(user.getLevel(SkillType.FORAGING, type.isNether) < type.levelRequirement) {
            // show progress, if the block is unlockable in the next level (In other words: block level = user level + 1)
            // if not tell them the level requirement:
            boolean progress = (type.levelRequirement - 1) == user.getLevel(SkillType.FORAGING, type.isNether);
            user.sendActionBar((progress) ? String.format(
                "&cYou need to forage &e%s &cmore &e%s&c to collect this!", 
                (int)(type.levelUp - user.getExperience(SkillType.FORAGING, type.isNether)),
                WoodType.values()[type.ordinal() - 1].formatName()
            ) : "&cYou need to have &eforaging level " + type.levelRequirement + " &cto forage this!");
            return;
        }

        event.setDropItems(true);
        
        double xpReward = 0.0;

        if(type.levelRequirement == user.getLevel(SkillType.FORAGING, type.isNether)
            || (type.levelRequirement == 0 && user.getLevel(SkillType.FORAGING, type.isNether) == 1)) {
            xpReward = user.addExperience(SkillType.FORAGING, 1.0, type.isNether);
        }
        
        user.sendActionBar(
            new StringBuilder(String.format(
                "&eIsland level: &2+&a%sxp " + (user.getBoosterMultiplier() > 1.0 ? "(x" + (int)user.getBoosterMultiplier() + " booster) " : ""),
                Main.DECIMAL_FORMAT.format(user.addGlobalExperience(type.xpReward))
            )).append(
                (xpReward <= 0 ? "" : String.format("&8| &eForaging: &2+&a%sxp &8(&b%s&8)", Main.DECIMAL_FORMAT.format(xpReward), type.formatName()))
            ).toString()
        );
    }

    @EventHandler
	public void blockPlace(BlockPlaceEvent event) {
		WoodType type = WoodType.fromMaterial(event.getBlock().getType());
		
		if(type == null) return; // not wood :(.
		// if wood, do:
		// i hate this; it's not persistent, but I assume the server is gonna barely restart, and not a lot of people would really care/notice. it feels wrong, ok?
		event.getBlock().setMetadata("pp", new FixedMetadataValue(Main.getInstance(), "f"));
	}

}
