package farmsim.entities.predators;

import farmsim.Viewport;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;

/**
 * A Rabbit animal to attack crops 
 *  NOTE:
 *  Mole and rabbit roles have been swapped due to popular demand
 * We decided on just swapping the sprites for them rather than refactoring the code
 * @author r-portas
 */
public class RabbitPredator extends Predator {
	
	private World world;

	private int replacementTileType;
	private int targetTileType;
	// The maximum number of times the predators can dig
	private int maxDigs;
	// The time to wait at a position before digging it up
	private int maxWait;
	// The tick counter
	private int tickCounter;
	
    public RabbitPredator(double x, double y, int health, double speed) {
        super(x, y, health, speed, 75, "mole");
        world = WorldManager.getInstance().getWorld();
        
        maxDigs = 5;
        maxWait = 100;
        
        targetTileType = TileRegister.getInstance().getTileType("ploughed_dirt");
        replacementTileType = TileRegister.getInstance().getTileType("dirt");
    }

    private Point findClosestCrop(Viewport viewport){
    	// Get the bounds of the viewport
    	int topX = viewport.getX();
    	int topY = viewport.getY();
    	int bottomX = viewport.getWidthTiles() + topX;
    	int bottomY = viewport.getHeightTiles() + topY;
    	int curTileType;
    	for (int x = topX; x < bottomX; x++){
    		for (int y = topY; y < bottomY; y++){
    			curTileType = world.getTile(x, y).getTileType();
    			if (curTileType == targetTileType && attackable(new Point(x, 
    			        y))) {
    				return new Point(x, y);
    			}
    		}
    	}
    	return null;
    }

    private void startTask(Viewport viewport) {
        Point crop = findClosestCrop(viewport);
        // Move to the crop location
        destination = crop;
    }

    public void tick(Viewport viewport) {
        if (location != null && destination != null) {
            if (location.equals(destination)) {
            	if (tickCounter > maxWait) {
            		tickCounter = 0;
            		if (attackable(destination)) {
            		    world.setTile(destination, replacementTileType);
            		}
            		destination = null;
            	}
                // Check if the crop is dead then move to the next one
            	startTask(viewport);
            	tickCounter++;
            } else {
                moveToward(destination);
            }
        } else {
        	destination = findClosestCrop(viewport);
        }
    }
}
