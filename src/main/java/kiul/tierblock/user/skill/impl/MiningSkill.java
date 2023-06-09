package kiul.tierblock.user.skill.impl;

import org.bukkit.Sound;

import kiul.tierblock.user.User;
import kiul.tierblock.user.skill.Skill;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.utils.enums.MineableType;

public class MiningSkill extends Skill {

    public MiningSkill() {
        super(SkillType.MINING);
    }

    @Override
    public void levelUp(User user, double excessXp, boolean isNether) {
        user.addLevels(SkillType.MINING, 1, isNether);
        user.setExperience(SkillType.MINING, excessXp, isNether);

        if(user.getLevel(getSkillType(), false) >= getMaxLevel(false) && !isNether)
            user.getStats().setBoolean(getSkillType().toString().toLowerCase() + ".nether.unlocked", true);

		user.getPlayer().playSound(user.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
        user.sendMessage(getLevelUpMessage(user, isNether)); // level up message @ Skill abstract class.
    }

    @Override
    public boolean checkForLevelUp(User user, boolean isNether) {
        if(user.getLevel(SkillType.MINING, isNether) >= getMaxLevel(isNether)) return false;

        int indexInEnum = user.getLevel(SkillType.MINING, isNether) - 1;
        MineableType lastType = MineableType.values()[indexInEnum];
        if(user.getExperience(SkillType.MINING, isNether) < lastType.levelUp) return false;

        // if somehow the player got excess xp in MINING (most likely, if not only, via commands),
        // the xp will be set to the excess.
        double excess = user.getExperience(SkillType.MINING, isNether) - lastType.levelUp;
        
        levelUp(user, excess, isNether);
        return true;
    }

	private int getMaxLevel(boolean isNether) {
		if(isNether) return 5;
		return getSkillType().maxLevel;
	}
 
}
