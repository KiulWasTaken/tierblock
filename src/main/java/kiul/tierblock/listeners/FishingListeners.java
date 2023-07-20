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

import java.util.Map;
import java.util.Random;

import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillManager;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.user.skill.impl.FishingSkill;

/**
 * @themightyfrogge says:
 * not efficient, i urge you to figure out how mine works and use it!
 */
public class FishingListeners implements Listener {
    
    public FishingListeners() {
        SkillManager.getInstance().registerSkill(new FishingSkill());
    }

    // fishing level up checks done automatically in User class.
    private final double FISHING_VELOCITY_MULTIPLIER = 0.13;

    // from my listener, to make actionbars hot
    private final Map<EntityType, Double> FISHING_REWARD_ENTITIES = Map.of(  // EntityType, XP Reward...
        EntityType.SQUID, 12.0, 
        EntityType.GLOW_SQUID, 16.0,
        EntityType.PUFFERFISH, 20.0,
        EntityType.TURTLE, 30.0,
        EntityType.DROWNED, 50.0,
        EntityType.GUARDIAN, 80.0, 
        EntityType.ELDER_GUARDIAN, 600.0
    );

    // spawnAndFling has no extreme change towards the event, it only flings the mob a different way, and also preventing it from dying to fall damage.
    @EventHandler
    public void SeaCreatureCatch (PlayerFishEvent e) {
        User user = UserManager.getInstance().getUser(e.getPlayer());
        Location hookLocation = e.getHook().getLocation();
        Random random = new Random();
        if(!user.hasIsland()) return;
        if (user.getGlobalLevel() >= 10) {
            if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                if (random.nextDouble(0, 1) < user.getSeaCreatureChance()) {
                    e.setCancelled(true);
                    
                    // HOT and SEXY, switch statement
                    switch (user.getLevel(SkillType.FISHING, false)) {
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
							break;
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
							break;
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

    // Has no affect on your original code, just adds actionbar.
    @EventHandler
    public void SeaCreatureKill (EntityDeathEvent e) {
        // don't check if killer is instanceof Player, it returns Player regardless. (it'll be null if killer is not a player, will cause an error.)
        if (FISHING_REWARD_ENTITIES.containsKey(e.getEntityType()) && e.getEntity().getKiller() != null) {
            User user = UserManager.getInstance().getUser(e.getEntity().getKiller());

            String unfinishedEntityName = new String(
                e.getEntityType().toString().substring(0, 1).toUpperCase() +
                e.getEntityType().toString().substring(1).toLowerCase()
            ).replace("_", " ");
    
            // capitalise first letter.
            String finishedEntityName = unfinishedEntityName.contains(" ") ?
                unfinishedEntityName.split(" ")[1].substring(0, 1).toUpperCase() 
                + unfinishedEntityName.split(" ")[1].substring(1).toLowerCase() : unfinishedEntityName;

            user.sendActionBar(
				String.format(
					"&eIsland: &2+&a%sxp " + (user.getBoosterMultiplier() > 1.0 ? "(x" + (int)user.getBoosterMultiplier() + " booster) " : "") + "&8| &eFishing: &2+&a%sxp &8(&b%s&8)",
					Main.DECIMAL_FORMAT.format(user.addGlobalExperience(FISHING_REWARD_ENTITIES.get(e.getEntityType()))),
                    Main.DECIMAL_FORMAT.format(user.addExperience(SkillType.FISHING, 1.0, false)),
					finishedEntityName
				)
			);

            user.addSeaCreatureKills(1);
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