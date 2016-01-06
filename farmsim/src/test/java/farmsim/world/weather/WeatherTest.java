package farmsim.world.weather;

import farmsim.world.weather.components.WeatherComponent;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the Weather Base Class.
 */
public class WeatherTest {

    /**
     * Test setting the name and type of the weather atleast once.
     */
    @Test
    public void testSetNameType() {
        String name = "Test Name";

        Weather weather = new Weather();
        weather.setNameType(name, WeatherType.DEFAULT);

        Assert.assertEquals(name, weather.toString());
        Assert.assertEquals(WeatherType.DEFAULT, weather.weatherType());
    }

    /**
     * Test getting the name of the weather through the to string method.
     * Tests with multiple names.
     */
    @Test
    public void testToString() {
        Weather weather = new Weather();

        weather.setNameType("first name", WeatherType.DEFAULT);
        Assert.assertEquals("first name", weather.toString());

        weather.setNameType("second name", WeatherType.DEFAULT);
        Assert.assertEquals("second name", weather.toString());
    }

    /**
     * Tests setting the weather type multiple times.
     */
    @Test
    public void testWeatherType() {
        Weather weather = new Weather();

        weather.setNameType("Default", WeatherType.DEFAULT);
        Assert.assertEquals(WeatherType.DEFAULT, weather.weatherType());

        weather.setNameType("Default", WeatherType.RAINING);
        Assert.assertEquals(WeatherType.RAINING, weather.weatherType());
    }

    /**
     * Tests adding components.
     */
    @Test
    public void testAddComponents() {
        Weather weather = new Weather();
        WeatherComponent component = new WeatherComponent("Random");

        // check we have no components
        Assert.assertTrue(weather.getComponents().isEmpty());

        // check with a component added
        weather.addComponents(component);
        Assert.assertTrue(!weather.getComponents().isEmpty());
    }

    /**
     * Tests retrieving components in a meaningful format.
     */
    @Test
    public void testGetComponents() {
        Weather weather = new Weather();
        WeatherComponent random = new WeatherComponent("Random");
        // Random Component (single component)
        weather.addComponents(random);
        Assert.assertTrue(
                weather.getComponents().containsKey(random.getName()));
        if (weather.getComponents().containsKey(random.getName())) {
            Assert.assertEquals((long) random.getLevel(),
                    (long) weather.getComponents().get(random.getName()));
        } else {
            Assert.assertFalse(true);
        }

        WeatherComponent other = new WeatherComponent("Other");
        // Random Component (single component)
        weather.addComponents(other);
        Assert.assertTrue(
                weather.getComponents().containsKey(other.getName()));
        if (weather.getComponents().containsKey(other.getName())) {
            Assert.assertEquals((long)other.getLevel(),
                    (long)weather.getComponents().get(other.getName()));
        } else {
            Assert.assertFalse(true);
        }
    }

    /**
     * Tests loading settings from save.
     */
    @Test
    public void testLoad() {
        Weather weather = new Weather();
        List<String> settings = new ArrayList<>();
        settings.add("Example");
        settings.add("Settings");

        Assert.assertTrue(weather.load(settings));
        Assert.assertTrue(!weather.load(null));
    }

    /**
     * Tests freeing memory on death.
     */
    @Test
    public void testOnDeath() {
        Weather weather = new Weather();
        WeatherComponent random = new WeatherComponent("Random");
        // Random Component (single component)
        weather.addComponents(random);

        Assert.assertTrue(!weather.getComponents().isEmpty());

        weather.onDeath();

        Assert.assertTrue(weather.getComponents().isEmpty());
    }
}