package farmsim.world.generators;

import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.world.World;
import farmsim.world.terrain.Biomes;
import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class BasicWorldGeneratorTest extends TestCase {

	private Tile tile1;
	private Tile tile2;
	
	public void testConfigureWorld() throws Exception {
    	BasicWorldGenerator gen = new BasicWorldGenerator();
    	World world = new World(new String("test"), 50, 50, 2500, 123);
    	gen.configureWorld(world);
    	Assert.assertTrue(gen.currentWorld !=null);
    	Assert.assertEquals("configureWorld(world) not working",
                gen.currentWorld, world);
    }

    public void testConfigureWorld1() throws Exception {
    	BasicWorldGenerator gen = new BasicWorldGenerator();
    	World world = new World(new String("test"), 50, 50, 2500, 123);
    	gen.configureWorld(world);

        Assert.assertEquals("configureWorld(...) wrong name",
                gen.currentWorld.getName(), "test");
        Assert.assertEquals("configureWorld(...) wrong width",
                gen.currentWorld.getWidth(), 50);
        Assert.assertEquals("configureWorld(...) wrong height",
                gen.currentWorld.getHeight(), 50);
        Assert.assertEquals("configureWorld(...) wrong seed",
                gen.currentWorld.getSeed(), 123);
        this.tile1 = gen.currentWorld.getTile(10,10);
        Assert.assertEquals("getTile function error!.",tile1,gen.currentWorld.getTile(10,10));
    }

    public void testAddUnusable() throws Exception {

    }
    
    @Test 
    public void testAddRiver() throws Exception {
    	 BasicWorldGenerator gen = new BasicWorldGenerator();
    	 Assert.assertTrue("Error!! Current World is Null.",gen !=null);
  
    }

    @Test 
    public void testAddLakes() throws Exception {
    	 BasicWorldGenerator gen = new BasicWorldGenerator();
    	 Assert.assertTrue("Error!! Current World is Null.",gen !=null);

    }

    public void testSetBaseTileTypes() throws Exception {

    }

    public void testSetTileWaterLevel() throws Exception {
    	
    }

    public void testSetElevation() throws Exception {
    	 BasicWorldGenerator gen = new BasicWorldGenerator();
    	 World world = new World(new String("test"), 50, 50, 2500, 123);
    	 gen.configureWorld(world);
    	 this.tile1 = gen.currentWorld.getTile(10,10);
    	 Assert.assertTrue(gen.currentWorld !=null);
    	 Assert.assertEquals("Elevation Error!.",
    			 tile1.getProperty(TileProperty.ELEVATION),
    			 gen.currentWorld.getTile(10,10).getProperty(TileProperty.ELEVATION));
    }

    public void testSetMoisture() throws Exception {
    	
    }

    public void testUpdateMoisture() throws Exception {
    	 BasicWorldGenerator gen = new BasicWorldGenerator();
    	 World world = new World(new String("test"), 50, 50, 2500, 123);
    	 gen.configureWorld(world);
    	 this.tile2 = gen.currentWorld.getTile(20,20);
    	Assert.assertFalse("Tile Property Elevation is null",tile2.hasProperty(TileProperty.ELEVATION));
    }

    public void testSetBiomes() throws Exception {
    	BasicWorldGenerator gen = new BasicWorldGenerator();
    	World world = new World(new String("test"), 50, 50, 2500, 123);
    	gen.configureWorld(world);
    	Tile tile = gen.currentWorld.getTile(10, 10);
    	Assert.assertFalse("Tile Property BIOME is null",tile.hasProperty(TileProperty.BIOME));  
        Biomes biome = (Biomes) tile.getProperty(TileProperty.BIOME);
        Assert.assertEquals("Biome Error!.", biome,
   			 gen.currentWorld.getTile(10,10).getProperty(TileProperty.BIOME));
    }

    public void testAddBiomeEntities() throws Exception {
    	BasicWorldGenerator gen = new BasicWorldGenerator();
    	World world = new World(new String("test"), 50, 50, 2500, 123);
    	gen.configureWorld(world);
    	Tile tile = gen.currentWorld.getTile(10, 10);
    	Assert.assertFalse("Tile Property BIOME is null",tile.hasProperty(TileProperty.BIOME)); 
    	Biomes biome = (Biomes) tile.getProperty(TileProperty.BIOME);
    	Assert.assertTrue(biome!=Biomes.ROCKY||biome!=Biomes.ARID||biome!=Biomes.FOREST);
    }

    public void testAddSecretGold() throws Exception {

    }
}