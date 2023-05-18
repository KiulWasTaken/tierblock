package kiul.tierblockv2.gamelogic.listeners;

import kiul.tierblockv2.Tierblockv2;
import kiul.tierblockv2.gamelogic.ColoredText;
import kiul.tierblockv2.gamelogic.globalEXP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import kiul.tierblockv2.gamelogic.fishingXP;
import kiul.tierblockv2.userData;

public class newPlayerListener implements Listener {

    @EventHandler
    public void newPlayerJoin (PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (p.hasPlayedBefore() != false) { //for testing purpose, keep this !=
            globalEXP.setupExpData(p);
            globalEXP.setupLevelData(p);
            fishingXP.setupfishingXPData(p);
            fishingXP.setupFishingLevelData(p);
        }
    }
}
