package farmsim.entities.animal;

import static org.junit.Assert.*;

import farmsim.entities.animals.Sheep;
import farmsim.entities.animals.Duck;
import farmsim.world.WorldManager;
import org.junit.Test;
public class SheepTest {

    // Construct basic sheep for use in all tests
    public Sheep initSheep() {
        return new Sheep(WorldManager.getInstance().getWorld(), 10, 10, 0.06,
                0, 'f', 125);
    }

    // Test basic sheep construction + setting and getting attributes
    @Test
    public void initTest() {
        Sheep sheep = initSheep();

        assertTrue(sheep.getWorldX() == 10);
        assertTrue(sheep.getWorldY() == 10);
        assertTrue(sheep.getSpeed() == 0.06);
        assertTrue(sheep.getAge() == 0);
        assertTrue(sheep.getSex() == 'f');
        assertTrue(sheep.getWeight() == 125);

        sheep.setReadyToMate(true);
        sheep.setPregnant(false);
        sheep.setEdible(false);
        sheep.setShearable(false);
        sheep.setPregnant(true);

        assertTrue(sheep.getReadyToMate() == true);
        assertTrue(sheep.getEdible() == false);
        assertEquals(false, sheep.getShearable());
        assertEquals(true, sheep.getPregnant());

        sheep.setWeight(50);
        assertTrue(sheep.getWeight() == 50);
        sheep.setWeight(2001);
        assertTrue(sheep.getWeight() == 2000);
        sheep.setWeight(0);
        assertTrue(sheep.getWeight() == 1);

        sheep.setLambQuality(0.5);
        assertTrue(sheep.getLambQuality() == 0.5);
        sheep.setLambQuality(2);
        assertTrue(sheep.getLambQuality() == 1);
        sheep.setLambQuality(-1);
        assertTrue(sheep.getLambQuality() == 0);

        sheep.setWeightIncreaseRate(0.5);
        assertTrue(sheep.getWeightIncreaseRate() == 0.5);
        sheep.setWeightIncreaseRate(2);
        assertTrue(sheep.getWeightIncreaseRate() == 1);
        sheep.setWeightIncreaseRate(-1);
        assertTrue(sheep.getWeightIncreaseRate() == 0);

        sheep.setWoolQuantity(50);
        assertTrue(sheep.getWoolQuantity() == 50);
        sheep.setWoolQuantity(101);
        assertTrue(sheep.getWoolQuantity() == 100);
        sheep.setWoolQuantity(-1);
        assertTrue(sheep.getWoolQuantity() == 0);

        sheep.setWoolQuality(0.5);
        assertTrue(sheep.getWoolQuality() == 0.5);
        sheep.setWoolQuality(2);
        assertTrue(sheep.getWoolQuality() == 1);
        sheep.setWoolQuality(-1);
        assertTrue(sheep.getWoolQuality() == 0);

        sheep.setWoolProductionRate(0.5);
        assertTrue(sheep.getWoolProductionRate() == 0.5);
        sheep.setWoolProductionRate(2);
        assertTrue(sheep.getWoolProductionRate() == 1);
        sheep.setWoolProductionRate(-1);
        assertTrue(sheep.getWoolProductionRate() == 0);

        sheep.setSpeed(0.5);
        assertTrue(sheep.getSpeed() == 0.5);
        sheep.setSpeed(200);
        assertTrue(sheep.getSpeed() == 100);
        sheep.setSpeed(-1);
        assertTrue(sheep.getSpeed() == 0);
        sheep.updateSpeed(0.2);
        assertTrue(sheep.getSpeed() == 0.2);

        sheep.setHealth(50);
        assertTrue(sheep.getHealth() == 50);
        sheep.setHealth(-1);
        assertTrue(sheep.getHealth() == 0);
        sheep.setHealth(101);
        assertTrue(sheep.getHealth() == 100);
    }
}
