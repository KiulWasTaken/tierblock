package kiul.tierblock.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.patriciachips.tiercobblegen.Commands.Eco;
import org.patriciachips.tiercobblegen.CustomConfigs.PlayerDataConfig;

import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;

public class BuyGEXP implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            User user = UserManager.getInstance().getUser((Player)sender);

            if(user == null)
                return false;

            double amountToBeBought = Double.parseDouble(args[0]);
            double priceForEach = 2.0; // <--- Pat/kiul, change this.
            double finalPrice = amountToBeBought * priceForEach;

            double balance = PlayerDataConfig.get().getDouble(user.getUUID().toString() + ".bal");

            if(balance < finalPrice) {
                user.sendMessage("&cNot enough balance, you need &e" + (finalPrice - balance) + " &cmore balance!");
                return false;
            }
            
            Eco.changeBalance(null, user.getUUID(), "subtract", finalPrice);
            return true;
        }

        sender.sendMessage("No implementation for console.");
        return false;
    }

}