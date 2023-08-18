package kiul.tierblock.listeners;

import org.bukkit.GameMode;
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
import kiul.tierblock.user.skill.impl.MiningSkill;
import kiul.tierblock.utils.enums.MineableType;

public class MiningListener implements Listener {

    public MiningListener() {
        SkillManager.getInstance().registerSkill(new MiningSkill());
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        MineableType type = MineableType.fromMaterial(event.getBlock().getType());

        if (type == null)
            return;
        
        if (event.getBlock().hasMetadata("pp"))
            return; // pp as in player-placed, no other meaning.... trust me

        User user = UserManager.getInstance().getUser(event.getPlayer());

        event.setDropItems(false);

        if (user.getPlayer().getGameMode() != GameMode.SURVIVAL)
            return;

        if (type.isNether && !user.getStats().getBoolean("mining.nether.unlocked")) {
            user.sendActionBar("&cYou need to &emax mining &cto unlock nether content!");
            return;
        }

        int globalReq = Main.getGlobalLevelRequirement("mining." + type.label);

        if (user.getGlobalLevel() < globalReq) {
            user.sendActionBar("&cYou need &eisland level " + globalReq + " &cto collect this!");
            return;
        }

        if (user.getLevel(SkillType.MINING, type.isNether) < type.levelRequirement) {
            // show progress, if the block is unlockable in the next level (In other words:
            // block level = user level + 1)
            // if not tell them the level requirement:
            boolean progress = (type.levelRequirement - 1) == user.getLevel(SkillType.MINING, type.isNether);
            if (progress) {
                MineableType previousType = MineableType.values()[type.ordinal() - 1];
                user.sendActionBar(
                    String.format(
                        "&cYou need to break &e%s &cmore &e%s&c to collect this!",
                        (int) (previousType.levelUp - user.getExperience(SkillType.MINING, type.isNether)),
                        previousType.formatName())
                    );
                return;
            }

            user.sendActionBar("&cYou need to have &emining level " + type.levelRequirement + " &cto farm this!");
            return;
        }

        event.setDropItems(true);

        double xpReward = 0.0;

        if (type.levelRequirement == user.getLevel(SkillType.MINING, type.isNether)
            || (type.levelRequirement == 0 && user.getLevel(SkillType.MINING, type.isNether) == 1)) {
            xpReward = user.addExperience(SkillType.MINING, 1.0, type.isNether);
        }

        user.sendActionBar(
            new StringBuilder(
                String.format(
                 "&eIsland: &2+&a%sxp " + (user.getBoosterMultiplier() > 1.0
                    ? "(x" + (int) user.getBoosterMultiplier() + " booster) " : ""),
                    Main.DECIMAL_FORMAT.format(user.addGlobalExperience(type.xpReward)))
                )
            .append((xpReward <= 0.0 ? ""
                : String.format("&8| &eMining: &2+&a%sxp &8(&b%s&8)",
                Main.DECIMAL_FORMAT.format(xpReward), type.formatName()))).toString()
            );
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent event) {
        MineableType type = MineableType.fromMaterial(event.getBlock().getType());

        if (type == null)
            return;
            
        // block flagged, at least, until a restart is issued. (metadata not persistent)
        event.getBlock().setMetadata("pp", new FixedMetadataValue(Main.getInstance(), "f"));
    }
}
