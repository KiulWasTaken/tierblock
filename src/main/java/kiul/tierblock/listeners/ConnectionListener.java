package kiul.tierblock.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;

public class ConnectionListener implements Listener {
    
    @EventHandler
    public void playerJoinListener(PlayerJoinEvent event) {
        User user = new User(event.getPlayer());
        UserManager.getInstance().getOnlineUsers().add(user); // DO NOT DELETE! will fuck up User & UserManager if deleted.

        if(user.getPlayer().hasPlayedBefore()) { // TODO: negate boolean ( ! ) when your testing purposes are over.
            user.getStats().copyDefaults(); // resets stats.
        }
    }

    @EventHandler
    public void playerQuitListener(PlayerQuitEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());
        UserManager.getInstance().getOnlineUsers().remove(user); // prevent a massive doo doo.
    }
}
