package farmsim.events;

import java.util.Random;

import farmsim.entities.fire.FireManager;
import farmsim.entities.tileentities.objects.Tree;
import farmsim.events.statuses.StatusName;
import farmsim.tiles.Tile;
import farmsim.world.World;
import farmsim.world.WorldManager;

/**
 * 
 * @author bobri
 */
public class FireEvent extends AbstractEvent{
	
	private int severity = 1;
	private World world = WorldManager.getInstance().getWorld();
	FireManager fireManager = new FireManager(world);
	private static int NUM_FIRES = 200;
	/**
	 * Initializes the event
	 */
	public FireEvent(int severity){
		this.severity = severity;
	}

	/**
	 * Entry point for the Fire Event
	 */
	@Override
	public void begin() {
		createFires();
	}
	
	private void createFires() {
		Random rand = new Random();
		for (int i = 0; i < NUM_FIRES; i++) {
			fireManager.createFire(Math.abs(rand.nextInt(world.getWidth())), 
					Math.abs(rand.nextInt(world.getHeight())), severity);
			fireManager.createFire(Math.abs(rand.nextInt(world.getWidth())) + 2, 
					Math.abs(rand.nextInt(world.getHeight())) + 2, severity);
			fireManager.createFire(Math.abs(rand.nextInt(world.getWidth())) + 1, 
					Math.abs(rand.nextInt(world.getHeight())) + 2, severity);
			fireManager.createFire(Math.abs(rand.nextInt(world.getWidth())) + 2, 
					Math.abs(rand.nextInt(world.getHeight())) + 3, severity);
			fireManager.createFire(Math.abs(rand.nextInt(world.getWidth())) - 1, 
					Math.abs(rand.nextInt(world.getHeight())) - 2, severity);
			fireManager.createFire(Math.abs(rand.nextInt(world.getWidth())) - 2, 
					Math.abs(rand.nextInt(world.getHeight())) - 2, severity);
			fireManager.createFire(Math.abs(rand.nextInt(world.getWidth())) - 3, 
					Math.abs(rand.nextInt(world.getHeight())) - 1, severity);
		}		
	}

	/**
	 * Updates the Fire Event
	 */
	@Override
	public boolean update() {
		return false;
	}

	@Override
	public boolean needsTick() {
		return false;
	}

	/**
	 * Returns the StatusName of this event.
	 */
	@Override
	public StatusName getName() {
		return StatusName.FORESTFIRE;
	}

}
