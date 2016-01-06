package farmsim.entities.tools;

/**
 * Class representing a Pickaxe. A pickaxe is necessary for a peon to clear
 * rocks.
 * 
 * @author rachelcatchpoole, zenyth
 *
 */
public class Pickaxe extends Tool {

    // The amount which durability should decrease by after use
    private static final double DURABILITY_LOSS = 0.5;
    
    /**
     * Creates a Pickaxe.
     * 
     * @param x
     *            The x-position of the pickaxe
     * @param y
     *            The y-position of the pickaxe
     */
    public Pickaxe(double x, double y, double durability) {
        super("pickaxe", x, y, 1, 10, durability, ToolType.PICKAXE);
    }

    /**
     * Returns the amount by which durability should be reduced.
     */
    @Override
    public double getDurabilityLoss() {
        return DURABILITY_LOSS;
    }
    
}
