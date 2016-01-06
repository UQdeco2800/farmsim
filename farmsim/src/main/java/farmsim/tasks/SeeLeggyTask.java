package farmsim.tasks;

import farmsim.entities.agents.Agent;
import farmsim.buildings.LeggyShrine;

/**
 * <p>
 * Task to tell any worker to go see the almighty leggy shrine may they bask in
 * its yellow glory and be infuriated when people don't stop squashing it.
 * </p>
 *
 * @author Blake Lockett on 21/10/2015.
 */
public class SeeLeggyTask extends AbstractTask {
    private static final int DURATION = 8000;
    private static final String NAME = "See Leggy Shrine";
    private static final String ID = "leggy";
    private LeggyShrine leggyShrine;

    /**
     * basic SeeLeggyTask constructor.
     * 
     * @param leggyShrine
     *            the shrine the worker should go to.
     */
    public SeeLeggyTask(LeggyShrine leggyShrine) {
        super(leggyShrine.getLocation(), DURATION, leggyShrine.getWorld(),
                NAME, ID);
        this.leggyShrine = leggyShrine;
        setNoConflicts();
    }

    /**
     * update the given workers day they say the leggy statue.
     *
     * @param agent
     */
    private void updateHappiness(Agent agent) {
        agent.IHaveSeenLeggy();
    }

    @Override
    public AbstractTask copy() {
        return new SeeLeggyTask(leggyShrine);
    }

    @Override
    public void preTask() {
        // do nothing
    }

    /**
     * after task is completed update workers happiness.
     */
    @Override
    public void postTask() {
        if (getWorker() != null) {
            updateHappiness(getWorker());
        }
    }
}
