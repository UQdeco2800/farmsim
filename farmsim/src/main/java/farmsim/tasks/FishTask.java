package farmsim.tasks;

import farmsim.resource.SimpleResource;
import farmsim.buildings.AbstractBuilding;
import farmsim.buildings.Jetty;
import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.objects.Water;
import farmsim.entities.tools.ToolType;
import farmsim.inventory.SimpleResourceHandler;
import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;

import java.util.List;
import java.util.Set;

/**
 * FishTask allows users to fish in a body of water. Fishing is possible on
 * Water tiles and will be successful randomly.
 * 
 * @author rachelcatchpoole for Team Adleman
 *
 */
public class FishTask extends AgentRoleTask {

    private static final int BASE_DURATION = 5000;
    private static final String NAME = "Fish";
    private static final String ID = "fish";
    private Point jettyPoint;

    public FishTask(Point point, World world) {
        super(point, BASE_DURATION, world, NAME, ID);
        // Adjust duration depending on agent level
        this.duration *= defaultRoleLevelTimeModifier();
        setRequiredTool(ToolType.FISHING_ROD);
        isValid();
    }

    @Override
    public Agent.RoleType relatedRole() {
        return Agent.RoleType.FARMER;
    }

    @Override
    public int experienceGained() {
        return 1;
    }

    /**
     * @return the assigned agent for this task
     */
    public Agent getAssignedAgent() {
        AgentManager agentManager = AgentManager.getInstance();
        List<Agent> agents = agentManager.getAgents();

        for (Agent agent : agents) {
            if (agent.getTask().equals(this)) {
                return agent;
            }
        }
        return null;
    }

    @Override
    public void preTask() {
        // Do nothing
    }

    @Override
    public void postTask() {

        Agent agent = getAssignedAgent();

        double random = Math.random();
        if (random >= 0.75) {
            SimpleResource fish = SimpleResourceHandler.getInstance().fish;
            agent.getRucksack().addToRucksack(new SimpleResource(fish.getType(),
                    fish.getAttributes(), 1));
        }

    }

    /**
     * Returns the point from which the agent should fish.
     * 
     * @return Point the Point on the jetty where the agent should fish from.
     */
    @Override
    public Point getDestination() {
        return jettyPoint;
    }

    /**
     * 
     * @param jetty
     *            the jetty building to examine
     * @return true if
     */
    private boolean nearJetty(Jetty jetty) {
        double fishX = location.getX();
        double fishY = location.getY();
        double jettyX = jetty.getWorldX();
        double jettyY = jetty.getWorldY();
        jettyPoint = new Point(jetty.getWorldX() + 2, jetty.getWorldY());
        int radius = jetty.getRadius();

        return fishX >= jettyX - radius && fishX <= jettyX + radius
                && fishY >= jettyY - radius && fishY <= jettyY + radius;

    }

    /**
     * Fishing is valid if done on a water tile near a jetty.
     */
    @Override
    public boolean isValid() {
        boolean nearJetty = false;
        World world = WorldManager.getInstance().getWorld();
        Set<AbstractBuilding> buildings = world.getBuildings();
        for (AbstractBuilding building : buildings) {
            if (building instanceof Jetty) {
                nearJetty = nearJetty((Jetty) building);
            }
            if (nearJetty) {
                break;
            }
        }

        TileEntity entity = tile.getTileEntity();
        return nearJetty && entity instanceof Water
                && entity.getTileType().equals("water");
    }

    @Override
    public AbstractTask copy() {
        return new FishTask(location, world);
    }

}
