package kiul.tierblock.user.skill.impl;

import java.util.Map;
import java.lang.Math;

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
        user.addLevels(getSkillType(), 1, false);
        user.setExperience(getSkillType(), excessXp, false);
        
        if(checkForLevelUp(user, isNether)) return;

        user.sendMessage(getLevelUpMessage(user, isNether));
		user.getPlayer().playSound(user.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
    }

    @Override
    public boolean checkForLevelUp(User user, boolean isNether) {
        if(user.getLevel(getSkillType(), false) >= MAX_LEVEL) return false;

        double requirement = FISHING_LEVEL_REQUIREMENTS.get(user.getLevel(getSkillType(), false)+1);
        if(user.getExperience(getSkillType(), false) < requirement) return false;

        double excess = user.getExperience(getSkillType(), false) - requirement;
        levelUp(user, excess, false);
        return true;
    }

    public static double getRequirement(User user) {
        return FISHING_LEVEL_REQUIREMENTS.get(Math.min(6, user.getLevel(SkillType.FISHING, false)+1));
    }
    
}
