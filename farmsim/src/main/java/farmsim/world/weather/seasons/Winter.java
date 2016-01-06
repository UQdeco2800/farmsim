package farmsim.world.weather.seasons;

import farmsim.world.weather.Season;
import farmsim.world.weather.SeasonName;
import farmsim.world.weather.WeatherType;

import java.util.HashMap;
import java.util.Map;

/**
 * Subclass for Winter. Extends Season.
 * To contain Winter-specific data.
 */
public class Winter extends Season {
    
    /** 
     * Constructor sets season name to SeasonName.WINTER
     * Creates a list of scalars for weather types (default scalar 1.0)
     */
    public Winter() {
        super(SeasonName.WINTER);
        Map<WeatherType, Double> scalars = 
                new HashMap<>(WeatherType.values().length);
        for (WeatherType type : WeatherType.values()) {
            scalars.put(type, 1.0);
        }
        
        scalars.replace(WeatherType.HEATWAVE, 0.0);
        scalars.replace(WeatherType.SUNNY, 0.0);
        scalars.replace(WeatherType.FLOOD, 0.0);
        scalars.replace(WeatherType.STORM, 0.5);
        scalars.replace(WeatherType.THUNDERSTORM, 0.5);
        scalars.replace(WeatherType.SNOWING, 2.0);
        
        
        setScalars(scalars);
        setPath("weather/icons/winter64.png");
    }
}
