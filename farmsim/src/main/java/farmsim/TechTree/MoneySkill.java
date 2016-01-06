package farmsim.TechTree;

import farmsim.entities.tileentities.crops.Crop;
import farmsim.money.Money;

/**
 * Created by jack775544 on 22/10/2015.
 */
public class MoneySkill implements Skill {
    private int moneyRate;

    @Override
    public int applySkill(int level) {
        active = true;
        moneyRate = 10 * level;
        //By default we are on level 2 for this skill so it will give $20
        return 1;
    }

    @Override
    public int runSkill() throws RunSkillNotRequiredException{
        return 1;
    }

    public boolean isActive(){
        return active;
    }
}
