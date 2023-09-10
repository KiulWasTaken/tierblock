package kiul.tierblock.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.IslandsManager;

public class LeaderboardCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        IslandsManager islandsManager = BentoBox.getInstance().getIslandsManager();
        List<Island> unfilteredIslandList = new ArrayList<>(islandsManager.getIslands().stream().toList());

        unfilteredIslandList.sort((island1, island2) -> {
            int level1 = island1.getMetaData("level").get().asInt();
            int level2 = island2.getMetaData("level").get().asInt();
            return Integer.compare(level1, level2);
        });

		// because it's gonna be ascending, we're gonna need to flip it, so we can (easily) pick the first 0 islands
        Collections.reverse(unfilteredIslandList);
		
        List<Island> topTen = unfilteredIslandList.subList(0, Math.min(unfilteredIslandList.size(), 10));

		// console user
		if(sender instanceof ConsoleCommandSender) {
            topTen.forEach(island -> {
                int islandLevel = island.getMetaData("level").isPresent() ? island.getMetaData("level").get().asInt() : 0; // to make intellij happy x2
                String ownerName = Bukkit.getOfflinePlayer(island.getOwner()).getName();
				String name = (island.getName() == null) ? ownerName + "'s island" : island.getName();
                sender.sendMessage("#1 - " + name + " | Lvl: " + islandLevel + ((island.getName() == null) ? "" : ", Owner: " + ownerName));
            });
            return true;
        }

        // This is for an in-game player:
        User user = UserManager.getInstance().getUser((Player) sender);
        for(int i = 0; i < topTen.size(); i++) {
			Island island = topTen.get(i);
            int islandLevel = island.getMetaData("level").isPresent() ? island.getMetaData("level").get().asInt() : 0; // to make intellij happy x2
            String ownerName = Bukkit.getOfflinePlayer(island.getOwner()).getName();
			String name = (island.getName() == null) ? "&a" + ownerName + "'s island" : island.getName();
            user.sendMessage("&b#" + (i+1) + " &8- &a" + name + " &2| Lvl: &a" + islandLevel + ((island.getName() == null) ? "" : "&2, Owner: &a" + ownerName));
            // ^ Remove this, if you want a GUI
            // Make ItemStack, add to GUI.
        }
        // off to warthunder i go!
        return false;
    }
    
}
