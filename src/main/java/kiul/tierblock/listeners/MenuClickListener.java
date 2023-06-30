package kiul.tierblock.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.utils.item.ClickableEvent;
import kiul.tierblock.utils.menu.Menu;
import kiul.tierblock.utils.menu.MenuManager;

public class MenuClickListener implements Listener {

    @EventHandler
    public void menuClick(InventoryClickEvent event) {
        User user = UserManager.getInstance().getUser((Player)event.getWhoClicked());
        Menu clickedMenu = MenuManager.getInstance().getMenu(user.getUUID());
        
        if(clickedMenu == null) return;

        event.setCancelled(true);
        
		if(clickedMenu.getClickableEvents() == null) return;
        ClickableEvent clickEvent = clickedMenu.getClickableEvents().get(event.getRawSlot());
        if(clickEvent == null) return;

        clickEvent.fireEvent();
        
    }
	
	
	@EventHandler
	public void onClose(PlayerQuitEvent event) {
		MenuManager.getInstance().removeMenu(event.getPlayer().getUniqueId());
	}
  
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		MenuManager.getInstance().removeMenu(event.getPlayer().getUniqueId());
	}
    
}
