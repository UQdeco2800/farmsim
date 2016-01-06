package farmsim.world.weather.weathers;

import farmsim.world.weather.Weather;
import farmsim.world.weather.WeatherType;
import farmsim.world.weather.components.Sun;
import farmsim.world.weather.components.Temperature;
import farmsim.world.weather.components.WeatherComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Weather - Sunny.
 * <p>
 *     Made of components: sun, temperature.
 * </p>
 */
public class Sunny extends Weather {

    private static final Map<WeatherType, Double> SCALARS = createScalarMap();
    
    /**
     * Sunny Weather Constructor.
     */
    public Sunny() {
        super();
        this.setNameType("Sunny", WeatherType.SUNNY);
        setPath("weather/icons/sun64.png");
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
        result.put(WeatherType.SUNNY, 2.0);
        result.put(WeatherType.HEATWAVE, 1.0);
        result.put(WeatherType.DROUGHT, 1.0);
        result.put(WeatherType.THUNDERSTORM, 1.0);
        result.put(WeatherType.CLOUDY, 2.0);
        result.put(WeatherType.SANDSTORM, 1.0);
        result.put(WeatherType.FLOOD, 0.0);
        result.put(WeatherType.HAILING, 0.5);
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
        WeatherComponent sun = new Sun(1, 5);
        WeatherComponent temperature = new Temperature(3);
        this.addComponents(sun);
        this.addComponents(temperature);
    }
}
