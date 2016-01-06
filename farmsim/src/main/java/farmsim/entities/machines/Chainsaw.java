package farmsim.entities.machines;

public class Chainsaw extends Machine {

    public Chainsaw(double x, double y, double maxDurability, 
            double durability) {
        super("chainsaw", x, y, 1.5, maxDurability, durability, 
                MachineType.CHAINSAW);
    }

}
