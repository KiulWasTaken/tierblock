package kiul.tierblock.listeners;

import java.util.List;
import java.util.Map;
import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.user.skill.SkillManager;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.user.skill.impl.FarmingSkill;
import kiul.tierblock.utils.enums.CropType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityEnterBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import world.bentobox.bentobox.api.metadata.MetaDataValue;
import world.bentobox.bentobox.managers.RanksManager;

/*
 * ------------------------------ NOT NECESSARY TO READ, BUT IT'S HERE ANYWAYS. ------------------------------
 *
 * Don't mind all the comments, I write them so I don't get lost xd.
 *
 * How the Beehive works:
 *
 * NOTE: Placing, breaking, activating/deactivating boosters, all require the island owner, and not any member.
 *  + User#deactivateBooster() & User#activateBooster() will do its job for all the island members, and not exclusively the island owner.
 *
 * The beehive depends on the island's metadata (persistent through server restarts, don't worry), and the user's (owner) data file.
 * Beehive's variables in the user data are:
 * - booster_active (bool): self-explanatory.
 * - booster_available (bool): self-explanatory.
 * - next_refill (long): a timestamp in milliseconds, once surpassed, the booster will be deactivated. (While the player is on, or when the player joins)
 *
 * - Only island owners can place beehives.
 * - Only one beehive can be placed in an island.
 * - Beehives must only be placed at the owner's island.
 * - Beehives take exactly 24 hours to be "ready-for-harvest".
 *
 */

public class FarmingListener implements Listener {

    // private final List<Material> allowedDrops = List.of(
    // Material.POTATO,
    // Material.CARROT,
    // Material.BEETROOT_SEEDS,
    // Material.PUMPKIN_SEEDS,
    // Material.MELON_SEEDS
    // );

    public FarmingListener() {
        SkillManager.getInstance().registerSkill(new FarmingSkill());
    }

    @EventHandler
    public void cropBreakListener(BlockBreakEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());
        Block block = event.getBlock();
        CropType type = CropType.fromMaterial(block.getType());

        if (type == null)
            return; // not a crop, stopping here.
        if (!user.hasIsland())
            return;
        if (user.getIslandAtPosition().getRank(user.getUUID()) < RanksManager.COOP_RANK)
            return;

        List<Material> nonAgeable = List.of(
                Material.SUGAR_CANE,
                Material.PUMPKIN,
                Material.MELON);

        if (block.hasMetadata("pp"))
            return;

        // check ifit can age...
        if (!nonAgeable.contains(block.getType())) {
            Ageable crop = (Ageable) block.getState().getBlockData();
            if (crop.getAge() < crop.getMaximumAge())
                return; // plant not fully grown
        }

        event.setDropItems(false);

        if (type.isNether && !user.getStats().getBoolean("farming.nether.unlocked")) {
            user.sendActionBar(
                    "&cYou need to &emax farming &cto unlock nether/end content!");
            return;
        }

        int blockLevelRequirement = Main.getGlobalLevelRequirement(
                "farming." + type.label);
        if (
        // We check ifthe broken block has a level requirement:
        blockLevelRequirement != 0 &&
        // and also check ifthe player doesn't have the required level...
                user.getGlobalLevel() < blockLevelRequirement) {
            user.sendActionBar(
                    "&cYou need to have &eisland level " +
                            blockLevelRequirement +
                            " or above &cto collect this!");
            return;
        }

        if (user.getLevel(SkillType.FARMING, type.isNether) < type.levelRequirement) {
            // show progress, ifthe block is unlockable in the next level (In other words:
            // block level = user level + 1)
            // ifnot tell them the level requirement:
            if (type == CropType.CHORUS_FLOWER || type == CropType.NETHER_WART)
                return; // no progress checks for chorus fruits, no farming goal found.
            boolean progress = (type.levelRequirement - 1) == user.getLevel(SkillType.FARMING, type.isNether);
            CropType previousType = CropType.values()[type.ordinal() - 1];
            if (progress) {
                user.sendActionBar(
                        String.format(
                                "&cYou need to farm &e%s &cmore &e%s&c to collect this!",
                                (int) (previousType.levelUp -
                                        user.getExperience(SkillType.FARMING, type.isNether)),
                                previousType.formatName()));
                return;
            }

            user.sendActionBar(
                    "&cYou need to have &efarming level " +
                            type.levelRequirement +
                            " &cto farm this!");
            return;
        }

        event.setDropItems(true);

        double xpReward = 0.0;

        if (type.levelRequirement == user.getLevel(SkillType.FARMING, type.isNether) ||
                (type.levelRequirement == 0 &&
                        user.getLevel(SkillType.FARMING, type.isNether) == 1)) {
            xpReward = user.addExperience(SkillType.FARMING, 1.0, type.isNether);
        }

        user.sendActionBar(
                new StringBuilder(
                        String.format(
                                "&eIsland: &2+&a%sxp " +
                                        (user.getBoosterMultiplier() > 1.0
                                                ? "(x" + (int) user.getBoosterMultiplier() + " booster)"
                                                : ""),
                                Main.DECIMAL_FORMAT.format(user.addGlobalExperience(type.xpReward))))
                        .append(
                                (xpReward <= 0.0
                                        ? ""
                                        : String.format(
                                                " &8| &eFarming: &2+&a%sxp &8(&b%s&8)",
                                                Main.DECIMAL_FORMAT.format(xpReward),
                                                type.formatName())))
                        .toString());
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());

        if (user.getIslandAtPosition().getRank(user.getUUID()) < RanksManager.COOP_RANK)
            return;

        Block block = event.getBlock();

        List<Material> flaggableBlocks = List.of(
                Material.SUGAR_CANE,
                Material.PUMPKIN,
                Material.MELON);

        CropType type = CropType.fromMaterial(block.getType());

        if (type != null) {
            if (user.getIslandAtPosition().getRank(user.getUUID()) < RanksManager.MEMBER_RANK) {
                event.setCancelled(true);
                user.sendMessage("&cYou must be an island member to plant this!");
                return;
            } else {
                if (user.getLevel(SkillType.FARMING, type.isNether) < type.levelRequirement) {
                    event.setCancelled(true);
                    user.sendMessage(
                            "&cYou need farming &elevel " +
                                    type.levelRequirement +
                                    " &cto plant this!");
                    return;
                }
            }
        }

        if (block.getType() != Material.BEEHIVE) {
            if (flaggableBlocks.contains(block.getType()))
                block.setMetadata("pp", new FixedMetadataValue(Main.getInstance(), "f"));
            return;
        }

        if (user.getIsland() == null)
            return;

        event.setCancelled(true);

        Map<String, MetaDataValue> islandMetaData = user.getIsland().getMetaData().get();

        if (!user.isWithinOwnIsland()) {
            user.sendMessage("&cYou can only place this in &eyour island&c!");
            return;
        }

        if (user.getIsland().getMetaData().get().get("hasBeeHive").asBoolean() == true) {
            user.sendMessage("&cYou can only place &eone&c bee hive in your island!");
            return;
        }

        event.setCancelled(false);

        long nextRefill = ((Beehive) block.getBlockData()).getHoneyLevel() != 5
                ? System.currentTimeMillis() + 86400000
                : 0; // +24 hrs (in milliseconds)

        islandMetaData.put("hasBeeHive", new MetaDataValue(true));

        user.getStats().setNumber("farming.beehive.next_refill", nextRefill);
        user.getStats().setBoolean("farming.beehive.booster_active", false);
        user.getStats().setBoolean("farming.beehive.booster_available", false);
        user.setBeehive(block);
        user.getPlayer().playSound(user.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1, 1);
        if(!islandMetaData.get("beehivePlacedBefore").asBoolean()) {
            block.getWorld().spawnEntity(block.getLocation().add(0, 1, 0), EntityType.BEE);
            block.getWorld().spawnEntity(block.getLocation().add(0, 1, 0), EntityType.BEE);
            islandMetaData.put("beehivePlacedBefore", new MetaDataValue(true));
        }
    }

    @EventHandler
    public void harvestBeeHive(PlayerInteractEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());
        Block block = event.getClickedBlock();

        if (!user.isWithinOwnIsland()) {
            return;
        }

        if (block == null)
            return;

        if (block.getType() != Material.BEEHIVE) {
            if (event.getAction() == Action.PHYSICAL &&
                    block.getType() == Material.FARMLAND) {
                Block above = block.getLocation().getWorld().getBlockAt(block.getLocation().add(0, 1, 0));
                CropType type = CropType.fromMaterial(above.getType());

                if (type == null)
                    return; // make sure it's a plant above.
                if (user.getIslandAtPosition().getRank(user.getUUID()) < RanksManager.COOP_RANK)
                    return;

                if (user.getLevel(SkillType.FARMING, type.isNether) < type.levelRequirement) {
                    // show progress, ifthe block is unlockable in the next level (In other words:
                    // block level = user level + 1)
                    // ifnot tell them the level requirement:
                    event.setCancelled(true);
                    String message = "&cYou need to be level &e" + type.levelRequirement + " &cto farm this!";
                    boolean progress = (type.levelRequirement - 1) == user.getLevel(SkillType.FARMING, type.isNether);
                    CropType previousType = CropType.values()[type.ordinal() - 1];
                    if (progress) {
                        message = String.format(
                                "&cYou need to farm &e%s &cmore &e%s&c to collect this!",
                                (int) (previousType.levelUp -
                                        user.getExperience(SkillType.FARMING, type.isNether)),
                                previousType.formatName());
                    }

                    user.sendActionBar(message);
                    above
                            .getLocation()
                            .getWorld()
                            .playSound(above.getLocation(), Sound.BLOCK_CROP_BREAK, 1, 1);

                    // user.sendMessage(above.getDrops().size() + above.getDrops().toString());
                    // DEBUG
                    // Material lastItem = null; // to prevent duplicates no touchies
                    // for(ItemStack itemStack : above.getDrops()) {
                    // if(lastItem != itemStack.getType() &&
                    // allowedDrops.contains(itemStack.getType())) {
                    // itemStack.setAmount(1);
                    // above.getLocation().getWorld().dropItem(above.getLocation().add(0, 1, 0),
                    // itemStack);
                    // lastItem = itemStack.getType();
                    // }
                    // }
                    above.setType(Material.AIR);
                    block.setType(Material.DIRT);
                    user.getPlayer().teleport(user.getLocation().add(0, 0.00251, 0)); // to prevent user from phasing
                    // through block
                    return;
                }
            }
            return;
        }

        Beehive data = (Beehive) block.getBlockData();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        
        if (data.getHoneyLevel() != data.getMaximumHoneyLevel())
            return;
        
        if (event.getItem() == null || (event.getItem().getType() != Material.GLASS_BOTTLE && event.getItem().getType() != Material.SHEARS))
            return;

        event.setCancelled(true);

        if (user.getStats().getBoolean("farming.beehive.booster_active") ||
                user.getBooster() > 1) {
            user.sendMessage("&cYou're already boosted");
            return;
        }

        if (!user.getStats().getBoolean("farming.beehive.booster_available")) {
            user.sendMessage("&cBeehive isn't ready to harvest!");
            return;
        }

        event.setCancelled(false);

        // user.sendMessage("currentTimeMillis: " + System.currentTimeMillis());

        long nextRefill = System.currentTimeMillis() + 24L * 3600000L; // Current time (in milliseconds) + (24 * 1hr (in
        // milliseconds)) <- simplification

        user.getStats().setNumber("farming.beehive.next_refill", nextRefill);
        user.getStats().setBoolean("farming.beehive.booster_active", true);
        user.getStats().setBoolean("farming.beehive.booster_available", false);
        Main.activeHives.add(user.getBeehive());
        user.activateBooster(); // this does it for all the island members & above
    }

    @EventHandler
    public void breakBeeHive(BlockBreakEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());
        Block block = event.getBlock();

        if (user.getIslandAtPosition().getRank(user.getUUID()) < RanksManager.COOP_RANK)
            return;

        if (block.getType() != Material.BEEHIVE)
            return;

        if (user.getIslandRank() != RanksManager.OWNER_RANK) {
            event.setCancelled(true);
            return;
        }

        Map<String, MetaDataValue> islandMetaData = user
                .getIsland()
                .getMetaData()
                .get();

        if (islandMetaData.get("hasBeeHive").asBoolean()) {
            islandMetaData.put("hasBeeHive", new MetaDataValue(false));
            user.getStats().getConfiguration().set("farming.beehive.placed", false);
            if (!user.getStats().getBoolean("farming.beehive.booster_active"))
                return;
            user.getStats().setBoolean("farming.beehive.booster_active", false);
            Main.activeHives.remove(block);
            // idk why that was here in the first place
            // user.deActivateBooster(); // for all members and not just owner
        }
    }

    @EventHandler
    public void honeyLevelChange(EntityEnterBlockEvent event) {
        if (event.getBlock().getType() != Material.BEEHIVE)
            return;

        Beehive data = (Beehive) event.getBlock().getBlockData();
        if (data.getHoneyLevel() != 0)
            data.setHoneyLevel(0);
    }
}
