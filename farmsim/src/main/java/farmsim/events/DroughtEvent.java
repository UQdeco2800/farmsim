package farmsim.events;

import java.util.Random;

import farmsim.events.statuses.StatusName;
import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;
import farmsim.world.World;
import farmsim.world.WorldManager;

/**
 * An event that destroys planted crops.
 * 
 * @author bobri
 */
public class DroughtEvent extends AbstractEvent {
    private World world = WorldManager.getInstance().getWorld();
    private Random r = new Random();

    private int xSize, ySize;
    private Tile t;

    /**
     * Creates a drought event
     * 
     * @param severity The severity of the drought (0-100)
     */
    public DroughtEvent(int severity) {
        checkManagers();

        xSize = world.getWidth();
        ySize = world.getHeight();
        setLevel(severity);
    }

    /**
     * Initializes this drought event
     */
    @Override
    public void begin() {
        for (int x = 0; x < xSize; x++) {
            for (int y = 0; y < ySize; y++) {
                t = world.getTile(x, y);
                if (t.getTileType() == TileRegister.getInstance()
                        .getTileType("ploughed_dirt")) {
                    // Do stuff to plowed dirt
                }
                t.decreaseWaterLevel();
            }
        }
    }

    @Override
    public boolean update() {
        return false;
    }


    @Override
    public boolean needsTick() {
        return false;
    }

    @Override
    public StatusName getName() {
        return StatusName.DROUGHT;
    }

    @Override
    public boolean displayable() {
        return true;
    }

    /**
     * Checks that all managers exist
     */
    private void checkManagers() {
        if (world == null) {
            setWorld();
        }
    }

    /**
     * Gets and instance of the world
     */
    private void setWorld() {
        world = WorldManager.getInstance().getWorld();
    }
}
