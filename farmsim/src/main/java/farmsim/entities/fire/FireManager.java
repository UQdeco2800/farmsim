package farmsim.entities.fire;

import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.crops.Crop;
import farmsim.tiles.Tile;
import farmsim.util.Point;
import farmsim.world.World;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * FireManager class. Handles the creation, updating, spreading and removal of
 * individual fires.
 * 
 * @author 
 *      yojimmbo
 *
 */
public class FireManager {
    /**The game world*/
    private World world;
    /**List of fires in the world*/
    private List<Fire> fires;
    /**Timer*/
    private int count = 0;
    /**Random number generator*/
    private Random randomNumber;
    /**Logger to log fire creation**/
    private static final Logger LOGGER =
            LoggerFactory.getLogger(FireManager.class);

    /**
     * Initialise FireManager.
     * 
     */
    public FireManager(World world) {
        fires = new ArrayList<>();
        randomNumber = new Random();
        randomNumber.setSeed(2800);
        this.world = world;
    }

    /**
     * Spawns, updates and removes fires.
     * 
     */
    public void tick() {
        spawnFire();
        updateFires();
        removeFire();
        count++;
    }

    /**
     * Returns an unmodifiable list of fires.
     * 
     * @return a list of fires.
     */
    public List<Fire> getFires() {
        return Collections.unmodifiableList(fires);
    }

    /**
     * Spawns a fire at a tile.
     * 
     */
    public void spawnFire() {
        int height = world.getHeight();
        int width = world.getWidth();
        
        if (count % 60 == 0) {
            createFire(randomNumber.nextInt(width),
                    randomNumber.nextInt(height));
        }
    }

    /**
     * Helper method to update all fires.
     * 
     */
    private void updateFires() {
        List<Fire> spreadFires = new ArrayList<Fire>();
        for (Fire fire : fires) {
            fire.tick();
            if (fire.canSpread()) {
                spreadFires.add(fire);
            }
        }
        spreadFires(spreadFires);
    }

    /**
     * Spreads fires to adjacent tiles.
     * 
     * @param spreadFires
     *      List of fires that should be spread.
     */
    private void spreadFires(List<Fire> spreadFires) {
        if (count % 40 == 0) {
            for (Fire fire : spreadFires) {
                int x = (int) fire.getWorldX();
                int y = (int) fire.getWorldY();
                Point location = getTarget(x, y);
                if (location != null) {
                    createFire((int) location.getX(), (int) location.getY(),
                            fire.getBurnTime() - 40);
                    fire.stopSpreading();
                }
            }
        }
    }

    /**
     * Helper method to get a random adjacent tile that can be set on fire.
     * 
     * @param x
     *      Tile location x.
     * @param y 
     *      Tile location y.
     * @return 
     *      A point containing an adjacent tile if one exists. Null otherwise.
     */
    private Point getTarget(int x, int y) {
        for (int i = 0; i < 4; i++) {
            int randomX = randomNumber.nextInt(3) - 1;
            int randomY = randomNumber.nextInt(3) - 1;
            if (isValidLocation(x + randomX, y + randomY)
                    && canSetOnFire(x + randomX, y + randomY)) {
                return new Point(x + randomX, y + randomY);
            }
        }
        return null;
    }

    /**
     * Helper method to remove a fire when it is burnt out.
     * 
     */
    private void removeFire() {
        Iterator<Fire> it = fires.iterator();
        while (it.hasNext()) {
            Fire fire = it.next();
            if (fire.out()) {
                LOGGER.info("Removing fire at tile X: " + fire.getWorldX() + 
                        " Y: " + fire.getWorldY());
                it.remove();
            }
        }
    }

    /**
     * Creates a fire at a tile
     * 
     * @param x
     *      Tile location x.
     * @param y
     *      Tile location y.
     * @return 
     *      True if created, false otherwise.
     */
    public boolean createFire(int x, int y) {
        if (isValidLocation(x, y) && canSetOnFire(x, y)) {
            Tile target = world.getTile(x, y);
            Fire fire = new Fire(target, 200);
            LOGGER.info("Created fire at tile X: " + x + " Y: " + y);
            target.setTileEntity(fire);
            fires.add(fire);
            return true;
        }
        return false;
    }

    /**
     * Creates a fire at a tile
     * 
     * @param x
     *      Tile location x.
     * @param y
     *      Tile location y.
     * @param timeToBurn 
     *      ticks the fire should last for
     * @return 
     *      True if created, false otherwise.
     */
    public boolean createFire(int x, int y, int timeToBurn) {
        if (isValidLocation(x, y) && canSetOnFire(x, y)) {
            Tile target = world.getTile(x, y);
            Fire fire = new Fire(target, timeToBurn);
            LOGGER.info("Created fire at tile X: " + x + " Y: " + y);
            target.setTileEntity(fire);
            fires.add(fire);
            return true;
        }
        return false;
    }

    /**
     * Helper method to determine if the tile can be set on fire.
     * @return 
     *      True if tile can be set on fire, false otherwise.
     */
    private boolean canSetOnFire(int x, int y) {
        Map<String, Integer> weatherComponents = world.getWeatherComponents();
        //If its cold return false.
        if (weatherComponents.containsKey("Temperature")
                && weatherComponents.get("Temperature") < 3) {
            return false;
        }
        //If its raining, snowing, hailing
        if (weatherComponents.containsKey("Rain")
                || weatherComponents.containsKey("Snow")
                || weatherComponents.containsKey("Hail")) {
            return false;
        }
        //If the blocks hydration is < 50
        Tile tile = world.getTile(x, y);
        if(tile.getWaterLevel() > 0.5) {
            return false;
        }
        TileEntity entity = tile.getTileEntity();
        return entity instanceof Crop;
    }

    /**
     * Helper method to determine if a point location is valid on the map.
     * @return 
     *      True if the location is valid, false otherwise.
     */
    private boolean isValidLocation(int x, int y) {
        if (y < world.getHeight() && y >= 0 && x >= 0 && 
                x < world.getWidth()) {
            return true;
        }
        return false;
    }
}
