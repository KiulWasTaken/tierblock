package kiul.tierblock.user.skill.impl;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;

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
        if (isNether) {
            int levelUp = user.getLevel(SkillType.MINING, true) + 1;
            user.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "MINING LEVEL UP! " + ChatColor.RED + user.getLevel(SkillType.MINING, true) + ChatColor.GRAY + " -> " + ChatColor.RED + levelUp);
        } else {
            int levelUp = user.getLevel(SkillType.MINING, false) + 1;
            user.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "MINING LEVEL UP! " + ChatColor.GOLD + user.getLevel(SkillType.MINING, false) + ChatColor.GRAY + " -> " + ChatColor.GOLD + levelUp);
            user.sendMessage(ChatColor.DARK_GRAY + "Use " + ChatColor.GOLD + "/gen" + ChatColor.DARK_GRAY + " to configure your generator!");
        }
        user.addLevels(SkillType.MINING, 1, isNether);
        user.setExperience(SkillType.MINING, excessXp, isNether);
        LuckPerms api = LuckPermsProvider.get();
        net.luckperms.api.model.user.User lpUser = api.getUserManager().getUser(user.getUUID());
        Node permissionNode = Node.builder("tiercobblegen." + (isNether ? "nether" : "") + "gen" + user.getLevel(getSkillType(), isNether)).build();
        lpUser.data().add(permissionNode);
        api.getUserManager().saveUser(lpUser);

        if(user.getLevel(getSkillType(), false) >= getMaxLevel(false) && !isNether) {
            user.getStats().setBoolean(getSkillType().toString().toLowerCase() + ".nether.unlocked", true);
        }
		user.getPlayer().playSound(user.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
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
