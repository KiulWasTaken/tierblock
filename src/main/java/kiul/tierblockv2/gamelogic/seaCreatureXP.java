package kiul.tierblockv2.gamelogic;

import kiul.tierblockv2.userData;
import org.bukkit.entity.Player;

public class seaCreatureXP {


    public static void add (Player p, Integer amount) {

        Integer fishingExp = (Integer) userData.get().get(p.getUniqueId().toString() + ".fishingExp");
        userData.get().set(p.getUniqueId().toString() + ".fishingExp", fishingExp + amount);
        checkForLevelUp(p);
        userData.save();
        // Make a graphic show in actionbar telling the player how much XP they just gained.
        // Perhaps a bar showing how close they are to their goal? - looking at you pat.
    }

    public static void setupFishingExpData (Player p) {

        if (userData.get().get(p.getUniqueId().toString() + ".fishingExp") == null) {
            userData.get().set(p.getUniqueId().toString() + ".fishingExp", 0);
            userData.save();
        }
    }

    public static void setupFishingLevelData (Player p) {

        if (userData.get().get(p.getUniqueId().toString() + ".fishingLevel") == null) {
            userData.get().set(p.getUniqueId().toString() + ".fishingLevel", 1);
            userData.save();
        }
    }

    public static void checkForLevelUp (Player p) {

        if ((Integer) userData.get().get(p.getUniqueId().toString() + ".fishingLevel") <= 10) {
            if ((Integer) userData.get().get(p.getUniqueId().toString() + ".fishingExp") <=100) {
                levelUp(p);
            }
        } else if ((Integer) userData.get().get(p.getUniqueId().toString() + ".fishingLevel") >10) {
            if ((Integer) userData.get().get(p.getUniqueId().toString() + ".fishingExp") <=1000) {
                levelUp(p);
            }
        }
    }

    public static void levelUp (Player p) {

        Integer fishingLevel = (Integer) userData.get().get(p.getUniqueId().toString() + ".fishingLevel");
        userData.get().set(p.getUniqueId().toString() + ".fishingLevel", fishingLevel + 1);
        userData.save();
    }
}
