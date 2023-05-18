package kiul.tierblockv2;

import kiul.tierblockv2.gamelogic.commands.Commands;
import kiul.tierblockv2.gamelogic.listeners.ForagingListeners;
import kiul.tierblockv2.gamelogic.listeners.fishingListeners;
import kiul.tierblockv2.gamelogic.listeners.newPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tierblockv2 extends JavaPlugin {

    public static Plugin plugin = Tierblockv2.getPlugin(Tierblockv2.class);

    @Override
    public void onEnable() {

        saveDefaultConfig();

        userData.setup();
        userData.save();
        getCommand("givexp").setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(new newPlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new fishingListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ForagingListeners(), this);

    }

    @Override
    public void onDisable() {



    }
}
