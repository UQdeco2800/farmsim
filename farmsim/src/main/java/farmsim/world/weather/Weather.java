package farmsim.world.weather;

import farmsim.world.weather.components.WeatherComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Weather.
 */
public class Weather {

    private static final Logger LOGGER = LoggerFactory.getLogger(Weather.class);
    private String name = "Default";
    private WeatherType type = WeatherType.DEFAULT;
    private ArrayList<WeatherComponent> weatherComponents;
    private Map<WeatherType, Double> scalars;
    private String iconPath;

    /**
     * Creates a default weather type with no components.
     */
    public Weather() {
        this.weatherComponents = new ArrayList<>();
        scalars = new HashMap<>(WeatherType.values().length);
        for (WeatherType typeName : WeatherType.values()) {
            scalars.put(typeName, 1.0);
        }
    }

    /**
     * Creates a default weather with no components. The default weather cannot
     * load settings atm and ignores any values passed.
     * @require settings != null
     * @param settings to be passed to setup the weather
     */
    public Weather(List<String> settings) {
        LOGGER.error("Settings passed to default weather");
        LOGGER.debug(settings.toString());
    }

    /**
     * Sets the Weather Entities name.
     * 
     * @param name of the entity.
     * @param type the weather type enum.
     */
    protected void setNameType(String name, WeatherType type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Gets the weathers type represented in a human readable form.
     * 
     * @return string form of the weather type
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Returns the Weather.Type identifier of the Weather Element.
     *
     * @return Weather.Type value for the weather.
     */
    public WeatherType weatherType() {
        return this.type;
    }

    /**
     * Adds the components to the weather.
     *
     * @param component an immutable component
     */
    protected void addComponents(WeatherComponent component) {
        this.weatherComponents.add(component);
    }


    /**
     * A Map of weatherComponents in the current weather and the components
     * level.
     *
     * @return a map of all loaded components in the weather with the components
     *         lvl.
     */
    public Map<String, Integer> getComponents() {
        HashMap<String, Integer> currentComponents = new HashMap<>();

        for (WeatherComponent component : weatherComponents) {
            currentComponents.put(component.toString(), component.getLevel());
        }
        return currentComponents;
    }

    /**
     * Sets the weather to the settings given.
     *
     * @param settings to be loaded into the weather.
     * @return true if the settings were loaded, false otherwise.
     */
    public Boolean load(List<String> settings) {
        if (settings == null) {
            return false;
        }
        for (String setting : settings) {
            LOGGER.debug(setting);
        }
        return true;
    }

    /**
     * Cleanup of the weather on death.
     */
    public void onDeath() {
        LOGGER.info("Weather {} was Killed", this.name);
        for (WeatherComponent component : weatherComponents) {
            component.kill();
        }
        weatherComponents = new ArrayList<>();
    }
    
    /**
     * Update a value in the scalars map.
     * @param key the weather to update
     * @param val the new value
     */
    public void replace(WeatherType key, double val) {
        scalars.replace(key, val);
    }
    
    /**
     * Sets the filepath to the season icon.
     * @return the filepath
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
