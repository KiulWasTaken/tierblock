package kiul.tierblock.user.skill;

import kiul.tierblock.user.User;

// NOT DONE! - DO NOT USE
public abstract class Skill {
    
    String name;

    public Skill(String name) {
        this.name = name;
    }

    public abstract void levelUpRequirements();
    public abstract void levelUp(User user);

}
