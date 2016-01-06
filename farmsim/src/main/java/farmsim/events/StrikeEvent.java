package farmsim.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.events.statuses.StatusName;
import farmsim.tasks.StrikeTask;
import farmsim.world.World;
import farmsim.world.WorldManager;

/**
 * Contains the methods for the strike event. A strike can be initiated by
 * calling beginStrike(x,y,p,d);
 * 
 * @author bobri
 */
public class StrikeEvent extends AbstractEvent {
	private AgentManager agentManager = AgentManager.getInstance();
	private World world = WorldManager.getInstance().getWorld();
	private Random r = new Random();
	private Iterator<Agent> agentIter;
	private ArrayList<Agent> agents = new ArrayList<Agent>();

	private int x, y, p, moveFactor;

	// The number of ticks between each movement of the agent
	private static final int MOVEFACTOR = 80;

	/**
	 * Creates a strike event
	 * 
	 * @param xPos
	 *            The X location of the strike center
	 * @param yPos
	 *            The Y location of the strike center
	 * @param proportion
	 *            The proportion of peons to go on strike
	 * @param duration
	 *            The duration of the strike (in milliseconds)
	 */
	public StrikeEvent(int xPos, int yPos, int proportion, long duration) {
		if (!checkStartCondition(xPos, yPos, proportion, duration)) {
			return;
		}

		x = xPos;
		y = yPos;
		p = proportion;
		moveFactor = MOVEFACTOR;
		setDuration(duration / 50);
		setAgentsList();
		LOGGER.info("Created strike event for " + duration + " milliseconds");
		LOGGER.info(String.format("%d peons will go on strike", agents.size()));
	}

	/**
	 * Begins the strike event
	 */
	@Override
	public void begin() {
		LOGGER.info("Strike event has begun");
		moveAgents();
		return;
	}

	private void moveAgents() {
		int xPos, yPos;

		agentIter = agents.iterator();

		for (int i = 0; i < agents.size() / p; i++) {

			xPos = r.nextInt(5) - 1;
			yPos = r.nextInt(5) - 1;

			xPos = (x + xPos < 0) ? 0 : xPos;
			yPos = (y + yPos < 0) ? 0 : yPos;
			xPos = (x + xPos > world.getWidth()) ? world.getWidth() : xPos;
			yPos = (y + yPos > world.getHeight()) ? world.getHeight() : yPos;

			agentIter.next().setTask(new StrikeTask(x + xPos, y + yPos, world, getDuration() * 50));
		}
		return;
	}

	/**
	 * Gets all agents with a happiness less than 5.
	 */
	private void setAgentsList() {
		for (int i = 0; i < agentManager.getAgents().size() / p; i++) {
			if (agentManager.getAgents().get(i).getHappiness() < 5) {
				agents.add(agentManager.getAgents().get(i));
			}
		}
	}

	/**
	 * Updates the time remaining on this event
	 * 
	 * @return False when the event has completed, otherwise true.
	 */
	@Override
	public boolean update() {
		tickDuration();
		if (getDuration() <= 1) {
			return false;
		}
		if (moveFactor <= 0) {
			moveAgents();
			moveFactor = MOVEFACTOR;
		}
		moveFactor--;
		return true;
	}

	@Override
	public boolean needsTick() {
		return true;
	}

	@Override
	public StatusName getName() {
		return StatusName.STRIKE;
	}

	/**
	 * Determines if a valid strike is allowed to begin
	 * 
	 * @param x
	 *            The X location of the strike center
	 * @param y
	 *            The y location of the strike center
	 * @param proportion
	 *            The proportion of agents to go on strike
	 * @param duration
	 *            The duration of the strike
	 * @return True if this is a valid strike, otherwise false;.
	 */
	private Boolean checkStartCondition(int x, int y, int proportion, long duration) {
		if ((x < 0) || (y < 0) || (proportion <= 0) || (duration <= 0)) {
			return false;
		}
		return true;
	}
}
