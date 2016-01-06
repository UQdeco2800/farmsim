package farmsim.entities.tools;

/**
 * Class representing an Axe. An axe is necessary for a peon to clear a tree.
 * 
 * @author rachelcatchpoole, zenyth
 *
 */
public class Axe extends Tool {

    // The amount which durability should decrease by after use
    private static final double DURABILITY_LOSS = 0.5;

    /**
     * Creates an Axe.
     * 
     * @param x
     *            The x-position of the axe
     * @param y
     *            The y-position of the axe
     * @param durability
     * 			  The durability of the axe
     */
    public Axe(double x, double y, double durability) {
        super("axe", x, y, 1, 10, durability, ToolType.AXE);
    }

    /**
     * Returns the amount by which durability should be reduced for an axe.
     */
    @Override
    public double getDurabilityLoss() {
        return DURABILITY_LOSS;
    }

    /**
     * Increases the durability of the axe using Tool method.
     */
    public void increaseDurability() {
        increaseDurability(1);
    }

}
