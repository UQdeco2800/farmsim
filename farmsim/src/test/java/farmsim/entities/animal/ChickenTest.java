package farmsim.entities.animal;

import static org.junit.Assert.*;

import farmsim.entities.animals.Chicken;
import farmsim.entities.animals.Duck;
import farmsim.world.WorldManager;
import org.junit.Test;
public class ChickenTest {

    // Construct basic chicken for use in all tests
    public Chicken initChicken() {
        return new Chicken(WorldManager.getInstance().getWorld(), 10, 10, 0.06,
                0, 'f', 125);
    }

    // Test basic chicken construction + setting and getting attributes
    @Test
    public void initTest() {
        Chicken chicken = initChicken();

        assertTrue(chicken.getWorldX() == 10);
        assertTrue(chicken.getWorldY() == 10);
        assertTrue(chicken.getSpeed() == 0.06);
        assertTrue(chicken.getAge() == 0);
        assertTrue(chicken.getSex() == 'f');
        assertTrue(chicken.getWeight() == 125);

        chicken.setReadyToMate(true);
        chicken.setPregnant(false);
        chicken.setEdible(false);
        chicken.setEggable(false);

        assertTrue(chicken.getReadyToMate() == true);
        assertTrue(chicken.getEdible() == false);
        assertEquals(false, chicken.getEggable());

        chicken.setWeight(50);
        assertTrue(chicken.getWeight() == 50);
        chicken.setWeight(1001);
        assertTrue(chicken.getWeight() == 1000);
        chicken.setWeight(0);
        assertTrue(chicken.getWeight() == 1);

        chicken.setChickenBreastQuality(0.5);
        assertTrue(chicken.getChickBreastQuality() == 0.5);
        chicken.setChickenBreastQuality(2);
        assertTrue(chicken.getChickBreastQuality() == 1);
        chicken.setChickenBreastQuality(-1);
        assertTrue(chicken.getChickBreastQuality() == 0);

        chicken.setWeightIncreaseRate(0.5);
        assertTrue(chicken.getWeightIncreaseRate() == 0.5);
        chicken.setWeightIncreaseRate(2);
        assertTrue(chicken.getWeightIncreaseRate() == 1);
        chicken.setWeightIncreaseRate(-1);
        assertTrue(chicken.getWeightIncreaseRate() == 0);

        chicken.setEggQuantity(50);
        assertTrue(chicken.getEggQuantity() == 50);
        chicken.setEggQuantity(101);
        assertTrue(chicken.getEggQuantity() == 100);
        chicken.setEggQuantity(-1);
        assertTrue(chicken.getEggQuantity() == 0);

        chicken.setEggQuality(0.5);
        assertTrue(chicken.getEggQuality() == 0.5);
        chicken.setEggQuality(2);
        assertTrue(chicken.getEggQuality() == 1);
        chicken.setEggQuality(-1);
        assertTrue(chicken.getEggQuality() == 0);

        chicken.setFeatherQuality(0.5);
        assertTrue(chicken.getFeatherQuality() == 0.5);
        chicken.setFeatherQuality(2);
        assertTrue(chicken.getFeatherQuality() == 1);
        chicken.setFeatherQuality(-1);
        assertTrue(chicken.getFeatherQuality() == 0);

        chicken.setSpeed(0.5);
        assertTrue(chicken.getSpeed() == 0.5);
        chicken.setSpeed(200);
        assertTrue(chicken.getSpeed() == 100);
        chicken.setSpeed(-1);
        assertTrue(chicken.getSpeed() == 0);
        chicken.updateSpeed(0.2);
        assertTrue(chicken.getSpeed() == 0.2);

        chicken.setHealth(50);
        assertTrue(chicken.getHealth() == 50);
        chicken.setHealth(-1);
        assertTrue(chicken.getHealth() == 0);
        chicken.setHealth(101);
        assertTrue(chicken.getHealth() == 100);
    }
}
