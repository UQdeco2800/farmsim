package farmsim.entities.machines;

public class Harvester extends Machine {

    public Harvester(double x, double y, double maxDurability, 
            double durability) {
        super("harvester", x, y, 4, maxDurability, durability, 
                MachineType.HARVESTER);
        
    }
    
}
