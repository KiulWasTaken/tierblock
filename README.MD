# Bug fixes:

- ## Latest commit:
  - Pillagers don't spawn anywhere now.
  - You can actually access nether generators after unlocking nether subskill in mining, smh.
  - Fixed foraging, and farming level ups.

- ## Recent:
  - Level up recursion in farming, and foraging due to forgetting to account for ``global_level_requirement``
  - Island level ups happen again now.
  - Fixed a stupid where it tells user they can't place block, but never cancels event (Stupid frog).
  - ``/buygexp`` uses ``add`` instead of ``subtract`` now... (Yeah, idk how I messed that up)

# Latest changes:

- ## Latest commit:
  - Combat mob spawns improvements:
    - Due to how frequent pillager spawn rates are, they've been decreased from ``2%`` to ``0.1%``.
    - Which mobs spawned, and where are now finally determined by tierblock and not spigot. This means shulkers, wither skeletons, blazes now spawn naturally in their worlds.


- ## Recent:
  - Added sub-command ``/test whereami`` for dev purposes.
  - Islands have default names now.
  - When levelling up in a skill and the new acquired item requires a level higher than the island's level, user will be told.
  - Global level ups are now recursive.
  - Level up now plays a ``BLOCK_NOTE_BLOCK_PLING`` sound instead of ``UI_TOAST_CHALLENGE_COMPLETE`` sound.

# How to use the ItemBuilder

```java
// normal item stack
new ItemBuilder(Material.STONE).displayName("&7Hot stone block").lore(/*Your own List<String> as lore*/).build()

// clickable gui item. ONLY WORKS WITH Menu CLASS!!! <-------
new ItemBuilder(Material.STONE).displayName("&7Hot &cclickable &7stone block").lore(/*Your own List<String>*/).buidAsClickAble(() -> {
    // Do stuff here. fired once clicked only.
});```