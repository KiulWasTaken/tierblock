package kiul.tierblock.user.skill.impl;

import java.lang.Math;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import kiul.tierblock.user.User;
import kiul.tierblock.user.skill.Skill;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.utils.enums.CropType;

public class FarmingSkill extends Skill {

    final int MAX_LEVEL = SkillType.FARMING.maxLevel;

    public FarmingSkill() {
        super(SkillType.FARMING);
    }

    @Override
    public void levelUp(User user, double excessXp, boolean isNether) {
        user.addLevels(SkillType.FARMING, 1, isNether);
        user.setExperience(SkillType.FARMING, excessXp, isNether);

        int addition = 0 + (isNether ? 7 : 0);
        ItemStack itemStack = new ItemStack(CropType.toSeed(CropType.values()[user.getLevel(getSkillType(), isNether) + addition - 1]));
        itemStack.setAmount(1);
        user.getPlayer().getInventory().addItem(itemStack);
        
        if(user.getLevel(getSkillType(), false) >= MAX_LEVEL)
            user.getStats().setBoolean(getSkillType().toString().toLowerCase() + ".nether.unlocked", true);

        if(checkForLevelUp(user, isNether)) return;

        user.sendMessage(getLevelUpMessage(user, isNether));
	    user.getPlayer().playSound(user.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
    }

    @Override
    public boolean checkForLevelUp(User user, boolean isNether) {
        if(user.getLevel(SkillType.FARMING, isNether) >= MAX_LEVEL) return false;

        int indexInEnum = Math.min(user.getLevel(SkillType.FARMING, isNether) - 1 + (isNether ? 7 : 0), 8);
        CropType lastType = CropType.values()[indexInEnum];
        if(user.getExperience(SkillType.FARMING, isNether) < lastType.levelUp) return false;
        if(lastType.globalLevelRequirement > user.getGlobalLevel()) return false;

        double excess = lastType.globalLevelRequirement > 0 ? 0 :
                user.getExperience(SkillType.FARMING, isNether) - lastType.levelUp;
        
        levelUp(user, excess, isNether);
        return true;
    }
    
    
}
