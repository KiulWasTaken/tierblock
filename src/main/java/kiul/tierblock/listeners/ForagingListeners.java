package kiul.tierblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import kiul.tierblock.Main;

import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillManager;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.user.skill.impl.ForagingSkill;
import kiul.tierblock.utils.enums.WoodType;

// if making spaghetti code is considered a war crime, this class would be the second biggest war crime ever committed 
// grrr
public class ForagingListeners implements Listener {

    // pat i can't be bothered properly implementing user data
    // so please, remove gamelogic & all of its files, and use the User class (which provides easier access)
    // i'll teach you how to use it if you want! (or just look into the class)

    public ForagingListeners() {
        SkillManager.getInstance().registerSkill(new ForagingSkill());
    }

    Plugin plugin = (Plugin) Main.getPlugin(Main.class);

    double getLevelUpRequirement(String path) {
        return Main.getInstance().getConfig().contains(path + ".level_up") ?
            Main.getInstance().getConfig().getDouble(path + ".level_up") : 0;
    }

    @EventHandler
    public void WoodBroken (BlockBreakEvent e) {
        User user = UserManager.getInstance().getUser(e.getPlayer());
        WoodType type = WoodType.fromMaterial(e.getBlock().getType());
		
		// removed rewards, done automatically by WoodType.xpReward...

        // player-placed...
        if(!user.hasIsland()) return;
        if (!e.getBlock().hasMetadata("pp")) {

            switch (e.getBlock().getType()) {
                case OAK_LOG:
                    user.addExperience(SkillType.FORAGING, 1.0, false);
                    break;
                case BIRCH_LOG:
                    if (user.getExperience(SkillType.FORAGING, false) >= getLevelUpRequirement("foraging.oak")) {
                        user.addExperience(SkillType.FORAGING, 1.0, false);
                    } else {
                        e.setDropItems(false);
                        user.sendActionBar(
                                "&cYou have to mine &e" + (int)(getLevelUpRequirement("foraging.oak") - user.getExperience(SkillType.FORAGING, false)) + " &cmore oak to collect this!");
                    }
                    break;
                case ACACIA_LOG:
                    if (user.getExperience(SkillType.FORAGING, false) >= getLevelUpRequirement("foraging.birch")) {
                        user.addExperience(SkillType.FORAGING, 1.0, false);
                    } else {
                        e.setDropItems(false);
                        user.sendActionBar(
                                "&cYou have to mine &e" + (int)(getLevelUpRequirement("foraging.birch") - user.getExperience(SkillType.FORAGING, false)) + " &cmore birch to collect this!");
                    }
                    break;
                case DARK_OAK_LOG:
                    if (user.getExperience(SkillType.FORAGING, false) >= getLevelUpRequirement("foraging.acacia")) {
                        user.addExperience(SkillType.FORAGING, 1.0, false);
                    } else {
                        e.setDropItems(false);
                        user.sendActionBar(
                                "&cYou have to mine &e" + (int)(getLevelUpRequirement("foraging.acacia") - user.getExperience(SkillType.FORAGING, false)) + " &cmore acacia to collect this!");
                    }
                    break;
                case SPRUCE_LOG:
                    if (user.getExperience(SkillType.FORAGING, false) >= getLevelUpRequirement("foraging.dark_oak")) {
                        user.addExperience(SkillType.FORAGING, 1.0, false);
                    } else {
                        e.setDropItems(false);
                        user.sendActionBar(
                                "&cYou have to mine &e" + (int)(getLevelUpRequirement("foraging.dark_oak") - user.getExperience(SkillType.FORAGING, false)) + " &cmore dark oak to collect this!");
                    }
                    break;
                case JUNGLE_LOG:
                    if (user.getExperience(SkillType.FORAGING, false) >= getLevelUpRequirement("foraging.spruce")) {
                        user.addExperience(SkillType.FORAGING, 1.0, false);
                    } else {
                        e.setDropItems(false);
                        user.sendActionBar(
                                "&cYou have to mine &e" + (int)(getLevelUpRequirement("foraging.spruce") - user.getExperience(SkillType.FORAGING, false)) + " &cmore spruce to collect this!");
                    }
                    break;
                case CRIMSON_STEM:
                    if(!user.getStats().getBoolean("foraging.nether.unlocked")) {
                        user.sendActionBar("&cYou need to &emax foraging &cto unlock nether content!");
                        return;
                    }
                    if (user.getGlobalLevel() >= 40) {
                        user.addExperience(SkillType.FORAGING, 1.0, true);
                    } else {
                        e.setDropItems(false);
                        user.sendActionBar("&cYou need island level &e40 &cto collect this!");
                    }
                    break;
                case WARPED_STEM:
                    if(!user.getStats().getBoolean("foraging.nether.unlocked")) {
                        user.sendActionBar("&cYou need to &emax foraging &cto unlock nether content!");
                        return;
                    }
                    if (user.getExperience(SkillType.FORAGING, true) >= getLevelUpRequirement("foraging.crimson")) {
                        user.addExperience(SkillType.FORAGING, 1.0, true);
                    } else {
                        e.setDropItems(false);
                        user.sendActionBar(
                                "&cYou have to mine &e" + (int)(getLevelUpRequirement("foraging.crimson") - user.getExperience(SkillType.FORAGING, false)) + " &cmore crimson to collect this!");
                    }
                    break;
                default:
					break;
            }
			
			
			// actionbar message, thank me later, pat.
			if(type != null) {
                // user.setLastSkill(SkillType.FORAGING);
				user.sendActionBar(
					String.format(
						"&eIsland Level: &2+&a%sxp " + (user.getBoosterMultiplier() > 1.0 ? "(x" + (int)user.getBoosterMultiplier() + " booster) " : "") + "&8| &eForaging &2+&a1.0xp &8(&b%s&8)",
						Main.DECIMAL_FORMAT.format(user.addGlobalExperience(type.xpReward)),
						type.formatName()
					)
				);
			}

            // LEVEL-UPS ARE DONE AUTOMATICALLY VIA USER CLASS!

        }

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
