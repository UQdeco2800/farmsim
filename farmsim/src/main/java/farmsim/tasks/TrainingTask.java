package farmsim.tasks;

import farmsim.entities.agents.Agent;
import farmsim.entities.agents.Agent.RoleType;
import farmsim.util.Point;
import farmsim.world.World;

/**
 * This training task allows a specific agent to gain experience for a specific role
 * Once the task is created, it should be assigned to selected agent immediately
 *
 * @author yiwen
 */
public class TrainingTask extends AgentRoleTask {

    private static final String NAME = "Traning Task";
    private static final String ID = "training";
    //The radio define how much experience can agent gain for one second training
    private static final int EXP_RADIO = 10;
    public static final int BASE_DURATION = 5000;

    private RoleType selectedRole;

    /**
     * Constructor for training task
     *
     * @param duration     the duration of the task, longer duration, more experience
     * @param world        world object
     * @param selectedRole specific role of this agent to be trained
     */
    public TrainingTask(int duration, World world,
                        RoleType selectedRole) {
        super(world.getStaffHouseLocation(), BASE_DURATION, world, NAME, ID);
        this.selectedRole = selectedRole;
        setNoConflicts();
    }

    @Override
    public AbstractTask copy() {
        return new TrainingTask(duration, world,
                selectedRole);
    }

    @Override
    public void preTask() {
        //No pre-task required
    }

    @Override
    public void postTask() {

    }

    @Override
    public RoleType relatedRole() {
        return selectedRole;
    }

    @Override
    public int experienceGained() {
        return this.duration / 1000 * EXP_RADIO;
    }
}
