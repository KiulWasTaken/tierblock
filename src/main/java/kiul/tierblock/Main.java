package kiul.tierblock;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import kiul.tierblock.commands.BuyGEXP;
import kiul.tierblock.commands.Commands;
import kiul.tierblock.commands.LeaderboardCommand;
import kiul.tierblock.commands.StatsCommand;
import kiul.tierblock.commands.TestCommand;
import kiul.tierblock.listeners.CombatListener;
import kiul.tierblock.listeners.ConnectionListener;
import kiul.tierblock.listeners.FarmingListener;
import kiul.tierblock.listeners.FishingListeners;
import kiul.tierblock.listeners.ForagingListener;
import kiul.tierblock.listeners.IslandListener;
import kiul.tierblock.listeners.MenuClickListener;
import kiul.tierblock.listeners.MiningListener;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillManager;
import kiul.tierblock.utils.menu.MenuManager;
import net.md_5.bungee.api.ChatColor;
import world.bentobox.bentobox.managers.RanksManager;
import world.bentobox.bentobox.BentoBox;

/*
 * Put constants in the class they're gonna be used in.
 * If it's used across the project, put them here.
 */
public final class Main extends JavaPlugin {

    private static Main instance;
    private static World bSkyBlockWorld;

    public static final double BOOSTER_MULTIPLIER = 3.0;
	public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    public static List<Block> activeHives = new ArrayList<>();

    @Override
    public void onEnable() {

        // Why set a provider value if you're not gonna use it?
        // Bukkit.getServicesManager().getRegistration(LuckPerms.class);

        instance = this;
        
		DECIMAL_FORMAT.setRoundingMode(RoundingMode.UP);
		bSkyBlockWorld = Bukkit.getWorld("bskyblock_world");

        new UserManager();
		new MenuManager();
        new SkillManager(); // skills registered in each skill listener's constructor.

        // Prevents massive shit show after plugin reloads.
        for(Player player : Bukkit.getOnlinePlayers()) {
            User user = new User(player);
            UserManager.getOnlineUsers().add(user);

            if(user.getBooster() != 0) UserManager.getBoostedUsers().add(user);
        }

        saveDefaultConfig();
		
		Commands commands = new Commands();

        // should just put them in one command
        getCommand("resetnetherstats").setExecutor(commands);
        getCommand("untag").setExecutor(commands);
        getCommand("buygexp").setExecutor(new BuyGEXP());
		getCommand("islandflight").setExecutor(commands);
		getCommand("reloadconfig").setExecutor(commands);
        getCommand("givexp").setExecutor(commands);
		getCommand("modifydouble").setExecutor(commands);
        getCommand("modifyint").setExecutor(commands);
        getCommand("test").setExecutor(new TestCommand());
        getCommand("leaderboard").setExecutor(new LeaderboardCommand());
        getCommand("stats").setExecutor(new StatsCommand());

        Bukkit.getPluginManager().registerEvents(new MenuClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new IslandListener(), this);
        Bukkit.getPluginManager().registerEvents(new FarmingListener(), this);
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new CombatListener(), this);
        Bukkit.getPluginManager().registerEvents(new MiningListener(), this);
        Bukkit.getPluginManager().registerEvents(new FishingListeners(), this); // kiul's listener
        Bukkit.getPluginManager().registerEvents(new ForagingListener(), this); // pat's listener

		BentoBox.getInstance().getIslands().getIslands().forEach(island -> {
			BentoBox.getInstance().getIslands().save(island);
		});

        // checks player boosters & refill beehives if possible
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            UserManager.getBoostedUsers().forEach(user -> {
                if(user.getBooster() <= System.currentTimeMillis()) {
                    user.getPlayer().playSound(user.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
                    user.sendMessage("&c&lBooster is over!");
                    user.setBooster(0);
                    user.setBoosterMultiplier(1.0);
                    UserManager.getBoostedUsers().remove(user);

                    if(user.getBeehive() != null) { 
                        user.getStats().setBoolean("farming.beehive.booster_active", false);
                        activeHives.remove(user.getBeehive());
                    }
                }

                if(user.getIsland().getRank(user.getUUID()) == RanksManager.OWNER_RANK) {
                    if(user.getBeehive() == null) return;
                    
                    long nextRefill = user.getStats().getLong("farming.beehive.next_refill");
                    if(nextRefill <= System.currentTimeMillis() && nextRefill != 0L) {
                        Beehive data = (Beehive)user.getBeehive().getBlockData();
                        data.setHoneyLevel(5);
                        user.getStats().setBoolean("farming.beehive.booster_available", true);;
                        user.getStats().setNumber("farming.beehive.next_refill", 0L);
                        user.getPlayer().playSound(user.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 2, 1);
                        user.sendMessage("&a&lYour beehive is ready to harvest!");
                    }
                }

                if(user.getDebugBoard() != null) {
                    user.getDebugBoard().updateLine(5, ChatColor.translateAlternateColorCodes('&', " &8- &aTime left&8: " + user.formatBoosterTime()));
                    user.getDebugBoard().updateLine(7, ChatColor.translateAlternateColorCodes('&', " &8- &aMultiplier&8: &fx" + user.getBoosterMultiplier()));
                }
            });
        }, 0L, 1200L); // every minute
    }

    @Override
    public void onDisable() {

        UserManager.getOnlineUsers().forEach(user -> {
            if(user.getDebugBoard() != null) {
                user.getDebugBoard().delete();
                user.setDebugBoard(null);
            }
        });
		
		BentoBox.getInstance().getIslands().getIslands().forEach(island -> {
			BentoBox.getInstance().getIslands().save(island);
		});

        UserManager.getOnlineUsers().clear();
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

    public static World getBSkyBlockWorld() {
        return bSkyBlockWorld;
    }
    
}
