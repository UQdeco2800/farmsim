package farmsim.world.weather.seasons;

import farmsim.world.weather.Season;
import farmsim.world.weather.SeasonName;
import farmsim.world.weather.WeatherType;

import java.util.HashMap;
import java.util.Map;

/**
 * Subclass for Summer. Extends Season.
 * To contain Summer-specific data.
 */
public class Summer extends Season {

    /** 
     * Constructor sets season name to SeasonName.SUMMER
     * Creates a list of scalars for weather types (default scalar 1.0)
     */
    public Summer() {
        super(SeasonName.SUMMER);
        Map<WeatherType, Double> scalars = 
                new HashMap<>(WeatherType.values().length);
        for (WeatherType type : WeatherType.values()) {
            scalars.put(type, 1.0);
        }
        
        scalars.replace(WeatherType.SNOWING, 0.0);
        scalars.replace(WeatherType.SUNNY, 2.0);
        scalars.replace(WeatherType.HEATWAVE, 2.0);
        scalars.replace(WeatherType.RAINING, 2.0);
        scalars.replace(WeatherType.STORM, 2.0);
        scalars.replace(WeatherType.THUNDERSTORM, 2.0);
        
        setScalars(scalars);
        setPath("weather/icons/summer64.png");
    }
}
