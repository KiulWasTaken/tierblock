package kiul.tierblock.utils.item;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ItemBuilder {

    ItemStack itemStack;
    ItemMeta itemMeta;

    ClickableEvent clickableEvent;

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder displayName(String displayName) {
        itemMeta.setDisplayName(
                ChatColor.translateAlternateColorCodes('&', displayName));
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder lore(List<String> lore) {
        List<String> formattedList = new ArrayList<>();

        lore.forEach(string -> {
            formattedList.add(ChatColor.translateAlternateColorCodes('&', string));
        });

        itemMeta.setLore(formattedList);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level, boolean illegalEnchants) {
        itemMeta.addEnchant(enchantment, level, illegalEnchants);
        return this;
    }

    public ClickableItem buildAsClickable(ClickableEvent clickEvent) {
        clickableEvent = clickEvent;
        return new ClickableItem(build(), clickEvent);
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
