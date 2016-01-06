package farmsim.world.weather.components;

import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;

import java.util.HashMap;

/**
 * WeatherComponent - Sun.
 * <p>
 *     Conditional Component, applies modifier to slowly increase water
 *     evaporation.
 * </p>
 */
public class Sun extends WeatherComponent {

    /**
     * Creates a component with a set level.
     * @param level to set the component.
     */
    public Sun(int level) {
        super("Sun", level);
        addModifier();
    }

    /**
     * Creates a component with a level between the bounds inclusively.
     * @param lowerLevel lowest level.
     * @param higherLevel highest level.
     */
    public Sun(int lowerLevel, int higherLevel) {
        super("Sun", lowerLevel, higherLevel);
        addModifier();
    }

    private void addModifier() {
        World world = WorldManager.getInstance().getWorld();
        //creates a modifier
        HashMap<String, String> modifierSettings = new HashMap<>();
        modifierSettings.put("target", "all");
        modifierSettings.put("tag", "Weather");
        modifierSettings.put("position", (new Point(0, 0)).toString());
        modifierSettings.put("end", (
                new Point(world.getWidth(),world.getHeight())).toString());
        modifierSettings.put("shape", "square");
        modifierSettings.put("attribute", "moisture");
        modifierSettings.put("effect", Double.toString(-0.00005 * getLevel()));

        //make the thing
        WorldManager.getInstance().getWorld().getModifierManager()
                .addNewAttributeModifier(modifierSettings);
    }
}
