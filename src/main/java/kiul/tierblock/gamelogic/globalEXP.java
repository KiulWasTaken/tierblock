package kiul.tierblock.gamelogic;

import org.bukkit.entity.Player;

import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillType;

/**
 * literally only exists to make pat's code work
 */
@Deprecated
public class globalEXP {

    public static void add (Player p, double amount) {
        User user = UserManager.getInstance().getUser(p);
        user.addExperience(SkillType.GLOBAL, amount);
    }

}