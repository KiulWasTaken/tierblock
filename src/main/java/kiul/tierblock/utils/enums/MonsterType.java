package kiul.tierblock.utils.enums;

import java.util.Arrays;
import java.util.List;
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
    public final double spawnChance;
    public final String label;
    public final int islandLevelRequirement;
	public final String worldName;

    public EntityType entityType;

    private MonsterType(double reward, int islandLevelRequirement, String worldName) {
        this.islandLevelRequirement = islandLevelRequirement;
        this.label = toString().toLowerCase();
        this.xpReward = Main.getInstance().getConfig().getDouble("combat." + label + ".xp_reward");
        this.spawnChance = Main.getInstance().getConfig().getDouble("combat." + label + ".spawn_chance");
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

    public static MonsterType getMonsterByChance(double chance, String worldName, int islandLevel) {
        List<MonsterType> monsters = Arrays.asList(MonsterType.values()).stream().filter(monsterType -> {
			if(monsterType == PILLAGER || monsterType == SPIDER) return false;
			if(!monsterType.worldName.equals(worldName)) return false;
			if(monsterType.islandLevelRequirement > islandLevel) return false;
			if(monsterType.spawnChance < chance) return false;
            return true;
        }).toList();

		MonsterType closest = null;

		if(monsters.size() > 0) {
			closest = monsters.get(0);
			double minDifference = Math.abs(chance - closest.spawnChance);
			for(int i = 0; i < monsters.size(); i++) {
				MonsterType current = monsters.get(i);
				
				if(current == null) break;
				
				double difference = Math.abs(chance - current.spawnChance);

				if(difference < minDifference) {
					closest = current;
					minDifference = difference;
				}
			}
		}
        return closest;
    }
}
