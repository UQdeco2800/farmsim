package farmsim.entities.fire;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;

/**
 * Tests for the Fire class.
 * 
 * @author 
 *      yojimmbo
 */
@PrepareForTest({TileRegister.class})
@RunWith(PowerMockRunner.class)
public class FireTests {
    
    private Tile tile;
    private Fire fire;
    private TileRegister tileRegister;
    
    /**
     * Setup before tests are run.
     */
    @Before
    public void setup() {
        tile = mock(Tile.class);
        tileRegister = mock(TileRegister.class);
        PowerMockito.mockStatic(TileRegister.class);
        PowerMockito.when(TileRegister.getInstance()).thenReturn(tileRegister);
        fire = new Fire(tile, 100);
    }
    
    /**
     * Tests if a fire is initialised.
     */
    @Test
    public void intialisationTest() {
        assertEquals(100, fire.getBurnTime());
        assertFalse(fire.out());
        assertTrue(fire.canSpread());
    }
    
    /**
     * Tests the tick method of a fire. Fire should be out after ticks is >= 
     * timeToBurn.
     */
    @Test
    public void tickTest() {
        for (int i = 0; i < 100; i++) {
            fire.tick();
        }
        assertFalse(fire.out());
        fire.tick();
        assertTrue(fire.out());
    }
    
    /**
     * Tests if a fire can spread.
     */
    @Test
    public void canSpreadTest() {
    	fire = new Fire(tile, 100);
    	assertTrue(fire.canSpread());
    }
    
    /**
     * Tests negative time to burn.
     */
    @Test
    public void negativeTimeToBurnTest() {
        fire = new Fire(tile, -100);
        assertTrue(fire.out());
        assertFalse(fire.canSpread());
        fire.tick();
        assertTrue(fire.out());
        assertFalse(fire.canSpread());
    }
    
    /**
     * Tests if a fire can be stopped from spreading.
     */
    @Test
    public void stopSpreadTest() {
    	fire = new Fire(tile, 100);
    	fire.stopSpreading();
    	assertFalse(fire.canSpread());
    }
    
    /**
     * Tests if a fire is not burnt out.
     */
    @Test
    public void notBurntOutTest() {
    	fire = new Fire(tile, 100);
    	assertFalse(fire.out());
    }
    
    /**
     * Tests getting the right image for the fire animation.
     */
    @Test
    public void getImageTest() {
        fire = new Fire(tile, 350);
        for (int i = 0; i < 7; i++) {
            assertEquals("fire", fire.getImageName());
            fire.tick();
        }
        for (int i = 7; i < 14; i++) {
            assertEquals("fire2", fire.getImageName());
            fire.tick();
        }
        for (int i = 14; i < 20; i++) {
            assertEquals("fire3", fire.getImageName());
            fire.tick();
        }
    }
    
    /**
     * Tests draw method.
     */
    @Test
    public void drawTest() {
        fire = new Fire(tile, 350);
    }

}
