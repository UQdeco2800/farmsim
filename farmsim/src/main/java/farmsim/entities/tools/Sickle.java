package farmsim.entities.tools;

/**
 * Class representing a Sickle. A sickle can be used by a peon to cut crops.
 * 
 * @author rachelcatchpoole, zenyth
 *
 */
public class Sickle extends Tool {
    
    // The amount which durability should decrease by after use
    private static final double DURABILITY_LOSS = 0.3;

    /**
     * Creates a Sickle.
     * 
     * @param x
     *            The x-position of the sickle
     * @param y
     *            The y-position of the sickle
     */
    public Sickle(double x, double y, double durability) {
        super("sickle", x, y, 1, 10, durability, ToolType.SICKLE);
    }

    /**
     * Returns the amount by which durability should be reduced.
     */
    @Override
    public double getDurabilityLoss() {
        return DURABILITY_LOSS;
    }

}
