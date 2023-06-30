package kiul.tierblock.utils.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import kiul.tierblock.user.User;

public class MenuManager {
    
    private static MenuManager instance;

    Map<UUID, Menu> menus = new HashMap<>();
    
    public static MenuManager getInstance() {
        return instance;
    }

    public MenuManager() {
        if(instance == null) instance = this;
    }

    public void addMenu(Menu menu) {
        menus.put(menu.getUniqueId(), menu);
    }

	public void removeMenu(Menu menu) {
		menus.remove(menu.getUniqueId());
	}
	
	public void removeMenu(UUID uuid) {
		menus.remove(uuid);
	}

    public Menu getMenu(UUID uuid) {
        return menus.get(uuid);
    }

    public Menu getMenu(User user) {
        return getMenu(user.getUUID());
    }
}
