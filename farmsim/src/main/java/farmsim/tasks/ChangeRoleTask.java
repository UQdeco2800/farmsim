package farmsim.tasks;

import farmsim.entities.agents.Agent;
import farmsim.util.Point;
import farmsim.world.World;

public class ChangeRoleTask extends AbstractTask {

    private static final int BASE_DURATION = 1000;
    private static final String ID = "change-role";

    private Agent.RoleType selectedRoleType;
    private boolean lock;
    private Point staffHouseLocation;

    public ChangeRoleTask(Agent.RoleType roleType, World world, boolean lock) {
        super(world.getStaffHouseLocation(), BASE_DURATION, world,
                "Change role to " + roleType, ID);
        this.selectedRoleType = roleType;
        this.lock = lock;
        staffHouseLocation = world.getStaffHouseLocation();
        setNoConflicts();
    }

    public Agent.RoleType getSelectedRoleType() {
        return selectedRoleType;
    }

    @Override
    public void preTask() {
        // do nothing
    }

    @Override
    public void postTask() {
        if (selectedRoleType == null) {
            throw new RuntimeException("Cannot change agent roleType to null");
        }
        getWorker().setCurrentRoleType(selectedRoleType);
        if (lock) {
            getWorker().lockRole();
        }
        TaskManager.getInstance().updateTaskRoles();
    }

    @Override
    public boolean isValid() {
        if (staffHouseLocation == null) {
            return false;
        }
        if (selectedRoleType == getWorker().getCurrentRoleType()) {
            return false;
        }
        return true;
    }

    @Override
    public AbstractTask copy() {
        return new ChangeRoleTask(selectedRoleType, world, lock);
    }

}
