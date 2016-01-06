package farmsim.entities.tools;

/**
 * Class representing a Fishing rod. A fishing rod is necessary for peons to
 * fish.
 * 
 * @author rachelcatchpoole
 *
 */
public class FishingRod extends Tool {

    // The amount which durability should decrease by after use
    private static final double DURABILITY_LOSS = 0.2;
    
    /**
     * Creates a Fishing Rod.
     * 
     * @param x
     *            The x-position of the rod
     * @param y
     *            The y-position of the rod
     */
    public FishingRod(double x, double y, double durability) {
        super("fishingrod", x, y, 1, 10, durability, ToolType.FISHING_ROD);
    }
    
    /**
     * Returns the amount by which durability should be reduced.
     */
    @Override
    public double getDurabilityLoss() {
        return DURABILITY_LOSS;
    }

}
