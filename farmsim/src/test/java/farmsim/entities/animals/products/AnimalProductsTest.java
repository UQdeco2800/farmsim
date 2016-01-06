package farmsim.entities.animals.products;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;
import farmsim.inventory.Inventory;
import farmsim.inventory.SimpleResourceHandler;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test for all the classes on the farmsim.entities.animals.process package.
 *
 * @author gjavi1 for Team Floyd
 */
public class AnimalProductsTest {

    /* Farm animals for the test */
    private Cow cow; // cow
    private Chicken chicken; // chicken
    private Duck duck; // duck
    private Sheep sheep; // sheep
    private Pig pig; // pig
    private Fish fish; // fish
    private Agent animalHandler;
    private SimpleResourceHandler product = SimpleResourceHandler.getInstance();
    private Inventory rucksack;
    private int aaa = 0;
    private int a, b;
    /**
     * Initialize test variables.
     */
    @Before
    public void setUp() {
        mockStatic(Agent.class);
        mockStatic(Inventory.class);
        animalHandler = mock(Agent.class);
        rucksack = new Inventory();
        /* Initialization of cows */
        cow = mock(Cow.class);
    	/* Initialization of chicken */
        chicken = mock(Chicken.class);
    	/* Initialization of duck */
        duck = mock(Duck.class);
    	/* Initialization of sheep */
        sheep = mock(Sheep.class);
    	/* Initialization of pig */
        pig = mock(Pig.class);
        fish = mock(Fish.class);
        when(animalHandler.getRucksack()).thenReturn(rucksack);
    }

    /**
     * Test for a bacon animal product.
     */
    @Test
    public void BaconTest() {
        Bacon bacon = new Bacon(pig,animalHandler);
        String expected = "Bacon" + pig.getIdentifier();
        assertTrue("Product should equal type + UID", bacon.getProductName()
                .equals(expected));
        a = animalHandler.getRucksack().getSize();
        bacon.produceProduct();
        b = animalHandler.getRucksack().getSize();
        assertTrue("Inventory size should change!", b > a);
    }

    /**
     * Test for a beef animal product.
     */
    @Test
    public void BeefTest() {
        Beef beef = new Beef(cow, animalHandler);
        String expected = "Beef" + cow.getIdentifier();
        assertTrue("Product should equal type + UID", beef.getProductName()
                .equals(expected));
        a = animalHandler.getRucksack().getSize();
        beef.produceProduct();
        b = animalHandler.getRucksack().getSize();
        assertTrue("Inventory size should change!", b > a);
    }

    /**
     * Test for a chicken breast animal product.
     */
    @Test
    public void ChickenBreastTest() {
        ChickenBreast chickenBreast = new ChickenBreast(chicken, animalHandler);
        String expected = "ChickenBreast" + chicken.getIdentifier();
        assertTrue("Product should equal type + UID", chickenBreast
                .getProductName().equals(expected));
        a = animalHandler.getRucksack().getSize();
        chickenBreast.produceProduct();
        b = animalHandler.getRucksack().getSize();
        assertTrue("Inventory size should change!", b > a);
    }

    /**
     * Test for a duck breast animal product.
     */
    @Test
    public void DuckBreastTest() {
        DuckBreast duckBreast = new DuckBreast(duck, animalHandler);
        String expected = "DuckBreast" + duck.getIdentifier();
        assertTrue("Product should equal type + UID", duckBreast
                .getProductName().equals(expected));
        a = animalHandler.getRucksack().getSize();
        duckBreast.produceProduct();
        b = animalHandler.getRucksack().getSize();
        assertTrue("Inventory size should change!", b > a);
    }

    /**
     * Test for a egg animal product.
     */
    @Test
    public void EggTestDuck() {
        Egg eggs = new Egg(duck, animalHandler);
        String expected = "Egg" + duck.getIdentifier();
        assertTrue("Product should equal type + UID", eggs.getProductName()
                .equals(expected));
        a = animalHandler.getRucksack().getSize();
        eggs.produceProduct();
        b = animalHandler.getRucksack().getSize();
        assertTrue("Inventory size should change!", b > a);
    }

    /**
     * Test for a egg animal product.
     */
    @Test
    public void EggTestChicken() {
        Egg egg = new Egg(chicken, animalHandler);
        String expectedChicken = "Egg" + chicken.getIdentifier();
        assertTrue("Product should equal type + UID", egg.getProductName()
                .equals(expectedChicken));
        a = animalHandler.getRucksack().getSize();
        egg.produceProduct();
        b = animalHandler.getRucksack().getSize();
        assertTrue("Inventory size should change!", b > a);
    }

    /**
     * Test for a feathers animal product.
     */
    @Test
    public void FeathersTestDuck() {
        Feathers eggs = new Feathers(duck, animalHandler);
        String expected = "Feathers" + duck.getIdentifier();
        assertTrue("Product should equal type + UID", eggs.getProductName()
                .equals(expected));
        a = animalHandler.getRucksack().getSize();
        eggs.produceProduct();
        b = animalHandler.getRucksack().getSize();
        assertTrue("Inventory size should change!", b > a);
    }

    /**
     * Test for a feathers animal product.
     */
    @Test
    public void FeathersTestChicken() {
        Feathers egg = new Feathers(chicken, animalHandler);
        String expectedChicken = "Feathers" + chicken.getIdentifier();
        assertTrue("Product should equal type + UID", egg.getProductName()
                .equals(expectedChicken));
        a = animalHandler.getRucksack().getSize();
        egg.produceProduct();
        b = animalHandler.getRucksack().getSize();
        assertTrue("Inventory size should change!", b > a);
    }

    /**
     * Test for a lamb animal product.
     */
    @Test
    public void LambTest() {
        Lamb lamb = new Lamb(sheep, animalHandler);
        String expected = "Lamb" + sheep.getIdentifier();
        assertTrue("Product should equal type + UID", lamb.getProductName()
                .equals(expected));
        a = animalHandler.getRucksack().getSize();
        lamb.produceProduct();
        b = animalHandler.getRucksack().getSize();
        assertTrue("Inventory size should change!", b > a);
    }

    /**
     * Test for a milk animal product.
     */
    @Test
    public void MilkTest() {
        Milk milk = new Milk(cow, animalHandler);
        String expected = "Milk" + cow.getIdentifier();
        assertTrue("Product should equal type + UID", milk.getProductName()
                .equals(expected));
        a = animalHandler.getRucksack().getSize();
        milk.produceProduct();
        b = animalHandler.getRucksack().getSize();
        assertTrue("Inventory size should change!", b > a);
    }

    /**
     * Test for a wool animal product.
     */
    @Test
    public void WoolTest() {
        Wool wool = new Wool(sheep, animalHandler);
        String expected = "Wool" + sheep.getIdentifier();
        assertTrue("Product should equal type + UID", wool.getProductName()
                .equals(expected));
        a = animalHandler.getRucksack().getSize();
        wool.produceProduct();
        b = animalHandler.getRucksack().getSize();
        assertTrue("Inventory size should change!", b > a);
    }

    /**
     * Test for a tuna animal product.
     */
    @Test
    public void TunaTest() {
        Tuna tuna = new Tuna(fish, animalHandler);
        String expected = "Tuna" + sheep.getIdentifier();
        assertTrue("Product should equal type + UID", tuna.getProductName()
                .equals(expected));
        a = animalHandler.getRucksack().getSize();
        tuna.produceProduct();
        b = animalHandler.getRucksack().getSize();
        assertTrue("Inventory size should change!", b > a);
    }
}
