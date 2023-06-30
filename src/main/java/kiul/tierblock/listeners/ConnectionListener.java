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
        UserManager.getOnlineUsers().add(user); // delete this and i'll delete you fish head

        if(!user.getPlayer().hasPlayedBefore()) {
            user.getStats().copyDefaults();
            return;
        }

        if(user.getBooster() != 0 && user.getBooster() > System.currentTimeMillis()) {
            UserManager.getBoostedUsers().add(user);
        }else if(user.getBooster() > 0 && user.getBooster() < System.currentTimeMillis()) {
            user.setBooster(0);
            user.setBoosterMultiplier(1.0);
        }
    }

    @EventHandler
    public void playerQuitListener(PlayerQuitEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());
        UserManager.getOnlineUsers().remove(user); // prevent a massive doo doo.
        UserManager.getBoostedUsers().remove(user);
    }
}
