package kiul.tierblock.user;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.errorprone.annotations.DoNotCall;

import kiul.tierblock.Main;
import kiul.tierblock.user.data.Stats;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.utils.enums.CropType;
import kiul.tierblock.utils.enums.WoodType;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

@Getter
public class User {

    /*
     * Do NOT use any of the check-for-levelup methods inside of set level / xp methods.
     * If used, it'll call itself recursively, and consequently cause a massive error.
     */
    
    private Player player; 
    private Stats stats;

    public User(Player player) {
        this.player = player;
        this.stats = new Stats(this);
    }

    public void sendMessage(String message) {
        this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendActionBar(String message) {
        this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
            ChatColor.translateAlternateColorCodes('&', message)
        ));
    }

    /**
     * Sends a title message to the user.
     * @param title Title of the message.
     * @param subtitle Subtitle.
     * @param stayInSeconds The amount of time the message will stay on the screen for.
     */
    public void sendTitle(String title, String subtitle, int stayInSeconds) {
		String t = ChatColor.translateAlternateColorCodes('&', title);
		String st = ChatColor.translateAlternateColorCodes('&', subtitle);
        this.player.sendTitle(t, st, 20, stayInSeconds * 20, 20);
    }

    public boolean isOp() {
        return this.player.isOp();
    }

    public String getName() {
        return this.player.getName();
    }

    public UUID getUUID() {
        return this.player.getUniqueId();
    }

    public Location getLocation() {
        return this.player.getLocation();
    }
    
    public Vector getVelocity(Vector vector) {
        return this.player.getVelocity();
    }

    public void setVelocity(Vector vector) {
        this.player.setVelocity(vector);
    }

    // DO NOT USE... NOT COMPLETE.

    /**
     * @deprecated No functionality implemented!
     * @param skill
     */
    @Deprecated
    @DoNotCall
    public void levelUp(SkillType skill) {
        // @themightyfrogge TODO: Make this work 
    }

    // COMPLETE:

    public int getLevel(SkillType skillType) {
        return getStats().getInt(skillType.toString().toLowerCase() + ".level");
    }

    public double getExperience(SkillType skillType) {
        return getStats().getDouble(skillType.toString().toLowerCase() + ".xp");
    }

    public void setLevel(SkillType skillType, int levels) {
        getStats().setNumber(skillType.toString().toLowerCase() + ".level", levels);
    }
    
    public void setExperience(SkillType skillType, double experience) {
        getStats().setNumber(skillType.toString().toLowerCase() + ".xp", experience);
    }

    public void addLevels(SkillType skillType, int levels) {
        getStats().addInt(skillType.toString().toLowerCase() + ".level", levels);
    }
    
    public void addExperience(SkillType skillType, double experience) {
        getStats().addDouble(skillType.toString().toLowerCase() + ".xp", experience);
		checkGlobalLevelUp();
		checkFishingLevelUp();
    }
   
    // FISHING
    
    /**
     * 
     * @return Whether the user is eligible for a fishing level-up.
     */
	public boolean checkFishingLevelUp() {
		int fishingLevel = getLevel(SkillType.FISHING);
		double requirement = fishingLevel < 6 ? Main.FISHING_LEVEL_REQUIREMENTS.get(fishingLevel+1) : getExperience(SkillType.FISHING)+1.0;
		if(getExperience(SkillType.FISHING) >= requirement) {
			setExperience(SkillType.FISHING, getExperience(SkillType.FISHING) - requirement); // excess xp
			addLevels(SkillType.FISHING, 1);
			return true; // returns true, and stops here.
		}
		return false; // else, returns false.
	}

    /**
     * Changes the user's sea_creature_chance according to their global level automatically!
     */
    public void adjustSeaCreatureChance() {
        if(getLevel(SkillType.GLOBAL) >= 10 && getLevel(SkillType.GLOBAL) <= 110) {
            double chance = (getLevel(SkillType.GLOBAL) == 10) ? 0.05 : 0.04 + (getLevel(SkillType.GLOBAL) * 0.001); // DO NOT TOUCH the formula.
            setSeaCreatureChance(chance);
        }
    }

    public double getSeaCreatureChance() {
        return getStats().getDouble("fishing.sea_creature_chance");
    }

    public void setSeaCreatureChance(double chance) {
        getStats().setNumber("fishing.sea_creature_chance", chance);
    }

    // GLOBAL
    /**
     * The method name tells you what you need to know.
     * @param excessXp (excess xp given to the player after consumption by the level-up process)
     */
    public void globalLevelUp(double excessXp) {
        addLevels(SkillType.GLOBAL, 1);
        setExperience(SkillType.GLOBAL, excessXp);
        adjustSeaCreatureChance();
        this.player.playSound(getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
        sendTitle(
			"&2&lGLOBAL LEVEL UP&2!",
            String.format("&a%s &2-> &a%s&2!",
                getLevel(SkillType.GLOBAL)-1,
                getLevel(SkillType.GLOBAL)
            ),
			5
        );
    }

    /**
     * @return Wheter user is eligible for a global level-up, or not.
     */
    public boolean checkGlobalLevelUp() {
        if(getLevel(SkillType.GLOBAL) < 10) {
            
            if(getExperience(SkillType.GLOBAL) < 100) return false;

            globalLevelUp(getExperience(SkillType.GLOBAL) - 100);
            return true;
        }

        if(getExperience(SkillType.GLOBAL) < 1000) return false;

        globalLevelUp(getExperience(SkillType.GLOBAL) - 1000);
        return true;
    }
    
    /**
     * literally made an enum for this
     * @param woodType (Use WoodType and NOT Material)
     * @return the amount of wood blocks the player destroyed
     */
    public int getCollectedWood(WoodType woodType) {
        return getStats().getInt("foraging." + woodType.toString().toLowerCase() + ".collected");
    }

    /**
     * Increments the collected amount of the specified wood type.
     * @param woodType (the wood type that was broken)
     * @param leftHandSide (amount-to-be-incremented)
     */
    public void addForaging(WoodType woodType, int leftHandSide) {
        getStats().addInt("foraging." + woodType.toString() + ".collected", leftHandSide);
    }

    /**
     * @param cropType (no need to explain)
     * @return The collected amount of the specified crop type.
     */
    public int getCollectedCrops(CropType cropType) {
        return getStats().getInt("farming." + cropType.toString().toLowerCase() + ".collected");
    }

    /**
     * Increments the collected amount of the specified crop type.
     * @param cropType (the crop that was farmed)
     * @param leftHandSide (amount-to-be-incremented)
     */
    public void addFarming(CropType cropType, int leftHandSide) {
        getStats().addInt("farming." + cropType.toString() + ".collected", leftHandSide);
    }

}
