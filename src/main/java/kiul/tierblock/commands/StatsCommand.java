package kiul.tierblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.utils.menu.impl.UserStatsMenu;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage("No console-sender implementation for this command was made!");
            return true;
        }

        User user = UserManager.getInstance().getUser((Player)sender);

        new UserStatsMenu(user);
        return false;
    }
    
}
