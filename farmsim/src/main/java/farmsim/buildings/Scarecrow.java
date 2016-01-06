package farmsim.buildings;

import farmsim.world.World;
import farmsim.world.WorldManager;

/**
 * A scarecrow which provides a safe-zone from all predators.
 * 
 * @author rachelcatchpoole, zenyth for Team Adleman
 */
public class Scarecrow extends AbstractBuilding {
    public static final int WIDTH = 1;
    public static final int HEIGHT = 1;
    public static final String SPRITE_LOCATION = "/buildings/scarecrow.png";
    // the tile-radius of the safe zone around the scarecrow
    private int radius = 5;
    private int daysLeft = 7;

    /**
     * Creates a scarecrow in the world
     * 
     * @param world
     *            The world in which the scarecrow resides
     */
    public Scarecrow(World world) {
        super(world, WIDTH, HEIGHT, SPRITE_LOCATION);
        if (world.getTimeManager() != null) {
            world.getTimeManager().registerDailyTask(() -> reduceDaysLeft());
        }
    }

    /**
     * Returns the sacrecrow's radius
     * @return the radius of the safe zone
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Sets the safeZone value of the tiles around the scarecrow
     * 
     * @param safe
     *            true if the scarecrow's radius should be safe, false otherwise
     */
    private void makeSafeZone(boolean safe) {
        World currentWorld = WorldManager.getInstance().getWorld();

        int leftX = (int) (Math.max(this.getWorldX() - radius, 0));
        int rightX = (int) (Math.min(this.getWorldX() + radius,
                currentWorld.getWidth()));
        int topY = (int) (Math.max(this.getWorldY() - radius, 0));
        int bottomY = (int) (Math.min(this.getWorldY() + radius,
                currentWorld.getHeight()));

        // set all tiles around scarecrow to be safe
        for (int x = leftX; x <= rightX; x++) {
            for (int y = topY; y <= bottomY; y++) {
                currentWorld.getTile(x, y).setSafeZone(safe);
            }
        }

    }

    /**
     * Adds a safe zone around the scarecrow when it is built
     */
    @Override
    public void onBuild() {
        makeSafeZone(true);
    }

    /**
     * Removes the safeZone from around the scarecrow when it is removed
     */
    @Override
    public void onRemove() {
        makeSafeZone(false);
    }

    /**
     * Returns the daysLeft before scarecrow dies
     */
    public int getDaysLeft() {
        return daysLeft;
    }

    /**
     * Called at the end of each day, decrements days left of scarecrow's life
     * and then checks if it's dead
     */
    public void reduceDaysLeft() {
        daysLeft -= 1;
        this.isScarecrowDead();
    }

    /**
     * Checks if scarecrow has any days left in it, if 0, removes the scarecrow
     * Also called at the end of each day.
     */
    public boolean isScarecrowDead() {
        if (daysLeft == 0) {
            this.removeFromWorld();
            return true;
        } else
            return false;
    }
}
