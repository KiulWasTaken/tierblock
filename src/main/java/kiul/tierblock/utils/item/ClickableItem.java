package kiul.tierblock.utils.item;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;

@Getter
public class ClickableItem {

    ItemStack itemStack;
    ClickableEvent clickableEvent;

    public ClickableItem(ItemStack itemStack, ClickableEvent clickableEvent) {
        this.itemStack = itemStack;
        this.clickableEvent = clickableEvent;
    }
    
}
