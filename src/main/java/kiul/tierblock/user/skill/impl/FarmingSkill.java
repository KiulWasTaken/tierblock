package kiul.tierblock.user.skill.impl;

import java.lang.Math;

import org.bukkit.ChatColor;
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
        if (isNether) {
            int levelUp = user.getLevel(SkillType.FARMING, true) + 1;
            user.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "FARMING LEVEL UP! " + ChatColor.RED + user.getLevel(SkillType.FARMING, true) + ChatColor.GRAY + " -> " + ChatColor.RED + levelUp);
        } else {
            int levelUp = user.getLevel(SkillType.FARMING, false) + 1;
            user.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "FARMING LEVEL UP! " + ChatColor.GOLD + user.getLevel(SkillType.FARMING, false) + ChatColor.GRAY + " -> " + ChatColor.GOLD + levelUp);
        }
        user.addLevels(SkillType.FARMING, 1, isNether);
        user.setExperience(SkillType.FARMING, excessXp, isNether);

        int addition = 0 + (isNether ? 7 : 0);
        ItemStack itemStack = new ItemStack(CropType.toSeed(CropType.values()[user.getLevel(getSkillType(), isNether) + addition - 1]));
        user.getPlayer().getInventory().addItem(itemStack);
        
        if(user.getLevel(getSkillType(), false) >= MAX_LEVEL)
            user.getStats().setBoolean(getSkillType().toString().toLowerCase() + ".nether.unlocked", true);

	user.getPlayer().playSound(user.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
    }

    @Override
    public boolean checkForLevelUp(User user, boolean isNether) {
        if(user.getLevel(SkillType.FARMING, isNether) >= MAX_LEVEL) return false;

        int indexInEnum = Math.min(user.getLevel(SkillType.FARMING, isNether) - 1 + (isNether ? 7 : 0), 8);
        CropType lastType = CropType.values()[indexInEnum];
        if(user.getExperience(SkillType.FARMING, isNether) < lastType.levelUp) return false;

        // if somehow the player got excess xp in farming (most likely, if not only, via commands),
        // the xp will be set to the excess.
        double excess = user.getExperience(SkillType.FARMING, isNether) - lastType.levelUp;
        
        levelUp(user, excess, isNether);
        return true;
    }
    
    
}
