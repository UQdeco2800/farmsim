package farmsim.entities.agents;

import farmsim.entities.disease.Illness;
import farmsim.util.Animation.SpriteSheetAnimation;

public class AgentRole {

    // level experience constants
    protected static final int[] levelExperienceConstants =
            {100, 300, 700, 1500};


    // Walking animations (must be defined for all roles)
    private AgentRoleTravellingSpriteSheets spriteSheets;
    private SpriteSheetAnimation walkNorth;
    private SpriteSheetAnimation walkNorthEast;
    private SpriteSheetAnimation walkEast;
    private SpriteSheetAnimation walkSouthEast;
    private SpriteSheetAnimation walkSouth;
    private SpriteSheetAnimation walkSouthWest;
    private SpriteSheetAnimation walkWest;
    private SpriteSheetAnimation walkNorthWest;

    // Name and RoleType variables
    private Agent.RoleType roleType;

    // The experience for this roleType;
    private int experience;

    /**
     * Constructor for AgentRole. Sets up the specified roleType.
     */
    public AgentRole(Agent.RoleType roleType,
                     AgentRoleTravellingSpriteSheets spriteSheets) {
        this.roleType = roleType;
        this.spriteSheets = spriteSheets;
        initAnimations();
        this.experience = 0;
    }

    /**
     * Method that returns marketplace property string, by converting the roleType
     * name into marketplace format.
     * @return marketplace experience property name
     */
    public String marketPlaceString() {
        // Convert roleType name to lowercase and remove spaces and prefix with
        // 'exp_'
        return "exp_" + roleType.displayName().toLowerCase().replaceAll("\\s+", "");
    }

    /**
     * Setter method for experience variable.
     * @param experience int number of experience for roleType
     */
    public void setExperience(int experience){
        this.experience = experience;
    }


    private void initAnimations() {
        walkSouth = new SpriteSheetAnimation(null, 0, 3);
        walkWest = new SpriteSheetAnimation(null, 4, 2);
        walkEast = new SpriteSheetAnimation(null, 6, 2);
        walkNorth = new SpriteSheetAnimation(null, 8, 3);
        walkSouthEast = new SpriteSheetAnimation(null, 12, 4);
        walkNorthEast = new SpriteSheetAnimation(null, 16, 4);
        walkSouthWest = new SpriteSheetAnimation(null, 20, 4);
        walkNorthWest = new SpriteSheetAnimation(null, 24, 4);
    }

    /**
     * Getter method for name of roleType
     *
     * @return Name of roleType
     */
    public String getName() {
        return roleType.displayName();
    }

    /**
     * Method for getting enum roleType of this agent roleType
     *
     * @return
     */
    public Agent.RoleType getRoleType() {
        return roleType;
    }

    /**
     * @param orientation Agent.Orientation enum indicating orientation ofw
     *                    agent
     * @param ()    boolean value indicating if the agent is diseased
     * @return a SpriteSheet animation for the agent to travel in the given
     * orientation
     */
    public SpriteSheetAnimation getTravellingAnimation(
            Agent.Orientation orientation, Illness disease) {
        SpriteSheetAnimation animation = null;
        switch (orientation) {
            case NORTH:
                animation = walkNorth;
                break;
            case NORTHEAST:
                animation = walkNorthEast;
                break;
            case EAST:
                animation = walkEast;
                break;
            case SOUTHEAST:
                animation = walkSouthEast;
                break;
            case SOUTH:
                animation = walkSouth;
                break;
            case SOUTHWEST:
                animation = walkSouthWest;
                break;
            case WEST:
                animation = walkWest;
                break;
            case NORTHWEST:
                animation = walkNorthWest;
                break;
        }
        // Get relevant sprite sheet
        if (disease != null) {
        	animation.setSpriteSheet(spriteSheets.getSpriteSheet(disease.getName()));
        } else {
        	animation.setSpriteSheet(spriteSheets.getSpriteSheet(null));
        }
        return animation;
    }

     * Add experience. If experience after is more than the maximum, set it to
     * the maximum;
     * @param exp Integer level of experience to add
     * @require that exp > 0
     * @return 0 if the the experience is just added 1 if level up
    public int addExp(int exp) {
        int max = levelExperienceConstants[3];
        int beforeAdd = experience;
        if (experience != max) {
            experience += exp;
            if (experience > max) {
                experience = max;
            return checkLevelUp(beforeAdd);
        }

        return 0;
    }

    /**
     * Checks to see if the worker has leveled up from the adding of experience
     *
     * @param beforeAdd experience before anything has been added.
     * @return 0 if no level up 1 if it occurred.
     */
    protected int checkLevelUp(int beforeAdd) {
        for (int levelExperienceConstant : levelExperienceConstants) {
            if (beforeAdd < levelExperienceConstant && experience >=
                    levelExperienceConstant) {
                return 1;
            }
            if (experience < levelExperienceConstant) {
                // if we get in here it can't be in a further level.
                break;
            }
        }
        return 0;

    /**
     * Get the experience
     *
     * @return the current experience
     */
    public int getExp() {
        return experience;
    }

     * Calculates the level of the worker based on their current experience. The
     * experience is compared to the level experience constants until the
     * correct level is found
     * @return The current level of the roleType which is 1-5
    public int getLevel() {
        for (int i = 0; i < 4; i++) {
            if (experience < levelExperienceConstants[i]) {
                return i + 1;
            }
        }
        return 5;
    }

    /**
     * @return String representation of class.
     */
    @Override
    public String toString() {
        return "Level " + Integer.toString(getLevel()) + " " + getName();
    }
