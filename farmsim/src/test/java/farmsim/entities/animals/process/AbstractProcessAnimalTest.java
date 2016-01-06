package farmsim.entities.animals.process;

import farmsim.entities.agents.Agent;
import farmsim.entities.animals.*;
import farmsim.inventory.Inventory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test for all the classes on the farmsim.entities.animals.process package.
 *
 * @author gjavi1 for Team Floyd
 */
public class AbstractProcessAnimalTest {
    //Exit status for an invalid process
    protected static int PROCESS_INVALID = -1;
    //Exit status for a successful process
    private static int PROCESS_SUCCESS = 0;
    //Exit status for an unsuccessful process
    private static int PROCESS_FAILED = 1;
    //Exit status for an unsuccessful process
    private static int PROCESS_LOW_LEVEL = 2;

    /* Agents for the test */
    private Agent animalHandler; // worker
    private Agent.RoleType butcher = Agent.RoleType.BUTCHER;
    private Agent.RoleType shearer = Agent.RoleType.SHEARER;
    private Agent.RoleType milker = Agent.RoleType.MILKER;
    private Agent.RoleType eggCollector = Agent.RoleType.EGG_HANDLER;
    private Agent.RoleType farmer = Agent.RoleType.FARMER;

    /* Farm animals for the test */
    private Cow cow; //young cow
    private Cow cowAdult; //adult cow
    private Cow cowOld; //old cow

    private Chicken chicken; //young chicken
    private Chicken chickenAdult; //adult chicken
    private Chicken chickenOld; //old chicken

    private Duck duck; //young duck
    private Duck duckAdult; //adult duck
    private Duck duckOld; //old duck

    private Sheep sheep; //young sheep
    private Sheep sheepAdult; //adult sheep
    private Sheep sheepOld; //old sheep

    private Pig pig; //young pig
    private Pig pigAdult; //adult pig
    private Pig pigOld; //old pig

    private Fish fish;
    private Fish fishAdult;
    private Fish fishOld;

    /* Process for farm animals */
    private ProcessCow cowProducts;
    private ProcessPig pigProducts;
    private ProcessChicken chickenProducts;
    private ProcessDuck duckProducts;
    private ProcessSheep sheepProducts;
    private ProcessFish fishProducts;

    /* Inventory to the output items */
    private Inventory rucksack;
    /**
     * Initialize test variables.
     */
    @Before
    public void setUp() {
        mockStatic(Agent.class);
        animalHandler = mock(Agent.class);
        
        /* Initialization of cows */
        cow = mock(Cow.class);
        cowAdult = new Cow(null, 0, 0, 0, 55.0, (char) 0, 0);
        cowOld = new Cow(null, 0, 0, 0, 80.0, (char) 0, 0);

    	/* Initialization of chicken */
        chicken = mock(Chicken.class);
        chickenAdult = new Chicken(null, 0, 0, 0, 55.0, (char) 0, 0);
        chickenOld = new Chicken(null, 0, 0, 0, 80.0, (char) 0, 0);

    	/* Initialization of duck */
        duck = mock(Duck.class);
        duckAdult = new Duck(null, 0, 0, 0, 55.0, (char) 0, 0);
        duckOld = new Duck(null, 0, 0, 0, 80.0, (char) 0, 0);
    	
    	/* Initialization of sheep */
        sheep = mock(Sheep.class);
        sheepAdult = new Sheep(null, 0, 0, 0, 55.0, (char) 0, 0);
        sheepOld = new Sheep(null, 0, 0, 0, 80.0, (char) 0, 0);
    	
    	/* Initialization of pig */
        pig = mock(Pig.class);
        pigAdult = new Pig(null, 0, 0, 0, 55.0, (char) 0, 0);
        pigOld = new Pig(null, 0, 0, 0, 80.0, (char) 0, 0);

        fish = mock(Fish.class);
        fishAdult = mock(Fish.class);
        fishOld = mock(Fish.class);

        rucksack = new Inventory();
        when(animalHandler.getRucksack()).thenReturn(rucksack);
        when(fishAdult.getAge()).thenReturn(60.5);
        when(fishOld.getAge()).thenReturn(70.0);
    }

    /**
     * Method for initializing the level of animal handlers.
     */
    public void setRolesToLevel(int level) {
        when(animalHandler.getLevelForRole(Agent.RoleType.SHEARER)).thenReturn
                (level);
        when(animalHandler.getLevelForRole(Agent.RoleType.MILKER)).thenReturn
                (level);
        when(animalHandler.getLevelForRole(Agent.RoleType.EGG_HANDLER)).
                thenReturn(level);
        when(animalHandler.getLevelForRole(Agent.RoleType.BUTCHER)).thenReturn
                (level);
    }

    /**
     * Test when the agent level is 1 and process number is a positive integer
     * < 4 for ProcessChicken
     */
    @Test
    public void testProcessChickenWithLevel1Agent() {
        setRolesToLevel(1);
        for (int i = 0; i < 15; i++) {
            chickenProducts = new ProcessChicken(chicken, animalHandler,
                    butcher);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, chickenProducts.getProducts());
            }
            chickenProducts = new ProcessChicken(chicken, animalHandler,
                    eggCollector);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, chickenProducts.getProducts());
            }
            chickenProducts = new ProcessChicken(chicken, animalHandler,
                    shearer);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, chickenProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 2 and process number is a positive integer
     * < 4 for ProcessChicken
     */
    @Test
    public void testProcessChickenWithLevel2Agent() {
        setRolesToLevel(2);
        for (int i = 0; i < 15; i++) {
            chickenProducts = new ProcessChicken(chickenAdult, animalHandler,
                    butcher);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, chickenProducts.getProducts());
            }
            chickenProducts = new ProcessChicken(chicken, animalHandler,
                    eggCollector);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, chickenProducts.getProducts());
            }
            chickenProducts = new ProcessChicken(chicken, animalHandler,
                    shearer);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, chickenProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 3 and process number is a positive integer
     * < 4 for ProcessChicken
     */
    @Test
    public void testProcessChickenWithLevel3Agent() {
        setRolesToLevel(3);

        for (int i = 0; i < 15; i++) {
            chickenProducts = new ProcessChicken(chickenAdult, animalHandler,
                    eggCollector);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, chickenProducts.getProducts());
            }
            chickenProducts = new ProcessChicken(chicken, animalHandler,
                    eggCollector);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, chickenProducts.getProducts());
            }
            chickenProducts = new ProcessChicken(chicken, animalHandler,
                    shearer);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, chickenProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 4 and process number is a positive integer
     * < 4 for ProcessChicken
     */
    @Test
    public void testProcessChickenWithLevel4Agent() {
        setRolesToLevel(4);
        for (int i = 0; i < 15; i++) {
            chickenProducts = new ProcessChicken(chickenOld, animalHandler,
                    eggCollector);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, chickenProducts.getProducts());
            }
            chickenProducts = new ProcessChicken(chicken, animalHandler,
                    eggCollector);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, chickenProducts.getProducts());
            }
            chickenProducts = new ProcessChicken(chicken, animalHandler,
                    shearer);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, chickenProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 5 and process number is a positive integer
     * < 4 for ProcessChicken
     */
    @Test
    public void testProcessChickenWithLevel5Agent() {
        setRolesToLevel(5);
        for (int i = 0; i < 15; i++) {
            chickenProducts = new ProcessChicken(chickenOld, animalHandler,
                    butcher);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, chickenProducts.getProducts());
            }
            chickenProducts = new ProcessChicken(chicken, animalHandler,
                    eggCollector);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, chickenProducts.getProducts());
            }
            chickenProducts = new ProcessChicken(chicken, animalHandler,
                    shearer);
            if (chickenProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, chickenProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, chickenProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 1 and process number is a positive
     * integer < 3 for ProcessCow
     */
    @Test
    public void testProcessCowWithLevel1Agent() {
        setRolesToLevel(1);
        for (int i = 0; i < 15; i++) {
            cowProducts = new ProcessCow(cow, animalHandler, butcher);
            if (cowProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, cowProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, cowProducts.getProducts());
            }
            cowProducts = new ProcessCow(cow, animalHandler, milker);
            if (cowProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, cowProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, cowProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 2 and process number is a positive
     * integer < 3 for ProcessCow
     */
    @Test
    public void testProcessCowWithLevel2Agent() {
        setRolesToLevel(2);
        for (int i = 0; i < 15; i++) {
            cowProducts = new ProcessCow(cowOld, animalHandler, butcher);
            if (cowProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, cowProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, cowProducts.getProducts());
            }
            cowProducts = new ProcessCow(cowOld, animalHandler, milker);
            if (cowProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, cowProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, cowProducts.getProducts());
            }

        }
    }

    /**
     * Test when the agent level is 3 and process number is a positive
     * integer < 3 for ProcessCow
     */
    @Test
    public void testProcessCowWithLevel3Agent() {
        setRolesToLevel(3);
        for (int i = 0; i < 15; i++) {
            cowProducts = new ProcessCow(cowOld, animalHandler, butcher);
            if (cowProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, cowProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, cowProducts.getProducts());
            }
            cowProducts = new ProcessCow(cowOld, animalHandler, milker);
            if (cowProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, cowProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, cowProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 4 and process number is a positive
     * integer < 3 for ProcessCow
     */
    @Test
    public void testProcessCowWithLevel4Agent() {
        setRolesToLevel(4);
        for (int i = 0; i < 15; i++) {
            cowProducts = new ProcessCow(cowAdult, animalHandler, butcher);
            if (cowProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, cowProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, cowProducts.getProducts());
            }
            cowProducts = new ProcessCow(cowAdult, animalHandler, milker);
            if (cowProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, cowProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, cowProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 5 and process number is a positive
     * integer < 3 for ProcessCow
     */
    @Test
    public void testProcessCowWithLevel5Agent() {
        setRolesToLevel(5);
        for (int i = 0; i < 15; i++) {
            cowProducts = new ProcessCow(cowAdult, animalHandler, butcher);
            if (cowProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, cowProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, cowProducts.getProducts());
            }
            cowProducts = new ProcessCow(cowAdult, animalHandler, milker);
            if (cowProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, cowProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, cowProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 1 and process number is a positive
     * integer < 4 for ProcessDuck
     */
    @Test
    public void testProcessDuckWithLevel1Agent() {
        setRolesToLevel(1);
        for (int i = 0; i < 15; i++) {
            duckProducts = new ProcessDuck(duck, animalHandler, butcher);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, duckProducts.getProducts());
            }
            duckProducts = new ProcessDuck(duck, animalHandler, eggCollector);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, duckProducts.getProducts());
            }
            duckProducts = new ProcessDuck(duck, animalHandler, shearer);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, duckProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 2 and process number is a positive
     * integer < 4 for ProcessDuck
     */
    @Test
    public void testProcessDuckWithLevel2Agent() {
        setRolesToLevel(2);
        for (int i = 0; i < 15; i++) {
            duckProducts = new ProcessDuck(duckAdult, animalHandler,
                    butcher);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, duckProducts.getProducts());
            }
            duckProducts = new ProcessDuck(duckAdult, animalHandler,
                    eggCollector);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, duckProducts.getProducts());
            }
            duckProducts = new ProcessDuck(duckAdult, animalHandler,
                    shearer);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, duckProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 3 and process number is a positive
     * integer < 4 for ProcessDuck
     */
    @Test
    public void testProcessDuckWithLevel3Agent() {
        setRolesToLevel(3);
        for (int i = 0; i < 15; i++) {
            duckProducts = new ProcessDuck(duckAdult, animalHandler,
                    butcher);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, duckProducts.getProducts());
            }
            duckProducts = new ProcessDuck(duckAdult, animalHandler,
                    eggCollector);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, duckProducts.getProducts());
            }
            duckProducts = new ProcessDuck(duckAdult, animalHandler,
                    shearer);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, duckProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 4 and process number is a positive
     * integer < 4 for ProcessDuck
     */
    @Test
    public void testProcessDuckWithLevel4Agent() {
        setRolesToLevel(4);
        for (int i = 0; i < 15; i++) {
            duckProducts = new ProcessDuck(duckOld, animalHandler,
                    butcher);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, duckProducts.getProducts());
            }
            duckProducts = new ProcessDuck(duckOld, animalHandler,
                    eggCollector);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, duckProducts.getProducts());
            }
            duckProducts = new ProcessDuck(duckOld, animalHandler,
                    shearer);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, duckProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 5 and process number is a positive
     * integer < 4 for ProcessDuck
     */
    @Test
    public void testProcessDuckWithLevel5Agent() {
        setRolesToLevel(5);
        for (int i = 0; i < 15; i++) {
            duckProducts = new ProcessDuck(duckOld, animalHandler,
                    butcher);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, duckProducts.getProducts());
            }
            duckProducts = new ProcessDuck(duckOld, animalHandler,
                    eggCollector);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, duckProducts.getProducts());
            }
            duckProducts = new ProcessDuck(duckOld, animalHandler,
                    shearer);
            if (duckProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, duckProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, duckProducts.getProducts());
            }
        }
    }


    /**
     * Test when the agent level is 1 and process number is a positive
     * integer < 2 for ProcessPig
     */
    @Test
    public void testProcessPigWithLevel1Agent() {
        setRolesToLevel(1);
        for (int i = 0; i < 15; i++) {
            pigProducts = new ProcessPig(pig, animalHandler,
                    butcher);
            if (pigProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, pigProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, pigProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 2 and process number is a positive
     * integer < 2 for ProcessPig
     */
    @Test
    public void testProcessPigWithLevel2Agent() {
        setRolesToLevel(2);
        for (int i = 0; i < 15; i++) {
            pigProducts = new ProcessPig(pigOld, animalHandler,
                    butcher);
            if (pigProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, pigProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, pigProducts.getProducts());
            }

        }
    }

    /**
     * Test when the agent level is 3 and process number is a positive
     * integer < 2 for ProcessPig
     */
    @Test
    public void testProcessPigWithLevel3Agent() {
        setRolesToLevel(3);
        for (int i = 0; i < 15; i++) {
            pigProducts = new ProcessPig(pigOld, animalHandler,
                    butcher);
            if (pigProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, pigProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, pigProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 4 and process number is a positive
     * integer < 2 for ProcessPig
     */
    @Test
    public void testProcessPigWithLevel4Agent() {
        setRolesToLevel(4);
        for (int i = 0; i < 15; i++) {
            pigProducts = new ProcessPig(pigAdult, animalHandler,
                    butcher);
            if (pigProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, pigProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, pigProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 5 and process number is a positive
     * integer < 2 for ProcessPig
     */
    @Test
    public void testProcessPigWithLevel5Agent() {
        setRolesToLevel(5);
        for (int i = 0; i < 15; i++) {
            pigProducts = new ProcessPig(pigAdult, animalHandler,
                    butcher);
            if (pigProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, pigProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, pigProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 1 and process number is a positive
     * integer < 3 for ProcessSheep
     */
    @Test
    public void testProcessSheepWithLevel1Agent() {
        setRolesToLevel(1);
        for (int i = 0; i < 15; i++) {
            sheepProducts = new ProcessSheep(sheep, animalHandler, butcher);
            if (sheepProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, sheepProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, sheepProducts.getProducts());
            }
            sheepProducts = new ProcessSheep(sheep, animalHandler, shearer);
            if (sheepProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, sheepProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, sheepProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 2 and process number is a positive
     * integer < 3 for ProcessSheep
     */
    @Test
    public void testProcessSheepWithLevel2Agent() {
        setRolesToLevel(2);
        for (int i = 0; i < 15; i++) {
            sheepProducts = new ProcessSheep(sheepAdult, animalHandler,
                    butcher);
            if (sheepProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, sheepProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, sheepProducts.getProducts());
            }
            sheepProducts = new ProcessSheep(sheepAdult, animalHandler,
                    shearer);
            if (sheepProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, sheepProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, sheepProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 3 and process number is a positive
     * integer < 3 for ProcessSheep
     */
    @Test
    public void testProcessSheepWithLevel3Agent() {
        setRolesToLevel(3);
        for (int i = 0; i < 15; i++) {
            sheepProducts = new ProcessSheep(sheepAdult, animalHandler,
                    butcher);
            if (sheepProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, sheepProducts.getProducts());
            } else {
                assertEquals(PROCESS_LOW_LEVEL, sheepProducts.getProducts());
            }
            sheepProducts = new ProcessSheep(sheepAdult, animalHandler,
                    shearer);
            if (sheepProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, sheepProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, sheepProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 4 and process number is a positive
     * integer < 3 for ProcessSheep
     */
    @Test
    public void testProcessSheepWithLevel4Agent() {
        setRolesToLevel(4);
        for (int i = 0; i < 15; i++) {
            sheepProducts = new ProcessSheep(sheepOld, animalHandler, butcher);
            if (sheepProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, sheepProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, sheepProducts.getProducts());
            }
            sheepProducts = new ProcessSheep(sheepOld, animalHandler, shearer);
            if (sheepProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, sheepProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, sheepProducts.getProducts());
            }
        }
    }

    /**
     * Test when the agent level is 5 and process number is a positive
     * integer < 3 for ProcessSheep
     */
    @Test
    public void testProcessSheepWithLevel5Agent() {
        setRolesToLevel(5);
        for (int i = 0; i < 15; i++) {
            sheepProducts = new ProcessSheep(sheepOld, animalHandler, butcher);
            if (sheepProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, sheepProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, sheepProducts.getProducts());
            }
            sheepProducts = new ProcessSheep(sheepOld, animalHandler, shearer);
            if (sheepProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, sheepProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, sheepProducts.getProducts());
            }
        }
    }

    /**
     * Test if each process initializes without errors.
     */
    @Test
    public void testAllAnimalProcess() {
        ProcessingFactory.startProcess(chicken, animalHandler, butcher);
        ProcessingFactory.startProcess(cow, animalHandler, butcher);
        ProcessingFactory.startProcess(duck, animalHandler, butcher);
        ProcessingFactory.startProcess(pig, animalHandler, butcher);
        ProcessingFactory.startProcess(sheep, animalHandler, butcher);
        ProcessingFactory.startProcess(fish, animalHandler, butcher);
        ProcessingFactory.startProcess(null, animalHandler, butcher);
    }

    @Test
    public void testFactory() {

        assertTrue("Expecting null got different class", ProcessingFactory
                .startProcess(null, animalHandler, butcher) == null);
    }

    /**
     * Test invalid chicken process.
     */
    @Test
    public void testInvalidAnimalProcessChicken() {
        setRolesToLevel(1);
        for (int i = 0; i < 15; i++) {
            chickenProducts = new ProcessChicken(chicken, animalHandler,
                    farmer);
            if (chickenProducts.getProbabilitySuccess() != 0) {
                assertEquals(PROCESS_INVALID, chickenProducts.getProducts());
            }
        }
    }

    /**
     * Test invalid cow process.
     */
    @Test
    public void testInvalidAnimalProcessCow() {
        setRolesToLevel(1);
        for (int i = 0; i < 15; i++) {
            cowProducts = new ProcessCow(cow, animalHandler, farmer);
            if (cowProducts.getProbabilitySuccess() != 0) {
                assertEquals(PROCESS_INVALID, cowProducts.getProducts());
            }
        }
    }

    /**
     * Test invalid duck process.
     */
    @Test
    public void testInvalidAnimalProcessDuck() {
        setRolesToLevel(1);
        setRolesToLevel(1);
        for (int i = 0; i < 15; i++) {
            duckProducts = new ProcessDuck(duck, animalHandler, farmer);
            if (duckProducts.getProbabilitySuccess() != 0) {
                assertEquals(PROCESS_INVALID, duckProducts.getProducts());
            }
        }
    }

    /**
     * Test invalid pig process.
     */
    @Test
    public void testInvalidAnimalProcessPig() {
        setRolesToLevel(1);
        for (int i = 0; i < 15; i++) {
            pigProducts = new ProcessPig(pig, animalHandler, farmer);
            if (pigProducts.getProbabilitySuccess() != 0) {
                assertEquals(PROCESS_INVALID, pigProducts.getProducts());
            }
        }
    }

    /**
     * Test invalid sheep process.
     */
    @Test
    public void testInvalidAnimalProcessSheep() {
        setRolesToLevel(1);
        for (int i = 0; i < 15; i++) {
            sheepProducts = new ProcessSheep(sheep, animalHandler, farmer);
            if (sheepProducts.getProbabilitySuccess() != 0) {
                assertEquals(PROCESS_INVALID, sheepProducts.getProducts());
            }
        }
    }

    @Test
    public void testProcessFishWithLevel1Agent() {
        setRolesToLevel(1);
        for (int i = 0; i < 15; i++) {
            fishProducts = new ProcessFish(fishAdult, animalHandler, farmer);
            if (fishProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, fishProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, fishProducts.getProducts());
            }
        }
    }

    @Test
    public void testProcessFishWithLevel2Agent() {
        setRolesToLevel(2);
        for (int i = 0; i < 15; i++) {
            fishProducts = new ProcessFish(fishOld, animalHandler, farmer);
            if (fishProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, fishProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, fishProducts.getProducts());
            }
        }
    }

    @Test
    public void testProcessFishWithLevel3Agent() {
        setRolesToLevel(3);
        for (int i = 0; i < 15; i++) {
            fishProducts = new ProcessFish(fishOld, animalHandler, farmer);
            if (fishProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, fishProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, fishProducts.getProducts());
            }
        }
    }

    @Test
    public void testProcessFishWithLevel4Agent() {
        setRolesToLevel(4);
        for (int i = 0; i < 15; i++) {
            fishProducts = new ProcessFish(fishOld, animalHandler, farmer);
            if (fishProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, fishProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, fishProducts.getProducts());
            }
        }
    }

    @Test
    public void testProcessFishWithLevel5Agent() {
        setRolesToLevel(5);
        for (int i = 0; i < 15; i++) {
            fishProducts = new ProcessFish(fishOld, animalHandler, farmer);
            if (fishProducts.getProbabilitySuccess() == 0) {
                assertEquals(PROCESS_FAILED, fishProducts.getProducts());
            } else {
                assertEquals(PROCESS_SUCCESS, fishProducts.getProducts());
            }
        }
    }

    /**
     * Test invalid fish process.
     */
    @Test
    public void testInvalidAnimalProcessFish() {
        setRolesToLevel(1);
        for (int i = 0; i < 15; i++) {
            fishProducts = new ProcessFish(fish, animalHandler, butcher);
            if (fishProducts.getProbabilitySuccess() != 0) {
                assertEquals(PROCESS_INVALID, fishProducts.getProducts());
            }
        }
    }
}
