package farmsim.entities.machines;

import static org.junit.Assert.*;

import org.junit.Test;

import farmsim.util.Point;

/**
 * Tests for the Machine class.
 * 
 * @author 
 *      yojimmbo
 *
 */
public class MachineTests {
    
    /**
     * Tests the machine constructor.
     */
    @Test
    public void testConstructor() {
        Machine machine = new Machine("machine", 1, 1, 50, 50, 50, 
                MachineType.TRACTOR);
        assertEquals("machine", machine.getImageName());
    }
    
    /**
     * Tests getting the location of a machine.
     */
    @Test
    public void testGetLocation() {
        Machine machine = new Machine("tractor", 1, 1, 50, 50, 50, 
                MachineType.TRACTOR);
        assertEquals(new Point(1, 1), machine.getLocation());
    }
    
    /**
     * Tests getting the speed of a machine.
     */
    @Test
    public void testGetSpeed() {
        Machine machine = new Machine("harvester", 1, 1, 50, 50, 50,
                MachineType.HARVESTER);
        assertEquals(50.0, machine.getSpeed(), 1.0);
    }
    
    /**
     * Tests that the machine is not broken.
     */
    @Test
    public void testIsNotBroken() {
        Machine machine = new Machine("harvester", 1, 1, 50, 50, 50,
                MachineType.HARVESTER);
        assertFalse(machine.isBroken());
        
    }
    
    /**
     * Tests getting the durability of a machine.
     */
    @Test
    public void testGetDurability() {
        Machine machine = new Machine("harvester", 1, 1, 50, 50, 50,
                MachineType.HARVESTER);
        assertEquals(50.0, machine.getDurability(), 1.0);
    }
    
    /**
     * Tests getting the max durability of a machine.
     */
    @Test
    public void testGetMaxDurability() {
        Machine machine = new Machine("harvester", 1, 1, 50, 50, 50,
                MachineType.HARVESTER);
        assertEquals(50.0, machine.getMaxDurability(), 1.0);
    }
    
    /**
     * Tests getting the machines type. Tests multiple machine types.
     */
    @Test
    public void testGetMachineType() {
        Machine machine = new Machine("harvester", 1, 1, 50, 50, 50,
                MachineType.HARVESTER);
        assertEquals(MachineType.HARVESTER, machine.getMachineType());
        machine = new Machine("tractor", 1, 1, 50, 50, 50,
                MachineType.TRACTOR);
        assertEquals(MachineType.TRACTOR, machine.getMachineType());
    }
    
    /**
     * Tests the tick method of a machine.
     */
    @Test
    public void testTick() {
        Machine machine = new Machine("harvester", 1, 1, 50, 50, 50,
                MachineType.HARVESTER);
        machine.tick();
    }
    
    /**
     * Tests getting the image name of a machine.
     */
    @Test
    public void testGetImageName() {
        Machine machine = new Machine("harvester", 1, 1, 50, 50, 50,
                MachineType.HARVESTER);
        assertEquals("harvester", machine.getImageName());
        machine = new Machine("tractor", 1, 1, 50, 50, 50,
                MachineType.TRACTOR);
        assertEquals("tractor", machine.getImageName());
    }
    
    /**
     * Tests losing durability.
     */
    @Test
    public void testDurabilityLoss() {
        Machine machine = new Machine("harvester", 1, 1, 50, 50, 50,
                MachineType.HARVESTER);
        machine.decreaseDurability();
        assertEquals(50.0 - machine.getDurabilityLoss(), machine.getDurability(), 0.1);
    }
    
    /**
     * Tests is machine broken.
     */
    @Test
    public void testIsBroken() {
        Machine machine = new Machine("harvester", 1, 1, 0.1, 0.1, 0.1,
                MachineType.HARVESTER);
        machine.decreaseDurability();
        assertTrue(machine.isBroken());
    }

}
