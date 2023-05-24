package kiul.tierblock.listeners;

import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillType;

public class FROGGEFishingListener implements Listener{

    private final double FISHING_VELOCITY_MULTIPLIER = 0.13;

    private final Map<EntityType, Double> FISHING_REWARD_ENTITIES = Map.of(  // EntityType, XP Reward...
        EntityType.SQUID, 12.0, 
        EntityType.GLOW_SQUID, 16.0,
        EntityType.PUFFERFISH, 20.0,
        EntityType.TURTLE, 30.0,
        EntityType.DROWNED, 50.0,
        EntityType.GUARDIAN, 80.0, 
        EntityType.ELDER_GUARDIAN, 600.0
    );

    // pyramid of doom is goners
    @EventHandler
    public void creatureCatchListener(PlayerFishEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());

        if(user.getLevel(SkillType.GLOBAL) < 10) return; // stops here if fishing level is less than 10.

        // now that the player has a high enough level we check if something was caught or not:
        if(event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return; // nothing caught, stopping here.

		double chance = new Random().nextDouble(0, 1);

		if(chance > user.getSeaCreatureChance()) return; // random double is bigger than chance, stopping here.
        
        event.setCancelled(true);

        int random = new Random().nextInt(1, 7);
        boolean isGlowSquid = random == 2;
        EntityType entityType = EntityType.SQUID; // if fishing level isn't 1-6, it'll always spawn a squid.

        switch(user.getLevel(SkillType.FISHING)) { 
            case 1:
                entityType = isGlowSquid ? EntityType.GLOW_SQUID : EntityType.SQUID;
                break;
            case 2:
                entityType = random == 1 ? isGlowSquid ? EntityType.GLOW_SQUID : EntityType.SQUID : EntityType.PUFFERFISH;
                break;
            case 3:
                switch(random) {
                    case 1:
                        entityType = isGlowSquid ? EntityType.GLOW_SQUID : EntityType.SQUID;
                        break;
                    case 2:
                        entityType = EntityType.PUFFERFISH;
                        break;
                    case 3:
                        entityType = EntityType.TURTLE;
                        break;
                }
                break;
            case 4:
                switch(random) {
                    case 1:
                        entityType = isGlowSquid ? EntityType.GLOW_SQUID : EntityType.SQUID;
                        break;
                    case 2:
                        entityType = EntityType.PUFFERFISH;
                        break;
                    case 3:
                        entityType = EntityType.TURTLE;
                        break;
                    case 4:
                        entityType = EntityType.DROWNED;
                        break;
                }
                break;
            case 5:
                switch(random) {
                    case 1:
                        entityType = isGlowSquid ? EntityType.GLOW_SQUID : EntityType.SQUID;
                        break;
                    case 2:
                        entityType = EntityType.PUFFERFISH;
                        break;
                    case 3:
                        entityType = EntityType.TURTLE;
                        break;
                    case 4:
                        entityType = EntityType.DROWNED;
                        break;
                    case 5:
                        entityType = EntityType.GUARDIAN;
                        break;
                }
                break;
            case 6:
                switch(random) {
                    case 1:
                        entityType = isGlowSquid ? EntityType.GLOW_SQUID : EntityType.SQUID;
                        break;
                    case 2:
                        entityType = EntityType.PUFFERFISH;
                        break;
                    case 3:
                        entityType = EntityType.TURTLE;
                        break;
                    case 4:
                        entityType = EntityType.DROWNED;
                        break;
                    case 5:
                        entityType = EntityType.GUARDIAN;
                        break;
                    case 6:
                        entityType = EntityType.ELDER_GUARDIAN;
                        break;
                }
                break;
        }
        spawnAndFling(entityType, event.getHook().getLocation(), user.getLocation());
		event.getHook().remove();
	}

    @EventHandler
    public void creatureKillListener(EntityDeathEvent event) {
        // getKiller() always returns Player, so instead check for null.
        if(event.getEntity().getKiller() == null) return; // not killed by player.
        
        EntityType entityType = event.getEntityType();
        if(! (FISHING_REWARD_ENTITIES.containsKey(entityType)) ) return; // not in the fishing creatures hash map
        
        User user = UserManager.getInstance().getUser(event.getEntity().getKiller());
        user.addExperience(SkillType.FISHING, 1);
        user.addExperience(SkillType.GLOBAL, FISHING_REWARD_ENTITIES.get(entityType)); // adds gxp reward from the hash map

        user.sendActionBar(
            String.format("&eGlobal: &2+&a%sxp &8| &eFishing: &2+&a1xp &8(&b%s&8)", 
                FISHING_REWARD_ENTITIES.get(entityType),
                entityType.toString().toLowerCase().replace("_", " ") // too lazy to capitalise first char
            )
        );
    }

	// this somehow doesn't work with both squids... maybe their movement is limited by bukkit
    private void spawnAndFling(EntityType type, Location location, Location playerLocation) {
        LivingEntity entity = (LivingEntity)location.getWorld().spawnEntity(location, type);
        entity.addPotionEffect( // prevent fall damage
            new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3*20, 200, false, false) // no particles.
        ); 
        Vector target = playerLocation.add(0, 8, 0).toVector(); // adjusted y; it's better, trust me.
        Vector velocity = target.subtract(location.toVector());
        entity.setVelocity(velocity.multiply(FISHING_VELOCITY_MULTIPLIER));
    }

}
