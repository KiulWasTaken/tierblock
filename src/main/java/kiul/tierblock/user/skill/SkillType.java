package kiul.tierblock.user.skill;

public enum SkillType {
    
    FORAGING(7, 2),
    MINING(8, 5),
    FARMING(8, 2),
    FISHING(6),
    COMBAT(0); // completely based on island-level.

    public final int maxLevel;
	public int maxNetherLevel;

    private SkillType(int maxLevel) {
        this.maxLevel = maxLevel;
    }
	
	private SkillType(int maxLevel, int maxNetherLevel) {
		this.maxLevel = maxLevel;
		this.maxNetherLevel = maxNetherLevel;
	}

}
