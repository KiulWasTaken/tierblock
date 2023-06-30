package kiul.tierblock.user.skill.impl;

import java.util.Map;
import org.bukkit.Sound;

import kiul.tierblock.user.User;
import kiul.tierblock.user.skill.Skill;
import kiul.tierblock.user.skill.SkillType;

public class FishingSkill extends Skill {

    final int MAX_LEVEL = getSkillType().maxLevel;
    private static final Map<Integer, Double> FISHING_LEVEL_REQUIREMENTS = Map.of( // level, requirement... (indent if you want to)
        2, 25.0, 3, 50.0, 4, 100.0, 5, 200.0, 6, 500.0
    );

    public FishingSkill() {
        super(SkillType.FISHING);
    }

    @Override
    public void levelUp(User user, double excessXp, boolean isNether) {
        user.addLevels(getSkillType(), 1, isNether);
        user.setExperience(getSkillType(), excessXp, isNether);
        
		user.getPlayer().playSound(user.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
        user.sendMessage(getLevelUpMessage(user, isNether));
    }

    @Override
    public boolean checkForLevelUp(User user, boolean isNether) {
        if(user.getLevel(getSkillType(), isNether) >= MAX_LEVEL) return false;

        double requirement = FISHING_LEVEL_REQUIREMENTS.get(user.getLevel(getSkillType(), isNether));
        if(user.getExperience(getSkillType(), isNether) < requirement) return false;

        double excess = user.getExperience(getSkillType(), isNether) - requirement;
        levelUp(user, excess, isNether);
        return true;
    }

    public static double getRequirement(User user) {
        return FISHING_LEVEL_REQUIREMENTS.get(user.getLevel(SkillType.FISHING, false));
    }
    
}
