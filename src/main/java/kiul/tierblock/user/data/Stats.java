package kiul.tierblock.user.data;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.utils.enums.CropType;
import kiul.tierblock.utils.enums.WoodType;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Stats {

    private final File USER_DATA_FOLDER = new File(Main.getInstance().getDataFolder() + "/userdata");

    private final File userFile;
    private final User user;
    private FileConfiguration configuration;

    /**
     * Makes file if it doesn't exist.
     * @param user
     */
    public Stats(@NonNull User user) {
        this.user = user;
        this.userFile = new File(USER_DATA_FOLDER.toString(), user.getUUID().toString() + ".yml");
        
        if(userFile.exists()) {
			this.configuration = YamlConfiguration.loadConfiguration(userFile);	
			return;
        }
		if(!USER_DATA_FOLDER.exists()) USER_DATA_FOLDER.mkdirs();
		
        try {
            userFile.createNewFile();
			this.configuration = YamlConfiguration.loadConfiguration(userFile);
            copyDefaults();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    /**
     * Resets player data that's specified in <code>map</code>  
     * @param map
     */
    public void copyDefaults(@NonNull Map<String, Object> map) {
        map.forEach((key, value) -> configuration.set(key, value));
    }

    /**
     * Resets the player's stats to zero (levels to 1).
     */
    public void copyDefaults() {

        configuration.set("global.level", 1);
        configuration.set("global.xp", 0.0);

        configuration.set("fishing.level", 1);
        configuration.set("fishing.xp", 0.0);
        configuration.set("fishing.sea_creature_chance", 0.0);
		
        configuration.set("combat.level", 1);
        configuration.set("combat.xp", 0.0);

		configuration.set("mining.level", 1);
		configuration.set("mining.xp", 0.0);
		
		configuration.set("farming.level", 1);
		configuration.set("farming.xp", 0.0);
        for (CropType cropType : CropType.values()) {
            configuration.set("farming." + cropType.toString().toLowerCase() + ".collected", 0);
        }
        
		configuration.set("foraging.level", 1);
		configuration.set("foraging.xp", 0.0);
        for (WoodType woodType : WoodType.values()) {
            configuration.set("foraging." + woodType.toString().toLowerCase() + ".collected", 0);
        }
        
		saveChanges();
    }

    /** Shitty wrapper method */
    public void saveChanges() {
        try {
            configuration.save(userFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 

    public Object get(String path) {
        return configuration.get(path);
    }

    public int getInt(String path) {
        return configuration.getInt(path);
    }

    public double getDouble(String path) {
        return configuration.getDouble(path);
    }

    public String getString(String path) {
        return configuration.getString(path);
    }
    
    /**
     * Set a value of {@code path} to string.
     * @param path (I.E. The path of the string in the user's data file)
     * @param str (I.E. The string that will replace the old value.)
     */
    public void setString(String path, String str) {
        configuration.set(path, str);
        saveChanges();
    }

    /**
     * Set a value of {@code path} to any number (int, double, float, etc...)
     * @param path (I.E. The path of the number in the user's data file)
     * @param number (I.E. The number that will replace the old value.)
     */
    public void setNumber(String path, Number number) {
        configuration.set(path, number);
        saveChanges();
    }

    /**
     * Adds two integers together.
     * @param path (I.E. The path of the number in the user's data file)
     * @param leftHandSide
     */
    public void addInt(String path, int leftHandSide) {
        if(!(configuration.get(path) instanceof Integer)) 
            throw new ArithmeticException("Can't add an integer with something that's not a integer!");
        configuration.set(path, (configuration.getInt(path) + leftHandSide));
		saveChanges();
    }

    /**
     * Adds two doubles together.
     * @param path (I.E. The path of the number in the user's data file)
     * @param leftHandSide
     */
    public void addDouble(String path, double leftHandSide) {
        if(!(configuration.get(path) instanceof Double))
            throw new ArithmeticException("Can't add a double with something that's not a double!");
        configuration.set(path, (configuration.getDouble(path) + leftHandSide));
		saveChanges();
    }

}
