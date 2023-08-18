# Bug fixes:
- Sub-skills in stats menu used to display user's (non-nether/end) experience as progress percentage.
- Player flight removed after player changes gamemode to creative/spectator.
- After the reimplementation of global levels, the plugin didn't check for global levelups.
- getIsland() used to return inconsistent results after server restarts, causing most of the plugin to not work.
- Island Metadata seems to not be saved after server reloads, only working after server restarts.
- User data value ``has_island`` couldn't be changed due to ``Stats#setBoolean()`` not saving changes to user data file (dum dum).
- SkillType max level mistakes:
  - Farming max level was 8 instead of 7
  - Foraging max level was 7 instead of 6
- Fixed dropping beetroot & other crops (while the player doesn't fit the level requirement) when trampled.

# Latest changes:

- ## Latest commit:
  - Added LuckPerms to the dependency list
  - Added some scuffed integration for the cobblegen addon

- ## Recent:
  - ``IslandCreationListener`` renamed to ``IslandListener``.
  - Booster time left (or whether you have a booster or not) is added to the stats menu.
  - User stats menu doesn't show a maxed skill's progress bar, instead just says "MAXED".    
  - Sorta despaghettified ``UserStatsMenu``.
  - Added nether/end sub-skill stats to ``UserStatsMenu`` (Only for the skills that have it).
  - Minor combat changes:
    - User data value ``pillager_kills`` renamed to ``pillagers_killed``.
    - Added new user data value ``monsters_killed``.
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