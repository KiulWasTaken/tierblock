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
        super(user, "&3&lYour statistics", Menu.InventorySize.BIG);
        
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
                        Main.DECIMAL_FORMAT.format((user.getGlobalExperience()/((user.getGlobalLevel() > 10) ? 1000.0 : 100.0))*100) +
                        "% &8[ " + drawProgressBar(user.getGlobalExperience(), ((user.getGlobalLevel() > 10) ? 1000.0 : 100.0)) + " &8]")
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

            String progress = user.getLevel(skillType, false) == skillType.maxLevel ? "&aMAXED" : Main.DECIMAL_FORMAT.format((user.getExperience(skillType, false)/levelUp)*100) +
            "% &8[ " + drawProgressBar(user.getExperience(skillType, false), levelUp) + " &8]";

            List<String> skillLore = new ArrayList<>(skillType == SkillType.COMBAT ? 
                List.of(
                    ("&bMonsters Killed: &e" + user.getMonsterKills())
                ) :
                List.of(
                    ("&bLevel: &e" + user.getLevel(skillType, false)),
                    ("&bExperience: &e" + Main.DECIMAL_FORMAT.format(user.getExperience(skillType, false))),
                    ("&bProgress: &a" + progress)
                ));

            if(skillType != SkillType.FISHING && skillType != SkillType.COMBAT) {
                SkillCollectible netherCollectibleType = getSkillCollectibleType(user, skillType, true);
				double netherProgress = (netherCollectibleType.levelUp() <= 0 ? 0 : Math.min(0, user.getExperience(skillType, true)/netherCollectibleType.levelUp()));
                skillLore.addAll(
					new ArrayList<String>(
                    user.getStats().getBoolean(skillType.toString().toLowerCase() + ".nether.unlocked") ?
                    List.of(
                        " ",
                        "&c&lSub-skill &estats:",
                        "&cLevel: &e" + user.getLevel(skillType, true),
                        "&cExperience: &e" + Main.DECIMAL_FORMAT.format(user.getExperience(skillType, true)),
						// cuz last nether collectibles have no levelUp value.
                        "&cProgress: " + ((user.getLevel(skillType, true) == skillType.maxNetherLevel) ? "&aMAXED" : Main.DECIMAL_FORMAT.format(netherProgress) + "% &8[ " +
                        drawProgressBar(user.getExperience(skillType, true), netherCollectibleType.levelUp()) + " &8]")
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
