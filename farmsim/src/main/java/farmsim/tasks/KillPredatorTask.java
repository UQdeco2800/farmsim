package farmsim.tasks;

import farmsim.entities.agents.Agent;
import farmsim.entities.predators.Predator;
import farmsim.util.Point;
import farmsim.world.World;

/**
 * Kill predator task for the hunter role.
 */
public class KillPredatorTask extends AgentRoleTask implements MovingTask {
    private static final int BASE_DURATION = 100;
    private static final int BASE_PRIORITY = 15;
    private static final String ID = "killPredator";
    private Predator predator;

    public KillPredatorTask(Predator predator, int x, int y,
                            World
                                    world, String name) {
        super(new Point(x, y), BASE_DURATION, world, name, ID);
        this.predator = predator;

        // Adjust duration depending on agent level
        this.duration *= defaultRoleLevelTimeModifier();
        setNoConflicts();
    }

    public KillPredatorTask(Predator predator, Point point, int duration,
                            World world, int priority, String name,
                            String id) {
        super(point, duration, world, name, id);
        this.predator = predator;
        setNoConflicts();
    }

    @Override
    public Agent.RoleType relatedRole() {
        return Agent.RoleType.HUNTER;
    }

    @Override
    public int experienceGained() {
        return 10;
    }

    @Override
    public AbstractTask copy() {
        return null;
    }

    @Override
    public void preTask() {

    }

    @Override
    public void postTask() {
        // Killing actually give concurrentModification exception
        //predator.killPredator();
    	/*
    	 * takes 20 points off of the predator's health with each strike
    	 * (This addition was done by Team Pearl) responsible for the
    	 * predators in the game.
    	 */
    	predator.attackPredator();
    }

    @Override
    public void updateTaskLocation() {
        location = predator.getLocation();
    }
}
