package kiul.tierblock.gamelogic;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;

/**
 * @deprecated only exists for pat's code to work
 */
@Deprecated
public class userData {

    public static FileConfiguration get(Player player) {
        User user = UserManager.getInstance().getUser(player);
        return user.getStats().getConfiguration();
    }

    public static void add(Player player, String key, int value) {
        User user = UserManager.getInstance().getUser(player);
        user.getStats().addInt(key, value);
    }

    public static void set(Player player, String key, Object value) {
        User user = UserManager.getInstance().getUser(player);
        user.getStats().getConfiguration().set(key, value);
        user.getStats().saveChanges();
    }
}