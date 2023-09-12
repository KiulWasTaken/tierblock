package kiul.tierblock.utils.enums;

import org.bukkit.Material;

import kiul.tierblock.Main;

public enum CropType implements SkillCollectible {

    WHEAT(0, false), 
    BEETROOT(2, false), 
    CARROT(3, false), 
    POTATO(4, false), 
    SUGAR_CANE(5, false), 
    MELON(6, false), 
    PUMPKIN(7, false), 
    
    // nether stuff:

    NETHER_WART(1, true),
    CHORUS_FLOWER(2, true);

    public final String label;

    public final int levelRequirement;
    public int globalLevelRequirement;

    public final boolean isNether;

    public final double levelUp;
    public final double xpReward;

    private CropType(int levelRequirement, boolean isNether) {

        

        this.levelRequirement = levelRequirement;
        this.isNether = isNether;
        this.label = toString().toLowerCase();

        boolean containsGlobalLevel = Main.getInstance().getConfig().contains("farming." + label + ".global_level_requirement");
        this.globalLevelRequirement = containsGlobalLevel ? Main.getInstance().getConfig().getInt("farming." + label + ".global_level_requirement") : 0;
        
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
            case MELON_STEM:
            case MELON: return MELON;
            case PUMPKIN_STEM:
            case PUMPKIN: return PUMPKIN;
            case NETHER_WART: return NETHER_WART;
            case CHORUS_FLOWER: return CHORUS_FLOWER;
            default: return null;
        }
    }

    public static Material toSeed(CropType cropType) {
        switch(cropType) {
            case BEETROOT: return Material.BEETROOT_SEEDS;
            case CARROT: return Material.CARROT;
            case CHORUS_FLOWER: return Material.CHORUS_FLOWER;
            case MELON: return Material.MELON_SEEDS;
            case NETHER_WART: return Material.NETHER_WART;
            case POTATO: return Material.POTATO;
            case PUMPKIN: return Material.PUMPKIN_SEEDS;
            case SUGAR_CANE: return Material.SUGAR_CANE;
            case WHEAT: return Material.WHEAT_SEEDS; // i don't see how that's gonna happen but i put it here.
            default: return Material.AIR;
        }
    }

    @Override
    public String label() {
        return label;
    }

    @Override
    public double levelUp() {
        return levelUp;
    }

    @Override
    public double xpReward() {
        return xpReward;
    }
    
}
