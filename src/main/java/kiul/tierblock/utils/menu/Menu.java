package kiul.tierblock.utils.menu;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import kiul.tierblock.user.User;
import kiul.tierblock.utils.item.ClickableEvent;
import kiul.tierblock.utils.item.ClickableItem;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

@Getter
public abstract class Menu {
    
    public static enum InventorySize {
        SMALL(9), MEDIUM(27), BIG(54);

        int size;

        InventorySize(int size) {
            this.size = size;
        }

        public int toInt() {
            return size;
        }
    }

    private Inventory inventory;
    private UUID uniqueId;
    private Map<Integer, ClickableEvent> clickableEvents;

    public Menu(User user, String displayName, InventorySize size) {
        this.inventory = Bukkit.createInventory(null, 
            size.toInt(), 
            ChatColor.translateAlternateColorCodes('&', displayName)
        );
		
		this.clickableEvents = new HashMap<>();

        uniqueId = user.getUUID();
        MenuManager.getInstance().addMenu(this);
    }

    public void addItem(ClickableItem clickableItem) {
        clickableEvents.put(inventory.firstEmpty(), clickableItem.getClickableEvent());
        inventory.addItem(clickableItem.getItemStack());
    }

    public void addItem(ItemStack itemStack) {
        inventory.addItem(itemStack);
    }

    public void setItem(ItemStack itemStack, int position) {
        inventory.setItem(position, itemStack);
    }
	
	public void fillRestWith(ItemStack stack) {
		for(int i = 0; i < inventory.getSize(); i++) {
			if(inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR) {
				setItem(stack, i);
			}
		}
	}

    public void show(User user) {
        user.getPlayer().openInventory(inventory);
    }

}
