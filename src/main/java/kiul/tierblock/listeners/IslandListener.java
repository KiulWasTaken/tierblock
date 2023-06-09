package kiul.tierblock.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;

import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import world.bentobox.bentobox.api.events.island.IslandCreateEvent;
import world.bentobox.bentobox.api.events.island.IslandEnterEvent;
import world.bentobox.bentobox.api.events.island.IslandExitEvent;
import world.bentobox.bentobox.api.events.island.IslandResetEvent;
import world.bentobox.bentobox.api.events.team.TeamJoinEvent;
import world.bentobox.bentobox.api.events.team.TeamLeaveEvent;
import world.bentobox.bentobox.api.metadata.MetaDataValue;
import world.bentobox.bentobox.database.objects.Island;

public class IslandListener implements Listener {

    private void initMetaData(Island island) {
	    island.putMetaData("level", new MetaDataValue(0));
	    island.putMetaData("xp", new MetaDataValue(0.0));
        island.putMetaData("hasBeeHive", new MetaDataValue(false));
        island.putMetaData("pillagerSpawnChance", new MetaDataValue(0.02)); // 2%
        island.putMetaData("raidCaptain", new MetaDataValue(false));
    }

    @EventHandler
    public void islandCreate(IslandCreateEvent event) {
		User user = UserManager.getInstance().getUser(event.getPlayerUUID());
		user.setHasIsland(true);
		
        initMetaData(event.getIsland());
    }
    
    @EventHandler
    public void islandReset(IslandResetEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayerUUID());
        user.getIslandMembers().forEach(islandMember -> {
            islandMember.getStats().copyDefaults();
        });
        
		user.setHasIsland(true);
        initMetaData(event.getIsland());
    }

    @EventHandler
    public void islandExit(IslandExitEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayerUUID());
        if(user.isAllowedToFly()) user.getPlayer().setAllowFlight(false);
    }

    @EventHandler
    public void islandEnter(IslandEnterEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayerUUID());
        if(user.isAllowedToFly()) user.getPlayer().setAllowFlight(true);
    }

    @EventHandler
    public void islandJoin(TeamJoinEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayerUUID());
        user.setHasIsland(true);
    }

    public void islandLeave(TeamLeaveEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayerUUID());
        user.getStats().copyDefaults();
    }

    @EventHandler
    public void gameModeChange(PlayerGameModeChangeEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());

        if(event.getNewGameMode() == GameMode.SURVIVAL && user.isWithinOwnIsland()) {
            Bukkit.getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
				user.getPlayer().setAllowFlight(true);
			}, 10);
        }
    }

}
