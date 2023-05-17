package kiul.tierblockv2.gamelogic;

import kiul.tierblockv2.userData;
import org.bukkit.entity.Player;

public class globalEXP {


    public static void add (Player p, Integer amount) {

        Integer globalExp = (Integer) userData.get().get(p.getUniqueId().toString() + ".globalExp");
        userData.get().set(p.getUniqueId().toString() + ".globalExp", globalExp + amount);
        checkForLevelUp(p);
        userData.save();
        // Make a graphic show in actionbar telling the player how much XP they just gained.
        // Perhaps a bar showing how close they are to their goal? - looking at you pat.
    }

    public static void setupExpData (Player p) {

        if (userData.get().get(p.getUniqueId().toString() + ".globalExp") == null) {
            userData.get().set(p.getUniqueId().toString() + ".globalExp", 0);
            userData.save();
        }
    }

    public static void setupLevelData (Player p) {

        if (userData.get().get(p.getUniqueId().toString() + ".globalLevel") == null) {
            userData.get().set(p.getUniqueId().toString() + ".globalLevel", 1);
            userData.save();
        }
    }

    public static void checkForLevelUp (Player p) {

        if ((Integer) userData.get().get(p.getUniqueId().toString() + ".globalLevel") <= 10) {
            if ((Integer) userData.get().get(p.getUniqueId().toString() + ".globalExp") <=100) {
                levelUp(p);
            }
        } else if ((Integer) userData.get().get(p.getUniqueId().toString() + ".globalLevel") >10) {
            if ((Integer) userData.get().get(p.getUniqueId().toString() + ".globalExp") <=1000) {
                levelUp(p);
            }
        }
    }

    public static void levelUp (Player p) {

        Integer globalLevel = (Integer) userData.get().get(p.getUniqueId().toString() + ".globalLevel");
        userData.get().set(p.getUniqueId().toString() + ".globalExp", globalLevel+1);
        userData.save();
        // Add some messages here for notifying the player when they level up, not too intrusive
        // as it will happen often.
    }
}
