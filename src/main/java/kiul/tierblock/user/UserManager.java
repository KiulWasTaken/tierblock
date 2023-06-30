package kiul.tierblock.user;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import kiul.tierblock.user.exceptions.InstanceAlreadyExistsException;
import lombok.Getter;

public class UserManager {
    
    //one instance only
    private static UserManager instance;

    public UserManager() {
        if(instance != null) {
            throw new InstanceAlreadyExistsException(getClass());
        }
		instance = this;
        onlineUsers = new HashSet<>();

        onlineUsers.forEach(user -> {
            user.getBooster();
        });
        boostedUsers = new HashSet<>();
    }
    
    @Getter private static Set<User> onlineUsers;
    @Getter private static Set<User> boostedUsers;

    public static UserManager getInstance() {
        return instance;
    }

    public User getUser(Player player) {
        for (User user : onlineUsers)
            if(user.getPlayer() == player) return user;
        return null;
    }

    public User getUser(UUID uuid) {
        for (User user : onlineUsers)
            if(user.getUUID().equals(uuid)) return user;
        return null;
    }

    public User getUser(String name) {
        for (User user : onlineUsers) 
            if(user.getName().equals(name)) return user;
        return null;
    }

}
