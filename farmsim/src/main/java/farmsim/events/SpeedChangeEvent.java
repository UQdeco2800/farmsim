package farmsim.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.events.statuses.StatusName;

/**
 * Changes the speed of all peons by a specified amount.
 * 
 * @author bobri
 */
public class SpeedChangeEvent extends AbstractEvent {
    private AgentManager agentManager = AgentManager.getInstance();

    private HashMap<Agent, Double> originalSpeed = new HashMap<Agent, Double>();

    /**
     * Creates a speedchange event that changes the speed of peons temporarily.
     * 
     * @param increaseFactor The factor b which the speed should increase.
     * @param duration The duration of the sped increase (in milliseconds).
     */
    public SpeedChangeEvent(double increaseFactor, long duration) {
        if (duration < 1) {
            return;
        }

        if (increaseFactor <= 0) {
            setLevel(1);
        } else {
            setLevel(increaseFactor);
        }

        setDuration(duration / 50);
        
        LOGGER.info("Created speed change event for " + duration + " milliseconds");
    }

    /**
     * Resets the speed of all agents to what it was before the event started
     */
    private void resetSpeed() {
        for (Map.Entry<Agent, Double> entry : originalSpeed.entrySet()) {
            // Try-Finally block handles any peons that have disappeared from
            // the world.
            try {
                entry.getKey().setSpeed(entry.getValue());
            } finally {
                originalSpeed.remove(entry);
            }

        }
    }

    /**
     * Begins the SpeedChange event
     */
    @Override
    public void begin() {
    	LOGGER.info("Speed change event ahs begun");
        List<Agent> agentList = agentManager.getAgents();
        for (int i = 0; i < agentList.size(); i++) {
            originalSpeed.put(agentList.get(i), agentList.get(i).getSpeed());
            agentList.get(i)
                    .setSpeed((agentList.get(i).getSpeed() * getLevel()));
        }
        return;
    }

    /**
     * Updates the time remaining on this event. Resets the agent speed when the
     * event is complete
     */
    @Override
    public boolean update() {
        tickDuration();
        if (getDuration() < 1) {
            resetSpeed();
            return false;
        }
        return true;
    }

    @Override
    public boolean needsTick() {
        return true;
    }

    @Override
    public StatusName getName() {
        return StatusName.SPEEDCHANGE;
    }

}
