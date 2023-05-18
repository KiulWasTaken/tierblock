package kiul.tierblockv2;

import kiul.tierblockv2.Tierblockv2;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class userData {

    public Plugin plugin = Tierblockv2.getPlugin(Tierblockv2.class);

    private static File file;
    private static FileConfiguration userDataFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("tierblockv2").getDataFolder(), "userdata.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        }
        userDataFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return userDataFile;
    }



    public static void save(){
        try {
            userDataFile.save(file);
        } catch (IOException e) {
            System.out.println("Failed to save userdata.yml");
        }
    }

    public static void reload(){
        userDataFile = YamlConfiguration.loadConfiguration(file);
    }



}

