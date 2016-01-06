package farmsim.entities.tools;

/**
 * Class representing a Hammer. A hammer is a multi-purpose tool for use by a
 * peon.
 * 
 * @author rachelcatchpoole, zenyth
 *
 */
public class Hammer extends Tool {

    // The amount which durability should decrease by after use
    private static final double DURABILITY_LOSS = 0.3;
    
    /**
     * Creates a Hammer.
     * 
     * @param x
     *            The x-position of the hammer
     * @param y
     *            The y-position of the hammer
     */
    public Hammer(double x, double y, double durability) {
        super("hammer", x, y, 1, 10, durability, ToolType.HAMMER);
    }
    
    /**
     * Returns the amount by which durability should be reduced.
     */
    @Override
    public double getDurabilityLoss() {
        return DURABILITY_LOSS;
    }

}
