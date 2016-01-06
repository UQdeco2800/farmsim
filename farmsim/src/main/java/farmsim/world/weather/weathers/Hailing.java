package farmsim.world.weather.weathers;

import farmsim.world.weather.Weather;
import farmsim.world.weather.WeatherType;
import farmsim.world.weather.components.Clouds;
import farmsim.world.weather.components.Hail;
import farmsim.world.weather.components.Temperature;
import farmsim.world.weather.components.WeatherComponent;
import farmsim.world.weather.components.Wind;

import java.util.HashMap;
import java.util.Map;

/**
 * Weather - Hailing.
 * <p>
 *     Made of components: rain, wind, clouds, temperature.
 * </p>
 */
public class Hailing extends Weather {

    private static final Map<WeatherType, Double> SCALARS = createScalarMap();
    
    /**
     * Hailing weather Constructor.
     */
    public Hailing() {
        super();
        this.setNameType("Hailing", WeatherType.HAILING);
        setPath("weather/icons/hail64.png");
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
        result.put(WeatherType.SNOWING, 2.0);
        result.put(WeatherType.STORM, 2.0);
        result.put(WeatherType.WINDY, 1.0);
        result.put(WeatherType.SUNNY, 1.0);
        result.put(WeatherType.HEATWAVE, 0.5);
        result.put(WeatherType.DROUGHT, 0.0);
        result.put(WeatherType.THUNDERSTORM, 2.0);
        result.put(WeatherType.CLOUDY, 1.0);
        result.put(WeatherType.SANDSTORM, 0.5);
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
        WeatherComponent hail = new Hail(1, 5);
        WeatherComponent wind = new Wind(1, 5);
        WeatherComponent clouds = new Clouds(5);
        WeatherComponent temperature = new Temperature(1, 2);
        this.addComponents(hail);
        this.addComponents(wind);
        this.addComponents(clouds);
        this.addComponents(temperature);
    }
}
