package farmsim.entities.machines;

public class Irrigator extends Machine {

    public Irrigator(double x, double y, double maxDurability, 
            double durability) {
        super("irrigator", x, y, 3, maxDurability, durability, 
                MachineType.IRRIGATOR);
    }

}
