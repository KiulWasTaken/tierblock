# Latest changes:

- All skills are now managed with the new ``SkillManager`` class.
- Fully implemented Farming (With beehive boosters).
- Fully implemented Mining.
- Monster-combat system implemented (With the pillager & the raid captain system).
- Rewrote ``Commands`` class (contains most of the dev commands)
- Added new features to ``TestCommand (/test)`` for dev tools:
  - ``booster`` subcommand, displays booster time left.
  - ``metadata`` subcommand, displays island's main metadata value, and available ones.
  - ``setbool`` & ``setdouble`` subcommands, both change an island's metadata value of their type.
  - ``copydefaults`` subcommand, completely resets the user's stats (Only the executor).
  - ``readyuphive`` subcommand, makes a hive ready to boosted (Doesn't change the honey state).
  - ``islandlevel`` subcommand, changes an island's level.
  - ``debug_scoreboard`` subcommand, displays a hot scoreboard for skill-debugging purposes
- Added new ``/leaderboard`` command which has other aliases:
  - leaderb
  - lboard
  - lb
  - leaderousboardous
- Added new ``/stats`` command which displays a small menu that contains general user stats, and has other aliases:
  - stat
  - statistics
  - statisticus
  - je_mange_un_chat
- Major overhauls for ``WoodType``, ``MineableType``, ``CropType``, and ``MonsterType``
- New ``Menu`` abstract class for easier menu creation
  - New ``ItemBuilder`` class to make ItemStacks, and ``ClickableItem``
  - New ``ClickableItem`` (created from ItemBuilder's ``buildAsClickable(ClickableEvent)``)

# How to use the ItemBuilder

```java
// normal item stack
new ItemBuilder(Material.STONE).displayName("&7Hot stone block").lore(/*Your own List<String> as lore*/).build()

// clickable gui item. ONLY WORKS WITH Menu CLASS!!! <-------
new ItemBuilder(Material.STONE).displayName("&7Hot &cclickable &7stone block").lore(/*Your own List<String>*/).buidAsClickAble(() -> {
    // Do stuff here. fired once clicked only.
});```