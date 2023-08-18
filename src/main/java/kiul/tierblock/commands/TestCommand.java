package kiul.tierblock.commands;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Sound;
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
import net.md_5.bungee.api.ChatColor;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.metadata.MetaDataValue;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.IslandsManager;
import world.bentobox.bentobox.managers.RanksManager;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(sender instanceof ConsoleCommandSender) return false;
        
        User user = UserManager.getInstance().getUser((Player)sender);

        if(args.length < 1) {
            user.sendMessage("&cInsufficient arguments");
            return false;
        }

        if("aboutme".equalsIgnoreCase(args[0])) {
            user.sendMessage("&eGay: " + (user.getPlayer().getName().equals("themightyfrogge") ? "&cNo" : "&aYes"));
                
            user.sendMessage("has_island: " + user.hasIsland());
            user.sendMessage("&8- &eIs Island Owner: &f" + (user.hasIsland() ? (user.getIslandRank() == RanksManager.OWNER_RANK) : "&chas_island is false"));

            user.sendMessage("booster: " + (user.getBooster() > System.currentTimeMillis()));
            user.sendMessage("booster_multiplier: " + user.getBoosterMultiplier());
            user.sendMessage("sc_chance: " + user.getSeaCreatureChance());
            user.sendMessage("flight: " + user.isAllowedToFly());
        }
	
        if(!user.isOp()) {
            user.sendMessage("&cInsufficient permissions");
            return false;
        }

        if("setmeupchief".equalsIgnoreCase(args[0])) {
            user.setSeaCreatureChance(0.50);
            user.sendMessage("&esc_chance &ais now &350%");
        }

        if("scc".equalsIgnoreCase(args[0])) {
            if(!user.hasIsland()) return false;
            user.getIslandMembers().forEach(member -> {
                user.sendMessage(member.getName() + " (sc_chance): " + member.getSeaCreatureChance() * 100 + "%");
            });
            return true;
        }

        if("metadata".equalsIgnoreCase(args[0])) {
            if(!user.isWithinAnyIsland()) {
                user.sendMessage("&cYou aren't in any island!");
                return false;
            }

            user.sendMessage("Island's meta-data:");
            Location location = user.getLocation();
            IslandsManager manager = BentoBox.getInstance().getIslandsManager();
            Island island = manager.getIslandAt(location).get();
            
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
            user.sendMessage(" &8- &fpillagerSpawnChance &e-> &f" + (island.getMetaData("pillagerSpawnChance").get().asDouble() * 100) + "%");
            user.sendMessage(" &8- &fraidCaptain &e-> &f" + island.getMetaData("raidCaptain").get().asBoolean());
            return true;
        }

        if("setdouble".equalsIgnoreCase(args[0])) {
            Map<String, MetaDataValue> metaData = user.getIsland().getMetaData().get();
            if(!metaData.keySet().contains(args[1])) {
                user.sendMessage("&e" + args[1] + " &cdoesn't exist in island's metadata");
                return false;
            }

            double val = Double.parseDouble(args[2]);

            user.getIsland().putMetaData(args[1], new MetaDataValue(val));
            user.sendMessage("&aValue set to: &e" + user.getIsland().getMetaData(args[1]).get().asDouble() * 100 + "%");
            return true;
        }

        if("setbool".equalsIgnoreCase(args[0])) {
            Map<String, MetaDataValue> metaData = user.getIsland().getMetaData().get();
            if(!metaData.keySet().contains(args[1])) {
                user.sendMessage("&e" + args[1] + " &cdoesn't exist in island's metadata");
                return false;
            }

            boolean val = Boolean.parseBoolean(args[2]);

            user.getIsland().putMetaData(args[1], new MetaDataValue(val));
            user.sendMessage("&aValue set to: &e" + user.getIsland().getMetaData(args[1]).get().asBoolean());
        }

        if("copydefaults".equalsIgnoreCase(args[0])) {
            user.getStats().copyDefaults();
            user.setHasIsland(true);
        }

        if("readyuphive".equalsIgnoreCase(args[0])) {
            if(user.getBeehive() == null) {
                user.sendMessage("&cYou have no beehive placed!");
                return false;
            }
            user.getStats().setBoolean("farming.beehive.booster_available", true);
            user.getStats().setBoolean("farming.beehive.booster_active", false);;
            user.getStats().setNumber("farming.beehive.next_refill", 0L);

            ((Beehive)user.getBeehive().getBlockData()).setHoneyLevel(5);
            user.getPlayer().playSound(user.getBeehive().getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 2, 1);

            user.deActivateBooster();
            Main.activeHives.remove(user.getBeehive());
            user.sendMessage("Beehive ready!");
            return true;
        }

        if("booster".equalsIgnoreCase(args[0])) {
            user.sendMessage("BOOSTER TIME LEFT: " + user.formatBoosterTime());
            return true;
        }

        if ("islandlevel".equalsIgnoreCase(args[0])) {
            int level = Integer.parseInt(args[1]);
            user.getIsland().putMetaData("level", new MetaDataValue(level));
            user.sendMessage(user.getIsland().getMetaData("level").get().asInt() + "");
        }

        if("debug_scoreboard".equalsIgnoreCase(args[0])) {
            if(user.getDebugBoard() != null) {
                user.getDebugBoard().delete();
                user.setDebugBoard(null);
                user.sendMessage("&cTurned off debug board");
                return true;
            }
            FastBoard fastBoard = new FastBoard(user.getPlayer());

            List<String> lines = List.of(
                "&2Island&8:",
                " &8- &aLVL&8, &aXP&8: &8(&a" + (user.getGlobalLevel()) + "&f, &a" + (Main.DECIMAL_FORMAT.format(user.getGlobalExperience())) + "&8)",
                " &8- &aMetadata&8: &f/test metadata",
                " ",
                "&2Booster&8:",
                " &8- &aTime left&8: " + user.formatBoosterTime(),
                " &8- &aBeehive&8: " + (user.getBeehive() == null ? "&cNot found" : "&aFound"),
                " &8- &aMultiplier&8: &fx" + user.getBoosterMultiplier(),
                " ",
                "&2" + (user.getLastSkill() == null ? "&cNo last skill" : user.getLastSkill().toString()) + "&8:",
                " &8- &aLevel&8: &f" + (user.getLastSkill() == null ? 0 : user.getLevel(user.getLastSkill(), false)),
                " &8- &aExperience&8: &f" + (Main.DECIMAL_FORMAT.format(user.getLastSkill() == null ? 0.0 : user.getExperience(user.getLastSkill(), false)))
            );

            List<String> formattedLines = new ArrayList<>();

            for(int i = 0; i < lines.size(); i++) {
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
