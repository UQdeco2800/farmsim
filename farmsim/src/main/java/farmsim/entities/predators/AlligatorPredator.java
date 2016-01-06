package farmsim.entities.predators;

import java.util.List;

import farmsim.Viewport;
import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.util.Point;
import farmsim.world.WorldManager;

/**
 * An alligator predator to attack the peons.
 * It has greater affinity towards water, hence tries to go towards the water
 * bodies present on the farm.
 * 
 * @author TheSpecialist4
 *
 */
public class AlligatorPredator extends Predator {
	
	// agent manager
	private AgentManager agentManager = AgentManager.getInstance();
	// Agent the current target
	private Agent currentTarget;
	
	/**
	 * Creates an Alligator predator
	 * @param x
	 * 			The x location
	 * @param y
	 * 			The y location
	 * @param health
	 * 			The health of the alligator
	 * @param speed
	 * 			The speed of the alligator
	 * @param attackSpeed
	 * 			The alligator's movement speed
	 */
	public AlligatorPredator(double x, double y, int health, double speed) {
		super(x, y, health, speed, 75, "alligator");
		world = WorldManager.getInstance().getWorld();
	}

	/**
	 * Finds the closest water body and makes the alligator go towards it.
	 * Also finds any animal whilst going towards the water body, and makes
	 * the alligator go towards it first. 
	 */
	private void startTask() {
		try {
			getClosestWater();
		}catch (NullPointerException e) {
			destination = new Point(world.getWidth(),world.getHeight());
		}
		if (currentTarget != null) {
			destination = currentTarget.getLocation();
		}
	}
	
	/**
	 * Gets the closest water body from the current location of the alligator.
	 */
	private void getClosestWater() {
		if ((world == null)) {
			//System.err.println("world is null!!"); @Todo -> user logger!!
		}
		else {
			int worldWidth = world.getWidth();
			int worldHeight = world.getHeight();
			for (double i = location.getX(), j = location.getY(); 
					(i < worldWidth && j < worldHeight); i+=6, j+=6) {
				if (world.getTile((int) i, (int) j).getTileEntity().
								getTileType().equals("water")) {
					destination = new Point(i,j);
					break;
				}
				else {
					destination = new Point(i,j);
				}
			}
		}
	}
	
	/**
	 * If there is a peon within 100 points from the alligator,
	 * it goes towards it. Or, goes towards the closest waterbody on the farm.
	 */
	public void tick(Viewport viewport) {
		Agent closest = getClosestAgent();
		if (!canDestroy && closest != null) {

			if (closest != null && getLocation().distance(closest.getLocation()) < 100) {
				currentTarget = closest;
				destination = currentTarget.getLocation();

				if (location.equals(destination) && attackable(destination)) {
					attack();
				} else {
				    moveToward(destination);
				}

			}
			else {
				startTask();
			}
		}
		if (canDestroy) {
			moveOffScreen(viewport);
		}
	}

	/**
	 * Attacks the targeted peon
	 */
	private void attack() {
		if (attackCounter > attackSpeed) {
			if (currentTarget != null && currentTarget.getHappiness() > 0) {
				currentTarget.setHappiness(currentTarget.getHappiness() - 1);
			} else {
				currentTarget = null;
			}
			attackCounter = 0;
		}
	}
	
	/**
	 * Returns the peon closest to the alligator.
	 * @return
	 * 		The agent closest to the alligator.
	 */
	private Agent getClosestAgent() {
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
}
