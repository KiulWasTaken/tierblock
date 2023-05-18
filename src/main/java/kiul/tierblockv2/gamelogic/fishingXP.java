package kiul.tierblockv2.gamelogic;

import kiul.tierblockv2.userData;
import org.bukkit.entity.Player;

public class fishingXP {

    static int level2RequiredXP = 25;
    static int level3RequiredXP = 50;
    static int level4RequiredXP = 100;
    static int level5RequiredXP = 200;

    static int level6RequiredXP = 500;


    public static void addFishingXP (Player p, double amount) {


        if ((int)userData.get().get(p.getUniqueId() + ".fishing.level") != 6) {
            double fishingXP = (double) userData.get().get(p.getUniqueId() + ".fishing.xp");
            userData.get().set(p.getUniqueId() + ".fishing.xp", fishingXP + amount);
            checkForLevelUp(p);
            userData.save();
            // Make a graphic show in actionbar telling the player how much XP they just gained.
            // Perhaps a bar showing how close they are to their goal? - looking at you pat.
        }
    }

    public static void setupfishingXPData (Player p) {

        if (userData.get().get(p.getUniqueId() + ".fishing.xp") == null) {
            userData.get().set(p.getUniqueId() + ".fishing.xp", 0);
            userData.save();
        }
    }

    public static void setupFishingLevelData (Player p) {

        if (userData.get().get(p.getUniqueId() + ".fishing.level") == null) {
            userData.get().set(p.getUniqueId() + ".fishing.level", 1);
            userData.save();
        }
    }

    public static void checkForLevelUp (Player p) {

        switch ((int) userData.get().get(p.getUniqueId() + ".fishing.level")) {
            case 1:
                if ((double) userData.get().get(p.getUniqueId() + ".fishing.xp") >= level2RequiredXP) {
                    levelUp(p);
                }
                break;
            case 2:
                if ((double) userData.get().get(p.getUniqueId() + ".fishing.xp") >= level3RequiredXP) {
                    levelUp(p);
                }
                break;
            case 3:
                if ((double) userData.get().get(p.getUniqueId() + ".fishing.xp") >= level4RequiredXP) {
                    levelUp(p);
                }
                break;
            case 4:
                if ((double) userData.get().get(p.getUniqueId() + ".fishing.xp") >= level5RequiredXP) {
                    levelUp(p);
                }
                break;
            case 5:
                if ((double) userData.get().get(p.getUniqueId() + ".fishing.xp") >= level6RequiredXP) {
                    levelUp(p);
                }
                break;
        }
        }

    public static void levelUp (Player p) {

        int fishingLevel = (int) userData.get().get(p.getUniqueId() + ".fishing.level");
        userData.get().set(p.getUniqueId() + ".fishing.level", fishingLevel + 1);
        userData.get().set(p.getUniqueId() + ".fishing.xp", 0);
        userData.save();
    }
}
