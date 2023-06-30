package kiul.tierblock.utils.enums;

import org.bukkit.Material;

import kiul.tierblock.Main;

public enum MineableType {
    
    STONE(0, false),
    COAL(2, false),
    IRON(3, false),
    GOLD(4, false),
    REDSTONE_LAPIS(5, false),
    DIAMOND(6, false),
    EMERALD(7, false),
    OBSIDIAN(8, false), // MAX LEVEL (8)
    BASALT(1, true),
    NETHERRACK_GROUP(2, true),
    BLACKSTONE_GROUP(3, true),
    SOUL_GROUP(4, true),
    ANCIENT_DEBRIS(5, true);

    public final String label;
    public final int levelRequirement; // skill level, and not global.
    
    public final double xpReward;
    public final double levelUp;

    public final boolean isNether;

    public Material blockType;

    private MineableType(int levelRequirement, boolean isNether) {
        this.levelRequirement = levelRequirement;
        this.isNether = isNether;
        this.label = toString().toLowerCase();
        this.xpReward = Main.getInstance().getConfig().getDouble("mining." + label + ".xp_reward");
        this.levelUp = Main.getInstance().getConfig().getDouble("mining." + label + ".level_up");
    }

    public String formatName() {
        String name = blockType == null ? label : blockType.toString(); 

        String first = new String(
            name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase()
        ).replace("_", " ");
		
        String finished =
            first.contains(" ") ? 
			first.split(" ")[0] + " "
            + first.split(" ")[1].substring(0, 1).toUpperCase()
            + first.split(" ")[1].substring(1).toLowerCase()
            : first;
			
        return finished.replace(" Group", "");
    }

    private static MineableType groupWithSetMaterial(Material material, MineableType group) {
        group.blockType = material;
        return group;
    }

    public static MineableType fromMaterial(Material material) {
        switch(material) {
			case STONE:
            case COBBLESTONE: return STONE;
            case COAL_ORE: return COAL;
            case IRON_ORE: return IRON;
            case GOLD_ORE: return GOLD;

			case DEEPSLATE_REDSTONE_ORE:
			case DEEPSLATE_LAPIS_ORE:
            case REDSTONE_ORE:
            case LAPIS_ORE: return groupWithSetMaterial(material, REDSTONE_LAPIS);
            
			case DEEPSLATE_DIAMOND_ORE:
            case DIAMOND_ORE: return DIAMOND;
			
			case DEEPSLATE_EMERALD_ORE:
            case EMERALD_ORE: return EMERALD;
            
			case OBSIDIAN: return OBSIDIAN;
            case BASALT: return BASALT;

            case NETHERRACK:
            case NETHER_GOLD_ORE: 
            case MAGMA_BLOCK: return groupWithSetMaterial(material, NETHERRACK_GROUP);

            case BLACKSTONE:
            case GOLD_BLOCK:
            case GILDED_BLACKSTONE: return groupWithSetMaterial(material, BLACKSTONE_GROUP);

            case SOUL_SAND:
            case SOUL_SOIL: return groupWithSetMaterial(material, SOUL_GROUP);
            default: return null;
        }
    }

}
