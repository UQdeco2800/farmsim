package farmsim.world.weather;

import farmsim.world.World;
import farmsim.world.WorldManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Weather Manager.
 *
 * <p>
 * creates a manager that controls the current weather active in the world.
 * requires a pass through of the world to ensure able to modify the world.
 * </p>
 */
public class WeatherManager extends Observable {

    private Weather currentWeather;
    private int startHour;
    private int duration;   // measured in hours
    private WeatherQueue queue;
    private WorldManager worldManager = WorldManager.getInstance();

    private static final Logger LOGGER =
            LoggerFactory.getLogger(WeatherManager.class);
    public static final int WEATHER_LOADED = 1;
    public static final int WEATHER_ERROR_DNE = 0;
    public static final int WEATHER_ERROR_SETTINGS = 2;

    /**
     * Constructor for Weather Manager which stores an instance of the world.
     */
    public WeatherManager() {
        this.addWeather(WeatherType.DEFAULT);
        LOGGER.info("Created Weather Manager");
    }
    
    /**
     * Constructor for Weather Manager which stores an instance of the world and
     * lads up save data in the form of {......}
     *
     * @require saveData != null
     * @param saveData string that once passed gives initial values
     */
    public WeatherManager(String saveData) {
        this();
        LOGGER.info("Loaded save info" + saveData);
    }

    /**
     * Removes the current weather and calls the closing functions to let the
     * weather cleanup what ever actions it has going.
     */
    private void removeWeather() {
        // close the weather (call the ending transition)
        this.currentWeather.onDeath();
        this.currentWeather = new Weather();
    }

    /**
     *
     * @param weather the representation of the weather found in
     *        farmsim.world.weather.Weather
     * @return WEATHER_LOADED if completed or WEATHER_ERROR_DNE for a weather
     *         type which hasn't been implemented.
     */
    private int addWeather(WeatherType weather) {
        int status = WEATHER_ERROR_DNE;
        // original design by @Trmanderson
        Class<? extends Weather> weatherClass = weather.getWeatherClass();
        if (weatherClass != null) {
            try {
                this.currentWeather = weatherClass.newInstance();
            } catch (Exception e) {
                LOGGER.error("Weather could not be created", e);
            }
            status = WEATHER_LOADED;
        } else {
            LOGGER.error("Weather not implemented");
        }
        return status;
    }

    /**
     *
     * @param weather the integer representation of the weather found in
     *        farmsim.world.weather.Weather. If the settings are invalid the
     *        weather is loaded without the settings applied.
     * @param settings settings to pass through into the weather
     * @return returns WEATHER_ERROR_DNE when could not load weather,
     *         WEATHER_LOADED if the settings are applied to the weather,
     *         WEATHER_ERROR_SETTINGS if the settings could not be loaded but
     *         the weather has been loaded.
     */
    private int addWeather(WeatherType weather, List<String> settings) {
        if (this.addWeather(weather) == WEATHER_LOADED) {
            if (this.currentWeather.load(settings)) {
                return WEATHER_LOADED;
            } else {
                return WEATHER_ERROR_SETTINGS;
            }
        }
        return WEATHER_ERROR_DNE;
    }

    /**
     * Closes the currently active weather and switches it with the new weather
     * type given with default settings.
     *
     * @param nextWeather the representation of the weather found in
     *        farmsim.world.weather.Weather
     * @return true if the weather was applied successfully
     */
    public boolean switchWeather(WeatherType nextWeather) {
        this.removeWeather();
        WorldManager.getInstance().getWorld().getModifierManager().purge(
                "Weather");
        return this.addWeather(nextWeather) == WEATHER_LOADED;
    }

    /**
     * Closes the currently active weather and switches it with the new weather
     * type given with the supplied settings.
     *
     * @param nextWeather the representation of the weather found in
     *        farmsim.world.weather.Weather
     * @param settings settings to pass through into the weather
     * @return true when weather was loaded successfully
     */
    public boolean switchWeather(WeatherType nextWeather,
            List<String> settings) {
        this.removeWeather();
        WorldManager.getInstance().getWorld().getModifierManager().purge(
                "Weather");
        return this.addWeather(nextWeather, settings) == WEATHER_LOADED;
    }

    /**
     * Gets the Weather.Type of the currently active weather
     *
     * @return Weather.Type of the currently active weather
     */
    public WeatherType getWeather() {
        return currentWeather.weatherType();
    }

    /**
     * Gets the name of the currently active weather.
     *
     * @return name of the currently active weather
     */
    public String getWeatherName() {
        return currentWeather.toString();
    }


    /**
     * Gets a Map of the name and level of each component loaded into the
     * current weather.
     *
     * @return map of the component names and their level.
     */
    public Map<String, Integer> getWeatherComponents() {
        return this.currentWeather.getComponents();
    }
    
    /**
     * Get the next weather from the head of the queue and change active weather
     * while tracking start time (hours). Also notifies observers.
     */
    public void nextWeatherFromQueue() {
        WeatherQueue.Element nextElement;
        WeatherType nextWeather;
        
        this.startHour = getTotalHours();
        nextElement = queue.pop();
        nextWeather = nextElement.getWeather();
        duration = nextElement.getDuration();
        switchWeather(nextWeather);
        setChanged();
        notifyObservers(getWeatherName());
    }
    
    /**
     * Build the initial weather queue.
     */
    public void buildQueue() {
        queue = new WeatherQueue();
    }
    
    /**
     * Update the forecast.
     */
    public void updateForecast() {
        queue.updateForecast();
    }
    
    /**
     * Get the forecast list.
     * @return the forecast list.
     */
    public List<WeatherQueue.Element> getForecast() {
        return queue.getForecast();
    }
    
    /**
     * Function to run on each hour tick. Called from DayNight.java.
     */
    public void hourTick() {
        if (getTotalHours() > this.startHour + duration) {
            nextWeatherFromQueue();
            queue.addNew();
        }
    }
    
    /**
     * Get total hours (days*24 + hours).
     */
    public int getTotalHours() {
        return ((worldManager.getWorld().getTimeManager().getDays() - 1) * 24) 
                + worldManager.getWorld().getTimeManager().getHours();
    }
    
    /**
     * Gets the string for the styleClass for the current weather icon.
     * @param weather type of weather event
     * @return style class for weather
     */
    public String getWeatherIconStyle(WeatherType weather) {
        switch (weather) {
            case DEFAULT:       break;
            case CLOUDY:        return "weatherCloudy";
            case DROUGHT:       return "weatherDrought";
            case FLOOD:         return "weatherFlood";
            case HAILING:       return "weatherHailing";
            case HEATWAVE:      return "weatherHeatwave";
            case RAINING:       return "weatherRaining";
            case SANDSTORM:     return "weatherSandstorm";
            case SNOWING:       return "weatherSnowing";
            case STORM:         return "weatherStorm";
            case SUNNY:         return "weatherSunny";
            case THUNDERSTORM:  return "weatherThunderstorm";
            case WINDY:         return "weatherWindy";
        }
        return null; // unreachable
    }
    
    /**
     * Gets the string for the styleClass for a forecast icon.
     * @param weather type of weather event
     * @return style class for forecast icon
     */
    public String getForecastIconStyle(WeatherType weather) {
        switch (weather) {
            case DEFAULT:       return "forecastFine";
            case CLOUDY:        return "forecastCloudy";
            case DROUGHT:       return "forecastDrought";
            case FLOOD:         return "forecastFlood";
            case HAILING:       return "forecastHailing";
            case HEATWAVE:      return "forecastHeatwave";
            case RAINING:       return "forecastRaining";
            case SANDSTORM:     return "forecastSandstorm";
            case SNOWING:       return "forecastSnowing";
            case STORM:         return "forecastStorm";
            case SUNNY:         return "forecastSunny";
            case THUNDERSTORM:  return "forecastThunderstorm";
            case WINDY:         return "forecastWindy";
        }
        return null; // unreachable
    }
    
    /**
     * Returns the filepath to the current weather's icon.
     * @return path to weather icon
     */
    public String getIconPath() {
        return currentWeather.getPath();
    }
    
    /**
     * Register hourTick() as an hourly task with the time manager.
     */
    public void registerHourlyTask() {
        worldManager.getWorld().getTimeManager().registerHourlyTask(() -> 
                hourTick());
    }
}
