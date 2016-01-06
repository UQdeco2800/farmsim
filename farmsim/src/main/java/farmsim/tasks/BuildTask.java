package farmsim.tasks;

import farmsim.buildings.AbstractBuilding;
import farmsim.buildings.LeggyShrine;
import farmsim.entities.agents.Agent;
import farmsim.entities.disease.Medicine;
import farmsim.entities.tools.ToolType;
import farmsim.util.Sound;

/**
 * A task for agents to build buildings.
 */
public class BuildTask extends AgentRoleTask {
    private static final String ID = "build";

    AbstractBuilding building;
    private Sound construction = new Sound("construction.mp3");
    private Sound leggy = new Sound("heavenly.mp3");
    
    private static final ToolType BONUS_TOOL = ToolType.HAMMER;

    public BuildTask(AbstractBuilding building) {
        super(building.getLocation(), building.getBuildTime(),
                building.getWorld(), "Build " + building, ID);
        this.building = building;
        // Adjust duration depending on agent level
        this.duration *= defaultRoleLevelTimeModifier();
        setBonusTool(BONUS_TOOL);
    }

    @Override
    public void preTask() {
        construction.play();
    }

    @Override
    public void postTask() {
    	Medicine.alterTreatmentPoints(building.getBuildTime());
        building.completeBuilding();
        if (building instanceof LeggyShrine) {
            leggy.play();
        }
        construction.stop();
    }

    @Override
    public Agent.RoleType relatedRole() {
        return Agent.RoleType.BUILDER;
    }

    @Override
    public int experienceGained() {
        return 50;
    }

    @Override
    public AbstractTask copy() {
        return new BuildTask(building);
    }

}
