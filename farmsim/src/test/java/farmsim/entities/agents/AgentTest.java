package farmsim.entities.agents;


import farmsim.entities.tools.*;
import farmsim.tasks.AbstractTask;
import farmsim.tasks.AgentRoleTask;
import farmsim.util.Animation.SpriteSheet;
import farmsim.util.Point;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.awt.*;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Test class for Agent methods (does not test trivial getter/setter code)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AgentRoleTravellingSpriteSheets.class)
public class AgentTest {

    Agent agent;

    @Before
    public void setupAgent() {
        //Mock sprite sheets
        mockStatic(AgentRoleTravellingSpriteSheets.class);
        SpriteSheet spriteSheet = mock(SpriteSheet.class);
        when(AgentRoleTravellingSpriteSheets.initSpriteSheet(any()))
                .thenReturn(spriteSheet);
        agent = new Agent("test", 0, 0, 1);
        agent.setCurrentRoleType(Agent.RoleType.FARMER);
        agent.getRucksack().addToRucksack(new Axe(0, 0, 10).getResource());
        agent.getRucksack().addToRucksack(new Hammer(0, 0, 10).getResource());
        agent.getRucksack().addToRucksack(new Hoe(0, 0, 10).getResource());
        agent.getRucksack().addToRucksack(new Shovel(0, 0, 10).getResource());
        agent.getRucksack().addToRucksack(new Pickaxe(0, 0, 0.1).getResource());
        agent.getRucksack().addToRucksack(new Sickle(0, 0, 10).getResource());
        agent.removeResource(ToolType.FISHING_ROD.displayName());
        agent.removeResource(ToolType.WATERING_CAN.displayName());
    }

    /**
     * checks to see wage calls work. by default workers start at 100 with and
     * likely a level of 1.
     */
    @Test
    public void wageTest() {
        assertEquals("wage not 100", 100, agent.getWage());
        assertEquals("happiness not 5", 5, agent.getHappiness());

    }

    @Test
    public void testGetAttributes() {
        Map<String, String> attr = agent.getAttributes();
        String farmerExp = attr.get("exp_farmer");
        assertEquals("0", farmerExp);
    }

    @Test
    public void testIsRequiredRole() {
        // Test that agent always eligible when task isnt AgentTask
        AbstractTask task = mock(AbstractTask.class);
        assertTrue(agent.isRequiredRole(task));

        // Test that agent with wrong current roleType is not eligible for task
        AgentRoleTask agentRoleTask = mock(AgentRoleTask.class);
        agent.setCurrentRoleType(Agent.RoleType.FARMER);
        // get task to require builder roleType with no min level
        when(agentRoleTask.minimumLevelRequired()).thenReturn(0);
        when(agentRoleTask.relatedRole()).thenReturn(Agent.RoleType.BUILDER);
        assertFalse(agent.isRequiredRole(agentRoleTask));

        // Test that agent with correct roleType is eligible for task
        agent.setCurrentRoleType(Agent.RoleType.BUILDER);
        assertTrue(agent.isRequiredRole(agentRoleTask));

        //Test that agent with correct roleType but with a lower than miminum
        // level is not eligible
        when(agentRoleTask.minimumLevelRequired()).thenReturn(2);
        assertFalse(agent.isRequiredRole(agentRoleTask));

    }
    

    @Test
    public void testCalculateOrientationToTask() {
        // Setup spy object to mock out getDirection method
        Agent agent = mock(Agent.class);
        // Tolerance for degress conversions
        double tolerance = 0.1;
        // When direction inside [- pi/8, pi/8) orientation east
        doCallRealMethod().when(agent).calculateOrientationToTask();
        when(agent.getDirection()).thenReturn(-Math.PI / 8 + tolerance);
        assertEquals(Agent.Orientation.EAST,
                agent.calculateOrientationToTask());
        when(agent.getDirection()).thenReturn(Math.PI / 8 - tolerance);
        assertEquals(Agent.Orientation.EAST, agent
                .calculateOrientationToTask());

        // When direction inside [pi/8, 3pi/8) North east
        when(agent.getDirection()).thenReturn(Math.PI / 8 + tolerance);
        assertEquals(Agent.Orientation.NORTHEAST, agent
                .calculateOrientationToTask());
        when(agent.getDirection()).thenReturn(3 * Math.PI / 8 - tolerance);
        assertEquals(Agent.Orientation.NORTHEAST, agent
                .calculateOrientationToTask());

        // When direction inside [3pi/8, 5pi/8) North
        when(agent.getDirection()).thenReturn(3 * Math.PI / 8 + tolerance);
        assertEquals(Agent.Orientation.NORTH, agent
                .calculateOrientationToTask());
        when(agent.getDirection()).thenReturn(5 * Math.PI / 8 - tolerance);
        assertEquals(Agent.Orientation.NORTH, agent
                .calculateOrientationToTask());

        // When direction inside [5pi/8, 7pi/8) North west
        when(agent.getDirection()).thenReturn(5 * Math.PI / 8 + tolerance);
        assertEquals(Agent.Orientation.NORTHWEST, agent
                .calculateOrientationToTask());
        when(agent.getDirection()).thenReturn(7 * Math.PI / 8 - tolerance);
        assertEquals(Agent.Orientation.NORTHWEST, agent
                .calculateOrientationToTask());

        // When direction inside [7pi/8, 9pi/4) West
        when(agent.getDirection()).thenReturn(7 * Math.PI / 8 + tolerance);
        assertEquals(Agent.Orientation.WEST, agent
                .calculateOrientationToTask());
        when(agent.getDirection()).thenReturn(9 * Math.PI / 8 - tolerance);
        assertEquals(Agent.Orientation.WEST, agent
                .calculateOrientationToTask());

        // When direction inside [9pi/8, 11pi/8) South west
        when(agent.getDirection()).thenReturn(9 * Math.PI / 8 + tolerance);
        assertEquals(Agent.Orientation.SOUTHWEST, agent
                .calculateOrientationToTask());
        when(agent.getDirection()).thenReturn(11 * Math.PI / 8 - tolerance);
        assertEquals(Agent.Orientation.SOUTHWEST, agent
                .calculateOrientationToTask());

        // When direction inside [11pi/8, 13pi/8) South
        when(agent.getDirection()).thenReturn(11 * Math.PI / 8 + tolerance);
        assertEquals(Agent.Orientation.SOUTH, agent
                .calculateOrientationToTask());
        when(agent.getDirection()).thenReturn(13 * Math.PI / 8 - tolerance);
        assertEquals(Agent.Orientation.SOUTH, agent
                .calculateOrientationToTask());

        // When direction inside [13pi/8, 15pi/8) South east
        when(agent.getDirection()).thenReturn(13 * Math.PI / 8 + tolerance);
        assertEquals(Agent.Orientation.SOUTHEAST, agent
                .calculateOrientationToTask());
        when(agent.getDirection()).thenReturn(15 * Math.PI / 8 - tolerance);
        assertEquals(Agent.Orientation.SOUTHEAST, agent
                .calculateOrientationToTask());
    }

    @Test
    public void testSkillInit() {
        assertEquals(1, this.agent.getLevelForRole(Agent.RoleType.SHEARER));
        assertEquals(1, this.agent.getLevelForRole(Agent.RoleType.MILKER));
        assertEquals(1, this.agent.getLevelForRole(Agent.RoleType.EGG_HANDLER));
        assertEquals(1, this.agent.getLevelForRole(Agent.RoleType.BUTCHER));
        assertEquals(1, this.agent.getLevelForRole(Agent.RoleType.BUILDER));
        assertEquals(1, this.agent.getLevelForRole(Agent.RoleType.FARMER));
        assertEquals(1, this.agent.getLevelForRole(Agent.RoleType.HUNTER));


        assertEquals("Highest level is not 1", 1, agent.getHighestLevel());
        //definatly maxes out the level.
        agent.addExperienceForRole(Agent.RoleType.SHEARER, 5000);
        assertEquals("highest level is not 5", 5, agent.getHighestLevel());
    }


    @Test
    public void testRoleTypeLocking() {
        // Agent should not be locked into a RoleType by default
        assertFalse(agent.isRoleTypeLocked());
        // Lock agent to the hunter RoleType
        agent.setCurrentRoleType(Agent.RoleType.HUNTER);
        agent.lockRole();
        assertTrue(agent.isRoleTypeLocked());
        assertTrue(Agent.RoleType.HUNTER.equals(agent.getCurrentRoleType()));

        agent.unlockRole();
        assertFalse(agent.isRoleTypeLocked());
    }

    /**
     * Tests equipping tools to a peon.
     */
    @Test
    public void equipTest() {
        assertTrue(agent.equip(ToolType.SHOVEL));
        assertTrue(agent.equip(ToolType.AXE));
        assertTrue(agent.equip(ToolType.HAMMER));
        assertTrue(agent.equip(ToolType.HOE));
        assertTrue(agent.equip(ToolType.PICKAXE));
        assertTrue(agent.equip(ToolType.SICKLE));
        assertFalse(agent.equip(ToolType.FISHING_ROD));
        assertFalse(agent.equip(ToolType.WATERING_CAN));
    }

    /**
     * Tests to see if they can equip a tool
     */
    @Test
    public void canEquipTest() {
        assertTrue(agent.canEquip(ToolType.SHOVEL));
        assertTrue(agent.canEquip(ToolType.AXE));
        assertTrue(agent.canEquip(ToolType.SICKLE));
        assertFalse(agent.canEquip(ToolType.FISHING_ROD));
        assertFalse(agent.canEquip(ToolType.WATERING_CAN));
    }

    /**
     * Tests to see if the correct tool type is returned.
     */
    @Test
    public void getToolTypeTest() {
        agent.equip(ToolType.AXE);
        assertEquals(ToolType.AXE, agent.getToolType());
        agent.equip(ToolType.SHOVEL);
        assertEquals(ToolType.SHOVEL, agent.getToolType());
        agent.equip(ToolType.HOE);
        assertEquals(ToolType.HOE, agent.getToolType());
    }

    /**
     * Tests remove tool from peon.
     */
    @Test
    public void removeToolTest() {
        agent.removeTool();
        assertNull(agent.getToolType());
    }

    /**
     * Tests remove tool from peon.
     */
    @Test
    public void getToolTest() {
        agent.equip(ToolType.SHOVEL);
        assertEquals(new Shovel(0, 0, 10).getClass(),
                agent.getTool().getClass());
        agent.equip(ToolType.AXE);
        assertEquals(new Axe(0, 0, 10).getClass(), agent.getTool().getClass());
        agent.equip(ToolType.HOE);
        assertEquals(new Hoe(0, 0, 10).getClass(), agent.getTool().getClass());
    }

    /**
     * Tests remove tool from peon.
     */
    @Test
    public void brokenToolTest() {
        agent.equip(ToolType.PICKAXE);
        agent.getTool().decreaseDurability();
        assertTrue(agent.getTool().isBroken());
        agent.removeTool();
        assertNull(agent.getTool());
        assertFalse(agent.canEquip(ToolType.PICKAXE));
    }
}
