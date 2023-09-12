package kiul.tierblock.utils.enums;

import kiul.tierblock.user.skill.SkillType;

public interface SkillCollectible {
    
    String label();
    double levelUp();
    double xpReward();

    public static SkillCollectible[] getSkillCollectibleType(SkillType skillType) {
        switch(skillType) {
            case FORAGING:
                return WoodType.values();
            case FARMING:
                return CropType.values();
            case MINING:
                return MineableType.values();
            default:
                return null;
        }
    }
}
