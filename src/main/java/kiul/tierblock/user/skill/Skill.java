package kiul.tierblock.user.skill;

import java.util.Map;

import kiul.tierblock.user.User;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

@Getter
public abstract class Skill {

    private SkillType skillType;

    private static Map<SkillType, ChatColor> skillColor = Map.of(
        SkillType.FARMING,  ChatColor.YELLOW,
        SkillType.FISHING,  ChatColor.BLUE,
        SkillType.FORAGING, ChatColor.GOLD,
        SkillType.MINING,   ChatColor.GRAY,
        SkillType.COMBAT,   ChatColor.RED
    );

    public Skill(SkillType skillType) {
        this.skillType = skillType;
    }

    public abstract void levelUp(User user, double excessXp, boolean nether);
    public abstract boolean checkForLevelUp(User user, boolean nether);

    public String getLevelUpMessage(User user, boolean nether) {
        String levelColor = nether ? "&c" : "&6";

        return String.format(
            skillColor.get(this.skillType) + "&l%s LEVEL UP! %s%s &7-> %s%s",
            getSkillType().toString(), 
            levelColor,
            user.getLevel(skillType, nether) - 1, // old level
            levelColor,
            user.getLevel(skillType, nether) // new level
        );
    }

}
