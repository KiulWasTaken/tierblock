package kiul.tierblock.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
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
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.RanksManager;

public class ForagingListener implements Listener {

    public ForagingListener() {
        SkillManager.getInstance().registerSkill(new ForagingSkill());
    }

    @EventHandler
    public void blockBreakListener(BlockBreakEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());
        
        if(!user.hasIsland()) return;
        
        Block block = event.getBlock();
        WoodType type = WoodType.fromMaterial(block.getType());

        if(type == null)
            return; // not wood, stopping here.
        if(user.getIslandAtPosition().getRank(user.getUUID()) < RanksManager.COOP_RANK)
            return;
        if(user.getPlayer().getGameMode() != GameMode.SURVIVAL)
            return;
        if(event.getBlock().hasMetadata("pp")) {
            block.removeMetadata("pp", Main.getInstance());
            return; // player placed
        }
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
            WoodType previousType = WoodType.values()[type.ordinal() - 1];
            user.sendActionBar((progress) ? String.format(
                "&cYou need to forage &e%s &cmore &e%s&c to collect this!", 
                (int)(previousType.levelUp - user.getExperience(SkillType.FORAGING, type.isNether)),
                previousType.formatName()
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
                "&eIsland: &2+&a%sxp " + (user.getBoosterMultiplier() > 1.0 ? "(x" + (int)user.getBoosterMultiplier() + " booster) " : ""),
                Main.DECIMAL_FORMAT.format(user.addGlobalExperience(type.xpReward))
            )).append(
                (xpReward <= 0.0 ? "" : String.format("&8| &eForaging: &2+&a%sxp &8(&b%s&8)", Main.DECIMAL_FORMAT.format(xpReward), type.formatName()))
            ).toString()
        );
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());
        WoodType type = WoodType.fromMaterial(event.getBlock().getType());
        String blockTypeString = event.getBlock().getType().toString().toLowerCase();

		if(user.getIslandAtPosition().getRank(user.getUUID()) < RanksManager.COOP_RANK)
            return;
		
        if(!blockTypeString.startsWith("potted") && blockTypeString.endsWith("sapling")) {
            if(event.getBlock().getType() == Material.CHERRY_SAPLING
                || event.getBlock().getType() == Material.BAMBOO_SAPLING) return;
            
            if(user.getIslandAtPosition().getRank(user.getUUID()) < RanksManager.MEMBER_RANK) {
				event.setCancelled(true);
                user.sendMessage("&cYou must be an island member to place this!");
                return;
            } else {
                WoodType typeFromSapling = WoodType.fromSapling(event.getBlock().getType());
                if(user.getLevel(SkillType.FORAGING, typeFromSapling.isNether) < typeFromSapling.levelRequirement) {
					event.setCancelled(true);
                    user.sendMessage("&cYou need foraging &elevel " + typeFromSapling.levelRequirement + " &cto place this.");
                    return;
                }
            }
        }

        if(type == null) return; // not wood :(.

        if(user.isWithinAnyIsland()) {
            Island island = user.getIslandAtPosition();
            if(island.getRank(user.getUUID()) < RanksManager.MEMBER_RANK) {
                user.sendMessage("&cYou must be an island member to place this!");
                return;
            }
        }

        // if wood, do:
        // i hate this; it's not persistent, but I assume the server is gonna barely restart, and not a lot of people would really care/notice. it feels wrong, ok?
        event.getBlock().setMetadata("pp", new FixedMetadataValue(Main.getInstance(), "f"));
    }

}
