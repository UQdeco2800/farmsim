package farmsim.world.weather.seasons;

import farmsim.world.weather.Season;
import farmsim.world.weather.SeasonName;
import farmsim.world.weather.WeatherType;

import java.util.HashMap;
import java.util.Map;

/**
 * Subclass for Autumn. Extends Season.
 * To contain Autumn-specific data.
 */
public class Autumn extends Season {

    
    /** 
     * Constructor sets season name to SeasonName.AUTUMN
     * Creates a list of scalars for weather types (default scalar 1.0)
     */
    public Autumn() {
        super(SeasonName.AUTUMN);
        Map<WeatherType, Double> scalars = 
                new HashMap<>(WeatherType.values().length);
        for (WeatherType type : WeatherType.values()) {
            scalars.put(type, 1.0);
        }
        
        scalars.replace(WeatherType.SNOWING, 0.0);
        scalars.replace(WeatherType.SUNNY, 0.0);
        
        setScalars(scalars);
        setPath("weather/icons/autumn64.png");
    }

}
