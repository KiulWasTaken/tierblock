package kiul.tierblockv2.gamelogic.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import kiul.tierblockv2.userData;
import kiul.tierblockv2.gamelogic.globalEXP;

public class Commands implements CommandExecutor,Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (label.equalsIgnoreCase("givexp")) {
            if (p.isOp()) {
                double num = Double.parseDouble(args[0]);
                globalEXP.add(p, num);
            } else {
                p.sendMessage(ChatColor.RED+"Insufficient Permissions");
            }
        } else if (label.equalsIgnoreCase("modifyvalue")) {
            if (p.isOp()) {
                String toModify = args[0];
                Integer value = Integer.parseInt(args[1]);

                userData.get().set(p.getUniqueId() + "." + toModify, value);
                userData.save();
                p.sendMessage(ChatColor.GREEN + "Successfully saved" + ChatColor.YELLOW + p.getUniqueId() + "." + toModify + " as " + ChatColor.WHITE + value);
            }
        }
        return false;
    }
}