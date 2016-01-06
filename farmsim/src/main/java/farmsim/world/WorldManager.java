package farmsim.world;

/**
 * WorldManager is responsible for the managing of current World.
 *
 * @author Leggy
 */
public class WorldManager {

    private static final WorldManager INSTANCE = new WorldManager();

    private World world;

    private WorldManager() {} 

    /**
     * Returns the instance of {@link WorldManager}.
     *
     * @return Returns an instance of WorldManager.
     */
    public static WorldManager getInstance() {
        return INSTANCE;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

}
