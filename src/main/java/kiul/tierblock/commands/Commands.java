package kiul.tierblock.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillType;

public class Commands implements CommandExecutor,Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return false;

        User user = UserManager.getInstance().getUser((Player)sender);

        if (label.equalsIgnoreCase("givexp")) {
            if (user.isOp()) {
                double num = Double.parseDouble(args[0]);
                user.addExperience(SkillType.GLOBAL, num);
            } else {
                user.sendMessage(ChatColor.RED+"Insufficient Permissions");
            }
        } else if (label.equalsIgnoreCase("modifyvalue")) {
            if (user.isOp()) {
                String toModify = args[0];
                Integer value = Integer.parseInt(args[1]);

                user.getStats().setNumber(toModify, value);
                user.sendMessage(ChatColor.GREEN + "Successfully saved" + ChatColor.YELLOW + toModify + " as " + ChatColor.WHITE + value);
            }
        }
        return false;
    }
}