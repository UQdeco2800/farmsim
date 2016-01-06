package farmsim.entities.agents;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class AgentRoleTypeTest {

    AgentRole role;

    @Before
    public void setup() {

        //Mock sprite sheets
        AgentRoleTravellingSpriteSheets mock = mock(AgentRoleTravellingSpriteSheets.class);

        // Specify a test roleType
        role = new AgentRole(Agent.RoleType.BUTCHER, mock);
    }

    @Test
    public void testAddExp() throws Exception {
        assertEquals(0, role.getExp());

        role.addExp(50);
        assertEquals(50, role.getExp());

        role.addExp(170);
        assertEquals(220, role.getExp());
    }

    @Test
    public void testGetLevel() throws Exception {
        assertEquals(1, role.getLevel());

        role.addExp(100);
        assertEquals(2, role.getLevel());

        role.addExp(200);
        assertEquals(3, role.getLevel());

        role.addExp(400);
        assertEquals(4, role.getLevel());

        role.addExp(800);
        assertEquals(5, role.getLevel());

        // Check that the level max is 5
        role.addExp(1600);
        assertEquals(5, role.getLevel());

    }

}
