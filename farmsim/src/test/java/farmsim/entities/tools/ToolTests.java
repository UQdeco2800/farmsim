package farmsim.entities.tools;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import farmsim.resource.SimpleResource;
import farmsim.util.Point;

/**
 * Tests for the Tool class and its descendants.
 * 
 * @author rachelcatchpoole
 *
 */
public class ToolTests {

    /**
     * Tests the tool constructor.
     */
    @Test
    public void testConstructor() {
        Tool tool = new Tool("axe", 1, 1, 50, 50, 50, ToolType.AXE);
        assertEquals("axe", tool.getImageName());
    }

    /**
     * Tests the Axe constructor.
     */
    @Test
    public void testAxeConstructor() {
        Axe axe = new Axe(5, 5, 10);
        assertEquals(ToolType.AXE, axe.getToolType());
    }
    
    /**
     * Tests the tool get resource method.
     */
    @Test
    public void testGetResource() {
        Map<String, String> attributes = new HashMap<String, String>();
        Tool tool = new Tool("axe", 1, 1, 50, 50, 50, ToolType.AXE);
        SimpleResource item = tool.getResource();
        attributes.put("isBroken", "false");
        attributes.put("durability", "50.0");
        attributes.put("speed", "50.0");
        attributes.put("imageName", "axe");
        SimpleResource resource = new SimpleResource("Axe", attributes, 1);
        assertEquals(resource.getAttributes().get("isBroken"), 
                item.getAttributes().get("isBroken"));
        assertEquals(resource.getAttributes().get("durability"), 
                item.getAttributes().get("durability"));
        assertEquals(resource.getAttributes().get("speed"), 
                item.getAttributes().get("speed"));
        assertEquals(resource.getAttributes().get("imageName"), 
                item.getAttributes().get("imageName"));
    }

    /**
     * Tests the Hammer constructor.
     */
    @Test
    public void testHammerConstructor() {
        Hammer hammer = new Hammer(5, 5, 10);
        assertEquals(ToolType.HAMMER, hammer.getToolType());
    }

    /**
     * Tests the Hoe constructor.
     */
    @Test
    public void testHoeConstructor() {
        Hoe hoe = new Hoe(5, 5, 10);
        assertEquals(ToolType.HOE, hoe.getToolType());
    }

    /**
     * Tests the Pickaxe constructor.
     */
    @Test
    public void testPickaxeConstructor() {
        Pickaxe pickaxe = new Pickaxe(5, 5, 10);
        assertEquals(ToolType.PICKAXE, pickaxe.getToolType());
    }

    /**
     * Tests the Shovel constructor.
     */
    @Test
    public void testShovelConstructor() {
        Shovel shovel = new Shovel(5, 5, 10);
        assertEquals(ToolType.SHOVEL, shovel.getToolType());
    }

    /**
     * Tests the Sickle constructor.
     */
    @Test
    public void testSickleConstructor() {
        Sickle sickle = new Sickle(5, 5, 10);
        assertEquals(ToolType.SICKLE, sickle.getToolType());
    }

    /**
     * Tests the WateringCan constructor.
     */
    @Test
    public void testWaterinCanConstructor() {
        WateringCan wateringCan = new WateringCan(5, 5, 10);
        assertEquals(ToolType.WATERING_CAN, wateringCan.getToolType());
    }

    /**
     * Tests getting the location of a tool.
     */
    @Test
    public void testGetLocation() {
        Tool tool = new Tool("axe", 1, 1, 50, 50, 50, ToolType.AXE);
        assertEquals(new Point(1, 1), tool.getLocation());
    }

    /**
     * Tests getting the speed of a tool.
     */
    @Test
    public void testGetSpeed() {
        Tool tool = new Tool("axe", 1, 1, 50, 50, 50, ToolType.AXE);
        assertEquals(50.0, tool.getSpeed(), 0.1);
    }

    /**
     * Tests that the tool is not broken.
     */
    @Test
    public void testIsNotBroken() {
        Tool tool = new Tool("axe", 1, 1, 50, 50, 50, ToolType.AXE);
        assertFalse(tool.isBroken());

    }
    
    /**
     * Tests that the tool is broken.
     */
    @Test
    public void testIsBroken() {
        Tool tool = new Tool("axe", 1, 1, 0.1, 0.1, 0.1, ToolType.AXE);
        tool.decreaseDurability();
        assertTrue(tool.isBroken());

    }

    /**
     * Tests getting the durability of a tool.
     */
    @Test
    public void testGetDurability() {
        Tool tool = new Tool("axe", 1, 1, 50, 50, 50, ToolType.AXE);
        assertEquals(50.0, tool.getDurability(), 0.1);
    }

    /**
     * Tests getting the max durability of a tool.
     */
    @Test
    public void testGetMaxDurability() {
        Tool tool = new Tool("axe", 1, 1, 50, 50, 50, ToolType.AXE);
        assertEquals(50.0, tool.getMaxDurability(), 0.1);
    }
    
    /**
     * Tests getting the durability loss factor of a tool.
     */
    @Test
    public void testGetDurabilityLoss() {
        Axe axe = new Axe(5, 5, 10);
        assertEquals(0.5, axe.getDurabilityLoss(), 0.1);
    }
    
    /**
     * Tests getting the durability loss factor of a tool.
     */
    @Test
    public void testAllDurabilityLoss() {
        Axe axe = new Axe(5, 5, 10);
        assertEquals(0.5, axe.getDurabilityLoss(), 0.1);
        
        Hammer hammer = new Hammer(5, 5, 10);
        assertEquals(0.3, hammer.getDurabilityLoss(), 0.1);
        
        Hoe hoe = new Hoe(5, 5, 10);
        assertEquals(0.4, hoe.getDurabilityLoss(), 0.1);
        
        Pickaxe pickaxe = new Pickaxe(5, 5, 10);
        assertEquals(0.5, pickaxe.getDurabilityLoss(), 0.1);
        
        Shovel shovel = new Shovel(5, 5, 10);
        assertEquals(0.3, shovel.getDurabilityLoss(), 0.1);
        
        Sickle sickle = new Sickle(5, 5, 10);
        assertEquals(0.3, sickle.getDurabilityLoss(), 0.1);
        
        WateringCan wateringCan = new WateringCan(5, 5, 10);
        assertEquals(0.1, wateringCan.getDurabilityLoss(), 0.1);
    }
    

    /**
     * Tests decreasing the durability of a tool.
     */
    @Test
    public void testDecreaseDurability() {
        Axe axe = new Axe(5,5,10);
        double durabilityLoss = axe.getDurabilityLoss();
        double maxDurability = axe.getMaxDurability();
        axe.decreaseDurability();
        assertEquals(maxDurability - durabilityLoss, axe.getDurability(), 0.1);
        
        while (axe.getDurability() > durabilityLoss) {
            axe.decreaseDurability();
        }
        axe.decreaseDurability();
        assertEquals(0, axe.getDurability(), 0.1);
    }
    
    /**
     * Tests increasing the durability of a tool.
     */
    @Test
    public void testIncreaseDurability() {
        Tool durableTool = new Tool("axe", 1, 1, 50, 50, 50, ToolType.AXE);
        durableTool.increaseDurability(2);
        assertEquals(50, durableTool.getDurability(), 0.1);
        
        Tool tool = new Tool("axe", 1, 1, 50, 50, 50, ToolType.AXE);
        while (tool.getDurability() > 45) {
            tool.decreaseDurability();
        }
        tool.increaseDurability(2);
        assertEquals(47, tool.getDurability(), 0.1);
    }
    
    /**
     * Tests restoring the durability of a tool.
     */
    @Test
    public void testRestoreDurability() {
        Axe axe = new Axe(5,5,10);
        axe.decreaseDurability();
        axe.decreaseDurability();
        axe.restoreDurability();
        assertEquals(axe.getMaxDurability(), axe.getDurability(), 0.1);
    }
    
    /**
     * Tests the tick method of a tool.
     */
    @Test
    public void testTick() {
        Tool tool = new Tool("axe", 1, 1, 50, 50, 50, ToolType.AXE);
        tool.tick();
    }
    
    /**
     * Tests fishing rod constructor.
     */
    @Test
    public void testFishingRodConstructor() {
        Tool tool = new FishingRod(0, 0, 10);
        tool.tick();
    }

}
