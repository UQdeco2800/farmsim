package farmsim.world.weather;

import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;
import farmsim.world.modifiers.ModifierManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Base class for a generic season. 
 */
public class Season {

    private SeasonName name; // enum - see SeasonName.java
    private Map<WeatherType, Double> weightScalars;   // Scalars for weathers
    private String iconPath;

    /**
     * Creates a season.
     * @param name the type of season to create.
     */
    public Season(SeasonName name) {
        setName(name);
        setModifier();
    }

    /**
     * Applies a season modifier to the world.
     */
    protected void setModifier() {
        if (WorldManager.getInstance() == null
                || WorldManager.getInstance().getWorld() == null
                || WorldManager.getInstance().getWorld()
                .getModifierManager() == null) {
            return;
        }
        World world = WorldManager.getInstance().getWorld();
        ModifierManager modifierManager = world.getModifierManager();
        modifierManager.purge("Seasonal");

        HashMap<String, String> modifierSettings = new HashMap<>();
        modifierSettings.put("target", "all");
        modifierSettings.put("tag", "Seasonal");
        modifierSettings.put("position", (new Point(0, 0)).toString());
        modifierSettings.put("end",
                (new Point(world.getWidth(), world.getHeight())).toString());
        modifierSettings.put("shape", "square");
        modifierSettings.put("attribute", "season");

        modifierManager.addNewAttributeModifier(modifierSettings);
    }

    /** 
     * Sets the name field.
     * @param name name of season per enum type
     */
    protected void setName(SeasonName name) {
        this.name = name;
    }

    /**
     * Gets the name field.
     * @return The season name as an enum member
     */
    public SeasonName getName() {
        return name;
    }

    /**
     * Gets the name field as a string.
     * @return the season name string.
     */
    public String getNameString() {
        return name.toString();
    }
    
    /**
     * Sets the scalar map to passed param map.
     * @param map Map to set
     */
    public void setScalars(Map<WeatherType, Double> map) {
        weightScalars = map;
    }
    
    /**
     * Perform a lookup on a weathertype in the scalars map.
     * @param query the weathertype to look up
     * @return scalar value
     */
    public Double getScalar(WeatherType query) {
        return weightScalars.get(query);
    }
    
    /**
     * Set the filepath to the season icon.
     */
    protected void setPath(String path) {
        iconPath = path;
    }
    
    /**
     * Get the filepath to the season icon.
     * @return the filepath
     */
    public String getPath() {
        return iconPath;
    }
}
