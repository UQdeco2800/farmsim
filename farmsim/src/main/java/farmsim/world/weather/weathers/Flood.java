package farmsim.world.weather.weathers;

import farmsim.world.World;
import farmsim.world.WorldManager;
import farmsim.world.weather.Weather;
import farmsim.world.weather.WeatherType;
import farmsim.world.weather.components.Clouds;
import farmsim.world.weather.components.Rain;
import farmsim.world.weather.components.Temperature;
import farmsim.world.weather.components.WeatherComponent;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Weather - Flood.
 * <p>
 *     Made of components: rain, clouds, temperature.
 * </p>
 */
public class Flood extends Weather {

    private static final Map<WeatherType, Double> SCALARS = createScalarMap();
    
    /**
     * Flooding weather Constructor.
     */
    public Flood() {
        super();
        this.setNameType("Flooding", WeatherType.FLOOD);
        setPath("weather/icons/flooding64.png");
        World currentWorld = WorldManager.getInstance().getWorld();
        try {
            currentWorld.updateLakes(-5);
        } catch (Exception e) {
            LOGGER.info("Update Lakes error: " + e);
        }
        this.loadComponents();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Flood.class);

    /**
     * SCALARS holds values to scale the next-weather-selection weightings 
     * based on the current weather being this one. 
     * For example, Drought should not follow Raining.  
     */
    /*
     * These values are all arbitrary and can be tweaked.
     */
    private static Map<WeatherType, Double> createScalarMap() {
        Map<WeatherType, Double> result = 
                new HashMap<WeatherType, Double>(WeatherType.values().length);
        result.put(WeatherType.DEFAULT, 2.0);
        result.put(WeatherType.RAINING, 1.0);
        result.put(WeatherType.SNOWING, 1.0);
        result.put(WeatherType.STORM, 1.0);
        result.put(WeatherType.WINDY, 1.0);
        result.put(WeatherType.SUNNY, 1.0);
        result.put(WeatherType.HEATWAVE, 0.5);
        result.put(WeatherType.DROUGHT, 0.0);
        result.put(WeatherType.THUNDERSTORM, 1.0);
        result.put(WeatherType.CLOUDY, 2.0);
        result.put(WeatherType.SANDSTORM, 0.0);
        result.put(WeatherType.FLOOD, 1.0);
        result.put(WeatherType.HAILING, 1.0);
        return result;
    }
    
    /**
     * Getter for the scalar map SCALARS.
     * @return the scalar map
     */
    public static Map<WeatherType, Double> getScalars() {
        return SCALARS;
    }

    private void loadComponents() {
        WeatherComponent rain = new Rain(5);
        WeatherComponent clouds = new Clouds(5);
        WeatherComponent temperature = new Temperature(2);
        this.addComponents(rain);
        this.addComponents(clouds);
        this.addComponents(temperature);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        LOGGER.info("Flood is ending");
    }
}
