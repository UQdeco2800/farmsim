package farmsim.TechTree;

import farmsim.entities.tileentities.crops.Crop;

/**
 * Created by jack775544 on 22/10/2015.
 */
public class WeatherSkill implements Skill {
    private Crop crop;
    private boolean active = false;

    @Override
    public int applySkill(int level) {
        // Do something involving weather here!!
        active = true;
        return 0;
    }

    @Override
    public int runSkill() throws RunSkillNotRequiredException {
        // Do this action when things need to be run!!
        return 0;
    }

    public boolean isActive(){
        return active;
    }
}
