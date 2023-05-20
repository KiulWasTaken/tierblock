package kiul.tierblock;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import kiul.tierblock.commands.DevToolCommand;
import kiul.tierblock.listeners.ConnectionListener;
import kiul.tierblock.listeners.FishingListener;
import kiul.tierblock.listeners.ForagingListener;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import lombok.Getter;

public final class Main extends JavaPlugin {

    /*
     * !!! - must-do.
     * !! - general conventions so everyone has a single standard for a good working enviroment.
     * ! - note.
     * # - optional.
     * 
     * -> - follow-up
     * 
     * 
     * !!! ALWAYS USE Main.getInstance() to access something (in Main) that is not static.
     * !!! NEVER make use static UNLESS making GLOBAL constants. (Like USER_DATA_FOLDER)
     * !!! Use full caps & underscores as a naming convetion for constants:
     * !!! -> public/private static final (int or any type) SOME_CONSTANT = 42
     * !!! When adding new levels/tiers add their functions to the User class, and not somewhere else,
     * !!! -> ALSO "zero" said levels in (user/data/Stats.java) @ method copyDefaults().
     * 
     * !! Use the PascalCase naming convention for classes, and types.
     * !! Use the camelCase naming convention for variables, and functions.
     * 
     * ! When listening for events, try to use the User class for ease of use (epecially with user files/stats)
     * 
     * # use my command manager plz
     * 
     */

    private static Main instance; // Make one single instance, and use it everywhere.

    // constant... no touchies...
    public static Path USER_DATA_FOLDER;
    public static final double FISHING_VELOCITY_MULTIPLIER = 0.13; 

    public static final Map<EntityType, Double> FISHING_REWARD_ENTITIES = Map.of(  // EntityType, XP Reward (repeat)...
        EntityType.SQUID, 12.0, 
        EntityType.GLOW_SQUID, 16.0,
        EntityType.PUFFERFISH, 20.0,
        EntityType.TURTLE, 30.0,
        EntityType.DROWNED, 50.0,
        EntityType.GUARDIAN, 80.0, 
        EntityType.ELDER_GUARDIAN, 600.0
    );
	
	public static final Map<Integer, Double> FISHING_LEVEL_REQUIREMENTS = Map.of(
		2, 25.0, 3, 50.0, 4, 100.0, 5, 200.0, 6, 500.0
	);

    public static Map<Material, Double> FORAGING_REWARD_BLOCKS; // not final cuz the compiler thinks it hasn't been initialized.

    
    @Getter
    private UserManager userManager;
    
    public static Main getInstance() {
        return instance;
    } 

    @Override
    public void onEnable() {
        instance = this;

        // commands - start
        getCommand("devtools").setExecutor(new DevToolCommand());
        // commands - end

        // listeners - start
        Bukkit.getPluginManager().registerEvents(new ForagingListener(), instance);
        Bukkit.getPluginManager().registerEvents(new FishingListener(), instance);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), instance);
        // listeners - end

        // config - start 
        saveDefaultConfig();

        USER_DATA_FOLDER = new File(getInstance().getDataFolder().getAbsolutePath() + "/userdata/").toPath();

        FORAGING_REWARD_BLOCKS = Map.of(
            Material.OAK_LOG, instance.getConfig().getDouble("foraging.oak.xp_reward"), 
            Material.BIRCH_LOG, instance.getConfig().getDouble("foraging.birch.xp_reward"),
            Material.ACACIA_LOG, instance.getConfig().getDouble("foraging.acacia.xp_reward"),
            Material.DARK_OAK_LOG, instance.getConfig().getDouble("foraging.dark_oak.xp_reward"),
            Material.SPRUCE_LOG, instance.getConfig().getDouble("foraging.spruce.xp_reward"),
            Material.JUNGLE_LOG, instance.getConfig().getDouble("foraging.jungle.xp_reward"), 
            Material.CRIMSON_STEM, instance.getConfig().getDouble("foraging.crimson_stem.xp_reward"),
            Material.WARPED_STEM, instance.getConfig().getDouble("foraging.warped_stem.xp_reward")
        );
        // config - end

        // other instances - start
        userManager = new UserManager();
        // other instances - end

        // so UserManager doesn't break on reload.
        for(Player player : Bukkit.getOnlinePlayers()) {
            User user = new User(player);
            userManager.getOnlineUsers().add(user);
        }
    }

    @Override
    public void onDisable() {
        instance = null;
        userManager = null;
    }

}
