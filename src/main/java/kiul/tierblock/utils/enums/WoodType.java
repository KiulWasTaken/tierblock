package kiul.tierblock.utils.enums;

import org.bukkit.Material;

// hello
public enum WoodType {

    OAK("oak", 0), 
    BIRCH("birch", 1), 
    ACACIA("acacia", 2), 
    DARK_OAK("dark_oak", 3), 
    SPRUCE("spruce", 4), 
    JUNGLE("jungle", 5), 
    CRIMSON("crimson", 6), 
    WARPED("warped", 7);

    private String label;
    private int index;

    private WoodType(String label, int index) {
        this.label = label;
        this.index = index;
    }

    public String toString() {
        return this.label;
    }

    /**
     * Formats the wood type into a good-looking name.
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

    public static WoodType fromInt(int number){
        switch(number) {
            case 0: return OAK;
            case 1: return BIRCH;
            case 2: return ACACIA;
            case 3: return DARK_OAK;
            case 4: return SPRUCE;
            case 5: return JUNGLE;
            case 6: return CRIMSON;
            case 7: return WARPED;
            default: return null;
        }
    }

    public static WoodType fromMaterial(Material material) {
        switch(material) {
            case OAK_LOG: return OAK;
            case BIRCH_LOG: return BIRCH;
            case ACACIA_LOG: return ACACIA;
            case DARK_OAK_LOG: return DARK_OAK;
            case SPRUCE_LOG: return SPRUCE;
            case JUNGLE_LOG: return JUNGLE;
            case CRIMSON_STEM: return CRIMSON;
            case WARPED_STEM: return WARPED;
            default: return null;
        }
    }
}
