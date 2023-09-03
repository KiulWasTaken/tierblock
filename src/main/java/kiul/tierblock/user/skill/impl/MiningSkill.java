package kiul.tierblock.user.skill.impl;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import world.bentobox.magiccobblestonegenerator.StoneGeneratorAddon;
import world.bentobox.magiccobblestonegenerator.database.objects.GeneratorDataObject;
import world.bentobox.magiccobblestonegenerator.database.objects.GeneratorTierObject;
import world.bentobox.magiccobblestonegenerator.managers.StoneGeneratorManager;

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
        LuckPerms api = LuckPermsProvider.get();
        net.luckperms.api.model.user.User lpUser = api.getUserManager().getUser(user.getUUID());
        Node permissionNode = Node.builder("tiercobblegen." + (isNether ? "nether" : "") + "gen" + user.getLevel(getSkillType(), isNether)).build();
        lpUser.data().add(permissionNode);
        api.getUserManager().saveUser(lpUser);

        unlockGenerator(user, isNether);

        if(user.getLevel(getSkillType(), false) >= getMaxLevel(false) && !isNether)
            user.getStats().setBoolean(getSkillType().toString().toLowerCase() + ".nether.unlocked", true);

		user.getPlayer().playSound(user.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        user.sendMessage(getLevelUpMessage(user, isNether)); // level up message @ Skill abstract class.
    }

    private void unlockGenerator(User user, boolean isNether) {
        StoneGeneratorManager manager = StoneGeneratorAddon.getInstance().getAddonManager();

        GeneratorDataObject dataObject = manager.getGeneratorData(user.getIsland());
        GeneratorTierObject generator = manager.getGeneratorByID("bskyblock_level_" + (isNether ? "nether_" : "") + user.getLevel(getSkillType(), isNether));
        dataObject.getUnlockedTiers().add(generator.getUniqueId());
        manager.saveGeneratorData(dataObject);

        manager.activateGenerator(
            // Oh boy, that's a mouthful.
            world.bentobox.bentobox.api.user.User.getInstance(user.getPlayer()), 
            user.getIsland(), 
            dataObject, 
            generator
        );
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
