package farmsim.entities.animal;

import static org.junit.Assert.*;

import farmsim.entities.animals.Duck;
import farmsim.world.WorldManager;
import org.junit.Test;
public class DuckTest {

    // Construct basic duck for use in all tests
    public Duck initDuck() {
        return new Duck(WorldManager.getInstance().getWorld(), 10, 10, 0.06,
                0, 'f', 125);
    }

    // Test basic duck construction + setting and getting attributes
    @Test
    public void initTest() {
        Duck duck = initDuck();

        assertTrue(duck.getWorldX() == 10);
        assertTrue(duck.getWorldY() == 10);
        assertTrue(duck.getSpeed() == 0.06);
        assertTrue(duck.getAge() == 0);
        assertTrue(duck.getSex() == 'f');
        assertTrue(duck.getWeight() == 125);

        duck.setReadyToMate(true);
        duck.setPregnant(false);
        duck.setEdible(false);

        assertTrue(duck.getReadyToMate() == true);
        assertTrue(duck.getEdible() == false);

        duck.setWeight(50);
        assertTrue(duck.getWeight() == 50);
        duck.setWeight(1001);
        assertTrue(duck.getWeight() == 1000);
        duck.setWeight(0);
        assertTrue(duck.getWeight() == 1);

        duck.setDuckBreastQuality(0.5);
        assertTrue(duck.getDuckBreastQuality() == 0.5);
        duck.setDuckBreastQuality(2);
        assertTrue(duck.getDuckBreastQuality() == 1);
        duck.setDuckBreastQuality(-1);
        assertTrue(duck.getDuckBreastQuality() == 0);

        duck.setWeightIncreaseRate(0.5);
        assertTrue(duck.getWeightIncreaseRate() == 0.5);
        duck.setWeightIncreaseRate(2);
        assertTrue(duck.getWeightIncreaseRate() == 1);
        duck.setWeightIncreaseRate(-1);
        assertTrue(duck.getWeightIncreaseRate() == 0);

        duck.setSpeed(0.5);
        assertTrue(duck.getSpeed() == 0.5);
        duck.setSpeed(200);
        assertTrue(duck.getSpeed() == 100);
        duck.setSpeed(-1);
        assertTrue(duck.getSpeed() == 0);
        duck.updateSpeed(0.2);
        assertTrue(duck.getSpeed() == 0.2);

        duck.setHealth(50);
        assertTrue(duck.getHealth() == 50);
        duck.setHealth(-1);
        assertTrue(duck.getHealth() == 0);
        duck.setHealth(101);
        assertTrue(duck.getHealth() == 100);
    }
}
