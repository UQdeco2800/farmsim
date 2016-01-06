package farmsim.world.weather.weathers;

import farmsim.world.weather.Weather;
import farmsim.world.weather.WeatherType;
import farmsim.world.weather.components.Sun;
import farmsim.world.weather.components.Temperature;
import farmsim.world.weather.components.WeatherComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Weather - Heatwave.
 * <p>
 *     Made of components: temperature, sun.
 * </p>
 */
public class Heatwave extends Weather {

    private static final Map<WeatherType, Double> SCALARS = createScalarMap();
    
    /**
     * Heatwave weather Constructor.
     */
    public Heatwave() {
        super();
        this.setNameType("Heatwave", WeatherType.HEATWAVE);
        setPath("weather/icons/heatwave64.png");
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
        result.put(WeatherType.RAINING, 2.0);
        result.put(WeatherType.SNOWING, 1.0);
        result.put(WeatherType.STORM, 2.0);
        result.put(WeatherType.WINDY, 1.0);
        result.put(WeatherType.SUNNY, 1.0);
        result.put(WeatherType.HEATWAVE, 2.0);
        result.put(WeatherType.DROUGHT, 2.0);
        result.put(WeatherType.THUNDERSTORM, 2.0);
        result.put(WeatherType.CLOUDY, 0.5);
        result.put(WeatherType.SANDSTORM, 1.0);
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
        WeatherComponent sun = new Sun(4, 5);
        WeatherComponent temperature = new Temperature(4);
        this.addComponents(sun);
        this.addComponents(temperature);
    }
}
