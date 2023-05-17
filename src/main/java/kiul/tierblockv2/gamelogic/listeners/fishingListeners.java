package kiul.tierblockv2.gamelogic.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import kiul.tierblockv2.gamelogic.globalEXP;
import org.bukkit.event.entity.EntityDeathEvent;
import kiul.tierblockv2.userData;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.ArrayList;
import java.util.Random;

public class fishingListeners implements Listener {

    @EventHandler
    public void SeaCreatureCatch (PlayerFishEvent e) {
        Player p = e.getPlayer();
        Random random = new Random();
        if (random.nextDouble(0,1)< (Double) userData.get().get(p.getUniqueId().toString() + ".seaCreatureChance")) {
            // Sea Creature Fishup Code Here
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
            Player p = (Player) e.getEntity().getKiller();
            Integer seaCreatureKills = (Integer) userData.get().get(p.getUniqueId().toString() + ".seaCreatureKills");
            if (userData.get().get(p.getUniqueId().toString() + ".seaCreatureKills") == null) {
                userData.get().set(p.getUniqueId().toString() + ".seaCreatureKills", 0);
                userData.save();
            }
            userData.get().set(p.getUniqueId().toString() + ".seaCreatureKills", seaCreatureKills+1);
            userData.save();
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
        }


    }



}
