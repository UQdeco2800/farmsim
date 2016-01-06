package farmsim.entities.predators;

import java.util.List;

import farmsim.Viewport;
import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;

/**
 * A Bear animal to attack peons
 * 
 * @author r-portas, rachelcatchpoole
 */
public class BearPredator extends Predator {
	Agent currentTarget;
	AgentManager agentManager;

	/**
	 * Creates a bear predator
	 * 
	 * @param x
	 *            The x location
	 * @param y
	 *            The y location
	 * @param health
	 *            The health of the predator
	 * @param speed
	 *            The predator's movement speed
	 */
	public BearPredator(double x, double y, int health, double speed) {
		super(x, y, health, speed, 100, "bear");
		agentManager = AgentManager.getInstance();
	}

	/**
	 * Overrides the agent manager This is only used for unit testing and
	 * mocking
	 * 
	 * @param am
	 *            The agent manager to override with
	 */
	public void overrideAgentManager(AgentManager am) {
		agentManager = am;
	}

	/**
	 * Tells the predator to attack
	 */
	void attack() {
		if (attackCounter > attackSpeed) {
			if (currentTarget != null && currentTarget.getHappiness() > 0) {
				currentTarget.setHappiness(currentTarget.getHappiness() - 1);
				// System.out.println("Setting " + closestAgentRadio.getName() +
				// " happiness to " + closestAgentRadio.getHappiness());
			} else {
				currentTarget = null;
			}

			attackCounter = 0;
		}
	}

	/**
	 * Finds the closest agent Only bears attack predators, so this should be in
	 * here instead of the base class
	 * 
	 * @param predator
	 * @return
	 */
	private Agent closestAgent() {
		List<Agent> agents = agentManager.getAgents();
		
		Agent closest = null;
		for (Agent agent : agents) {
		    
			if ((closest != null && attackable(agent.getLocation()) && 
			        agent.getLocation().distance(getLocation()) < 
			        closest.getLocation().distance(getLocation()))
					|| closest == null) {
				closest = agent;
			}
		}

		return closest;
	}

	public void tick(Viewport viewport) {
		Agent closest = closestAgent();
		if (!canDestroy && closest != null) {

			if (closest != null && getLocation().distance(closest.getLocation()) < 200) {
				currentTarget = closest;
				destination = currentTarget.getLocation();

				if (location.equals(destination) && attackable(destination)) {
					attack();
				} else {
				    moveToward(destination);
				}

			} else {
				moveOffScreen(viewport);
			}
		}
	}
}