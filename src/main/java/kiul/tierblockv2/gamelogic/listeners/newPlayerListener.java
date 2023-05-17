package kiul.tierblockv2.gamelogic.listeners;

import kiul.tierblockv2.gamelogic.ColoredText;
import kiul.tierblockv2.gamelogic.globalEXP;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class newPlayerListener implements Listener {

    @EventHandler
    public void newPlayerJoin (PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (p.hasPlayedBefore() != false) { //for testing purpose, keep this !=
            globalEXP.setupExpData(p);
            globalEXP.setupLevelData(p);
        }
    }
}
