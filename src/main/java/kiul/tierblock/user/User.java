package kiul.tierblock.user;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import kiul.tierblock.Main;
import kiul.tierblock.user.data.Stats;
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

   
    // FISHING
    
	public boolean checkFishingLevelUp() {
		double requirement = Main.FISHING_LEVEL_REQUIREMENTS.get(getFishingLevel()+1);
		if(getFishingExperience() >= requirement) {
			setFishingExperience(getFishingExperience() - requirement); // excess xp
			addFishingLevel(1);
			return true; // returns true, and stops here.
		}
		return false; // else, returns false.
	}

    public int getFishingLevel() {
        return getStats().getInt("fishing.level");
    }

    public double getFishingExperience() {
        return getStats().getDouble("fishing.xp");
    }

    public void adjustSeaCreatureChance() {
        if(getGlobalLevel() >= 10 && getGlobalLevel() <= 110) {
            double chance = (getGlobalLevel() == 10) ? 0.05 : 0.04 + (getGlobalLevel() * 0.001); // DO NOT TOUCH the formula.
            setSeaCreatureChance(chance);
        }
    }

    public void addFishingLevel(int level) {
        getStats().addInt("fishing.level", level);
    }

    public void addFishingExperience(double experience) {
        getStats().addDouble("fishing.xp", experience);
        checkFishingLevelUp();
    }

    public void setFishingLevel(int level) {
        getStats().setNumber("fishing.level", level);
    }

    public void setFishingExperience(double experience) {
        getStats().setNumber("fishing.xp", experience);
    }
    
    public double getSeaCreatureChance() {
        return getStats().getDouble("fishing.sea_creature_chance");
    }

    public void setSeaCreatureChance(double chance) {
        getStats().setNumber("fishing.sea_creature_chance", chance);
    }

    // GLOBAL

    public void globalLevelUp(double excessXp) {
        addGlobalLevel(1);
        setGlobalExperience(excessXp);
        adjustSeaCreatureChance();
        this.player.playSound(getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
        sendActionBar(
            String.format("&eGlobal &2level-up&e! &2%s &8-> &2%s&e!",
                getGlobalLevel()-1,
                getGlobalLevel()
            )
        );
    }

    public boolean checkGlobalLevelUp() {
        if(getGlobalLevel() < 10) {
            
            if(getGlobalExperience() < 100) return false;

            globalLevelUp(getGlobalExperience() - 100);
            return true;
        }

        if(getGlobalExperience() < 1000) return false;

        globalLevelUp(getGlobalExperience() - 1000);
        return true;
    }
    
    public int getGlobalLevel() {
        return getStats().getInt("global.level");
    }

    public double getGlobalExperience() {
        return getStats().getDouble("global.xp");
    }

    public void setGlobalLevel(int level) {
        getStats().setNumber("global.level", level);
    }

    public void addGlobalLevel(int level) {
        getStats().addInt("global.level", level);
    }

    public void setGlobalExperience(double experience) {
        getStats().setNumber("global.xp", experience);
    }

    public void addGlobalExperience(double experience) {
        getStats().addDouble("global.xp", experience);
        checkGlobalLevelUp();
    }

    // FORAGING

    /**
     * literally made an enum for this
     * @param woodType (Use WoodType and NOT Material)
     * @return the amount of wood blocks the player destroyed
     */
    public int getCollectedWood(WoodType woodType) {
        return getStats().getInt("foraging." + woodType.toString().toLowerCase() + ".collected");
    }


    public void addForaging(WoodType woodType, int leftHandSide) {
        getStats().addInt("foraging." + woodType.toString() + ".collected", leftHandSide);
    }
    
}
