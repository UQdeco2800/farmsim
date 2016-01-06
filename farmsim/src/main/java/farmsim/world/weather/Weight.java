package farmsim.world.weather;

import farmsim.world.WorldManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to provide methods for a weighted random weather selector based on 
 * season and previous weather. Uses a map of weather:weight for each
 * weather event. Weighting takes into account base rarity, current season, and
 * previous weather event.
 */
public class Weight {
    private Map<WeatherType, Double> weights;
    private Season season;
    public static final double BASE = 10;
    public static final int NUM_TYPES = WeatherType.values().length;
    
    /**
     * Constructor. Creates a map of weights for each weather event.
     * @param prevWeather the previous weather event (previous to the event for
     *     which to generate new weights)
     */
    public Weight(WeatherType prevWeather) {
        Map<WeatherType, Double> weatherScalars;
        
        season = WorldManager.getInstance().getWorld().getSeasonObject();
        
        weights = new HashMap<>(NUM_TYPES);
        for (WeatherType type : WeatherType.values()) {
            weights.put(type, BASE);
        }
        applyBaseRarity();
        applySeasonScalars();
        
        if (prevWeather != WeatherType.DEFAULT) {
            weatherScalars = getWeatherScalars(prevWeather);
            applyWeatherScalars(weatherScalars);
        }
        ceiling();
    }
    
    /**
     * Applies a set of scalars to determine the base (irrespective of season,
     * weather) rarity of a weather event. Edit these values to make an event
     * more or less likely to happen.
     * Default implementation: 
     * -DEFAULT (forecast "Fine") significantly more likely to be selected than 
     * any other individual event;
     * -High impact events half as likely to be selected as any other individual
     * event.
     */
    private void applyBaseRarity() {
        weights.replace(WeatherType.DEFAULT, 
                weights.get(WeatherType.DEFAULT) * 5.0);
        weights.replace(WeatherType.SANDSTORM, 
                weights.get(WeatherType.SANDSTORM) * 0.5);
        weights.replace(WeatherType.FLOOD, 
                weights.get(WeatherType.FLOOD) * 0.5);
        weights.replace(WeatherType.DROUGHT, 
                weights.get(WeatherType.DROUGHT) * 0.5);
        weights.replace(WeatherType.HEATWAVE, 
                weights.get(WeatherType.HEATWAVE) * 0.5);
    }
    
    /**
     * Takes a given weathertype, and returns the next-weather-scalars for it.
     * @param type weather to get scalars for
     * @return scalar map
     */
    private Map<WeatherType, Double> getWeatherScalars(WeatherType type) {
        switch (type) {
            case RAINING:
                return farmsim.world.weather.weathers.Raining.getScalars();
            case SNOWING:
                return farmsim.world.weather.weathers.Snowing.getScalars();
            case STORM:
                return farmsim.world.weather.weathers.Storm.getScalars();
            case WINDY:
                return farmsim.world.weather.weathers.Windy.getScalars();
            case SUNNY:
                return farmsim.world.weather.weathers.Sunny.getScalars();
            case HEATWAVE:
                return farmsim.world.weather.weathers.Heatwave.getScalars();
            case DROUGHT:
                return farmsim.world.weather.weathers.Drought.getScalars();
            case THUNDERSTORM:
                return farmsim.world.weather.weathers.ThunderStorm.getScalars();
            case CLOUDY:
                return farmsim.world.weather.weathers.Cloudy.getScalars();
            case SANDSTORM:
                return farmsim.world.weather.weathers.SandStorm.getScalars();
            case FLOOD:
                return farmsim.world.weather.weathers.Flood.getScalars();
            case HAILING:
                return farmsim.world.weather.weathers.Hailing.getScalars();
            default:
                break;
        }
        return null;
    }

    /**
     * Applies the seasonal scalars to the weighting map.
     */
    private void applySeasonScalars() {
        for (WeatherType type : WeatherType.values()) {
            weights.replace(type, weights.get(type) * season.getScalar(type));
        }
    }
    
    /**
     * Applies scalars from the previous weather event.
     */
    private void applyWeatherScalars(Map<WeatherType, Double> weatherScalars) {
        for (WeatherType type : WeatherType.values()) {
            weights.replace(type, weights.get(type) 
                    * weatherScalars.get(type));
        }
    }
    
    /**
     * Applies math.ceil to all scaled values (selector uses integer logic).
     */
    private void ceiling() {
        for (WeatherType type : WeatherType.values()) {
            weights.replace(type, Math.ceil(weights.get(type)));
        }
    }
    
    /**
     * Calculates the sum total of all weight values in the map. Used to get
     * liklihood as a fraction of total.
     * @return the total of all weights in the map.
     */
    public int total() {
        int total = 0;
        
        for (Double value : weights.values()) {
            total += value;
        }
        return total;
    }
    
    /**
     * Get a weather type from a randomly generated integer
     * @require num <= total
     * @ensure will return a valid weathertype (i.e. non-null)
     * @param num randomly generated integer
     * @return the selected weather type
     */
    public WeatherType selectWeather(int num) {
        int sum = 0;

        for (WeatherType type : weights.keySet()) {
            sum += weights.get(type);
            if (sum >= num) {
                return type;
            }
        }
        return null; // won't reach if @require met
    }

}
