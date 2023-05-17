package kiul.tierblockv2;

import kiul.tierblockv2.gamelogic.listeners.newPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Tierblockv2 extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(new userData(),this);
        Bukkit.getPluginManager().registerEvents(new newPlayerListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
