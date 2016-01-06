package farmsim.entities.tools;

/**
 * Class representing a Hoe. A hoe is necessary for a peon to plough land.
 * 
 * @author rachelcatchpoole, zenyth
 *
 */
public class Hoe extends Tool {
    
    // The amount which durability should decrease by after use
    private static final double DURABILITY_LOSS = 0.4;

    /**
     * Creates a Hoe.
     * 
     * @param x
     *            The x-position of the hoe
     * @param y
     *            The y-position of the hoe
     */
    public Hoe(double x, double y, double durability) {
        super("hoe", x, y, 1, 10, durability, ToolType.HOE);
    }

    /**
     * Returns the amount by which durability should be reduced.
     */
    @Override
    public double getDurabilityLoss() {
        return DURABILITY_LOSS;
    }

}
