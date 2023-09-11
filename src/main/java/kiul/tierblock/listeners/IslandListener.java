package kiul.tierblock.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World.Environment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;

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
import world.bentobox.bentobox.managers.RanksManager;

public class IslandListener implements Listener {

    private void initMetaData(Island island) {
        island.putMetaData("level", new MetaDataValue(1));
        island.putMetaData("xp", new MetaDataValue(0.0));
        island.putMetaData("hasBeeHive", new MetaDataValue(false));
        island.putMetaData("beehivePlacedBefore", new MetaDataValue(false));
        island.putMetaData("pillagerSpawnChance", new MetaDataValue(0.001)); // 0.1%
        island.putMetaData("raidCaptain", new MetaDataValue(false));
    }

    @EventHandler
    public void islandCreate(IslandCreateEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayerUUID());
        user.setHasIsland(true);
        if(event.getIsland().getName() == null) {
            event.getIsland().setName(user.getName() + "'s island");
        }
        
        initMetaData(event.getIsland());
    }

    @EventHandler
    public void islandReset(IslandResetEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayerUUID());
        user.getIslandMembers().forEach(islandMember -> {
            islandMember.getStats().copyDefaults();
        });

        if(event.getIsland().getName() == null) {
            event.getIsland().setName(user.getName() + "'s island");
        }
        
        user.setHasIsland(true);
        initMetaData(event.getIsland());
    }

    @EventHandler
    public void islandExit(IslandExitEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayerUUID());
        if(user.getPlayer().getGameMode() == GameMode.SURVIVAL && user.isAllowedToFly()) {
            user.setFlight(false);
        }
    }

    // useless for now.
    public void islandEnter(IslandEnterEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayerUUID());
        if(user.isAllowedToFly()) user.getPlayer().setAllowFlight(true);
        if(event.getIsland().getRank(user.getUUID()) < RanksManager.MEMBER_RANK) {
            boolean hasMeta = user.getIsland().getMetaData().get().containsKey("beehivePlacedBefore");
            if(hasMeta)
                user.getIsland().putMetaData("beehivePlacedBefore", new MetaDataValue(false));
        }
    }

    @EventHandler
    public void islandJoin(TeamJoinEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayerUUID());
        user.setHasIsland(true);
    }

    @EventHandler
    public void spawnerEvent(SpawnerSpawnEvent event) {
        event.setCancelled(true);
    }


    // 10/9, 2023: This would've fucked up players, if implemented the way it was...
    // Luckily, I forgot to mark it with EventHandler annotation.
    // It works the way it should rn.
    @EventHandler
    public void islandLeave(TeamLeaveEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayerUUID());
        int rank = event.getIsland().getRank(user.getUUID());
        if(rank >= RanksManager.MEMBER_RANK && rank <= RanksManager.OWNER_RANK) {
            user.getStats().copyDefaults();
        }
    }

    @EventHandler
    public void islandDeath(PlayerDeathEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());

        if(user.getIslandAtPosition() == null) {
            user.teleport(Bukkit.getWorld("world").getSpawnLocation());
            return;
        }

        if(user.getIslandAtPosition().equals(user.getIsland()))
            return;

        int rank = user.getIslandAtPosition().getRank(user.getUUID());

        if(rank < RanksManager.COOP_RANK && user.hasIsland()) {
            user.teleport(user.getIsland().getSpawnPoint(Environment.NORMAL));
            return;
        }

        user.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());

        if(user.getLocation().getY() > -64 /* Could be wrong */) return;

        if(user.getIslandAtPosition() == null) {
            user.setFlight(true);
            user.teleport(Bukkit.getWorld("world").getSpawnLocation());
            user.setFlight(false);
            return;
        }

        if(user.getIslandAtPosition().equals(user.getIsland()))
            return;

        int rank = user.getIslandAtPosition().getRank(user.getUUID());

        if(rank < RanksManager.COOP_RANK && user.hasIsland()) {
            user.setFlight(true);
            user.teleport(user.getIsland().getSpawnPoint(Environment.NORMAL));
            return;
        }

        user.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }

    /*

    @EventHandler
    public void gameModeChange(PlayerGameModeChangeEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());

        if(event.getNewGameMode() == GameMode.SURVIVAL && user.isWithinOwnIsland()) {
            Bukkit.getServer().getScheduler().runTaskLater(Main.getInstance(), () -> {
                user.getPlayer().setAllowFlight(true);
            }, 10);
        }
    }


    */

}
