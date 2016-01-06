package farmsim.entities.tileentities.objects;

import static org.junit.Assert.*;

import org.junit.Test;

public class WaterTest {

    @Test
    public void testWater() {
        Water water = new Water(null);
        assertEquals("water", water.getTileType());
        assertEquals(false, water.isClearable());
    }

    @Test
    public void testGetSpriteName() {
        Water water = new Water(null);

        String binary;
        boolean[] truth = {false, false, false, false};
        /* Possible outcomes, in order of binary required to get them */
        String[] names = {"waterNESW", "waterNES", "waterNEW", "waterNE",
                "waterNSW", "waterNS", "waterNW", "waterN", "waterESW",
                "waterES", "waterEW", "waterE", "waterSW", "waterS", "waterW",
                "water"};
        /*
         * Loops through all sixteen possible values, uses the binary numbers to
         * get a particular sprite and compares that to the expected sprite
         */
        for (int i = 0; i < 16; i++) {
            binary = Integer.toBinaryString(i);
            for (int j = 0; j < binary.length(); j++) {
                truth[3 - (binary.length() - 1) + j] = binary.charAt(j) == '1';
            }
            assertEquals(names[i], water.getSpriteName(truth[0], truth[1],
                    truth[2], truth[3]));
        }
    }
}
