package kiul.tierblock.utils.enums;

import org.bukkit.Material;

public enum CropType {

    WHEAT("wheat", 0), 
    BEETROOT("beetroot", 1), 
    CARROT("carrot", 2), 
    POTATO("potato", 3), 
    SUGAR_CANE("sugar_cane", 4), 
    MELON("melon", 5), 
    PUMPKIN("pumpkin", 6), 
    NETHER_WART("nether_wart", 7),
    CHORUS_FRUIT("chorus_fruit", 8);

    private String label;
    private int index;

    private CropType(String label, int index) {
        this.label = label;
        this.index = index;
    }

    public String toString() {
        return this.label;
    }

    /**
     * Formats the crop type into a good-looking name.
     * @return formatted name.
     */
    public String formatName() {
        String finished = new String(
            toString().substring(0, 1).toUpperCase() + toString().substring(1)
        ).replace("_", " ");
        return finished;
    }

    public int toInt() {
        return this.index;
    }

    public static CropType fromInt(int number){
        switch(number) {
            case 0: return WHEAT;
            case 1: return BEETROOT;
            case 2: return CARROT;
            case 3: return POTATO;
            case 4: return SUGAR_CANE;
            case 5: return MELON;
            case 6: return PUMPKIN;
            case 7: return NETHER_WART;
            case 8: return CHORUS_FRUIT;
            default: return null;
        }
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
