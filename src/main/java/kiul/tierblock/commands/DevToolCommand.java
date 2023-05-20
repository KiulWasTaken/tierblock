package kiul.tierblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;

public class DevToolCommand implements CommandExecutor{

    // imagine not using frogge's simple, and giga-chad command repository

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            if(args[0].equalsIgnoreCase("val")) {
                sender.sendMessage(
                    args[1] + " = " + Main.getInstance().getConfig().get(args[1])
                );
            }else {
                Main.FORAGING_REWARD_BLOCKS.forEach((k, v) -> {
                    sender.sendMessage(String.format("Key: %s, Value: %s", k.toString(), v));
                });
            }
            return true;
        }
        User user = UserManager.getInstance().getUser((Player)sender);

        if(!user.isOp()) {
            user.sendMessage("&cInsufficient permissions!");
            return false;
        }
        
        if(args.length < 2) {
            user.sendMessage("&cInsufficient arguments!");
            return false;
        }
        
        switch(args[0].toLowerCase()) { // always do toLowerCase() to avoid case-sensitive sub-commands!
            case "givexp":
                user.addGlobalExperience(Double.parseDouble(args[1]));
                user.sendMessage("&aAdded &e" + Double.parseDouble(args[1]) + " &ato your global experience!");
                break;
			case "get":
				user.sendMessage("returned: " + user.getStats().get(args[1]));
				break;
			case "set": 
				user.getStats().addDouble("fishing.sea_creature_chance", 1.0);
				user.addGlobalLevel(18);
				break;
            case "modifyvalue": // should be named modifyint
                int newValue = Integer.parseInt(args[2]);
                user.getStats().setNumber(args[1], newValue);
                user.sendMessage(
                    String.format("&aChanged value &e%s &ato &e%s&a!", args[0], newValue)
                );
                break;
            }
        return true;
    }
    
}
