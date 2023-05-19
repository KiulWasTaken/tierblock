package kiul.tierblockv2.gamelogic;

import kiul.tierblockv2.Tierblockv2;
import kiul.tierblockv2.userData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class globalEXP {




    public static void add (Player p, double amount) {

        double globalExp = userData.get().getDouble(p.getUniqueId() + ".global.xp");
        userData.get().set(p.getUniqueId() + ".global.xp", globalExp + amount);
        userData.save();
        checkForLevelUp(p);

        // green and red only look good like this if you're colourblind, pat..
        if (!checkForLevelUp(p) == true) {
            if (userData.get().getDouble(p.getUniqueId() + ".global.level") <= 10) {


                double current = userData.get().getDouble(p.getUniqueId() + ".global.xp") / 2.5;
                int rounded = ((int) current);

                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + "+ " + amount + " " + ChatColor.DARK_GREEN + "G-EXP " + ChatColor.DARK_AQUA + "- " + ChatColor.GREEN + String.valueOf("|").repeat(rounded) + ChatColor.RED + String.valueOf("|").repeat(40 - rounded)));

            } else if (userData.get().getDouble(p.getUniqueId() + ".global.level") > 10) {

                double current = userData.get().getDouble(p.getUniqueId() + ".global.xp") / 25;
                int rounded = ((int) current);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + "+ " + amount + " " + ChatColor.DARK_GREEN + "G-EXP " + ChatColor.DARK_AQUA + "- " + ChatColor.GREEN + String.valueOf("|").repeat(rounded) + ChatColor.RED + String.valueOf("|").repeat(40 - rounded)));

            }
        }
    }

    public static void setupExpData (Player p) {

        if (userData.get().get(p.getUniqueId() + ".global.xp") == null) {
            userData.get().set(p.getUniqueId() + ".global.xp", 0);
            userData.save();
        }
    }

    public static void setupLevelData (Player p) {

        if (userData.get().get(p.getUniqueId() + ".global.level") == null) {
            userData.get().set(p.getUniqueId() + ".global.level", 1);
            userData.save();
        }
    }

    public static boolean checkForLevelUp (Player p) {

        if ( userData.get().getInt(p.getUniqueId() + ".global.level") <= 10) {
            if ( userData.get().getDouble(p.getUniqueId() + ".global.xp") >= 100) {
                levelUp(p);
                return true;
            }
        } else if ( userData.get().getInt(p.getUniqueId() + ".global.level") > 10) {
            if ( userData.get().getDouble(p.getUniqueId() + ".global.xp") >= 1000) {
                levelUp(p);
                return true;
            }
        }
        return false;
    }

    public static void levelUp (Player p) {

        Integer globalLevel = (Integer) userData.get().get(p.getUniqueId() + ".global.level");
        userData.get().set(p.getUniqueId() + ".global.level", globalLevel + 1);
        userData.get().set(p.getUniqueId() + ".global.xp", 0);
        fishingLevelUp(p, (int) userData.get().get(p.getUniqueId() + ".global.level"));
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                "&f&lLevel Up! &3" + globalLevel + " &7-> &b" + (globalLevel + 1))));
        userData.save();
    }


    private static void fishingLevelUp(Player p, int globalLevel) {
        if (globalLevel >=10 && globalLevel <=110 && globalLevel % 10 == 0) {
            userData.get().set(p.getUniqueId() + ".fishing.seaCreatureChance", (globalLevel == 10) ? 0.05 : 0.04 + globalLevel);
        }
    }
}
