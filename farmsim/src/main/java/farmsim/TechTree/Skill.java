package farmsim.TechTree;

import farmsim.entities.tileentities.crops.Crop;

/**
 * Created by jack775544 on 22/10/2015.
 */
public interface Skill {
    /**
     * Initialises a skill
     * @param c the crop to apply the skill to
     * @param level the skills level in the tech tree
     * @return 0 on failure
     */
    public int applySkill(int level);

    /**
     * Runs a skill on an action. May not be required to run
     * @return 0 on failure
     * @throws RunSkillNotRequiredException If the method is not needed.
     */
    public int runSkill() throws RunSkillNotRequiredException;

    public boolean isActive();
}
