package farmsim.entities.predators;

import farmsim.Viewport;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;

/**
 * A mole animal to dig up any ploughed dirt and change it to dirt
 * NOTE:
 * Mole and rabbit roles have been swapped due to popular demand
 * We decided on just swapping the sprites for them rather than refactoring the code
 * @author modmod96
 */
public class MolePredator extends Predator {

    private World world;
    private int replacementTileType;
    private int targetTileType;
    //max number of times it can damage crop
    private int maxEat;
    //time for waiting between crop eeating
    private int maxWait;
    //tick counter
    private int tickCounter;

    /**
     * Creates a mole predator
     * @param x
     *             The x location
     * @param y
     *             The y location
     * @param health
     *             The health of the predator
     * @param speed
     *             The speed of the mole
     */
    public MolePredator(double x, double y, int health, double speed) {
        super(x, y, health, speed, 100, "rabbit");
        world = WorldManager.getInstance().getWorld();
        
        maxEat = 5;
        maxWait = 50;

        try {
        targetTileType = TileRegister.getInstance().getTileType("Apple1");
        replacementTileType = TileRegister.getInstance().getTileType("Apple1dead");
        } catch (NullPointerException e) {
        	// Only caused when unit testing
        }
        
    }



    /**
     * Finds the closest Ploughed dirt
     * @return
     */
    private Point closestPloughedDirt(Viewport viewport) {
        //Get bounds
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
        Point crop = closestPloughedDirt(viewport);
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
            destination = closestPloughedDirt(viewport);
        }
    }
}
