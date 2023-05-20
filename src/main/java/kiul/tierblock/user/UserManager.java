package kiul.tierblock.user;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import kiul.tierblock.Main;
import lombok.Getter;

public class UserManager {

    @Getter
    private List<User> onlineUsers;
    
    public static UserManager getInstance() {
        return Main.getInstance().getUserManager();
    }

    public UserManager() {
        onlineUsers = new ArrayList<>();
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
