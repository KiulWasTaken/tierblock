package kiul.tierblock.user.skill;

import java.util.HashMap;
import java.util.Map;

import kiul.tierblock.Main;
import kiul.tierblock.user.exceptions.InstanceAlreadyExistsException;

public class SkillManager {

    private static final Map<SkillType, Skill> registeredSkills = new HashMap<>();;
    private static SkillManager instance;

    public SkillManager() {
        if(instance != null) throw new InstanceAlreadyExistsException(getClass());
        instance = this;
    }

    public static SkillManager getInstance() {
        return instance;
    }

    public static Skill getSkill(SkillType type) {
        return registeredSkills.get(type);
    } 

    public static Map<SkillType, Skill> getRegisteredSkills() {
        return registeredSkills;
    }

    public void registerSkill(Skill skill) {
        registeredSkills.put(skill.getSkillType(), skill);
        Main.getInstance().getLogger().info("[SkillManager] Registered skill: " + skill.getClass().getSimpleName());
    }
    
}
