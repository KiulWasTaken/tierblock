package kiul.tierblock.utils.enums;

import org.bukkit.entity.EntityType;

import kiul.tierblock.Main;

public enum MonsterType {

    PILLAGER(0.0, 0),
    ZOMBIE(2.0, 0),
    SPIDER(2.8, 5),
    SKELETON_CREEPER(3.4, 10),
    ZOMBIE_VILLAGER(0.0, 20),
    PIGLIN(4.0, 40),
    HOGLIN(4.0, 40),
    BLAZE(5.6, 50),
    WITHER_SKELETON(5.6, 50),
    SHULKER(8.4, 100);

    public final double xpReward;
    public final String label;
    public final int islandLevelRequirement;

    public EntityType entityType;

    private MonsterType(double reward, int islandLevelRequirement) {
        this.islandLevelRequirement = islandLevelRequirement;
        this.label = toString().toLowerCase();
        this.xpReward = Main.getInstance().getConfig().getDouble("combat." + label + ".xp_reward");
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
}
