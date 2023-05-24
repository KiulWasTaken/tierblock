package kiul.tierblock.listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import kiul.tierblock.Main;
import kiul.tierblock.gamelogic.globalEXP;
import kiul.tierblock.gamelogic.userData;


// if making spaghetti code is considered a war crime, this class would be the second biggest war crime ever committed 
@SuppressWarnings("deprecation") // Remove this when you stop using userData instead of user.getCollectedWood() & other shit
public class ForagingListeners implements Listener {

    // pat i can't be bothered properly implementing user data
    // so please, remove gamelogic & all of its files, and use the User class (which provides easier access)
    // i'll teach you how to use it if you want! (or just look into the class)

    Plugin plugin = (Plugin) Main.getPlugin(Main.class);

    @EventHandler
    public void WoodBroken (BlockBreakEvent e) {
        Player p = e.getPlayer();

        double oak = Main.getXPReward("foraging.oak");
        double birch = Main.getXPReward("foraging.birch");
        double acacia = Main.getXPReward("foraging.acacia");
        double darkoak = Main.getXPReward("foraging.dark_oak");
        double spruce = Main.getXPReward("foraging.spruce");
        double jungle = Main.getXPReward("foraging.jungle");
        double crimson = Main.getXPReward("foraging.crimson");
        double warped = Main.getXPReward("foraging.warped");

        if (!e.getBlock().hasMetadata("null")) {

            switch (e.getBlock().getType()) {
                case OAK_LOG:
                    globalEXP.add(p, oak);
                    userData.add(p, "foraging.oak.collected", 1);
                    break;
                case BIRCH_LOG:
                    if (userData.get(p).getInt("foraging.oak") >= Main.getLevelUpRequirement("foraging.oak")) {
                        globalEXP.add(p, birch);
                        userData.add(p, "foraging.birch.collected", 1);
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to mine &l" + (Main.getLevelUpRequirement("foraging.oak") - userData.get(p).getInt("foraging.oak.collected")) + " &cmore oak to mine this!")));
                    }
                    break;
                case ACACIA_LOG:
                    if (userData.get(p).getInt("foraging.birch.collected") >= Main.getLevelUpRequirement("foraging.birch")) {
                        globalEXP.add(p, acacia);
                        userData.add(p, "foraging.acacia.collected", 1);
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to mine &l" + (Main.getLevelUpRequirement("foraging.birch") - userData.get(p).getInt("foraging.birch.collected")) + " &cmore birch to mine this!")));
                    }
                    break;
                case DARK_OAK_LOG:
                    if (userData.get(p).getInt("foraging.acacia.collected") >= Main.getLevelUpRequirement("foraging.acacia")) {
                        globalEXP.add(p, darkoak);
                        userData.add(p, "foraging.dark_oak.collected", 1);
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to mine &l" + (Main.getLevelUpRequirement("foraging.acacia") - userData.get(p).getInt("foraging.acacia.collected")) + " &cmore acacia to mine this!")));
                    }
                    break;
                case SPRUCE_LOG:
                    if (userData.get(p).getInt("foraging.dark_oak.collected") >= Main.getLevelUpRequirement("foraging.dark_oak")) {
                        globalEXP.add(p, spruce);
                        userData.add(p, "foraging.spruce.collected", 1);
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to mine &l" + (Main.getLevelUpRequirement("foraging.dark_oak") - userData.get(p).getInt("foraging.dark_oak.collected")) + " &cmore dark oak to mine this!")));
                    }
                    break;
                case JUNGLE_LOG:
                    if (userData.get(p).getInt("foraging.spruce.collected") >= Main.getLevelUpRequirement("foraging.spruce")) {
                        globalEXP.add(p, jungle);
                        userData.add(p, "foraging.jungle.collected", 1);
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to mine &l" + (Main.getLevelUpRequirement("foraging.spruce") - userData.get(p).getInt("foraging.spruce.collected")) + " &cmore spruce to mine this!")));
                    }
                    break;
                case CRIMSON_STEM:
                    if (userData.get(p).getInt("global.level") >= 40) {
                        globalEXP.add(p, crimson);
                        userData.add(p, "foraging.crimson.collected", 1);
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to be level &l40&c or above to mine this!")));
                    }
                    break;
                case WARPED_STEM:
                    if (userData.get(p).getInt("foraging.crimson.collected") >= Main.getLevelUpRequirement("foraging.crimson")) {
                        globalEXP.add(p, warped);
                        userData.add(p, "foraging.warped.collected", 1);
                    } else {
                        e.setDropItems(false);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&',
                                "&cYou have to mine &l" + (Main.getLevelUpRequirement("foraging.crimson") - userData.get(p).getInt("foraging.crimson.collected")) + " &cmore crimson to mine this!")));
                    }
                    break;
                default:

            }

            //LEVEL-UP

            switch (e.getBlock().getType()) {
                case OAK_LOG:
                    if (userData.get(p).getInt("foraging.oak.collected") == Main.getLevelUpRequirement("foraging.oak")) {
                        break;
                    }
                case BIRCH_LOG:
                    if (userData.get(p).getInt("foraging.birch.collected") >= Main.getLevelUpRequirement("foraging.birch")) {
                        break;
                    }
                case ACACIA_LOG:
                    if (userData.get(p).getInt("foraging.acacia.collected") >= Main.getLevelUpRequirement("foraging.acacia")) {
                        break;
                    }
                case DARK_OAK_LOG:
                    if (userData.get(p).getInt("foraging.dark_oak.collected") >= Main.getLevelUpRequirement("foraging.dark_oak")) {
                    }
                    break;
                case SPRUCE_LOG:
                    if (userData.get(p).getInt("foraging.spruce.collected") >= Main.getLevelUpRequirement("foraging.spruce")) {
                    }
                    break;
                case JUNGLE_LOG:
                    if (userData.get(p).getInt("foraging.jungle.collected") >= Main.getLevelUpRequirement("foraging.jungle")) {
                    }
                    break;
                case CRIMSON_STEM:
                    if (userData.get(p).getInt("foraging.crimson.collected") >= Main.getLevelUpRequirement("foraging.crimson")) {
                    }
                    break;
                default:
            }
        }

    }

    @EventHandler
    public void WoodPlaced (BlockPlaceEvent e) {
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
            default:
                break;
        }

    }
}
