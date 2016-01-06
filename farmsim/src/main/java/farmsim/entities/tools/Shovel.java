package farmsim.entities.tools;

/**
 * Class representing a Shovel. A shovel is a gardening tool for use by a peon.
 * 
 * @author rachelcatchpoole, zenyth
 *
 */
public class Shovel extends Tool {

    // The amount which durability should decrease by after use
    private static final double DURABILITY_LOSS = 0.3;
    
    /**
     * Creates a Shovel.
     * 
     * @param x
     *            The x-position of the shovel
     * @param y
     *            The y-position of the shovel
     */
    public Shovel(double x, double y, double durability) {
        super("shovel", x, y, 1, 10, durability, ToolType.SHOVEL);
    }

    /**
     * Returns the amount by which durability should be reduced.
     */
    @Override
    public double getDurabilityLoss() {
        return DURABILITY_LOSS;
    }

}
