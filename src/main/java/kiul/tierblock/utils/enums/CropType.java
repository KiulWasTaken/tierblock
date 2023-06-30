package kiul.tierblock.utils.enums;

import org.bukkit.Material;

import kiul.tierblock.Main;

public enum CropType {

    WHEAT(0, false), 
    BEETROOT(2, false), 
    CARROT(3, false), 
    POTATO(4, false), 
    SUGAR_CANE(5, false), 
    MELON(6, false), 
    PUMPKIN(7, false), 
    
    // nether stuff:

    NETHER_WART(1, true),
    CHORUS_FRUIT(2, true);

    public final String label;

    public final int levelRequirement;

    public final boolean isNether;

    public final double levelUp;
    public final double xpReward;

    private CropType(int levelRequirement, boolean isNether) {
        this.levelRequirement = levelRequirement;
        this.isNether = isNether;
        this.label = toString().toLowerCase();
        this.xpReward = Main.getInstance().getConfig().getDouble("farming." + label + ".xp_reward");
        this.levelUp = Main.getInstance().getConfig().getDouble("farming." + label + ".level_up");
    }

    /**
     * Formats the crop type into a good-looking name.
     * @return formatted name.
     */
    public String formatName() {
        String first = new String(
            label.substring(0, 1).toUpperCase() + label.substring(1)
        ).replace("_", " ");
		
        String finished =
            first.contains(" ") ? 
			first.split(" ")[0] + " "
            + first.split(" ")[1].substring(0, 1).toUpperCase()
            + first.split(" ")[1].substring(1).toLowerCase()
            : first;
			
        return finished;
    }

    public static CropType fromMaterial(Material material) {
        switch(material) {
            case WHEAT: return WHEAT;
            case BEETROOTS: return BEETROOT;
            case CARROTS: return CARROT;
            case POTATOES: return POTATO;
            case SUGAR_CANE: return SUGAR_CANE;
            case MELON: return MELON;
            case PUMPKIN: return PUMPKIN;
            case NETHER_WART: return NETHER_WART;
            case CHORUS_FLOWER: return CHORUS_FRUIT;
            default: return null;
        }
    }
    
}
