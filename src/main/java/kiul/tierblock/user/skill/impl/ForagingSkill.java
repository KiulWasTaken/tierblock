package kiul.tierblock.user.skill.impl;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import kiul.tierblock.user.User;
import kiul.tierblock.user.skill.Skill;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.utils.enums.WoodType;

public class ForagingSkill extends Skill {

    final int MAX_LEVEL = getSkillType().maxLevel;

    public ForagingSkill() {
        super(SkillType.FORAGING);
    }

    @Override
    public void levelUp(User user, double excessXp, boolean isNether) {
        if (isNether) {
            int levelUp = user.getLevel(SkillType.FORAGING, true) + 1;
            user.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "FORAGING LEVEL UP! " + ChatColor.RED + user.getLevel(SkillType.FORAGING, true) + ChatColor.GRAY + " -> " + ChatColor.RED + levelUp);
        } else {
            int levelUp = user.getLevel(SkillType.FORAGING, false) + 1;
            user.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "FORAGING LEVEL UP! " + ChatColor.GOLD + user.getLevel(SkillType.FORAGING, false) + ChatColor.GRAY + " -> " + ChatColor.GOLD + levelUp);
        }
        user.addLevels(getSkillType(), 1, isNether);
        user.setExperience(getSkillType(), excessXp, isNether);

        int addition = 0 + (isNether ? 6 : 0);
        ItemStack itemStack = new ItemStack(WoodType.toSapling(WoodType.values()[user.getLevel(getSkillType(), isNether) + addition - 1]));
        user.getPlayer().getInventory().addItem(itemStack);

        // user is max-level, now able to mine beyond max-level blocks.
        if(user.getLevel(getSkillType(), false) >= MAX_LEVEL) {
        	user.getStats().setBoolean(getSkillType().toString().toLowerCase() + ".nether.unlocked", true);
            user.setFlight(true);
            user.getPlayer().setAllowFlight(true);
			user.sendMessage("&aYou've unlocked the flight ability! (Only works while in island)");
        }
        
		user.getPlayer().playSound(user.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
    }

    @Override
    public boolean checkForLevelUp(User user, boolean isNether) {
        if(user.getLevel(getSkillType(), isNether) >= MAX_LEVEL) return false;

        int indexInEnum = Math.min(user.getLevel(getSkillType(), isNether) - 1 + (isNether ? 6 : 0), 7);
        WoodType lastType = WoodType.values()[indexInEnum];
        if(user.getExperience(getSkillType(), isNether) < lastType.levelUp) return false;

        // if somehow the player got excess xp (most likely, if not only, via commands),
        // the xp will be set to the excess.
        double excess = user.getExperience(getSkillType(), isNether) - lastType.levelUp;
        
        levelUp(user, excess, isNether);
        return true;
    }
    
}
