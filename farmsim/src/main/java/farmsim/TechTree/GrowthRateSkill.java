package farmsim.TechTree;

import farmsim.entities.tileentities.crops.Crop;

/**
 * Created by jack775544 on 22/10/2015.
 */
public class GrowthRateSkill implements Skill {
    private boolean active = false;
    int rate = 0;

    @Override
    public int applySkill(int level) {
        active = true;
        rate = -150 * level;
        return 1;
    }

    public int getModifiedGrowthTime(){
        return rate;
    }

    @Override
    public int runSkill() throws RunSkillNotRequiredException{
        throw new RunSkillNotRequiredException("Growth Rate not required");
    }

    public boolean isActive(){
        return active;
    }
}
