package farmsim.entities.machines;
/**
 * Class representing a tractor. Tractor make peons faster!
 * 
 * @author zenyth
 *
 */
public class Tractor extends Machine {

	 /**
     * Creates a tractor!
     * 
     * @param x
     *            The x-position of the tractor.
     * @param y
     *            The y-position of the tractor.
     * @param durability
     * 			  The durability of the tractor.
     */
    public Tractor(double x, double y, double durability) {
        super("tractor", x, y, 4, 50, durability, MachineType.TRACTOR);
    }
}
