package farmsim.entities.machines;
/**
 * Defines the type of machines that peons can use in-game. 
 * 
 * @author zenyth
 * 
 */
public enum MachineType {
    CHAINSAW("Chainsaw"), 
    TRACTOR("Tractor"), 
    HARVESTER("Harvestor"),
    IRRIGATOR("Irrigator");
    
    private String displayName;
    
    MachineType(String displayName) {
        this.displayName = displayName;
    }
    
    public String displayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
