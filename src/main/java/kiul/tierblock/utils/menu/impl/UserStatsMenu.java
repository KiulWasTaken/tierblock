package kiul.tierblock.utils.menu.impl;

import java.util.List;

import org.bukkit.Material;

import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.user.skill.SkillType;
import kiul.tierblock.user.skill.impl.FishingSkill;
import kiul.tierblock.utils.enums.CropType;
import kiul.tierblock.utils.enums.MineableType;
import kiul.tierblock.utils.enums.WoodType;
import kiul.tierblock.utils.item.ItemBuilder;
import kiul.tierblock.utils.menu.Menu;

public class UserStatsMenu extends Menu {

    private String drawProgressBar(double xp, double requiredXp) {
        int size = 10;
        double progress = xp/requiredXp;
        String bar = 
            new String("&a■").repeat((int)(progress * size)) + 
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
                    ("&bLevel: &e" + user.getGlobalLevel()),
                    ("&bExperience: &e" + Main.DECIMAL_FORMAT.format(user.getGlobalExperience())),
                    ("&bProgress: &a" + 
                        "%" + Main.DECIMAL_FORMAT.format((user.getGlobalExperience()/((user.getGlobalLevel() > 10) ? 1000.0 : 100.0))*100) +
                        " &8[ " + drawProgressBar(user.getGlobalExperience(), ((user.getGlobalLevel() > 10) ? 1000.0 : 100.0)) + " &8]")
                )
            ).build(), 13
        );
		
        // COMBAT

		setItem(
            new ItemBuilder(Material.IRON_SWORD).
            displayName("&c&lCombat &estats:").
            lore(
                List.of(
                    ("&bMonsters Killed: &e" + user.getMonsterKills())
                )
            ).build(), 20
        );
		
        int foragingLevel = user.getLevel(SkillType.FORAGING, false);
        WoodType woodType = WoodType.values()[foragingLevel-1];
        double foragingXp = user.getExperience(SkillType.FORAGING, false);

		setItem(
            new ItemBuilder(Material.IRON_AXE).
            displayName("&6&lForaging &estats:").
            lore(
                List.of(
                    ("&bLevel: &e" + user.getGlobalLevel()),
                    ("&bExperience: &e" + Main.DECIMAL_FORMAT.format(user.getGlobalExperience())),
                    ("&bProgress: &a" + 
                        "%" + Main.DECIMAL_FORMAT.format((user.getGlobalExperience()/((user.getGlobalLevel() > 10) ? 1000.0 : 100.0))*100) +
                        " &8[ " + drawProgressBar(foragingXp, woodType.levelUp) + " &8]")
                )
            ).build(), 24
        );
		
        int farmingLevel = user.getLevel(SkillType.FARMING, false);
        CropType cropType = CropType.values()[farmingLevel-1];
        double farmingXp = user.getExperience(SkillType.FARMING, false);

		setItem(
            new ItemBuilder(Material.GOLDEN_HOE).
            displayName("&e&lFarming &estats:").
            lore(
                List.of(
                    ("&bLevel: &e" + user.getGlobalLevel()),
                    ("&bExperience: &e" + Main.DECIMAL_FORMAT.format(user.getGlobalExperience())),
                    ("&bProgress: &a" + 
                        "%" + Main.DECIMAL_FORMAT.format((user.getGlobalExperience()/((user.getGlobalLevel() > 10) ? 1000.0 : 100.0))*100) +
                        " &8[ " + drawProgressBar(farmingXp, cropType.levelUp) + " &8]")
                )
            ).build(), 31
        );

        double fishingXp = user.getExperience(SkillType.FISHING, false);
        double fishingRequirement = FishingSkill.getRequirement(user);
		
		setItem(
            new ItemBuilder(Material.FISHING_ROD).
            displayName("&9&lFishing &estats:").
            lore(
                List.of(
                    ("&bLevel: &e" + user.getGlobalLevel()),
                    ("&bExperience: &e" + Main.DECIMAL_FORMAT.format(user.getGlobalExperience())),
                    ("&bProgress: &a" + 
                        "%" + Main.DECIMAL_FORMAT.format((user.getGlobalExperience()/((user.getGlobalLevel() > 10) ? 1000.0 : 100.0))*100) +
                        " &8[ " + drawProgressBar(fishingXp, fishingRequirement) + " &8]")
                )
            ).build(), 38
        );
		
        int miningLevel = user.getLevel(SkillType.MINING, false);
        MineableType mineableType = MineableType.values()[miningLevel-1];
        double miningXp = user.getExperience(SkillType.MINING, false);

		setItem(
            new ItemBuilder(Material.DIAMOND_PICKAXE).
            displayName("&7&lMining &estats:").
            lore(
                List.of(
                    ("&bLevel: &e" + user.getGlobalLevel()),
                    ("&bExperience: &e" + Main.DECIMAL_FORMAT.format(user.getGlobalExperience())),
                    ("&bProgress: &a" + 
                        "%" + Main.DECIMAL_FORMAT.format((user.getGlobalExperience()/((user.getGlobalLevel() > 10) ? 1000.0 : 100.0))*100) +
                        " &8[ " + drawProgressBar(miningXp, mineableType.levelUp) + " &8]")
                )
            ).build(), 42
        );

        show(user);
    }
    


}
