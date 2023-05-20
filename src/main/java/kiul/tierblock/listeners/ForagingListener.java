package kiul.tierblock.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;

import kiul.tierblock.Main;
import kiul.tierblock.user.User;
import kiul.tierblock.user.UserManager;
import kiul.tierblock.utils.enums.WoodType;

public class ForagingListener implements Listener {

    @EventHandler
    public void blockBreakListener(BlockBreakEvent event) {
        User user = UserManager.getInstance().getUser(event.getPlayer());
        Block block = event.getBlock();
        WoodType type = WoodType.fromMaterial(block.getType());
        
		if(type == null) return; // not wood, stopping here.
        if(block.hasMetadata("used")) return; // player-placed block

        event.setDropItems(false); // just incase you can't mine it

        if((type == WoodType.CRIMSON || type == WoodType.WARPED) && user.getGlobalLevel() < 40) { // if crimson/warped & level isn't 40, we tell him to fuck off.
            user.sendActionBar("&cYou need to have &elevel 40 or above&c to break this!");
            return;
        }

        WoodType requiredType = WoodType.fromInt(type.toInt()-1);
		int levelUpRequirement = Main.getInstance().getConfig().getInt("foraging." + requiredType + ".level_up");
        
        // if isn't oak & hasn't reached the wood type, we tell him to fuck off.
        if(!(type == WoodType.OAK) && user.getCollectedWood(requiredType) < levelUpRequirement) {
            // C/C++ like-ish enums. for easier work...
            WoodType nextWoodType = WoodType.fromInt(type.toInt()+1); // WoodType + 1
            user.sendActionBar(
                String.format("&cYou need to forage &e%s &c%s to be able to mine %s!",
                    (levelUpRequirement - user.getCollectedWood(requiredType)), 
                    requiredType.formatName(),
                    nextWoodType.toString()
                )
            );
            return;
        }

        event.setDropItems(true);

        double reward = Main.FORAGING_REWARD_BLOCKS.get(block.getType());
        user.sendActionBar(
            String.format(
                "&eGlobal: &2+&a%sxp &8| &eForaging &2+&a1xp &8(&b%s&8)",
                reward,
                type.toString()
            )
        );
        user.addForaging(WoodType.fromMaterial(block.getType()), 1);
        user.addGlobalExperience(reward);
    }

    @EventHandler
    public void blockPlaceListener(BlockPlaceEvent event) {
        Block block = event.getBlock();

        if(!Main.FORAGING_REWARD_BLOCKS.containsKey(block.getType())) return; // not wood, stopping here.
        block.setMetadata("used", new FixedMetadataValue(Main.getInstance(), "state"));
    }
    
}
