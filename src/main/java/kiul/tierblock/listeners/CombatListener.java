package kiul.tierblock.listeners;

import java.util.Random;
import java.util.Optional;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.utils.enums.MonsterType;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.metadata.MetaDataValue;
import world.bentobox.bentobox.database.objects.Island;
import world.bentobox.bentobox.managers.IslandsManager;
import world.bentobox.bentobox.managers.RanksManager;

@SuppressWarnings("deprecation")
public class CombatListener implements Listener {

    // note: my balls were itchy when i wrote this.
    @EventHandler
    public void spawningListener(CreatureSpawnEvent event) {
        if(!(event.getEntity() instanceof Monster)) return;

        MonsterType monsterType = MonsterType.fromEntityType(event.getEntityType());

        World world = event.getLocation().getWorld();

        // then we actually choose the mob that's supposed to spawn...
        Random random = new Random();

		// get spawn chance with some bias on nether.
        double mobChance = random.nextDouble(0.0001, 1);

        event.setCancelled(true);

        IslandsManager manager = BentoBox.getInstance().getIslands();
        Island island = manager.getIslandAt(event.getLocation()).get();

        if(island == null) return; // not spawning inside island
		
		int islandLevel = island.getMetaData("level").get().asInt();
		
		if(monsterType != null && monsterType.islandLevelRequirement > islandLevel) event.getEntity().remove();

        if(event.getSpawnReason() != SpawnReason.NATURAL) {
            event.setCancelled(false); // incase it's an other way of spawning, we don't cancel this
            return;
        }

        // event.getEntity().remove();
        // to prevent spiders from spawning where they shouldn't
        if(monsterType != MonsterType.SPIDER)
            monsterType = MonsterType.getMonsterByChance(
                mobChance, // chance, it'll find the closest
                world.getName(), // world name, it'll find the ones that fit
                islandLevel// and island level so it doesn't spawn everything when it shouldn't
            );
		
		if(monsterType == null) {
			event.setCancelled(false);
			return;
		} else {
			event.getEntity().remove();
		}
		
        // pillager stuff:
        final String message = island.getMetaData("raidCaptain").get().asBoolean() ? 
            "&cA raid captain pillager has spawned in your island!" : 
            "&cA pillager has spawned in your island!";

        // pillager spawning mechanism
        double pillagerChance = random.nextDouble();
        if(pillagerChance < island.getMetaData("pillagerSpawnChance").get().asDouble()
                && event.getLocation().getWorld().getName().equals("bskyblock_world")) {
			event.getEntity().remove();
            event.setCancelled(true);
            monsterType = MonsterType.PILLAGER;

            if(island.getMetaData("raidCaptain").get().asBoolean()) {
                spawnPillagerRaidCaptain(event.getLocation());
                island.putMetaData("raidCaptain", new MetaDataValue(false));
            }else {
                ItemStack crossbow = new ItemStack(Material.CROSSBOW);
                Pillager pillager = (Pillager) event.getLocation().getWorld().spawnEntity(event.getLocation(), EntityType.PILLAGER, false);
                pillager.getEquipment().setItemInMainHand(crossbow);
            }
            
            island.getMembers().forEach((uuid, rank) -> {
                if(rank >= RanksManager.MEMBER_RANK || rank <= RanksManager.OWNER_RANK) {
                    User user = UserManager.getInstance().getUser(uuid);
                    if(user == null) return;
                    if(user.isWithinOwnIsland()) {
                        user.getPlayer().playSound(user.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1, 1);
                        user.sendMessage(message);
                    }
                }
            });
            
            island.putMetaData("pillagerSpawnChance", new MetaDataValue((double)0.001));
            return;
        }
        event.setCancelled(false);
        event.getLocation().getWorld().spawnEntity(event.getLocation(), MonsterType.toEntityType(monsterType));
    }

    @EventHandler
    public void deathListener(EntityDeathEvent event) {
        MonsterType type = MonsterType.fromEntityType(event.getEntityType());
        Optional<Island> island = BentoBox.getInstance().getIslands().getIslandAt(event.getEntity().getLocation());

        if(!island.isPresent()) return;
        if(type == null) return; // monster type returns null when mob isn't rewardable (i.e. listed in plan doc)
        if(event.getEntity().getKiller() == null) {
            event.getDrops().clear(); // monsters should only drop items when killed by player (from document)
            return;
        }

        User user = UserManager.getInstance().getUser(event.getEntity().getKiller());

        if(!user.hasIsland()) return;
        if(!user.getIsland().equals(island.get())) return; // not a member, no rewards given
        if(type.islandLevelRequirement > island.get().getMetaData("level").get().asInt()) {
            user.sendActionBar("&cYou need island level &e" + type.islandLevelRequirement + " &cto gain this monster's drop(s)!");
            return;
        }

        
        if(type == MonsterType.ZOMBIE_VILLAGER) {
            double currentChance = island.get().getMetaData("pillagerSpawnChance").get().asDouble();
            island.get().putMetaData("pillagerSpawnChance", new MetaDataValue(currentChance+0.001)); // +0.1%
            user.sendActionBar("&cNo XP reward &8| &4Pillager Spawn Chance +&c0.1% &8(&bZombie Villager&8)");
            return;
        }

        if(type == MonsterType.PILLAGER) {
            user.addPillagerKills(1);
            String actionBarMessage = "&cRaid captain progress: (&4" + user.getPillagerKills() + "&8/&420&c)";
            Pillager pillager = (Pillager) event.getEntity();

            if(pillager.isPatrolLeader()) {
                actionBarMessage = "&c&lYou've killed a raid captain!";
                applyBadOmen(user.getPlayer());
                user.sendActionBar(actionBarMessage);
                return;
            }

            if(user.getPillagerKills() >= 20) {
                user.setPillagerKills(0);
                island.get().putMetaData("pillagerSpawnChance", new MetaDataValue(1.0));
                island.get().putMetaData("raidCaptain", new MetaDataValue(true));
                actionBarMessage = "&cRaid captain progress reset! (Raid captain will spawn, at some point)";
            }
            
            user.sendActionBar(actionBarMessage);
            return;
        }

        // combat has no skill class; it's purely based on the user's island level (counts if user is a member or the owner of the mentioned island)
        // user.addExperience(SkillType.COMBAT, 1, false); unnecessary.
        
        user.sendActionBar(
            String.format(
                "&eIsland: &2+&a%sxp &8(&ekilled &b%s&8)", // &eCombat &2+&a1.0xp 
                Main.DECIMAL_FORMAT.format(user.addGlobalExperience(type.xpReward)),
                type.formatName()
            )
        );
        
        user.addMonsterKills(1);
    }

    private void spawnPillagerRaidCaptain(Location location) {
        Pillager pillager = (Pillager) location.getWorld().spawnEntity(location, EntityType.PILLAGER, false);

        ItemStack banner = new ItemStack(Material.WHITE_BANNER);
        BannerMeta bannerMeta = (BannerMeta) banner.getItemMeta();
        bannerMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&oOminous Banner"));
        bannerMeta.addPattern(new Pattern(DyeColor.CYAN, PatternType.RHOMBUS_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.LIGHT_GRAY, PatternType.STRIPE_BOTTOM));
        bannerMeta.addPattern(new Pattern(DyeColor.GRAY, PatternType.STRIPE_CENTER));
        bannerMeta.addPattern(new Pattern(DyeColor.LIGHT_GRAY, PatternType.BORDER));
        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.LIGHT_GRAY, PatternType.HALF_HORIZONTAL));
        bannerMeta.addPattern(new Pattern(DyeColor.LIGHT_GRAY, PatternType.CIRCLE_MIDDLE));
        bannerMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.BORDER));
        banner.setItemMeta(bannerMeta);    

        ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        
        pillager.getEquipment().setItemInMainHand(crossbow);
        pillager.getEquipment().setHelmetDropChance(1.0F);
        pillager.getEquipment().setHelmet(banner);
        pillager.setPatrolLeader(true);
    }

    // proudly copied from spigot, because i can't bother typing
    private void applyBadOmen(Player player){
        if (player.hasPotionEffect(PotionEffectType.BAD_OMEN)){
           int currentAmplifier = player.getPotionEffect(PotionEffectType.BAD_OMEN).getAmplifier();
           if (currentAmplifier < 4){
              player.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, -1, currentAmplifier + 1, false, false));
           } else {
              player.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, -1, 4, false, false));
           }
        } else {
           player.addPotionEffect(new PotionEffect(PotionEffectType.BAD_OMEN, -1, 0, false, false));
        }
    }

}
