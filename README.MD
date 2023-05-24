# Latest changes:

- Farming mostly implemented. (Bee functionality, and other stuff not implemented, yet.)
- Improved configuration system implemented, and ported Kiul's and PatriciaChips' to the system.
- Added a ``User``, and a ``UserManager`` to increase productivity and provide easier access to the player's stats.

# Deprecation (devs):

- ``userData.java`` is deprecated, use ``User.java``.
- ``globalEXP.java`` is deprecated, also use the above classes.

# ``User`` class & usage:

The ``User`` class is a new addition to the project, and it provides a basic user data framework, and it does some of the heavy-lifting for you.

It offers (Not everything, but here's the most important stuff):

- Access to stats (change & retrieve values) through many methods:
  - ``getLevel(SkillType skillType)``
  - ``getExperience(SkillType skillType)``
  - ``getSeaCreatureChance()``
  - ``getCollectedCrops(CropType cropType)``
  - ``And many more!`` (Look into the class method and its comments)
- Other classes included in the ``user`` package like:
  - ``SkillType`` - An enum of all the different SkillTypes.
  - ``Stats`` - A class that gives you complete access to the user's data.
  - ``UserManager`` - A simple user manager.

## Usage:

Usage requires a ``UserManager`` instance to be made (Already done in the Main class), and then you can access all the ``UserManager``'s methods through it's ``static getInstance()`` method.

### Example:

```java
@EventHandler
public void listen(PlayerJoinEvent event) {
    /*
     * UserManager contains three methods, currently:
     * - getUser(Player player) - Gets a User from a Bukkit Player Object
     * - getUser(String name) - Gets a User from a username.
     * - getOnlineUsers() - Gets a collection of all the online players in-game.
     */ 
    User user = UserManager.getInstance().getUser(event.getPlayer());

    user.sendTitle("&2chees burbur", "&ayou've been chees burbur'd", 3 /* Makes it stay on screen for x seconds*/);
    user.sendMessage("Your global experience is: " + user.getExperience(SkillType.GLOBAL));
}
```