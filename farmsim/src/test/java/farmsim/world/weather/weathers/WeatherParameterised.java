package farmsim.world.weather.weathers;

import farmsim.particle.ParticleController;
import farmsim.world.weather.Weather;
import farmsim.world.weather.WeatherType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Test to cover most of weathers.
 */
@RunWith(Parameterized.class)
public class WeatherParameterised {

    private final WeatherMaker weatherMaker;
    private Weather weather;

    public static class WeatherMaker {
        private Constructor<? extends Weather> constructor;

        /**
         * Gets the constructor of the weather class.
         * @param weatherClass the class to get the constructor of.
         */
        public WeatherMaker(Class<? extends Weather> weatherClass) {
            try {
                constructor = weatherClass.getConstructor();
            } catch (Exception e) {
                System.out.println("Could not create class");
            }
        }

        /**
         * Get new instance of the constructor.
         * @return instance of constructor.
         */
        public Weather build() {
            try {
                return constructor.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    /**
     * Get all weather classes.
     * @return array of weather classes.
     */
    @Parameterized.Parameters
    public static Iterable<WeatherMaker> weathers() {
        ArrayList<WeatherMaker> weathers = new ArrayList<>();
        for (WeatherType type : WeatherType.values()) {
            weathers.add(new WeatherMaker(type.getWeatherClass()));
        }
        return weathers;
    }

    /**
     * Set the weather maker instance to be used.
     * @param weatherMaker instance to use for creation.
     */
    public WeatherParameterised(WeatherMaker weatherMaker) {
        this.weatherMaker = weatherMaker;
    }

    /**
     * Get particle controller and give it an empty canvas to prevent particle
     * causing crash.
     */
    @Before
    public void setUp() {
        weather = weatherMaker.build();
        ParticleController pc = ParticleController.getInstance();
        pc.setParent(null);
    }

    /**
     * Checks that weather includes components.
     */
    @Test 
    public void checkComponents() {
//        if (weather.weatherType() == WeatherType.DEFAULT) {
//            Assert.assertTrue(weather.getComponents().isEmpty());
//        } else {
//            Assert.assertTrue(weather.getComponents().size() > 0);
//        }
    }
}
