package farmsim.world.weather;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Weather Manager Tests.
 */
public class WeatherManagerTest {

    /**
     * Testing switching weather through the manager.
     */
    @Test
    public void testSwitchWeather() {
        WeatherManager manager = new WeatherManager();
        Assert.assertEquals(WeatherType.DEFAULT, manager.getWeather());
    }

    /**
     * Testing switching weather through the manager with settings.
     */
    @Test
    public void testSwitchWeatherWithSaveData() {
        WeatherManager manager = new WeatherManager("Some Settings");
        Assert.assertEquals(WeatherType.DEFAULT, manager.getWeather());


        List<String> settings = new ArrayList<>();
        settings.add("test");
        manager.switchWeather(WeatherType.SUNNY, settings);
        Assert.assertEquals(WeatherType.SUNNY, manager.getWeather());

        // test a bad add
        Assert.assertTrue(!manager.switchWeather(WeatherType.SUNNY, null));
    }

    /**
     * Gets the currently loaded weather.
     */
    @Test
    public void testGetWeather() {
        WeatherManager manager = new WeatherManager("Some Settings");
        Assert.assertEquals(WeatherType.DEFAULT, manager.getWeather());

        manager.switchWeather(WeatherType.SUNNY);
        Assert.assertEquals(WeatherType.SUNNY, manager.getWeather());
    }

    /**
     * Tests getting the currently loaded weather name.
     */
    @Test
    public void testGetWeatherName() {
        WeatherManager manager = new WeatherManager("Some Settings");
        Assert.assertEquals("Default", manager.getWeatherName());

        manager.switchWeather(WeatherType.SUNNY);
        Assert.assertEquals("Sunny", manager.getWeatherName());
    }

    /**
     * Tests getting the components associated with the weather.
     */
    @Test
    public void testGetWeatherComponents() {
        WeatherManager manager = new WeatherManager("Some Settings");
        Assert.assertTrue(manager.getWeatherComponents().isEmpty());

        manager.switchWeather(WeatherType.SUNNY);
        Assert.assertTrue(!manager.getWeatherComponents().isEmpty());
    }
    
    /**
     * Test returns for all cases of getIconStyle.
     */
    @Test
    public void testGetIconStyle() {
        WeatherManager manager = new WeatherManager();
        for (WeatherType t : WeatherType.values()) {
            if (t == WeatherType.DEFAULT) {
                continue;
            }
            Assert.assertTrue(manager.getWeatherIconStyle(t).equals(
                    "weather" + t.toString().charAt(0) + 
                    t.toString().substring(1).toLowerCase()));
            Assert.assertTrue(manager.getForecastIconStyle(t).equals(
                    "forecast" + t.toString().charAt(0) + 
                    t.toString().substring(1).toLowerCase()));
        }
    }
}