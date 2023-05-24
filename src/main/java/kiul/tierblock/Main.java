package kiul.tierblock;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import kiul.tierblock.commands.Commands;
import kiul.tierblock.listeners.ConnectionListener;
import kiul.tierblock.listeners.FarmingListener;
import kiul.tierblock.listeners.FishingListeners;
import kiul.tierblock.listeners.ForagingListeners;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;

/*
 * Put constants in the class they're gonna be used in.
 * If it's used across the project, put them here.
 */
public final class Main extends JavaPlugin {

    private static Main instance;
    private UserManager userManager; // don't access from Main, instead use: UserManager.getInstance().getUser(name or Player object);

	public static final Map<Integer, Double> FISHING_LEVEL_REQUIREMENTS = Map.of( // level, requirement... (indent if you want to)
		2, 25.0, 3, 50.0, 4, 100.0, 5, 200.0, 6, 500.0
	);

    @Override
    public void onEnable() {
        instance = this;

        userManager = new UserManager();

        // Prevents massive shit show after plugin reloads.
        for(Player player : Bukkit.getOnlinePlayers()) {
            User user = new User(player);
            userManager.getOnlineUsers().add(user);
        }

        saveDefaultConfig();

        getCommand("givexp").setExecutor(new Commands());
        getCommand("modifyvalue").setExecutor(new Commands());

        Bukkit.getPluginManager().registerEvents(new FarmingListener(), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new FishingListeners(), this); // kiul's listener
        Bukkit.getPluginManager().registerEvents(new ForagingListeners(), this); // pat's listener
    }

    @Override
    public void onDisable() {
        userManager.getOnlineUsers().clear();
        userManager = null;
    }

    public static Main getInstance() {
        return instance;
    }

    // Until a better place is figured out, these stay here...
    
    // ternary operator syntax:
    // boolean ? (something if true) : (something if false)
    // ternary operator example: 
    public static int getGlobalLevelRequirement(String path) {
        return getInstance().getConfig().contains(path + ".global_level_requirement") ? // item contains level requirement? yes: return value
            getInstance().getConfig().getInt(path + ".global_level_requirement") : 0; // no: return 0
    } 

    public static int getLevelUpRequirement(String path) {
        return getInstance().getConfig().contains(path + ".level_up") ?
            getInstance().getConfig().getInt(path + ".level_up") : 0;
    }

    public static double getXPReward(String path) {
        return getInstance().getConfig().contains(path + ".xp_reward") ?
            getInstance().getConfig().getDouble(path + ".xp_reward") : 0.0;
    }
}
