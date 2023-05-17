package kiul.tierblockv2.gamelogic.listeners;

import kiul.tierblockv2.Tierblockv2;
import kiul.tierblockv2.gamelogic.globalEXP;
import kiul.tierblockv2.userData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Random;

public class ForagingListeners implements Listener {

    Plugin plugin = (Plugin) Tierblockv2.getPlugin(Tierblockv2.class);

    @EventHandler
    public void WoodBroken (BlockBreakEvent e) {
        Player p = e.getPlayer();

        double oak = plugin.getConfig().getDouble("Oak");
        double birch = plugin.getConfig().getDouble("Birch");
        double acacia = plugin.getConfig().getDouble("Acacia");
        double darkoak = plugin.getConfig().getDouble("Dark_Oak");
        double spruce = plugin.getConfig().getDouble("Spruce");
        double jungle = plugin.getConfig().getDouble("Jungle");
        double crimson = plugin.getConfig().getDouble("Crimson");
        double warped = plugin.getConfig().getDouble("Crimson");

        if (!e.getBlock().hasMetadata("null")) {

            switch (e.getBlock().getType()) {
                case OAK_LOG:
                    globalEXP.add(p, oak);
                    userData.get().set(p.getUniqueId() + ".Foraging.Oak", userData.get().getInt(p.getUniqueId() + ".Foraging.Oak") + 1);
                    userData.save();
                    break;
                case BIRCH_LOG:
                    if (userData.get().getInt(p.getUniqueId() + ".Foraging.Oak") >= (plugin.getConfig().getInt("Oak>Birch"))) {
                        globalEXP.add(p, birch);
                        userData.get().set(p.getUniqueId() + ".Foraging.Birch", userData.get().getInt(p.getUniqueId() + ".Foraging.Birch") + 1);
                        userData.save();
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to mine &l" + (plugin.getConfig().getInt("Oak>Birch") - userData.get().getInt(p.getUniqueId() + ".Foraging.Oak")) + " &cmore oak to mine this!")));
                    }
                    break;
                case ACACIA_LOG:
                    if (userData.get().getInt(p.getUniqueId() + ".Foraging.Birch") >= plugin.getConfig().getInt("Birch>Acacia")) {
                        globalEXP.add(p, acacia);
                        userData.get().set(p.getUniqueId() + ".Foraging.Acacia", userData.get().getInt(p.getUniqueId() + ".Foraging.Acacia") + 1);
                        userData.save();
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to mine &l" + (plugin.getConfig().getInt("Birch>Acacia") - userData.get().getInt(p.getUniqueId() + ".Foraging.Birch")) + " &cmore birch to mine this!")));
                    }
                    break;
                case DARK_OAK_LOG:
                    if (userData.get().getInt(p.getUniqueId() + ".Foraging.Acacia") >= plugin.getConfig().getInt("Acacia>Dark_Oak")) {
                        globalEXP.add(p, darkoak);
                        userData.get().set(p.getUniqueId() + ".Foraging.Dark_Oak", userData.get().getInt(p.getUniqueId() + ".Foraging.Dark_Oak") + 1);
                        userData.save();
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to mine &l" + (plugin.getConfig().getInt("Acacia>Dark_Oak") - userData.get().getInt(p.getUniqueId() + ".Foraging.Acacia")) + " &cmore acacia to mine this!")));
                    }
                    break;
                case SPRUCE_LOG:
                    if (userData.get().getInt(p.getUniqueId() + ".Foraging.Dark_Oak") >= plugin.getConfig().getInt("Dark_Oak>Spruce")) {
                        globalEXP.add(p, spruce);
                        userData.get().set(p.getUniqueId() + ".Foraging.Spruce", userData.get().getInt(p.getUniqueId() + ".Foraging.Spruce") + 1);
                        userData.save();
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to mine &l" + (plugin.getConfig().getInt("Dark_Oak>Spruce") - userData.get().getInt(p.getUniqueId() + ".Foraging.Dark_Oak")) + " &cmore dark oak to mine this!")));
                    }
                    break;
                case JUNGLE_LOG:
                    if (userData.get().getInt(p.getUniqueId() + ".Foraging.Spruce") >= plugin.getConfig().getInt("Spruce>Jungle")) {
                        globalEXP.add(p, jungle);
                        userData.get().set(p.getUniqueId() + ".Foraging.Spruce", userData.get().getInt(p.getUniqueId() + ".Foraging.Spruce") + 1);
                        userData.save();
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to mine &l" + (plugin.getConfig().getInt("Spruce>Jungle") - userData.get().getInt(p.getUniqueId() + ".Foraging.Spruce")) + " &cmore spruce to mine this!")));
                    }
                    break;
                case CRIMSON_STEM:
                    if (userData.get().getInt(p.getUniqueId() + ".globalLevel") >= 40) {
                        globalEXP.add(p, crimson);
                        userData.get().set(p.getUniqueId() + ".Foraging.Crimson", userData.get().getInt(p.getUniqueId() + ".Foraging.Crimson") + 1);
                        userData.save();
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to be level &l40&c or above to mine this!")));
                    }
                    break;
                case WARPED_STEM:
                    if (userData.get().getInt(p.getUniqueId() + ".Foraging.Crimson") >= plugin.getConfig().getInt("Crimson>Warped")) {
                        globalEXP.add(p, warped);
                        userData.get().set(p.getUniqueId() + ".Foraging.Warped", userData.get().getInt(p.getUniqueId() + ".Foraging.Warped") + 1);
                        userData.save();
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to mine &l" + (plugin.getConfig().getInt("Crimson>Warped") - userData.get().getInt(p.getUniqueId() + ".Foraging.Crimson")) + " &cmore crimson to mine this!")));
                    }
                    break;

            }

            //LEVEL-UP

            switch (e.getBlock().getType()) {
                case OAK_LOG:
                    if (userData.get().getInt(p.getUniqueId() + ".Foraging.Oak") == (plugin.getConfig().getInt("Oak>Birch"))) {
                        break;
                    }
                case BIRCH_LOG:
                    if (userData.get().getInt(p.getUniqueId() + ".Foraging.Birch") >= plugin.getConfig().getInt("Birch>Acacia")) {
                        break;
                    }
                case ACACIA_LOG:
                    if (userData.get().getInt(p.getUniqueId() + ".Foraging.Acacia") >= plugin.getConfig().getInt("Acacia>Dark_Oak")) {
                        break;
                    }
                case DARK_OAK_LOG:
                    if (userData.get().getInt(p.getUniqueId() + ".Foraging.Dark_Oak") >= plugin.getConfig().getInt("Dark_Oak>Spruce")) {
                    }
                    break;
                case SPRUCE_LOG:
                    if (userData.get().getInt(p.getUniqueId() + ".Foraging.Spruce") >= (plugin.getConfig().getInt("Spruce>Jungle"))) {
                    }
                    break;
                case JUNGLE_LOG:
                    if (userData.get().getInt(p.getUniqueId() + ".Foraging.Jungle") >= (plugin.getConfig().getInt("Jungle>Flight"))) {
                    }
                    break;
                case CRIMSON_STEM:
                    if (userData.get().getInt(p.getUniqueId() + ".Foraging.Jungle") >= plugin.getConfig().getInt("Crimson>Warped")) {
                    }
                    break;
            }
        }

    }

    @EventHandler
    public void WoodPlaced (BlockPlaceEvent e) {
        Player p = e.getPlayer();

        switch (e.getBlock().getType()) {
            case OAK_LOG:
                e.getBlock().setMetadata("null", new FixedMetadataValue(plugin, "uwu"));
                break;
            case BIRCH_LOG:
                e.getBlock().setMetadata("null", new FixedMetadataValue(plugin, "uwu"));
                break;
            case ACACIA_LOG:
                e.getBlock().setMetadata("null", new FixedMetadataValue(plugin, "uwu"));
                break;
            case DARK_OAK_LOG:
                e.getBlock().setMetadata("null", new FixedMetadataValue(plugin, "uwu"));
                break;
            case SPRUCE_LOG:
                e.getBlock().setMetadata("null", new FixedMetadataValue(plugin, "uwu"));
                break;
            case JUNGLE_LOG:
                e.getBlock().setMetadata("null", new FixedMetadataValue(plugin, "uwu"));
                break;
            case CRIMSON_STEM:
                e.getBlock().setMetadata("null", new FixedMetadataValue(plugin, "uwu"));
                break;
            case WARPED_STEM:
                e.getBlock().setMetadata("null", new FixedMetadataValue(plugin, "uwu"));
                break;
        }

    }
}
