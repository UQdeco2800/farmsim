package farmsim.world.weather.weathers;

import farmsim.world.weather.Weather;
import farmsim.world.weather.WeatherType;
import farmsim.world.weather.components.Sand;
import farmsim.world.weather.components.Temperature;
import farmsim.world.weather.components.WeatherComponent;
import farmsim.world.weather.components.Wind;

import java.util.HashMap;
import java.util.Map;

/**
 * Weather - Sandstorm.
 * <p>
 *     Made of components: wind, sand, temperature.
 * </p>
 */
public class SandStorm extends Weather {

    private static final Map<WeatherType, Double> SCALARS = createScalarMap();
    
    /**
     * Sandstorm weather Constructor.
     */
    public SandStorm() {
        super();
        this.setNameType("SandStorm", WeatherType.SANDSTORM);
        setPath("weather/icons/sandstorm64.png");
        this.loadComponents();
    }

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
        result.put(WeatherType.WINDY, 2.0);
        result.put(WeatherType.SUNNY, 1.0);
        result.put(WeatherType.HEATWAVE, 1.0);
        result.put(WeatherType.DROUGHT, 1.0);
        result.put(WeatherType.THUNDERSTORM, 1.0);
        result.put(WeatherType.CLOUDY, 1.0);
        result.put(WeatherType.SANDSTORM, 2.0);
        result.put(WeatherType.FLOOD, 0.0);
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
        WeatherComponent wind = new Wind(1, 5);
        WeatherComponent sand = new Sand(1, 5);
        WeatherComponent temperature = new Temperature(3, 5);
        this.addComponents(wind);
        this.addComponents(sand);
        this.addComponents(temperature);
    }
}
