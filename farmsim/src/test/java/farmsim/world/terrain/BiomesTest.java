package farmsim.world.terrain;

import org.junit.Assert;
import org.junit.Test;

public class BiomesTest {

    @Test
    public void testExists() {
        Assert.assertTrue(Biomes.valueOf("ARID") != null);
        Assert.assertTrue(Biomes.valueOf("ROCKY") != null);
        Assert.assertTrue(Biomes.valueOf("FOREST") != null);
        Assert.assertTrue(Biomes.valueOf("GRASSLAND") != null);
        Assert.assertTrue(Biomes.valueOf("MARSH") != null);
    }
}
