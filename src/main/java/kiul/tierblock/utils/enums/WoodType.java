package kiul.tierblock.utils.enums;

import org.bukkit.Material;

import kiul.tierblock.Main;

/* Note: toInt() removed, use the already provided Java method Enum#ordinal() */
public enum WoodType implements SkillCollectible {

    OAK(0, false), 
    BIRCH(2, false), 
    ACACIA(3, false), 
    DARK_OAK(4, false), 
    SPRUCE(5, false), 
    JUNGLE(6, false),

    // nether stuff:

    CRIMSON(1, true), 
    WARPED(2, true);

    public final String label;
    public final int levelRequirement;
    
    public final double xpReward;
    public final double levelUp;

    public final boolean isNether;

    private WoodType(int levelRequirement, boolean isNether) {
        this.isNether = isNether;
        this.levelRequirement = levelRequirement;
        this.label = toString().toLowerCase();
        this.xpReward = Main.getInstance().getConfig().getDouble("foraging." + label + ".xp_reward");
        this.levelUp = Main.getInstance().getConfig().getDouble("foraging." + label + ".level_up");
    }

    /**
     * Formats the wood type into a good-looking name.
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

    public static Material toSapling(WoodType woodType) {
        switch(woodType) {
            case OAK: return Material.OAK_SAPLING;
            case BIRCH: return Material.BIRCH_SAPLING;
            case ACACIA: return Material.ACACIA_SAPLING;
            case DARK_OAK: return Material.DARK_OAK_SAPLING;
            case SPRUCE: return Material.SPRUCE_SAPLING;
            case JUNGLE: return Material.JUNGLE_SAPLING;
            case CRIMSON: return Material.CRIMSON_FUNGUS;
            case WARPED: return Material.WARPED_FUNGUS;
            default: return Material.AIR;
        }
    }
    
    public static WoodType fromSapling(Material material) {
        String materialString = material.toString().toLowerCase();
        if(!materialString.startsWith("potted") && materialString.endsWith("sapling")) {
            String woodTypeString = materialString.replace("_sapling", "").toUpperCase();
            for (WoodType type : WoodType.values()) {
                if(type.toString().equals(woodTypeString))
                    return type;
            }
        }
        return null;
    }
        
    @Override
    public double levelUp() {
        return levelUp;
    }

}
