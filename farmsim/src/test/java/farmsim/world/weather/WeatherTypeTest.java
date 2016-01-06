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

import org.junit.Assert;
import org.junit.Test;

/**
 * Testing the Weather Type Enums.
 */
public class WeatherTypeTest {

    /**
     * Tests getting the weather class from an enum type.
     * @throws Exception if type doesnt not have an associated type.
     */
    @Test
    public void testGetWeatherClass() throws Exception {
        // default weather
        Assert.assertEquals(Weather.class,
                WeatherType.DEFAULT.getWeatherClass());
        // raining
        Assert.assertEquals(Raining.class,
                WeatherType.RAINING.getWeatherClass());
        // snowing
        Assert.assertEquals(Snowing.class,
                WeatherType.SNOWING.getWeatherClass());
        // storming
        Assert.assertEquals(Storm.class,
                WeatherType.STORM.getWeatherClass());
        // windy
        Assert.assertEquals(Windy.class,
                WeatherType.WINDY.getWeatherClass());
        // sunny
        Assert.assertEquals(Sunny.class,
                WeatherType.SUNNY.getWeatherClass());
        // heatwave
        Assert.assertEquals(Heatwave.class,
                WeatherType.HEATWAVE.getWeatherClass());
        // drought
        Assert.assertEquals(Drought.class,
                WeatherType.DROUGHT.getWeatherClass());
        // thunderstorm
        Assert.assertEquals(ThunderStorm.class,
                WeatherType.THUNDERSTORM.getWeatherClass());
        // cloudy
        Assert.assertEquals(Cloudy.class,
                WeatherType.CLOUDY.getWeatherClass());
        // sandstorm
        Assert.assertEquals(SandStorm.class,
                WeatherType.SANDSTORM.getWeatherClass());
        // flood
        Assert.assertEquals(Flood.class,
                WeatherType.FLOOD.getWeatherClass());
        // hailing
        Assert.assertEquals(Hailing.class,
                WeatherType.HAILING.getWeatherClass());

    }
}