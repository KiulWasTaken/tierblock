package kiul.tierblock.user;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import kiul.tierblock.user.exceptions.InstanceAlreadyExistsException;
import lombok.Getter;

public class UserManager {

    private static UserManager instance;

    public UserManager() {
        if(instance != null) {
            throw new InstanceAlreadyExistsException(getClass());
        }
		instance = this;
        onlineUsers = new HashSet<>();
    }

    @Getter
    private Set<User> onlineUsers;
    
    public static UserManager getInstance() {
        return instance;
    }

    public User getUser(Player player) {
        for (User user : onlineUsers)
            if(user.getPlayer() == player) return user;
        return null;
    }

    public User getUser(String name) {
        for (User user : onlineUsers) 
            if(user.getName().equals(name)) return user;
        return null;
    }

}
