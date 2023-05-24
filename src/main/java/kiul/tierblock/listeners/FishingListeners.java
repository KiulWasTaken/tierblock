package kiul.tierblock.listeners;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Random;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillType;

/**
 * @themightyfrogge says:
 * not efficient, i urge you to figure out how mine works and use it.
 */
public class FishingListeners implements Listener {
    
    // fishing level up checks done automatically in User class.

    private final double FISHING_VELOCITY_MULTIPLIER = 0.13;

    @EventHandler
    public void SeaCreatureCatch (PlayerFishEvent e) {
        User user = UserManager.getInstance().getUser(e.getPlayer());
        Location hookLocation = e.getHook().getLocation();
        Random random = new Random();
        if (user.getLevel(SkillType.GLOBAL) >= 10) {
            if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                if (random.nextDouble(0, 1) < user.getSeaCreatureChance()) {
                    e.setCancelled(true);
                    
                    // HOT and SEXY, switch statement
                    switch (user.getLevel(SkillType.FISHING)) {
                        case 1:
                            if (random.nextInt(1, 11) == 2) {
                                spawnAndFling(EntityType.GLOW_SQUID, hookLocation, user.getLocation());
                            } else {
                                spawnAndFling(EntityType.SQUID, hookLocation, user.getLocation());
                            }
                            break;
                        case 2:
                            switch (random.nextInt(1, 3)) {
                                case 1:
                                    if (random.nextInt(1, 11) == 2) {
                                        spawnAndFling(EntityType.GLOW_SQUID, hookLocation, user.getLocation());
                                    } else {
                                        spawnAndFling(EntityType.SQUID, hookLocation, user.getLocation());
                                    }
                                    break;
                                case 2:
                                    spawnAndFling(EntityType.PUFFERFISH, hookLocation, user.getLocation());
                                    break;
                            }
                            break;
                        case 3:
                            switch (random.nextInt(1, 4)) {
                                case 1:
                                    if (random.nextInt(1, 11) == 2) {
                                        spawnAndFling(EntityType.GLOW_SQUID, hookLocation, user.getLocation());
                                    } else {
                                        spawnAndFling(EntityType.SQUID, hookLocation, user.getLocation());
                                    }
                                    break;
                                case 2:
                                    spawnAndFling(EntityType.PUFFERFISH, hookLocation, user.getLocation());
                                    break;
                                case 3:
                                    spawnAndFling(EntityType.TURTLE, hookLocation, user.getLocation());    
                                    break;
                            }
                        case 4:
                            switch (random.nextInt(1, 5)) {
                                    case 1:
                                    if (random.nextInt(1, 11) == 2) {
                                        spawnAndFling(EntityType.GLOW_SQUID, hookLocation, user.getLocation());
                                    } else {
                                        spawnAndFling(EntityType.SQUID, hookLocation, user.getLocation());
                                    }
                                    break;
                                case 2:
                                    spawnAndFling(EntityType.PUFFERFISH, hookLocation, user.getLocation());
                                    break;
                                case 3:
                                    spawnAndFling(EntityType.TURTLE, hookLocation, user.getLocation());
                                    break;
                                case 4:
                                    spawnAndFling(EntityType.DROWNED, hookLocation, user.getLocation());
                                    break;
                            }
                            break;
                        case 5:
                            switch (random.nextInt(1, 5)) {
                                case 1:
                                    if (random.nextInt(1, 11) == 2) {
                                        spawnAndFling(EntityType.GLOW_SQUID, hookLocation, user.getLocation());
                                    } else {
                                        spawnAndFling(EntityType.SQUID, hookLocation, user.getLocation());
                                    }
                                    break;
                                case 2:
                                    spawnAndFling(EntityType.PUFFERFISH, hookLocation, user.getLocation());
                                    break;
                                case 3:
                                    spawnAndFling(EntityType.TURTLE, hookLocation, user.getLocation());
                                    break;
                                case 4:
                                    spawnAndFling(EntityType.DROWNED, hookLocation, user.getLocation());
                                    break;
                                case 5:
                                    spawnAndFling(EntityType.GUARDIAN, hookLocation, user.getLocation());
                                    break;
                            }
                        case 6:
                            switch (random.nextInt(1, 5)) {
                                case 1:
                                    if (random.nextInt(1, 11) == 2) {
                                        spawnAndFling(EntityType.GLOW_SQUID, hookLocation, user.getLocation());
                                    } else {
                                        spawnAndFling(EntityType.SQUID, hookLocation, user.getLocation());
                                    }
                                    break;
                                case 2:
                                    spawnAndFling(EntityType.PUFFERFISH, hookLocation, user.getLocation());
                                    break;
                                case 3:
                                    spawnAndFling(EntityType.TURTLE, hookLocation, user.getLocation());
                                    break;
                                case 4:
                                    spawnAndFling(EntityType.DROWNED, hookLocation, user.getLocation());
                                    break;
                                case 5:
                                    spawnAndFling(EntityType.GUARDIAN, hookLocation, user.getLocation());
                                    break;
                                case 6:
                                    spawnAndFling(EntityType.ELDER_GUARDIAN, hookLocation, user.getLocation());
                                    break;
                            }
                            break;
                    }
                    e.getHook().remove();
                }
            }
        }
    }

    @EventHandler
    public void SeaCreatureKill (EntityDeathEvent e) {
        // ??? could be a final value at the start of the class instead of being made & removed every single time an entity is killed.
        ArrayList<EntityType> fishingRewardEntities = new ArrayList<>(); 

        fishingRewardEntities.add(EntityType.DROWNED); 
        fishingRewardEntities.add(EntityType.SQUID);
        fishingRewardEntities.add(EntityType.GLOW_SQUID);
        fishingRewardEntities.add(EntityType.GUARDIAN);
        fishingRewardEntities.add(EntityType.ELDER_GUARDIAN);
        fishingRewardEntities.add(EntityType.PUFFERFISH);
        if (fishingRewardEntities.contains(e.getEntityType()) && e.getEntity().getKiller() instanceof Player) {
            User user = UserManager.getInstance().getUser(e.getEntity().getKiller());
            // removed user xp null check. won't happen.
            user.addExperience(SkillType.FISHING, 1.0);
            switch (e.getEntityType()) {
                case SQUID:
                    user.addExperience(SkillType.GLOBAL, 12);
                    break;
                case GLOW_SQUID:
                    user.addExperience(SkillType.GLOBAL, 16);
                    break;
                case PUFFERFISH:
                    user.addExperience(SkillType.GLOBAL, 20);
                    break;
                case DROWNED:
                    user.addExperience(SkillType.GLOBAL, 50);
                    break;
                case GUARDIAN:
                    user.addExperience(SkillType.GLOBAL, 80);
                    break;
                case ELDER_GUARDIAN:
                    user.addExperience(SkillType.GLOBAL, 600);
                    break;
                default: // default was missing & that cause almost 105 warnings...
                    break; 
            }
            // userData.save(); all work is done with the user's Stats instance. (automatically saves changes)
        }

    }

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