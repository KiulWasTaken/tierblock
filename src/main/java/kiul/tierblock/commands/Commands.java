package kiul.tierblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillType;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage("No console-sender implementation for this command was made!");
            return false;
        }

        User user = UserManager.getInstance().getUser((Player)sender);

        switch(label.toLowerCase()) {
            case "islandflight":
                if(user.getLevel(SkillType.FORAGING, false) == SkillType.FORAGING.maxLevel) {
                    user.sendMessage("&cYou need &eforaging level 6 &cto unlock flight!");
                    return false;
                }
                user.setFlight(!user.isAllowedToFly());
                user.sendMessage("&aFlight has been" + (user.isAllowedToFly() ? "&2enabled" : "&cdisabled"));
        }

        if(!user.isOp()) {
            user.sendMessage("&cInsufficient permissions!");
            return false;
        }

        switch(label) {
            case "givexp":
                if(args[0].equalsIgnoreCase("help")) {
                    user.sendMessage("&aUsage: /givexp &7<amount> <(optional)skill index> <(optional)true/false for nether>");
                    user.sendMessage("&aSkill indices:");
                    for (int i = 0; i < SkillType.values().length; i++) {
                        user.sendMessage(" &8- &e" + SkillType.values()[i] + "&8, &bindex: " + i);
                    }
                    return true;
                }
                
                boolean isNether = false;

                if(args.length > 2) {
                    isNether = Boolean.parseBoolean(args[2]);
                }

                double xp = Double.parseDouble(args[0]);
            
                if(args.length > 1) {
                    int index = Integer.parseInt(args[1]);
                    SkillType skillType = SkillType.values()[index];
                    user.addExperience(skillType, xp, isNether);
                    user.sendMessage("&aAdded &e" + xp + "xp &ato &e" + skillType.toString());
                    return true;
                }

                user.setGlobalExperience(user.getGlobalExperience() + xp); // to avoid booster multipliers
                user.sendMessage("&aAdded &e" + xp + "xp &ato &eyour island");
                return true;
                
            case "modifyint":
                String intConfigPlace = args[0];
                int intValue = Integer.parseInt(args[1]);

                if(!user.getStats().getConfiguration().contains(intConfigPlace)) {
                    user.sendMessage("&e" + intConfigPlace + " &cdoesn't exist!");
                    return true;
                }

                user.getStats().setNumber(intConfigPlace, intValue);
                return true;
                

            case "modifydouble":
                String configPlace = args[0];
                double value = Double.parseDouble(args[1]);

                if(!user.getStats().getConfiguration().contains(configPlace)) {
                    user.sendMessage("&e" + configPlace + " &cdoesn't exist!");
                    return true;
                }

                user.getStats().setNumber(configPlace, value);
                return true;
            }
        
        return false;
    }
    
}
