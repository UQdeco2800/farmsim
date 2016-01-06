package farmsim.entities.disease;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


import farmsim.entities.agents.*;
import farmsim.entities.tileentities.crops.Apple;
import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.world.terrain.Biomes;

public class DiseaseTests {

    private Agent a1;
    private Agent a2;
    private Agent a3;
    private Agent a4;
    private Apple apple1;
    private Tile t1;
    private Tile t2;
    private BlackPlague b1;
    private Influenza b2;
    private Measles b4;

    @Before
    public void setUp() throws Exception {
        a1 = new Agent("Joe", 0, 0, 2);
        a1.calculateOrientationToTask();
        a2 = new Agent("Bloggs", 0, 1, 2);
        a2.calculateOrientationToTask();
        a3 = new Agent("Jack", 0, 2, 2);
		a4 = new Agent("Mr. Sniffles", 10, 10, 2);
		a4.calculateOrientationToTask();
		
		t1 = new Tile(0, 0, 0);
		t1.setProperty(TileProperty.BIOME, Biomes.GRASSLAND);
		apple1 = new Apple(t1);
		

        b1 = new BlackPlague();
        b2 = new Influenza();
        b4 = new Measles();
        
    }

    @After
    public void tearDown() throws Exception {
        a1 = null;
        a2 = null;
        a3 = null;
        a4 = null;
        apple1 = null;
        b1 = null;
        b2 = null;
        b4 = null;
        
    }
    
    /**
     * Test infection of crop
     */
    @Test
    public void testInfectCrop() {
    	assertFalse("Check crop is healthy",
    			apple1.isDiseased());
    	apple1.infect(new Blight());
    	assertTrue("Check apple is now infected", 
    			apple1.isDiseased());
    	
    }
    
    /**
     * Test create guaranteed pest.
     */
    @Test
    public void testCreateGuaranteedCropPest() {
    	apple1.createDisease(Double.MAX_VALUE);
    	assertTrue("Check disease created",
    			apple1.isDiseased());
    }
    
    /**
     * Test create impossible pest.
     */
    @Test
    public void testImpossibleCropPest() {
    	apple1.createDisease(Double.MIN_VALUE);
    	assertFalse("Check disease created",
    			apple1.isDiseased());
    }
    
    /**
     * Test spread of illness.
     */
    @Test
    @Ignore
	public void testSpread() {
    	AgentManager.getInstance().addAgent(a1);
 		AgentManager.getInstance().addAgent(a2);
 		AgentManager.getInstance().addAgent(a4);
		assertFalse("Check neighbouring agent is clean",
				a2.isDiseased());
		a1.infect(b1);
		a1.spreadDisease(true);
		assertTrue("Check neighbouring agent is now infected",
				a2.isDiseased());
		assertFalse("Check distant agent is not infected",
				a4.isDiseased());
	}
    
    /**
     * Test spread of non sick worker.
     */
    @Test
    @Ignore
    public void testNoSpread() {
    	AgentManager.getInstance().addAgent(a1);
 		AgentManager.getInstance().addAgent(a2);
 		AgentManager.getInstance().addAgent(a4);
    	assertFalse("Check agent is healthy", 
    			a1.isDiseased());
    	a1.spreadDisease(true);
    	assertFalse("Check neighbouring agent didn't catch disease from nothing", 
    			a2.isDiseased());
    }
    
    
    /**
     * Test creating disease.
     */
    @Test
    public void testGuaranteedCreate() {
    	a1.createDisease(Double.MAX_VALUE);
    	assertTrue("Check disease created",
    			a1.isDiseased());
    }
    
    /**
     * Test creating disease.
     */
    @Test
    public void testImpossibleCreate() {
    	a1.createDisease(Double.MIN_VALUE);
    	assertFalse("Check disease created",
    			a1.isDiseased());
    }
    
    /**
     * Test Black plague characteristics
     */
	@Test
	public void testBlackPlague() {
		BlackPlague plague = new BlackPlague();
		assertEquals("Check default contagiousness",
				plague.getContagiousness(), 50);
		assertEquals("Check default severity",
				plague.getSeverity(), 50);
		assertEquals("Check default lifetime",
				plague.getLifetime(), 10);
		assertEquals("Check name is correct", 
				plague.getName(), "BlackPlague");
	}
	
	/**
	 * Test Blight characteristics
	 */
	@Test
	public void testBlight() {
		Blight plague = new Blight();
		assertEquals("Check default contagiousness",
				plague.getContagiousness(), 50);
		assertEquals("Check default severity",
				plague.getSeverity(), 50);
		assertEquals("Check default lifetime",
				plague.getLifetime(), 100);
		assertEquals("Check name is correct", 
				plague.getName(), "Blight");
	}
	
	/**
	 * Test Influenza characteristics
	 */
	@Test
	public void testInfluenza() {
		Influenza plague = new Influenza();
		assertEquals("Check default contagiousness",
				plague.getContagiousness(), 50);
		assertEquals("Check default severity",
				plague.getSeverity(), 30);
		assertEquals("Check default lifetime",
				plague.getLifetime(), 10);
		assertEquals("Check name is correct", 
				plague.getName(), "Influenza");
	}
	
	/**
	 * Test Sars characteristics
	 */
	@Test
	public void testSars() {
		Sars plague = new Sars();
		assertEquals("Check default contagiousness",
				plague.getContagiousness(), 90);
		assertEquals("Check default severity",
				plague.getSeverity(), 60);
		assertEquals("Check default lifetime",
				plague.getLifetime(), 15);
		assertEquals("Check name is correct", 
				plague.getName(), "Sars");
	}
	
	/**
	 * Test Measles characteristics
	 */
	@Test
	public void testMeasles() {
		Measles uqAdvantage = new Measles();
		assertEquals("Check default severity",
				uqAdvantage.getSeverity(), 70);
		assertEquals("Check default contagiousness",
				uqAdvantage.getContagiousness(), 20);
		assertEquals("Check default lifetime",
				uqAdvantage.getLifetime(), 8);
		assertEquals("Check name is correct", 
				uqAdvantage.getName(), "Measles");
	}
}
