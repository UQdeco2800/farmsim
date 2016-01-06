package farmsim.entities.tools;

/**
 * Class representing a Watering Can. A watering can is necessary for a peon to
 * water a square of land.
 * 
 * @author rachelcatchpoole, zenyth
 *
 */
public class WateringCan extends Tool {

    // The amount which durability should decrease by after use
    private static final double DURABILITY_LOSS = 0.1;
    
    /**
     * Creates a WateringCan.
     * 
     * @param x
     *            The x-position of the watering can
     * @param y
     *            The y-position of the watering can
     */
    public WateringCan(double x, double y, double durability) {
        super("watering_can", x, y, 1, 10, durability, ToolType.WATERING_CAN);
    }

    /**
     * Returns the amount by which durability should be reduced.
     */
    @Override
    public double getDurabilityLoss() {
        return DURABILITY_LOSS;
    }

}
