# Bug fixes:

- ## Latest commit:
  - Dying in an island warp, will teleport you to your island/the spawn island, unless you're a member of that island.
  - Spiders only spawn where they should.
  - Farming listener doesn't stop operators from building on anyone's island anymore.
  - Foraging rewards increased for level 4 to 6 (inclusive).

- ## Recent:
  - Pillagers don't spawn anywhere now.
  - You can actually access nether generators after unlocking nether subskill in mining, smh.
  - Fixed foraging, and farming level ups.

# Latest changes:

- ## Latest commit:
  - New ``/test isthispp`` sub-command for mining debug purposes.

- ## Recent:
  - Combat mob spawns improvements:
    - Increased spawn chances of shulker.
    - Due to how frequent pillager spawn rates are, they've been decreased from ``2%`` to ``0.1%``.
    - Which mobs spawned, and where are now finally determined by tierblock and not spigot. This means shulkers, wither skeletons, blazes now spawn naturally in their worlds.


# How to use the ItemBuilder

```java
// normal item stack
new ItemBuilder(Material.STONE).displayName("&7Hot stone block").lore(/*Your own List<String> as lore*/).build()

// clickable gui item. ONLY WORKS WITH Menu CLASS!!! <-------
new ItemBuilder(Material.STONE).displayName("&7Hot &cclickable &7stone block").lore(/*Your own List<String>*/).buidAsClickAble(() -> {
    // Do stuff here. fired once clicked only.
});
```