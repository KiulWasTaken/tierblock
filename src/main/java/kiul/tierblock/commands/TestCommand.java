package kiul.tierblock.commands;

import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import fr.mrmicky.fastboard.FastBoard;
import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.utils.enums.SkillCollectible;
import net.md_5.bungee.api.ChatColor;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.metadata.MetaDataValue;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.RanksManager;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if ("logme".equalsIgnoreCase(args[0])) {
            User user = (sender instanceof ConsoleCommandSender) ? new User(null)
                    : UserManager.getInstance().getUser((Player) sender);
            File file = (user.getPlayer() == null) ? new File("")
                    : new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/userlogs/" + user.getUUID()
                            + ".userlog");

            
            if (args.length > 1) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(args[1]));

                if (!offlinePlayer.hasPlayedBefore()) {
                    sender.sendMessage("Couldn't find the player with the specified UUID!");
                    return false;
                }

                user = new User(offlinePlayer);

                file = new File(Main.getInstance().getDataFolder().getAbsolutePath() + "/userlogs/" + user.getUUID()
                        + ".userlog");
            }

            if(user.getPlayer() == null) return false;
            if(!user.isOp()) {
                user.sendMessage("&cInsufficient permissions!");
                return false;
            }

            file.mkdirs();

            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                FileWriter writer = new FileWriter(file);

                writer.write("Name: " + user.getName());
                writer.write("\nBooster (At file creation): " + (user.getBooster() > System.currentTimeMillis()));
                writer.write("\nBooster Multiplier: " + user.getBoosterMultiplier());
                writer.write("\nFlight: " + user.isAllowedToFly());

                // Island section

                writer.write("\n\n================== ISLAND ==================\n");
                writer.write("\nHas island: " + user.hasIsland() + ", Is island owner: "
                        + (user.getIslandRank() == RanksManager.OWNER_RANK));

                if (user.hasIsland()) {
                    writer.write("\nGlobal level: " + user.getGlobalLevel());
                    writer.write("\nGlobal experience: " + user.getGlobalExperience());
                    writer.write("\nPillager spawn chance: "
                            + (user.getIsland().getMetaData("pillagerSpawnChance").get().asDouble() * 100) + "%");
                    writer.write(
                            "\nRaid captain spawn: " + user.getIsland().getMetaData("raidCaptain").get().asBoolean());
                    // writer.write("Island has beehive: " +
                    // user.getIsland().getMetaData("hasBeehive").get().asBoolean());
                }

                writer.write("\n\n================== ISLAND ==================");

                for (SkillType skillType : SkillType.values()) {
                    writer.write("\n\n================== " + skillType.toString() + " ==================\n");
                    if (skillType == SkillType.COMBAT) {
                        writer.write("\nMonsters killed: " + user.getMonsterKills());
                        writer.write("\n\n================== " + skillType.toString() + " ==================");
                        continue;
                    }
                    boolean nether = (skillType != SkillType.FISHING);
                    writer.write("\n\nLevel: " + user.getLevel(skillType, false));
                    writer.write("\nExperience: " + user.getExperience(skillType, false));
                    if (!nether) {
                        writer.write("\nSea creature chance: " + user.getSeaCreatureChance());
                        writer.write("\nSea creature kills: " + user.getSeaCreatureKills());
                    } else {
                        writer.write("\nNether level: " + user.getLevel(skillType, true));
                        writer.write("\nNether experience: " + user.getExperience(skillType, true));
                    }
                    writer.write("\n\n================== " + skillType.toString() + " ==================");
                }

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (sender instanceof ConsoleCommandSender)
            return false;

        User user = UserManager.getInstance().getUser((Player) sender);

        if (args.length < 1) {
            user.sendMessage("&cInsufficient arguments");
            return false;
        }

        if ("aboutme".equalsIgnoreCase(args[0])) {
            user.sendMessage("&eGay: " + (user.getPlayer().getName().equals("themightyfrogge") ? "&cNo" : "&aYes"));

            user.sendMessage("has_island: " + user.hasIsland());
            user.sendMessage("&8- &eIs Island Owner: &f"
                    + (user.hasIsland() ? (user.getIslandRank() == RanksManager.OWNER_RANK) : "&chas_island is false"));

            user.sendMessage("booster: " + (user.getBooster() > System.currentTimeMillis()));
            user.sendMessage("booster_multiplier: " + user.getBoosterMultiplier());
            user.sendMessage("sc_chance: " + user.getSeaCreatureChance());
            user.sendMessage("flight: " + user.isAllowedToFly());
        }

        if (!user.isOp()) {
            user.sendMessage("&cInsufficient permissions");
            return false;
        }

        if("isthispp".equalsIgnoreCase(args[0])) {
            Block block = user.getPlayer().getTargetBlockExact(5);
            
            boolean isPp = block.hasMetadata("pp");

            user.sendMessage("&dThis block is" + (isPp ? "" : " &cnot&d") + " pp!");
            return true;
        }

        if("rewardtable".equalsIgnoreCase(args[0])) {
            if(args.length < 2) {
                user.sendMessage("&cIncorrect usage, use: /test rewardtable help");
                return false;
            }

            if(args[1].equalsIgnoreCase("help")) {
                for(SkillType skillType : SkillType.values()) {
                    if(skillType != SkillType.COMBAT && skillType != SkillType.FISHING) {
                        user.sendMessage("&b&l" + skillType.toString() + " &eordinal: " + skillType.ordinal());
                    }
                }
                return true;
            }

            int ordinal = Integer.parseInt(args[1]);
            SkillType skillType = SkillType.values()[ordinal];
            
            
            
            SkillCollectible[] skillCollectibles = SkillCollectible.getSkillCollectibleType(skillType);
            if(skillType == null || (skillType == SkillType.COMBAT || skillType == SkillType.FISHING)) {
                user.sendMessage("&cThis skill ordinal is invalid, or the skill has no rewardtable for breaking blocks");
                return false;
            }
            user.sendMessage("&3level_up & rewards for &b&l" + skillType.toString() + "&3:");
            for(SkillCollectible collectible : skillCollectibles) {
                user.sendMessage("&e&l" + collectible.label() + ", &3level_up: &b" + collectible.levelUp() + "&3, xp_reward: &b" + collectible.xpReward());
            }
        }

        if("whereami".equalsIgnoreCase(args[0])) {
            Island currentIsland = user.getIslandAtPosition();
            
            if(currentIsland == null) {
                user.sendMessage("You're not in an island!");
                return false;
            }

            String rank = BentoBox.getInstance().getRanksManager().getRank(currentIsland.getRank(user.getUUID()));
            int level = currentIsland.getMetaData("level").get().asInt();
            double xp = currentIsland.getMetaData("xp").get().asDouble();
            
            user.sendMessage("&aYou're in island: &b&l" + currentIsland.getName());
            user.sendMessage("&3Your rank in the island: &b&l" + rank);
            user.sendMessage("&3Island owner: &b&l" + Bukkit.getOfflinePlayer(currentIsland.getOwner()).getName());
            user.sendMessage("&3Global level&8/&3xp: &b" + level + " &8/&b " + xp);
        }

        if ("setmeupchief".equalsIgnoreCase(args[0])) {
            user.setSeaCreatureChance(0.50);
            user.sendMessage("&esc_chance &ais now &350%");
            return true;
        }

        if ("scc".equalsIgnoreCase(args[0])) {
            if (!user.hasIsland())
                return false;
            user.getIslandMembers().forEach(member -> {
                user.sendMessage(member.getName() + " (sc_chance): " + member.getSeaCreatureChance() * 100 + "%");
            });
            return true;
        }

        if ("metadata".equalsIgnoreCase(args[0])) {
            if (user.getIslandAtPosition() == null) {
                user.sendMessage("&cYou aren't in any island!");
                return false;
            }

            user.sendMessage("Island's meta-data:");
            Island island = user.getIslandAtPosition();

            StringBuilder keys = new StringBuilder().append("[");

            List<String> list = List.copyOf(island.getMetaData().get().keySet());

            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    keys.append(list.get(i) + "]");
                } else {
                    keys.append(list.get(i) + ", ");
                }
            }

            user.sendMessage(keys.toString());
            user.sendMessage(" &8- &fhasBeeHive &e-> &f" + island.getMetaData("hasBeeHive").get().asBoolean());
            user.sendMessage(" &8- &fpillagerSpawnChance &e-> &f"
                    + (island.getMetaData("pillagerSpawnChance").get().asDouble() * 100) + "%");
            user.sendMessage(" &8- &fraidCaptain &e-> &f" + island.getMetaData("raidCaptain").get().asBoolean());
            return true;
        }

        if ("setdouble".equalsIgnoreCase(args[0])) {
            Map<String, MetaDataValue> metaData = user.getIsland().getMetaData().get();
            if (!metaData.keySet().contains(args[1])) {
                user.sendMessage("&e" + args[1] + " &cdoesn't exist in island's metadata");
                return false;
            }

            double val = Double.parseDouble(args[2]);

            user.getIsland().putMetaData(args[1], new MetaDataValue(val));
            user.sendMessage("&aValue set to: &e" + user.getIsland().getMetaData(args[1]).get().asDouble() * 100 + "%");
            return true;
        }

        if ("setbool".equalsIgnoreCase(args[0])) {
            Map<String, MetaDataValue> metaData = user.getIsland().getMetaData().get();
            if (!metaData.keySet().contains(args[1])) {
                user.sendMessage("&e" + args[1] + " &cdoesn't exist in island's metadata");
                return false;
            }

            boolean val = Boolean.parseBoolean(args[2]);

            user.getIsland().putMetaData(args[1], new MetaDataValue(val));
            user.sendMessage("&aValue set to: &e" + user.getIsland().getMetaData(args[1]).get().asBoolean());
        }

        if ("copydefaults".equalsIgnoreCase(args[0])) {
            user.getStats().copyDefaults();
            user.setHasIsland(true);
        }

        if ("readyuphive".equalsIgnoreCase(args[0])) {
            if (user.getBeehive() == null) {
                user.sendMessage("&cYou have no beehive placed!");
                return false;
            }
            user.getStats().setBoolean("farming.beehive.booster_available", true);
            user.getStats().setBoolean("farming.beehive.booster_active", false);
            ;
            user.getStats().setNumber("farming.beehive.next_refill", 0L);

            ((Beehive) user.getBeehive().getBlockData()).setHoneyLevel(5);
            user.getPlayer().playSound(user.getBeehive().getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 2, 1);

            user.deActivateBooster();
            Main.activeHives.remove(user.getBeehive());
            user.sendMessage("Beehive ready!");
            return true;
        }

        if ("booster".equalsIgnoreCase(args[0])) {
            user.sendMessage("BOOSTER TIME LEFT: " + user.formatBoosterTime());
            return true;
        }

        if ("islandlevel".equalsIgnoreCase(args[0])) {
            int level = Integer.parseInt(args[1]);
            user.getIsland().putMetaData("level", new MetaDataValue(level));
            user.sendMessage(user.getIsland().getMetaData("level").get().asInt() + "");
        }

        if ("debug_scoreboard".equalsIgnoreCase(args[0])) {
            if (user.getDebugBoard() != null) {
                user.getDebugBoard().delete();
                user.setDebugBoard(null);
                user.sendMessage("&cTurned off debug board");
                return true;
            }
            FastBoard fastBoard = new FastBoard(user.getPlayer());

            List<String> lines = List.of(
                    "&2Island&8:",
                    " &8- &aLVL&8, &aXP&8: &8(&a" + (user.getGlobalLevel()) + "&f, &a"
                            + (Main.DECIMAL_FORMAT.format(user.getGlobalExperience())) + "&8)",
                    " &8- &aMetadata&8: &f/test metadata",
                    " ",
                    "&2Booster&8:",
                    " &8- &aTime left&8: " + user.formatBoosterTime(),
                    " &8- &aBeehive&8: " + (user.getBeehive() == null ? "&cNot found" : "&aFound"),
                    " &8- &aMultiplier&8: &fx" + user.getBoosterMultiplier(),
                    " ",
                    "&2" + (user.getLastSkill() == null ? "&cNo last skill" : user.getLastSkill().toString()) + "&8:",
                    " &8- &aLevel&8: &f"
                            + (user.getLastSkill() == null ? 0 : user.getLevel(user.getLastSkill(), false)),
                    " &8- &aExperience&8: &f" + (Main.DECIMAL_FORMAT.format(
                            user.getLastSkill() == null ? 0.0 : user.getExperience(user.getLastSkill(), false))));

            List<String> formattedLines = new ArrayList<>();

            for (int i = 0; i < lines.size(); i++) {
                formattedLines.add(ChatColor.translateAlternateColorCodes('&', lines.get(i)));
            }

            fastBoard.updateTitle(ChatColor.translateAlternateColorCodes('&', "&e&lDEBUG"));
            fastBoard.updateLines(formattedLines);
            user.setDebugBoard(fastBoard);
            user.sendMessage("&aTurned on debug board");
            return true;
        }

        return false;
    }

}