package farmsim.entities.predatorTest;

import com.sun.javafx.tk.Toolkit;
import farmsim.Viewport;
import farmsim.ViewportNotSetException;
import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.entities.agents.AgentRoleTravellingSpriteSheets;
import farmsim.entities.predators.*;
import farmsim.tasks.TaskManager;
import farmsim.util.Point;
import farmsim.world.World;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import farmsim.world.WorldManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.*;

/**
 * Tests the predator class
 * 
 * @author r-portas
 */

@RunWith( PowerMockRunner.class )
@PrepareForTest( {TaskManager.class, WorldManager.class} )
public class PredatorTest {

    double speed = 10;
    int health = 30;
    //World world = mock(World.class);
    //Agent a1 = mock(Agent.class);
    Predator predator = new Predator(10, 10, health, speed, 100, "alligator");
    PredatorManager predManager = new PredatorManager();

    /**
     * Test the predator functionality
     */
    @Test
    public void testCreate() {
        // Check that the set of predators is empty
        assertNotNull("Predator is null", predator);
        assertEquals("Predator speed is incorrect", predator.getSpeed(), speed,
                0.0001);
        assertEquals("Predator health is incorrect", predator.getHealth(),
                health);
        assertEquals("Predator toString is incorrect", predator.toString(), "Predator(10.0, 10.0)");
    }
    
    /**
     * Tests the new predator manager spawn options
     */
    @Test
    public void testSpawn() {
    	predManager.removeAllPredators();
    	predManager.spawnPredator("wolf");
    	assertEquals(predManager.getPredatorCount(), 1);
    	predManager.spawnPredator("bear");
    	assertEquals(predManager.getPredatorCount(), 2);
    	predManager.spawnPredator("mole");
    	assertEquals(predManager.getPredatorCount(), 3);
    	predManager.removeAllPredators();
    }
    
    /*
     * Test with a fake viewport
     */
    @Test
    public void testFakeViewport() {
    	Viewport vp = mock(Viewport.class);
    	predManager.setViewport(vp);
 
        when(vp.getWidthTiles()).thenReturn(1);
        when(vp.getHeightTiles()).thenReturn(1);
        when(vp.getX()).thenReturn(5);
        when(vp.getY()).thenReturn(5);
        
        Point loc = predManager.getDebugSpawn();
        loc = predManager.getDebugSpawn();
        loc = predManager.getDebugSpawn();
        loc = predManager.getDebugSpawn();
        loc = predManager.getDebugSpawn();
        
    	predManager.setViewport(null);
    }
    
    /**
     * Test if the predator can be assigned locations to move to
     */
    @Test
    public void testDestination() {
        int x = 10;
        int y = 12;
        Point loc = new Point(x, y);

        predator.moveTo(x, y);

        assertEquals("Predator destination setting failed",
                predator.getDestination().toString(), loc.toString());
    }

    /**
     * Test that the predator was created at the correct location
     */
    @Test
    public void testLocation() {
        Point loc = new Point(10, 10);
        assertEquals("Predator not at correct location",
                predator.getLocation().toString(), loc.toString());
    }

    //*************************************************************************
    // Predator manager tests
    //*************************************************************************
    
    @Test
    public void managerInitialize(){
    	// Check that there is an empty set of predators
    	predManager = new PredatorManager();
    	assertTrue("Check that predator list is empty", predManager.getPredators().isEmpty());
    	
    	int timerCutoff = 20;
    	predManager.setTimer(timerCutoff);
    	assertEquals("Timer cutoff is set correctly", timerCutoff, predManager.getTimer());
    	
    	predManager.setWorldSize(100, 200);
    	assertEquals("Checking x", (int) predManager.getWorldSize().get(0), 100);
    	assertEquals("Checking y", (int) predManager.getWorldSize().get(1), 200);
    	
    	assertEquals("Checking predator count", predManager.getPredatorCount(), 0);
    	
    }

    @Test
    public void enableDisableTest(){
    	predManager.disablePredators();
    	assertEquals("Checking if predatorManager is disabled", predManager.getPredatorStatus(), 0);
    	predManager.enablePredators();
    	assertEquals("Checking if predatorManager is enabled", predManager.getPredatorStatus(), 1);
    }
    
    @Test
    public void managerAdd(){
    	predManager.createPredator(predator);
    	assertEquals("Checking predator count", predManager.getPredatorCount(), 1);
    	assertEquals("Checking for correct predator", new ArrayList<>(predManager.getPredators()).get(0), predator);
    	predManager.removePredator(predator);
    	assertTrue("Check that predator list is empty", predManager.getPredators().isEmpty());
    }

    @Test
    public void testPredatorCleanup(){
    	predManager = new PredatorManager();
    	Predator p = new BearPredator(1, 1, 100, 0.1);
    	predManager.createPredator(p);
    	assertEquals("Checking predator count", predManager.getPredatorCount(), 1);
    	Viewport view = mock(Viewport.class);
    	predManager.setViewport(view);
    	
    	try {
			predManager.tick();
		} catch (ViewportNotSetException e) {
		}
    	p.killPredator();
    	assertTrue("The predator flag is incorrect", p.canDestroy());
    }
    
    @Test
    public void testWolfPredator(){
    	WolfPredator p = new WolfPredator(2, 2, 100, 0.1);
    	p.moveTo(1, 1);
    	
    	for (int i = 0; i < 100; i++){
    		p.tick();
    	}
    }
    
    //*************************************************************************
    // Bear predator tests
    //*************************************************************************
    @Test
    public void testBearConstructor(){
        AgentManager am = mock(AgentManager.class);
    	//Agent agent = mock(Agent.class);
    	Point location = new Point(10, 10);
    	ArrayList<Agent> agents = new ArrayList<Agent>();
    	//when(agent.getHappiness()).thenReturn(5);
    	//agents.add(agent);
    	//when(agent.getLocation()).thenReturn(location);
        when(am.getAgents()).thenReturn(agents);
    	
    	BearPredator bear = new BearPredator(2, 2, 100, 0.1);
    	bear.overrideAgentManager(am);
    	bear.tick();
    	bear.overrideAttackCounter(200);
    	bear.tick();
    }
    
    //*************************************************************************
    //Alligator predator tests
    //*************************************************************************
    @Test
    public void testAlligatorConstructor() {
    	AlligatorPredator alligator = new AlligatorPredator(2, 2, 100, 0.1);
    	alligator.tick();
    	alligator.moveTo(50, 50);
    	Point destination = new Point(50,50);
    	assertTrue(alligator.getDestination().hashCode() == destination.hashCode());
    }

    //*************************************************************************
    //mole predator tests
    //*************************************************************************
    @Test
    public void testMoleConstructor() {
        MolePredator mole = new MolePredator(2, 2, 100, 0.1);
        mole.tick();
        mole.moveTo(1,1);
        Point destination = new Point(1,1);
        assertTrue(mole.getDestination().hashCode() == destination.hashCode());
    }
}
