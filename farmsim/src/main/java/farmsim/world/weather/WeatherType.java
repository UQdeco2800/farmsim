package farmsim.world.weather;


import farmsim.world.weather.weathers.Cloudy;
import farmsim.world.weather.weathers.Drought;
import farmsim.world.weather.weathers.Flood;
import farmsim.world.weather.weathers.Hailing;
import farmsim.world.weather.weathers.Heatwave;
import farmsim.world.weather.weathers.Raining;
import farmsim.world.weather.weathers.SandStorm;
import farmsim.world.weather.weathers.Snowing;
import farmsim.world.weather.weathers.Storm;
import farmsim.world.weather.weathers.Sunny;
import farmsim.world.weather.weathers.ThunderStorm;
import farmsim.world.weather.weathers.Windy;

/**
 * Weather Type enum with class attachment.
 */
public enum WeatherType {
    DEFAULT(Weather.class),
    RAINING(Raining.class),
    SNOWING(Snowing.class),
    STORM(Storm.class),
    WINDY(Windy.class),
    SUNNY(Sunny.class),
    HEATWAVE(Heatwave.class),
    DROUGHT(Drought.class),
    THUNDERSTORM(ThunderStorm.class),
    CLOUDY(Cloudy.class),
    SANDSTORM(SandStorm.class),
    FLOOD(Flood.class),
    HAILING(Hailing.class);

    private Class<? extends Weather> weatherClass;

    /**
     * Sets a class to each type.
     * @param weatherClass to be set to the weather type.
     */
    WeatherType(Class<? extends Weather> weatherClass) {
        this.weatherClass = weatherClass;
    }

    /**
     * Get the class associated with the type.
     * @return class associated to the type.
     */
    public Class<? extends Weather> getWeatherClass() {
        return this.weatherClass;
    }
}
