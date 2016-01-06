package farmsim.entities.tileentities.objects;

import static org.junit.Assert.*;

import org.junit.Test;

public class RockTest {

    @Test
    public void testRocks() {
        Rock rock = new Rock(null);
        assertEquals("rocks", rock.getTileType());
        assertEquals(true, rock.isClearable());
    }

}
