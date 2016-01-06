package farmsim.entities.tools;
/**
 * Defines the type of tools that peons can use in-game. 
 * 
 * @author zenyth
 * 
 */
public enum ToolType {
    AXE("Axe"),
    FISHING_ROD("Fishing Rod"),
    HAMMER("Hammer"),
    HOE("Hoe"),
    PICKAXE("Pickaxe"),
    SHOVEL("Shovel"),
    SICKLE("Sickle"),
    WATERING_CAN("Watering Can");
    
    private String displayName;
    
    ToolType(String displayName) {
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