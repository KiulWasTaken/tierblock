package kiul.tierblock.utils.enums;

import java.util.Random;

import org.bukkit.entity.EntityType;

import kiul.tierblock.Main;

public enum MonsterType {

    PILLAGER(0.0, 0, "bskyblock_world"),
    ZOMBIE(2.0, 0, "bskyblock_world"),
    SPIDER(2.8, 5, "bskyblock_world"),
    SKELETON_CREEPER(3.4, 10, "bskyblock_world"),
    ZOMBIE_VILLAGER(0.0, 20, "bskyblock_world"),
    PIGLIN(4.0, 40, "bskyblock_world_nether"),
    HOGLIN(4.0, 40, "bskyblock_world_nether"),
    BLAZE(5.6, 50, "bskyblock_world_nether"),
    WITHER_SKELETON(5.6, 50, "bskyblock_world_nether"),
    SHULKER(8.4, 100, "bskyblock_world_the_end");

    public final double xpReward;
    public final String label;
    public final int islandLevelRequirement;
	public final String worldName;

    public EntityType entityType;

    private MonsterType(double reward, int islandLevelRequirement, String worldName) {
        this.islandLevelRequirement = islandLevelRequirement;
        this.label = toString().toLowerCase();
        this.xpReward = Main.getInstance().getConfig().getDouble("combat." + label + ".xp_reward");
        this.worldName = worldName;
	}

    public String formatName() {
        String name = entityType == null ? label : entityType.toString();

        String first = new String(
            name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase()
        ).replace("_", " ");
		
        String finished =
            first.contains(" ") ? 
			first.split(" ")[0] + " "
            + first.split(" ")[1].substring(0, 1).toUpperCase()
            + first.split(" ")[1].substring(1).toLowerCase()
            : first;
			
        return finished;
        
    }

    private static MonsterType groupWithSetEntityType(EntityType type, MonsterType group) {
        group.entityType = type;
        return group;
    }

    public static MonsterType fromEntityType(EntityType entityType) {
        switch(entityType) {
            case PILLAGER: return PILLAGER;
            case ZOMBIE: return ZOMBIE;
            case SPIDER: return SPIDER;

            case SKELETON: // returns skeleton/creeper... (displays Skeleton, or Creeper)
            case CREEPER: return groupWithSetEntityType(entityType, SKELETON_CREEPER);

            case ZOMBIE_VILLAGER: return ZOMBIE_VILLAGER;

            case ZOMBIFIED_PIGLIN: // returns piglin... (displays Zombified Piglin, or Piglin)
            case PIGLIN: return groupWithSetEntityType(entityType, PIGLIN);
 
            case HOGLIN: return HOGLIN;
            case BLAZE: return BLAZE;
            case WITHER_SKELETON: return WITHER_SKELETON;
            case SHULKER: return SHULKER;
            default: return null;
        }
    }

    public static EntityType toEntityType(MonsterType monsterType) {
        double random = new Random().nextDouble();
        switch(monsterType) {
            case PILLAGER:
                return EntityType.PILLAGER;
            case ZOMBIE:
                return EntityType.ZOMBIE;
            case SPIDER:
                return EntityType.SPIDER;
            case SKELETON_CREEPER:
                if(random > 0.4) return EntityType.SKELETON;
                return EntityType.CREEPER;
            case ZOMBIE_VILLAGER:
                return EntityType.ZOMBIE_VILLAGER;
            case PIGLIN:
                if(random > 0.3) return EntityType.ZOMBIFIED_PIGLIN;
                return EntityType.PIGLIN;
            case HOGLIN:
                return EntityType.HOGLIN;
            case BLAZE:
                return EntityType.BLAZE;
            case WITHER_SKELETON:
                return EntityType.WITHER_SKELETON;
            case SHULKER:
                return EntityType.SHULKER;
        }
        return null;
    }
}
