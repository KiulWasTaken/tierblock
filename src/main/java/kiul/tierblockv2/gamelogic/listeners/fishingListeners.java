package kiul.tierblockv2.gamelogic.listeners;

import kiul.tierblockv2.gamelogic.fishingXP;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import kiul.tierblockv2.gamelogic.globalEXP;
import org.bukkit.event.entity.EntityDeathEvent;
import kiul.tierblockv2.userData;
import org.bukkit.event.player.PlayerFishEvent;

import java.security.Guard;
import java.util.ArrayList;
import java.util.Random;

public class fishingListeners implements Listener {
    
    static double fishingVelocity = -0.3;

    @EventHandler
    public void SeaCreatureCatch (PlayerFishEvent e) {
        Player p = e.getPlayer();
        Random random = new Random();
        if ((int) userData.get().get(p.getUniqueId() + ".global.level") >= 10) {
            if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
                if (random.nextDouble(0, 1) < (Double) userData.get().get(p.getUniqueId() + ".fishing.seaCreatureChance")) {
                    // Sea Creature Fishup Code Here

                    e.setCancelled(true);
                    switch ((int) userData.get().get(p.getUniqueId() + ".fishing.level")) {
                        case 1:
                            if (random.nextInt(1, 11) == 2) {
                                GlowSquid glowSquid = (GlowSquid) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.GLOW_SQUID);
                                glowSquid.setVelocity(glowSquid.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                            } else {
                                Squid squid = (Squid) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.SQUID);
                                squid.setVelocity(squid.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                            }
                            break;
                        case 2:
                            switch (random.nextInt(1, 3)) {
                                case 1:
                                    if (random.nextInt(1, 11) == 2) {
                                        GlowSquid glowSquid = (GlowSquid) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.GLOW_SQUID);
                                        glowSquid.setVelocity(glowSquid.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    } else {
                                        Squid squid = (Squid) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.SQUID);
                                        squid.setVelocity(squid.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    }
                                    break;
                                case 2:
                                    PufferFish pufferFish = (PufferFish) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.PUFFERFISH);
                                    pufferFish.setVelocity(pufferFish.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                            }
                            break;
                        case 3:
                            switch (random.nextInt(1, 4)) {
                                case 1:
                                    if (random.nextInt(1, 11) == 2) {
                                        GlowSquid glowSquid = (GlowSquid) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.GLOW_SQUID);
                                        glowSquid.setVelocity(glowSquid.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    } else {
                                        Squid squid = (Squid) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.SQUID);
                                        squid.setVelocity(squid.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    }
                                    break;
                                case 2:
                                    PufferFish pufferFish = (PufferFish) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.PUFFERFISH);
                                    pufferFish.setVelocity(pufferFish.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                                case 3:
                                    Turtle turtle = (Turtle) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.TURTLE);
                                    turtle.setVelocity(turtle.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                            }
                        case 4:
                            switch (random.nextInt(1, 5)) {
                                case 1:
                                    if (random.nextInt(1, 11) == 2) {
                                        GlowSquid glowSquid = (GlowSquid) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.GLOW_SQUID);
                                        glowSquid.setVelocity(glowSquid.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    } else {
                                        Squid squid = (Squid) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.SQUID);
                                        squid.setVelocity(squid.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    }
                                    break;
                                case 2:
                                    PufferFish pufferFish = (PufferFish) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.PUFFERFISH);
                                    pufferFish.setVelocity(pufferFish.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                                case 3:
                                    Turtle turtle = (Turtle) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.TURTLE);
                                    turtle.setVelocity(turtle.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                                case 4:
                                    Drowned drowned = (Drowned) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.DROWNED);
                                    drowned.setVelocity(drowned.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                            }
                            break;
                        case 5:
                            switch (random.nextInt(1, 5)) {
                                case 1:
                                    if (random.nextInt(1, 11) == 2) {
                                        GlowSquid glowSquid = (GlowSquid) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.GLOW_SQUID);
                                        glowSquid.setVelocity(glowSquid.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    } else {
                                        Squid squid = (Squid) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.SQUID);
                                        squid.setVelocity(squid.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    }
                                    break;
                                case 2:
                                    PufferFish pufferFish = (PufferFish) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.PUFFERFISH);
                                    pufferFish.setVelocity(pufferFish.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                                case 3:
                                    Turtle turtle = (Turtle) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.TURTLE);
                                    turtle.setVelocity(turtle.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                                case 4:
                                    Drowned drowned = (Drowned) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.DROWNED);
                                    drowned.setVelocity(drowned.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                                case 5:
                                    Guardian guardian = (Guardian) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.GUARDIAN);
                                    guardian.setVelocity(guardian.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                            }
                        case 6:
                            switch (random.nextInt(1, 5)) {
                                case 1:
                                    if (random.nextInt(1, 11) == 2) {
                                        GlowSquid glowSquid = (GlowSquid) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.GLOW_SQUID);
                                        glowSquid.setVelocity(glowSquid.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    } else {
                                        Squid squid = (Squid) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.SQUID);
                                        squid.setVelocity(squid.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    }
                                    break;
                                case 2:
                                    PufferFish pufferFish = (PufferFish) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.PUFFERFISH);
                                    pufferFish.setVelocity(pufferFish.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                                case 3:
                                    Turtle turtle = (Turtle) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.TURTLE);
                                    turtle.setVelocity(turtle.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                                case 4:
                                    Drowned drowned = (Drowned) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.DROWNED);
                                    drowned.setVelocity(drowned.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                                case 5:
                                    Guardian guardian = (Guardian) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.GUARDIAN);
                                    guardian.setVelocity(guardian.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
                                    break;
                                case 6:
                                    ElderGuardian elderGuardian = (ElderGuardian) p.getWorld().spawnEntity(e.getHook().getLocation(), EntityType.ELDER_GUARDIAN);
                                    elderGuardian.setVelocity(elderGuardian.getLocation().toVector().subtract(p.getLocation().toVector()).clone().normalize().multiply(fishingVelocity));
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

        ArrayList<EntityType> fishingRewardEntities = new ArrayList();
        fishingRewardEntities.add(EntityType.DROWNED);
        fishingRewardEntities.add(EntityType.SQUID);
        fishingRewardEntities.add(EntityType.GLOW_SQUID);
        fishingRewardEntities.add(EntityType.GUARDIAN);
        fishingRewardEntities.add(EntityType.ELDER_GUARDIAN);
        fishingRewardEntities.add(EntityType.PUFFERFISH);
        if (fishingRewardEntities.contains(e.getEntityType()) && e.getEntity().getKiller() instanceof Player) {
            Player p = e.getEntity().getKiller();
            if (userData.get().get(p.getUniqueId() + ".fishing.xp") == null) {
                userData.get().set(p.getUniqueId() + ".fishing.xp", 0);
                userData.save();
            }
            fishingXP.addFishingXP(p,1);
            switch (e.getEntityType()) {
                case SQUID:
                    globalEXP.add(p,(double) 12);
                    break;
                case GLOW_SQUID:
                    globalEXP.add(p,(double) 16);
                    break;
                case PUFFERFISH:
                    globalEXP.add(p,(double) 20);
                    break;
                case DROWNED:
                    globalEXP.add(p,(double) 50);
                    break;
                case GUARDIAN:
                    globalEXP.add(p,(double) 80);
                    break;
                case ELDER_GUARDIAN:
                    globalEXP.add(p,(double) 600);
                    break;
            }
            userData.save();
        }


    }



}
