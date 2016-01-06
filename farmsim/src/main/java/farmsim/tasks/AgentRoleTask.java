package farmsim.tasks;

import farmsim.entities.agents.Agent;
import farmsim.util.Point;
import farmsim.world.World;

public abstract class AgentRoleTask extends AbstractTask {

    private int roleLevel;

    public AgentRoleTask(Point point, int duration, World world, String name,
            String id) {
        super(point, duration, world, name, id);
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    public int minimumLevelRequired(){
        return 0;
    }

    public abstract Agent.RoleType relatedRole();

    public abstract int experienceGained();

    public int getRoleLevel() {
        return roleLevel;
    }

    protected int defaultRoleLevelTimeModifier() {
        return 1 + getRoleLevel() / 20;
    }
    
}
