package farmsim.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

public class PerlinTest {

    /**
     * Testing makeNoise.
     */
    @Test
    public void makeNoiseTest() {
        try {
            // Test output is between 0 and 255
            Assert.assertTrue("Error in output range.",
                    (0 <= (int) Perlin.makeNoise(1, 2, 3, 4, 0,
                            255))
                    && ((int) Perlin.makeNoise(1, 2, 3, 4, 0, 255) <= 255));

            // Test output is between 64 and 128
            Assert.assertTrue("Error in output range.",
                    (64 <= (int) Perlin.makeNoise(1, 2, 3, 4, 64,
                            128))
                    && ((int) Perlin.makeNoise(1, 2, 3, 4, 64, 128) <= 128));

            // Check that output repeats for same input
            Assert.assertTrue("Not repeatable", Perlin.makeNoise(1, 2, 3, 4, 0,
                    255) == (Perlin.makeNoise(1, 2, 3, 4, 0, 255)));

            ArrayList<Double> data = new ArrayList<Double>();
            for (int i = 0; i < 1000; i++) {
                data.add(Perlin.makeNoise(i, i + 1000, 5, 1, 0, 255));
            }

            double sum = 0;
            for (double value : data) {
                sum += value;
            }
            double mean = sum / 1000;
            // Test that average is near centre of range
            Assert.assertTrue("Average not centred", 10 > Math.abs(mean - 128));

            sum = 0;
            for (Double value : data)
                sum += Math.pow((value - mean), 2);
            double sd = Math.sqrt(sum / (data.size() - 1));

            // Test that standard deviation is about 15% of range
            System.out.println(sd);
            Assert.assertTrue("Standard deviation too large or small",
                    15 > Math.abs(sd - (255 - 0) * 0.15));

        } catch (Exception e) {
            Assert.assertFalse("Exception using makeNoise",
                    e instanceof Exception);
        }
    }

    @Test(expected = Exception.class)
    public void minMaxErrorTest() throws Exception {
        // Try max = min
        Perlin.makeNoise(1, 1, 1, 1, 1, 1);

        // Try max < min
        Perlin.makeNoise(1, 1, 1, 1, 2, 1);
    }

    @Test(expected = Exception.class)
    public void numLayerErrorTest() throws Exception {
        // Try n = 0
        Perlin.makeNoise(1, 1, 0, 1, 1, 2);

        // Try n < 0
        Perlin.makeNoise(1, 1, -1, 1, 1, 2);
    }
}
