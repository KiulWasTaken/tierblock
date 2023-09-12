package kiul.tierblock.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.google.errorprone.annotations.DoNotCall;

import fr.mrmicky.fastboard.FastBoard;
import kiul.tierblock.Main;
import kiul.tierblock.user.data.Stats;
import kiul.tierblock.user.skill.SkillManager;
import kiul.tierblock.user.skill.SkillType;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.metadata.MetaDataValue;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.IslandsManager;
import world.bentobox.bentobox.managers.RanksManager;

@Getter
@SuppressWarnings("deprecation")
public class User {

    /*
     * Do NOT use any of the check-for-levelup methods inside of set level / xp
     * methods.
     * If used, it'll call itself recursively, and consequently cause a shitstorm.
     */

    private Player player;
    private OfflinePlayer offlinePlayer;
    private Stats stats;
    @Setter
    private FastBoard debugBoard;
    private SkillType lastSkill;
    private Map<String, Object> attributes = new HashMap<>();

    public User(Player player) {
        if (player == null)
            return;
        this.player = player;
        this.stats = new Stats(this);
    }

    /**
     * Creates an empty user with just the user stats, if found.
     * Sorta the equivalent of OfflinePlayer
     * 
     * @param stats
     */
    public User(OfflinePlayer offlinePlayer) {
        this.offlinePlayer = offlinePlayer;
        this.stats = new Stats(this);
    }

    public void setLastSkill(SkillType skillType) {
        lastSkill = skillType;
        if (getDebugBoard() == null)
            return;

        getDebugBoard().updateLine(1, ChatColor.translateAlternateColorCodes('&', " &8- &aLVL&8, &aXP&8: &8(&a"
                + getGlobalLevel() + "&f, &a" + Main.DECIMAL_FORMAT.format(getGlobalExperience()) + "&8)"));
        getDebugBoard().updateLine(9, ChatColor.translateAlternateColorCodes('&', "&2" + skillType + "&8:"));
        getDebugBoard().updateLine(10,
                ChatColor.translateAlternateColorCodes('&', " &8- &aLevel&8: &f" + getLevel(skillType, false)));
        getDebugBoard().updateLine(11, ChatColor.translateAlternateColorCodes('&',
                " &8- &aExperience&8: &f" + Main.DECIMAL_FORMAT.format(getExperience(skillType, false))));
    }

    public double getGlobalLevelUpRequirement() {
        return (getGlobalLevel() < 10 ? 100.0 : 1000.0);
    }
    
    public void sendMessage(String message) {
        if (this.offlinePlayer != null)
            return;
        this.player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void sendActionBar(String message) {
        if (this.offlinePlayer != null)
            return;
        this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                ChatColor.translateAlternateColorCodes('&', message)));
    }

    public void teleport(Location location) {
        if(this.offlinePlayer != null)
            return;
        this.player.teleport(location);
    }

    public void teleport(User user) {
        if(this.offlinePlayer != null)
            return;
        this.player.teleport(user.getLocation());
    }

    public void teleport(Player player) {
        if(this.offlinePlayer != null)
            return;
        this.player.teleport(player);
    }

    /**
     * Sends a title message to the user.
     * 
     * @param title         Title of the message.
     * @param subtitle      Subtitle.
     * @param stayInSeconds The amount of time the message will stay on the screen
     *                      for.
     */
    public void sendTitle(String title, String subtitle, int stayInSeconds) {
        if (this.offlinePlayer != null)
            return;
        String t = ChatColor.translateAlternateColorCodes('&', title);
        String st = ChatColor.translateAlternateColorCodes('&', subtitle);
        this.player.sendTitle(t, st, 20, stayInSeconds * 20, 20);
    }

    /**
     * @return The user's island (Where user is either a member or the owner of said
     *         island)
     */
    public Island getIsland() {
        if (!hasIsland())
            return null;

        IslandsManager islandsManager = BentoBox.getInstance().getIslands();
        Island userIsland = null;

        Predicate<Island> containsPlayer = island -> {
            if (!island.getMembers().containsKey(getUUID()))
                return false;
            int rank = island.getMembers().get(getUUID());

            return (rank >= RanksManager.MEMBER_RANK || rank <= RanksManager.OWNER_RANK);
        };

        List<Island> islandsContainingPlayer = islandsManager.getIslands().stream().filter(containsPlayer).toList();

        for (Island island : islandsContainingPlayer) {
            userIsland = island; // breaks if user is somehow a member in more than one island...
        }

        // userIsland = islandsManager.getIsland(Main.getBSkyBlockWorld(), getUUID());
        // wtf why did i do that

        return userIsland;
    }

    /**
     * @return Island members that fit the predicate:
     *         <p>
     *         <code>rank >= MEMBER_RANK && rank <= OWNER_RANK</code>
     */
    public List<User> getIslandMembers() {
        List<User> userList = new ArrayList<>();

        getIsland().getMembers().forEach((uuid, rank) -> {
            if (rank >= RanksManager.MEMBER_RANK && rank <= RanksManager.OWNER_RANK) {
                User user = UserManager.getInstance().getUser(uuid);
                if (user != null)
                    userList.add(user);
            }
        });

        return userList;
    }

    /**
     * @return Whether the play is a member of an island (even if not owner) or not.
     */
    public boolean hasIsland() {
        return getStats().getBoolean("has_island");
    }

    public void setHasIsland(boolean bool) {
        getStats().setBoolean("has_island", bool);
    }

    /**
     * @return Whether the user is with-in any island (Not strictly the user's
     *         island)
     */
    public boolean isWithinAnyIsland() {
        if (!getLocation().getWorld().getName().startsWith("bskyblock"))
            return false;
        if (BentoBox.getInstance().getIslandsManager().getIslandAt(getLocation()).get() == null)
            return false;
        return true;
    }

    /**
     * @return Whether the user is with-in own island
     */
    public boolean isWithinOwnIsland() {
        if (!hasIsland())
            return false;

        if (!getIsland().inIslandSpace(getLocation()))
            return false;
        
            return true;
    }

    /**
     * @return The {@link Island} at the user's current position, provided that <code>isWithinIsland()</code>
     *         returns true.
     */
    public Island getIslandAtPosition() {
        if(!isWithinAnyIsland()) return null;
        return BentoBox.getInstance().getIslandsManager().getIslandAt(getLocation()).get();
    }

    /**
     * @return The beehive block. (Returns null if not island owner!)
     */
    public Block getBeehive() {
        if (!hasIsland())
            return null;
        if (getIslandRank() != RanksManager.OWNER_RANK)
            return null; // only ownersa
        if (!getStats().getBoolean("farming.beehive.placed"))
            return null;

        Location location = new Location(
                Main.getBSkyBlockWorld(),
                getStats().getDouble("farming.beehive.x"),
                getStats().getDouble("farming.beehive.y"),
                getStats().getDouble("farming.beehive.z"));

        return location.getBlock();
    }

    public void setBeehive(Block block) {
        Location location = block.getLocation();
        getStats().setNumber("farming.beehive.x", location.getX());
        getStats().setNumber("farming.beehive.y", location.getY());
        getStats().setNumber("farming.beehive.z", location.getZ());
        getStats().setBoolean("farming.beehive.placed", true);
    }

    /**
     * Shouldda just named this isIslandOwner
     * 
     * @return Whether the player can place a beehive or not
     */
    public boolean canPlaceBeeHive() {
        if (getIsland().getRank(getUUID()) != RanksManager.OWNER_RANK)
            return false;
        return true;
    }

    public void activateBooster() {
        long[] durations = { 30L, 60L, 120L };
        int randomIndex = new Random().nextInt(durations.length);
        long selectedDuration = durations[randomIndex];
        getIsland().getMembers().forEach((uuid, rank) -> {
            if (rank < RanksManager.MEMBER_RANK || rank > RanksManager.OWNER_RANK)
                return;

            User user = UserManager.getInstance().getUser(uuid);
            if (user == null)
                return;

            // add booster time instead of resetting value, if user is already booster.
            long boosterEnd = ((user.getBooster() == 0) ? System.currentTimeMillis() : user.getBooster())
                    + 60000L * selectedDuration;

            user.setBooster(boosterEnd);
            user.setBoosterMultiplier(Main.BOOSTER_MULTIPLIER);

            UserManager.getBoostedUsers().add(user);

            long timeLeftHrs = (user.getBooster() - System.currentTimeMillis()) / 3600000;
            long timeLeftMins = (user.getBooster() - System.currentTimeMillis() - timeLeftHrs * 3600000) / 60000;

            player.playSound(getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
            user.sendMessage("&a&lISLAND BOOSTER ACTIVATED!");

            user.sendMessage(
                String.format(
                    "&aYou have a x%s booster for &e%s hrs&a, and &e%s mins",
                    user.getBoosterMultiplier(),
                    timeLeftHrs,
                    timeLeftMins
                )
            );
        });
    }

    public void deActivateBooster() {
        Island island = getIsland();

        if (island == null)
            return;

        island.getMembers().forEach((uuid, rank) -> {
            if (rank < RanksManager.MEMBER_RANK || rank > RanksManager.OWNER_RANK)
                return;

            User user = UserManager.getInstance().getUser(uuid);

            if (user == null || user.getBooster() <= 0L)
                return;

            UserManager.getBoostedUsers().remove(user);
            user.setBooster(0);
            user.setBoosterMultiplier(1.0);
            user.sendMessage("&c&lISLAND BOOSTER DEACTIVATED!");
            player.playSound(getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
        });
    }

    /**
     * @return The user's rank with-in the island, and <i><b>not</b></i> the Island
     *         ranking on other islands.
     */
    public int getIslandRank() {
        return getIsland().getRank(getUUID());
    }

    public boolean isBeehiveBoosterActive() {
        if (!Main.activeHives.contains(getBeehive()))
            return false;
        return true;
    }

    public boolean isOp() {
        return this.player.isOp();
    }

    public String getName() {
        if (offlinePlayer != null)
            return this.offlinePlayer.getName();
        return this.player.getName();
    }

    public UUID getUUID() {
        if (offlinePlayer != null)
            return this.offlinePlayer.getUniqueId();
        return this.player.getUniqueId();
    }

    public Location getLocation() {
        if (offlinePlayer != null)
            return null;
        return this.player.getLocation();
    }

    public Vector getVelocity(Vector vector) {
        if (offlinePlayer != null)
            return null;
        return this.player.getVelocity();
    }

    public void setVelocity(Vector vector) {
        if (offlinePlayer != null)
            return;
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
    }

    // COMPLETE:

    public void setBooster(long timeInMilliseconds) {
        getStats().setNumber("booster", timeInMilliseconds);
    }

    public void setBoosterMultiplier(double multiplier) {
        getStats().setNumber("booster_multiplier", multiplier);
    }

    public void addBoosterMultiplier(double rightHandSide) {
        getStats().addDouble("booster_multiplier", rightHandSide);
    }

    public String formatBoosterTime() {
        long timeLeftHrs = (getBooster() - System.currentTimeMillis()) / 3600000;
        long timeLeftMins = (getBooster() - System.currentTimeMillis() - timeLeftHrs * 3600000) / 60000;

        return getBooster() > 0L ? "&e" + timeLeftHrs + " &ahrs, &e" + timeLeftMins + " &amins" : "&cNo booster found!";
    }

    public long getBooster() {
        if (getStats().isNull("booster"))
            return 0;
        
        return getStats().getLong("booster");
    }

    public double getBoosterMultiplier() {
        return getStats().getDouble("booster_multiplier");
    }

    public void setGlobalLevel(int globalLevel) {
        getIsland().putMetaData("level", new MetaDataValue(globalLevel));
    }

    public void addGlobalLevel(int rightHandSide) {
        setGlobalLevel(getGlobalLevel() + rightHandSide);
    }

    public int getGlobalLevel() {
        return getIsland().getMetaData("level").get().asInt();
    }

    public void setGlobalExperience(double experience) {
        getIsland().putMetaData("xp", new MetaDataValue(experience));
    }

    public double addGlobalExperience(double rightHandSide) {
        setGlobalExperience(getGlobalExperience() + (rightHandSide * getBoosterMultiplier()));
        if(checkGlobalLevelUp()) globalLevelUp();
        
        return rightHandSide * getBoosterMultiplier();
    }

    public double getGlobalExperience() {
        return getIsland().getMetaData("xp").get().asDouble();
    }

    public int getLevel(SkillType skillType, boolean nether) {
        String extra = nether ? ".nether" : "";
        return getStats().getInt(skillType.toString().toLowerCase() + extra + ".level");
    }

    public double getExperience(SkillType skillType, boolean nether) {
        String extra = nether ? ".nether" : "";
        return getStats().getDouble(skillType.toString().toLowerCase() + extra + ".xp");
    }

    public void setLevel(SkillType skillType, int levels, boolean nether) {
        String extra = nether ? ".nether" : "";
        getStats().setNumber(skillType.toString().toLowerCase() + extra + ".level", levels);
    }

    public void setExperience(SkillType skillType, double experience, boolean nether) {
        String extra = nether ? ".nether" : "";
        getStats().setNumber(skillType.toString().toLowerCase() + extra + ".xp", experience);
    }

    public void addLevels(SkillType skillType, int levels, boolean nether) {
        int maxLevel = nether ? skillType.maxNetherLevel : skillType.maxLevel;
        if (getLevel(skillType, nether) >= maxLevel)
            return;

        String extra = nether ? ".nether" : "";
        getStats().addInt(skillType.toString().toLowerCase() + extra + ".level", levels);
    }

    public double addExperience(SkillType skillType, double experience, boolean nether) {
        int maxLevel = nether ? skillType.maxNetherLevel : skillType.maxLevel;
        if (getLevel(skillType, nether) == maxLevel)
            return 0;

        String extra = nether ? ".nether" : "";

        getStats().addDouble(skillType.toString().toLowerCase() + extra + ".xp", experience);
        SkillManager.getSkill(skillType).checkForLevelUp(this, nether);
        setLastSkill(skillType);

        return experience;
    }

    public boolean isAllowedToFly() {
        return this.player.getAllowFlight();
    }

    public void setFlight(boolean flight) {
        this.player.setAllowFlight(flight);
    }

    // FISHING

    /**
     * Changes the user's sea_creature_chance according to their global level
     * automatically!
     */
    public void adjustSeaCreatureChance() {
        if (getGlobalLevel() >= 10 && getGlobalLevel() <= 110) {
            // level == 10? yes: 0.05, no: calculate chance.
            double chance = (getGlobalLevel() == 10) ? 0.05 : 0.04 + (getGlobalLevel() * 0.001); // DO NOT TOUCH-a the formula.
            setSeaCreatureChance(chance);
        }
    }

    public int getSeaCreatureKills() {
        return getStats().getInt("fishing.sc_kills");
    }

    public void addSeaCreatureKills(int rightHandSide) {
        getStats().addInt("fishing.sc_kills", rightHandSide);
    }

    public void setSeaCreatureKills(int rightHandSide) {
        getStats().setNumber("fishing.sc_kills", rightHandSide);
    }

    public double getSeaCreatureChance() {
        return getStats().getDouble("fishing.sc_chance");
    }

    public void setSeaCreatureChance(double chance) {
        getStats().setNumber("fishing.sc_chance", chance);
    }

    public int getMonsterKills() {
        return getStats().getInt("combat.monsters_killed");
    }

    public void addMonsterKills(int rightHandSide) {
        getStats().addInt("combat.monsters_killed", rightHandSide);
    }

    public void setMonsterKills(int rightHandSide) {
        getStats().setNumber("combat.monsters_killed", rightHandSide);
    }

    public int getPillagerKills() {
        return getStats().getInt("combat.pillagers_killed");
    }

    public void addPillagerKills(int rightHandSide) {
        getStats().addInt("combat.pillagers_killed", rightHandSide);
    }

    public void setPillagerKills(int rightHandSide) {
        getStats().setNumber("combat.pillagers_killed", rightHandSide);
    }

    // GLOBAL
    /**
     * The method name tells you what you need to know.
     * 
     * @param excessXp (excess xp given to the player after consumption by the
     *                 level-up process)
     */
    public void globalLevelUp() {
        double excessXp = getGlobalExperience() - getGlobalLevelUpRequirement();

        addGlobalLevel(1);
        setGlobalExperience(excessXp);

        if(checkGlobalLevelUp()) {
            globalLevelUp();
            return;
        }

        getIslandMembers().forEach(user -> {
            user.adjustSeaCreatureChance();
            user.getPlayer().playSound(getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
            user.sendMessage(
                String.format(
                    "&a&lGLOBAL &2&lLEVEL-UP&2&l!\n" +
                    "&a%s &8-> &a%s&2!", getGlobalLevel() - 1, /* -> */ getGlobalLevel()
                )
            );
        });
    }

    /**
     * @return Wheter user is eligible for a global level-up, or not.
     */
    public boolean checkGlobalLevelUp() {
        return getGlobalExperience() >= getGlobalLevelUpRequirement();
    }

}
