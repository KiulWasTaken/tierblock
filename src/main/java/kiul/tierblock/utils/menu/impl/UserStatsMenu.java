package kiul.tierblock.utils.menu.impl;

import java.lang.Math;
import java.util.List;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.user.skill.impl.FishingSkill;
import kiul.tierblock.utils.enums.CropType;
import kiul.tierblock.utils.enums.MineableType;
import kiul.tierblock.utils.enums.SkillCollectible;
import kiul.tierblock.utils.enums.WoodType;
import kiul.tierblock.utils.item.ItemBuilder;
import kiul.tierblock.utils.menu.Menu;

@SuppressWarnings("deprecation")
public class UserStatsMenu extends Menu {

    private Material getIcon(SkillType skillType) {
        switch(skillType) {
            case COMBAT:
                return Material.IRON_SWORD;
            case FORAGING:
                return Material.STONE_AXE;
            case FARMING:
                return Material.GOLDEN_HOE;
            case FISHING:
                return Material.FISHING_ROD;
            case MINING:
                return Material.DIAMOND_PICKAXE;
            default:
                return Material.AIR;
        }
    }

    private ChatColor getColor(SkillType skillType) {
        switch(skillType) {
            case COMBAT:
                return ChatColor.RED;
            case FORAGING:
                return ChatColor.GOLD;
            case FARMING:
                return ChatColor.YELLOW;
            case FISHING:
                return ChatColor.BLUE;
            case MINING:
                return ChatColor.GRAY;
            default:
                return ChatColor.WHITE;
        }
    }

    private SkillCollectible getSkillCollectibleType(User user, SkillType skillType, boolean isNether) {
        // int maxLevel = (isNether) ? (skillType.maxNetherLevel) : (skillType.maxLevel);
        int userLevel = user.getLevel(skillType, isNether);

        switch(skillType) {
            case FORAGING:
                return WoodType.values()[Math.min(userLevel - 1 + (isNether ? 6 : 0), 7)];
            case FARMING:
                return CropType.values()[Math.min(userLevel - 1 + (isNether ? 7 : 0), 8)];
            case MINING:
                return MineableType.values()[Math.min(userLevel - 1 + (isNether ? 8 : 0), 12)];
            default:
                return null;
        }
    }

    private String drawProgressBar(double xp, double requiredXp) {
        int size = 10;
        double progress = xp/requiredXp;
        String bar = 
            new String("&a■").repeat(Math.min(10, (int)(progress * size))) + 
            new String("&7■").repeat(((int)(progress * size) > size) ? 0 : size - (int)(progress * size));
        return bar;
    }

    public UserStatsMenu(User user) {
        super(user, "&d&lYour statistics", Menu.InventorySize.BIG);
        
        fillRestWith(
            new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).
            displayName("").build()
        );
        
        // GLOBAL
        setItem(
            new ItemBuilder(Material.GRASS_BLOCK).
            displayName("&aYour island's stats:").
            lore(
                List.of(
                    ("&bBooster: &e" + user.formatBoosterTime()),
                    ("&bLevel: &e" + user.getGlobalLevel()),
                    ("&bExperience: &e" + Main.DECIMAL_FORMAT.format(user.getGlobalExperience())),
                    ("&bProgress: &a" + 
                        Main.DECIMAL_FORMAT.format((user.getGlobalExperience()/((user.getGlobalLevel() >= 10) ? 1000.0 : 100.0))*100) +
                        "% &8[ " + drawProgressBar(user.getGlobalExperience(), ((user.getGlobalLevel() >= 10) ? 1000.0 : 100.0)) + " &8]")
                )
            ).build(), 13
        );
        
        // ALL SKILLS
        double fishingRequirement = FishingSkill.getRequirement(user);
        int startingIndex = 20;
        for(SkillType skillType : SkillType.values()) {

            SkillCollectible collectibleType = getSkillCollectibleType(user, skillType, false);
            
            double levelUp = 0.0;
            if(skillType != SkillType.COMBAT) {
                levelUp = (skillType == SkillType.FISHING ? fishingRequirement : collectibleType.levelUp());
            }

            double experience = user.getExperience(skillType, false);
            String progress = user.getLevel(skillType, false) == skillType.maxLevel ? "&aMAXED" : Main.DECIMAL_FORMAT.format((experience/levelUp)*100) +
            "% &8[ " + drawProgressBar(experience, levelUp) + " &8]";

            List<String> skillLore = new ArrayList<>(skillType == SkillType.COMBAT ? 
                List.of(
                    ("&bMonsters Killed: &e" + user.getMonsterKills())
                ) :
                List.of(
                    ("&bLevel: &e" + user.getLevel(skillType, false)),
                    ("&bExperience: &e" + Main.DECIMAL_FORMAT.format(experience)),
                    ("&bProgress: &a" + progress),
                    ("&bProgress (Blocks left): &a" + ((experience == skillType.maxLevel) ? "&aMAXED" : ((int)(levelUp - experience))))

                ));

            if(skillType != SkillType.FISHING && skillType != SkillType.COMBAT) {
                SkillCollectible netherCollectibleType = getSkillCollectibleType(user, skillType, true);
                experience = user.getExperience(skillType, true);
                double netherProgress = Math.max(0, (experience/netherCollectibleType.levelUp()));
                skillLore.addAll(
                    new ArrayList<String>(
                    user.getStats().getBoolean(skillType.toString().toLowerCase() + ".nether.unlocked") ?
                    List.of(
                        " ",
                        "&c&lSub-skill &estats:",
                        "&cLevel: &e" + user.getLevel(skillType, true),
                        "&cExperience: &e" + Main.DECIMAL_FORMAT.format(user.getExperience(skillType, true)),
                        // cuz last nether collectibles have no levelUp value.
                        "&cProgress: &a" + ((user.getLevel(skillType, true) == skillType.maxNetherLevel) ? "&aMAXED" : Main.DECIMAL_FORMAT.format(netherProgress * 100) + "% &8[ " +
                        drawProgressBar(user.getExperience(skillType, true), netherCollectibleType.levelUp()) + " &8]"),
                        "&cProgress (Blocks left): &e" + ((user.getLevel(skillType, true) == skillType.maxNetherLevel) ? "&aMAXED" : ((int)(netherCollectibleType.levelUp() - experience)))
                    ) :
                    List.of(
                        "",
                        "&cYou need to get &emax level &cto unlock the sub-skill!"
                    ))
                );
            }

            setItem(
                new ItemBuilder(getIcon(skillType)).
                    displayName(getColor(skillType) + "&l" + skillType.toString() + " &estats:").
                    lore(skillLore).build(),
                    startingIndex
            );
            startingIndex++;
        }

        show(user);
    }
    


}
