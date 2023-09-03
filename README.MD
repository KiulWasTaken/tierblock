# Bug fixes:

- ## Latest commit:
  - ``/buygexp`` uses ``add`` instead of ``subtract`` now... (Yeah, idk how I messed that up)
  - Users can't place blocks that require a high level now.
  - Beehives now tell the user if the beehive is theirs or not.

- ## Recent:
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
  - Level up now plays a ``BLOCK_NOTE_BLOCK_PLING`` sound instead of ``UI_TOAST_CHALLENGE_COMPLETE`` sound.

- ## Recent:
  - Default GEXP value for stone & cobblestone has been changed from ``1 GEXP`` to ``0.05 GEXP``.
  - Added the new ``/test logme`` sub-command, for debugging, and admin purposes.
  - Minor changes to User, and it now has some compatibility with Bukkit's ``OfflinePlayer``.
  - Island flight is now toggleable, and not forced. Use command ``/islandflight``
  - Added support for cobblegen addon.
  - Removed LuckPerms to the dependency list

# How to use the ItemBuilder

```java
// normal item stack
new ItemBuilder(Material.STONE).displayName("&7Hot stone block").lore(/*Your own List<String> as lore*/).build()

// clickable gui item. ONLY WORKS WITH Menu CLASS!!! <-------
new ItemBuilder(Material.STONE).displayName("&7Hot &cclickable &7stone block").lore(/*Your own List<String>*/).buidAsClickAble(() -> {
    // Do stuff here. fired once clicked only.
});```