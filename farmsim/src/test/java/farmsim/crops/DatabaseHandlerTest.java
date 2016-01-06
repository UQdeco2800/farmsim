
package farmsim.crops;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test; 

import static org.mockito.Mockito.*;

import farmsim.entities.tileentities.crops.Apple;
import farmsim.entities.tileentities.crops.Banana;
import farmsim.entities.tileentities.crops.DatabaseHandler;
import farmsim.entities.tileentities.crops.Lemon;
import farmsim.entities.tileentities.crops.Lettuce;
import farmsim.entities.tileentities.crops.Mango;
import farmsim.entities.tileentities.crops.Pear;
import farmsim.entities.tileentities.crops.SugarCane;
import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;

import static org.junit.Assert.*;

/**
 * Test that the properties of two of the plants are retrieved from the
 * database correctly and checks that all of the expected plants are in the
 * database.
 * @author Blake
 *
 */
public class DatabaseHandlerTest {
    /**
     * Checks that the apple and lemon data was correctly retrieved.
     * @throws Exception
     */
    @Test
    public void importTest() { 
        DatabaseHandler.getInstance().importData();
        HashMap<String, Object> expectedAppleProperties = new HashMap<String, Object>();
        expectedAppleProperties.put("Clevel", 1); 
        expectedAppleProperties.put("Gtime1", 0);
        expectedAppleProperties.put("Gtime2", 320000);
        expectedAppleProperties.put("Gtime3", 620000); 
        expectedAppleProperties.put("Gtime4", 960000);
        expectedAppleProperties.put("Gtime5", 1440000);
        expectedAppleProperties.put("Hquantity", 8);
        expectedAppleProperties.put("StNames1", "seed"); 
        expectedAppleProperties.put("StNames2", "Apple1");
        expectedAppleProperties.put("StNames3", "Apple2");
        expectedAppleProperties.put("StNames4", "Apple3");
        expectedAppleProperties.put("DeadStage1", "Apple1dead");
        expectedAppleProperties.put("DeadStage2", "Apple2dead");
        expectedAppleProperties.put("DeadStage3", "Apple3dead");
        expectedAppleProperties.put("Penvironment", "FOREST");
        expectedAppleProperties.put("Nenvironment", "ROCKY");
        Map<String, Object> actualAppleProperties = 
                DatabaseHandler.getInstance().getPlantData("Apple");
        ArrayList<String> keys = new ArrayList<>();
        keys.add("Clevel");
        keys.add("Gtime1");
        keys.add("Gtime2");
        keys.add("Gtime3");
        keys.add("Gtime4");
        keys.add("Gtime5");
        keys.add("Hquantity");
        keys.add("StNames1");
        keys.add("StNames2");
        keys.add("StNames3");
        keys.add("StNames4");
        keys.add("DeadStage");
        keys.add("Penvironment");
        keys.add("Nenvironment");
        for(int i = 0; i < 13; i++) {
            assertEquals(expectedAppleProperties.get(keys.get(i)),
                    actualAppleProperties.get(keys.get(i)));
        }
        HashMap<String, Object> expectedLemonProperties = new HashMap<String, Object>();
        expectedLemonProperties.put("Clevel", 1); 
        expectedLemonProperties.put("Gtime1", 0);
        expectedLemonProperties.put("Gtime2", 560000);
        expectedLemonProperties.put("Gtime3", 1200000); 
        expectedLemonProperties.put("Gtime4", 1680000);
        expectedLemonProperties.put("Gtime5", 2520000);
        expectedLemonProperties.put("Hquantity", 14);
        expectedLemonProperties.put("StNames1", "seed"); 
        expectedLemonProperties.put("StNames2", "Lemon1");
        expectedLemonProperties.put("StNames3", "Lemon2");
        expectedLemonProperties.put("StNames4", "Lemon3");
        expectedLemonProperties.put("DeadStage1", "Lemon1dead");
        expectedLemonProperties.put("DeadStage2", "Lemon2dead");
        expectedLemonProperties.put("DeadStage3", "Lemon3dead");
        expectedLemonProperties.put("Penvironment", "FOREST");
        expectedLemonProperties.put("Nenvironment", "ROCKY");
        Map<String, Object> actualLemonProperties = 
                DatabaseHandler.getInstance().getPlantData("Lemon");
        for(int i = 0; i < 13; i++) {
            assertEquals(expectedLemonProperties.get(keys.get(i)),
                    actualLemonProperties.get(keys.get(i)));
        }
    }
    /**
     * Checks that all of the plants have been loaded from the database.
     * @throws Exception
     */
     @Test
     public void getPlantsTest() throws Exception { 
         DatabaseHandler.getInstance().importData();
         Set<String> plants = DatabaseHandler.getInstance().getPlants();
         assertTrue("Should be ten plants", plants.size() == 10); 
         assertTrue( "Should contain Lettuce", plants.contains("Lettuce")); 
         assertTrue("Should contain Strawberry", plants.contains("Strawberry")); 
         assertTrue( "Should contain Cotton", plants.contains("Cotton"));
         assertTrue( "Should contain Sugarcane", plants.contains("Sugarcane"));
         assertTrue( "Should contain Corn", plants.contains("Corn"));
         assertTrue( "Should contain Apple", plants.contains("Apple"));
         assertTrue( "Should contain Pear", plants.contains("Pear"));
         assertTrue( "Should contain Mango", plants.contains("Mango"));
         assertTrue( "Should contain Banana", plants.contains("Banana"));
         assertTrue( "Should contain Lemon", plants.contains("Lemon"));
     }
     
     /**
      * Tests that biomes don't affect harvest quantity if they aren't
      * positive or negative biomes and checks that harvest quantities are
      * loaded correctly.
      */
     @Test
     public void cropQuantityTest() {
         DatabaseHandler.getInstance().importData();
         Tile tile = mock(Tile.class);
         when(tile.getProperty(TileProperty.BIOME)).thenReturn("ARID");
         Lemon lemon = new Lemon(tile);
         assertEquals("Quantities not equal", 14, lemon.getQuantity());
         Banana banana = new Banana(tile);
         assertEquals("Quantities not equal", 12, banana.getQuantity());
         Mango mango = new Mango(tile);
         assertEquals("Quantities not equal", 10, mango.getQuantity());
         Pear pear = new Pear(tile);
         assertEquals("Quantities not equal", 10, pear.getQuantity());
         Apple apple = new Apple(tile);
         assertEquals("Quantities not equal", 8, apple.getQuantity());
         SugarCane sugarcane = new SugarCane(tile);
         assertEquals("Quantities not equal", 6, sugarcane.getQuantity());
     }
     
     /**
      * Checks that the biome correctly increases or decreases the harvest 
      * quantity (depending on whether it is a positive or negative 
      * environment).
      */
     @Test
     public void effectOfForestBiome() {
         DatabaseHandler.getInstance().importData();
         Tile tile = mock(Tile.class);
         when(tile.getProperty(TileProperty.BIOME)).thenReturn("FOREST");
         Lemon lemon = new Lemon(tile);
         assertEquals("Quantity not adjusted", 18, lemon.getQuantity());
         SugarCane cane = new SugarCane(tile);
         assertEquals("Quantity not adjusted", 4, cane.getQuantity());
         when(tile.getProperty(TileProperty.BIOME)).thenReturn("ROCKY");
     }
     
     
}
