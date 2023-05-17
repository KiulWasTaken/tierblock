package kiul.tierblockv2;

import kiul.tierblockv2.gamelogic.listeners.ForagingListeners;
import kiul.tierblockv2.gamelogic.listeners.fishingListeners;
import kiul.tierblockv2.gamelogic.listeners.newPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tierblockv2 extends JavaPlugin {

    @Override
    public void onEnable() {

        saveDefaultConfig();

        userData.setup();
        userData.save();

        Bukkit.getPluginManager().registerEvents(new userData(),this);
        Bukkit.getPluginManager().registerEvents(new newPlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new fishingListeners(), this);
        Bukkit.getPluginManager().registerEvents(new ForagingListeners(), this);

    }

    @Override
    public void onDisable() {



    }
}
