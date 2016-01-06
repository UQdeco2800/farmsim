package farmsim.world.weather.components;

import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;

import java.util.HashMap;

/**
 * WeatherComponent - Temperature.
 * <p>
 *     A conditional for elements in the game which want different reactions to
 *     different temperatures.
 * </p>
 */
public class Temperature extends WeatherComponent {

    /**
     * Creates a component with a set level.
     * @param level to set the component.
     */
    public Temperature(int level) {
        super("Temperature", level);
        setModifier();
    }

    /**
     * Creates a component with a level between the bounds inclusively.
     * @param lowerLevel lowest level.
     * @param higherLevel highest level.
     */
    public Temperature(int lowerLevel, int higherLevel) {
        super("Temperature", lowerLevel, higherLevel);
        setModifier();
    }

    private void setModifier() {
        if (getLevel() > 3) {
            addEvaporateModifier();
        }
        if (getLevel() < 3) {
            addPlantAntiTickModifier();
        }
    }

    private void addEvaporateModifier() {
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
        modifierSettings.put("effect", Double.toString(-0.0005 * getLevel()));

        //make the thing
        WorldManager.getInstance().getWorld().getModifierManager()
                .addNewAttributeModifier(modifierSettings);
    }

    private void addPlantAntiTickModifier() {
        World world = WorldManager.getInstance().getWorld();
        //creates a modifier
        HashMap<String, String> modifierSettings = new HashMap<>();
        modifierSettings.put("target", "all");
        modifierSettings.put("tag", "Weather");
        modifierSettings.put("position", (new Point(0, 0)).toString());
        modifierSettings.put("end", (
                new Point(world.getWidth(),world.getHeight())).toString());
        modifierSettings.put("shape", "square");
        modifierSettings.put("attribute", "growth");
        modifierSettings.put("effect", "-1");

        //make the thing
        WorldManager.getInstance().getWorld().getModifierManager()
                .addNewAttributeModifier(modifierSettings);
    }
}
