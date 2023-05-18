package kiul.tierblockv2.gamelogic;

import kiul.tierblockv2.userData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class globalEXP {


    public static void add (Player p, double amount) {

        double globalExp = userData.get().getDouble(p.getUniqueId() + ".globalExp");
        userData.get().set(p.getUniqueId() + ".globalExp", globalExp + amount);
        userData.save();


        if ( userData.get().getDouble(p.getUniqueId() + ".globalLevel") <= 10) {

            double current = userData.get().getInt(p.getUniqueId() + ".globalExp") / 2.5;

            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + "+ " + amount + " " + ChatColor.DARK_GREEN + "G-EXP " + ChatColor.DARK_AQUA + "- " + ChatColor.GREEN + String.valueOf("|").repeat((int) current) + ChatColor.RED + String.valueOf("|").repeat(40 - (int) current)));

        } else if ( userData.get().getDouble(p.getUniqueId() + ".globalLevel") >10) {

            int current = userData.get().getInt(p.getUniqueId() + ".globalExp") / 25;

            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + "+ " + amount + " " + ChatColor.DARK_GREEN + "G-EXP " + ChatColor.DARK_AQUA + "- " + ChatColor.GREEN + String.valueOf("|").repeat(current) + ChatColor.RED + String.valueOf("|").repeat(40 - current)));

        }

        checkForLevelUp(p);

    }

    public static void setupExpData (Player p) {

        if (userData.get().get(p.getUniqueId() + ".globalExp") == null) {
            userData.get().set(p.getUniqueId() + ".globalExp", 0);
            userData.save();
        }
    }

    public static void setupLevelData (Player p) {

        if (userData.get().get(p.getUniqueId() + ".globalLevel") == null) {
            userData.get().set(p.getUniqueId() + ".globalLevel", 1);
            userData.save();
        }
    }

    public static void checkForLevelUp (Player p) {

        if ( userData.get().getInt(p.getUniqueId() + ".globalLevel") <= 10) {
            if ( userData.get().getDouble(p.getUniqueId() + ".globalExp") >= 100) {
                levelUp(p);
            }
        } else if ( userData.get().getInt(p.getUniqueId() + ".globalLevel") > 10) {
            if ( userData.get().getDouble(p.getUniqueId() + ".globalExp") >= 1000) {
                levelUp(p);
            }
        }
    }

    public static void levelUp (Player p) {

        Integer globalLevel = (Integer) userData.get().get(p.getUniqueId() + ".globalLevel");
        userData.get().set(p.getUniqueId() + ".globalLevel", globalLevel+1);
        userData.get().set(p.getUniqueId() + ".globalExp", 0);
        userData.save();
        switch ((Integer) userData.get().get(p.getUniqueId() + ".globalLevel")) {
            case 10:
                userData.get().set(p.getUniqueId() + ".fishing.seaCreatureChance", 0.05);
                break;
            case 20:
                userData.get().set(p.getUniqueId() + ".fishing.seaCreatureChance", 0.06);
                break;
            case 30:
                userData.get().set(p.getUniqueId() + ".fishing.seaCreatureChance", 0.07);
                break;
            case 40:
                userData.get().set(p.getUniqueId() + ".fishing.seaCreatureChance", 0.08);
                break;
            case 50:
                userData.get().set(p.getUniqueId() + ".fishing.seaCreatureChance", 0.09);
                break;
            case 60:
                userData.get().set(p.getUniqueId() + ".fishing.seaCreatureChance", 0.10);
                break;
            case 70:
                userData.get().set(p.getUniqueId() + ".fishing.seaCreatureChance", 0.11);
                break;
            case 80:
                userData.get().set(p.getUniqueId() + ".fishing.seaCreatureChance", 0.12);
                break;
            case 90:
                userData.get().set(p.getUniqueId() + ".fishing.seaCreatureChance", 0.13);
                break;
            case 100:
                userData.get().set(p.getUniqueId() + ".fishing.seaCreatureChance", 0.14);
                break;
            case 110:
                userData.get().set(p.getUniqueId() + ".fishing.seaCreatureChance", 0.15);
                break;
        }

        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                "&6&lLevel Up! &2" + globalLevel + " &3-> &6" + (globalLevel + 1))));
    }
}
