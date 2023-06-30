package kiul.tierblock.user.skill;

import kiul.tierblock.user.User;
import lombok.Getter;

@Getter
public abstract class Skill {

    private SkillType skillType;

    public Skill(SkillType skillType) {
        this.skillType = skillType;
    }

    public abstract void levelUp(User user, double excessXp, boolean nether);
    public abstract boolean checkForLevelUp(User user, boolean nether);

    public String getLevelUpMessage(User user, boolean nether) {
        return String.format(
            "&a&l%s &2&lLEVEL-UP!\n" + 
            "&a%s &2-> &a%s&2!", // &a{old level} &2-> &a{new level}&2!
            getSkillType().toString(),
            user.getLevel(skillType, nether) - 1, // old level
            user.getLevel(skillType, nether) // new level
        );
    }

}
