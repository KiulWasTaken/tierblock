package kiul.tierblock.user.skill.impl;

import java.lang.Math;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import kiul.tierblock.user.User;
import kiul.tierblock.user.skill.Skill;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.utils.enums.CropType;

public class FarmingSkill extends Skill {

    private int maxLevel(boolean isNether) {
        return isNether ? SkillType.FARMING.maxNetherLevel : SkillType.FARMING.maxLevel;
	}

    public FarmingSkill() {
        super(SkillType.FARMING);
    }

    @Override
    public void levelUp(User user, double excessXp, boolean isNether) {
        user.addLevels(SkillType.FARMING, 1, isNether);
        user.setExperience(SkillType.FARMING, excessXp, isNether);

        int addition = 0 + (isNether ? 7 : 0);
        ItemStack itemStack = new ItemStack(CropType.toSeed(CropType.values()[user.getLevel(getSkillType(), isNether) + addition - 1]));
        user.getPlayer().getInventory().addItem(itemStack);
        
        if(user.getLevel(getSkillType(), false) >= maxLevel(isNether) && user.getLevel(getSkillType(), true) == 1) {
            user.getStats().setBoolean(getSkillType().toString().toLowerCase() + ".nether.unlocked", true);
            user.sendMessage("&aYou've unlocked the &cfarming sub-skill&a!");
            if(user.getGlobalLevel() < CropType.NETHER_WART.globalLevelRequirement) {
                user.sendMessage(
                    "&e&lNOTE: &cTo use the newly unlocked type of plant, you need &e&lisland level "
                    + CropType.NETHER_WART.globalLevelRequirement + "&c!"
                );
            }

            user.sendMessage(
                "&eYou've reached &lBOOSTERS AND &6&lBEE&e&lHIVES!\n" +
                "&6Bee&ehives, once harvested will &6&ltriple&e your island xp gains!\n" +
                "&8------------\n" +
                "&8* &eTo use the &6bee&ehive, your &6bees &emust be alive, and working in the &6bee&ehive\n" +
                "&8* &eYou'll also have to wait &624 hours &eafter first placement and between each harvest!\n" +
                "&4&lWARNING&4: &cBreaking the beehive will restart the &4&l24 hour&c countdown. Be careful!"
            );
			
            user.getPlayer().getInventory().addItem(new ItemStack(Material.BEEHIVE));
			user.getPlayer().getInventory().addItem(new ItemStack(CropType.toSeed(CropType.NETHER_WART)));
        }

        if(checkForLevelUp(user, isNether)) return;

        user.sendMessage(getLevelUpMessage(user, isNether));
	    user.getPlayer().playSound(user.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
    }

    @Override
    public boolean checkForLevelUp(User user, boolean isNether) {
        if(user.getLevel(SkillType.FARMING, isNether) >= maxLevel(isNether)) return false;

        int indexInEnum = Math.min(user.getLevel(SkillType.FARMING, isNether) + (isNether ? 7 : 0), 8);
        CropType lastType = CropType.values()[indexInEnum - 1];
		CropType newType = CropType.values()[indexInEnum];
        if(user.getExperience(SkillType.FARMING, isNether) < lastType.levelUp) return false;
        if(newType.globalLevelRequirement > user.getGlobalLevel()) {
            user.sendMessage(
                "&e&lNOTE: &cTo use the newly unlocked type of plant, you need &e&lisland level "
                + newType.globalLevelRequirement + "&c!"
            );
        }

        double excess = newType.globalLevelRequirement > 0 ? 0 :
                user.getExperience(SkillType.FARMING, isNether) - lastType.levelUp;
        
        levelUp(user, excess, isNether);
        return true;
    }
    
    
}
