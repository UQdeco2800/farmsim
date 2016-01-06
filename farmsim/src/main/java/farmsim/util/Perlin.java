package farmsim.util;

import java.util.Random;

/**
 * Implementation of Ken Perlin's improved noise algorithm
 *
 * @author matthew-lake
 */
public class Perlin {
    private static final int[] p = new int[512];
    private static final String MIN_MAX_EXCEPTION =
            "Max must be greater than min";
    private static final String NUM_LAYERS_EXCEPTION =
            "Must have at least 1 layer";

    /**
     * Privatee empty constructor to appease sonar
     */
    private Perlin() {

    }

    /**
     * Wrapper for generating Perlin noise from a 2D pattern based on coordinate
     * inputs
     *
     * @param x x-coordinate of noise to return
     * @param y y-coordinate of noise to return
     * @param n number of layers of noise to stack (more layers produces a more
     *        detailed and 'cloudy' pattern). Should be kept constant if you
     *        want to sample from the same pattern
     * @param seed seed for random pattern generation, should be kep constant if
     *        you want to keep sampling from the same pattern
     * @param min minimum bound on range
     * @param max maximum bound on range
     * @return returns a double between min and max, from a noisy normal
     *         distribution centred around (max-min)/2 with a standard deviation
     *         of about (max-min)/7.8
     */
    public static double makeNoise(int x, int y, int n, long seed, double min,
            double max) throws IllegalArgumentException {
        if (min >= max)
            throw new IllegalArgumentException(MIN_MAX_EXCEPTION);

        if (n < 1)
            throw new IllegalArgumentException(NUM_LAYERS_EXCEPTION);

        setUpArray(seed);

        double result = 0.0;
        for (int i = 0; i < n; i++) {
            result += (noise((double) x / (100 - i * 100 / n),
                    (double) y / (100 - i * 100 / n)) + 1) * (max - min) / 2;
        }
        result = result / n;
        // horrific hack to get the standard distribution looking right
        double multiplier = 0.484689 * Math.pow(n, 0.45) * (max - min) * 2.592
                * Math.pow(max - min, -1.003);
        result = (result - (max - min) / 2) * multiplier + (max - min) / 2
                + min;
        if (result < min) {
            return min;
        }
        if (result > max) {
            return max;
        }
        return result;
    }

    private static void setUpArray(long seed) {
        Random rnd = new Random(seed);
        for (int i = 0; i < 256; i++) {
            p[i] = i;
        }

        for (int i = 256 - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = p[index];
            p[index] = p[i];
            p[i] = a;
        }
        System.arraycopy(p, 0, p, 256, 256);
    }

    /**
     * Generates Perlin noise at a specific coordinate
     *
     * @param x x-coordinate of noise to return
     * @param y y-coordinate of noise to return
     * @return returns a double between -1 and 1, evenly distributed
     */
    private static double noise(double x, double y) {
        // transform x and y to ints between 0 and 255
        // (get grid plot from x,y coordinates)
        int X = (int) Math.floor(x) & 255, Y = (int) Math.floor(y) & 255;
        // get decimal values of x and y (location within grid plot)
        x -= Math.floor(x);
        y -= Math.floor(y);
        // get smoothing values for x and y
        double u = smooth(x), v = smooth(y);
        // generate deterministic hash from X and Y for all corners of square
        // X,Y,X+1,Y+1
        int A = p[X] + Y, AA = p[X] + Y + 1, B = p[X + 1] + Y,
                BB = p[X + 1] + Y + 1;

        // generate gradients and take the dot product on all corners, then
        // interpolate influence values weighted by u and v
        return interpolate(v,
                interpolate(u, gradientDotProduct(p[A], x, y),
                        gradientDotProduct(p[B], x - 1, y)),
                interpolate(u, gradientDotProduct(p[AA], x, y - 1),
                        gradientDotProduct(p[BB], x - 1, y - 1)));
    }

    /**
     * Smooths out the linear interpolation so the transitions aren't as sharp
     * Uses Ken Perlin's fade equation from his improved noise algorithm, which
     * forms an s-shaped graph such that values near 1 or 0 are moved even
     * closer, while intermediate values are hardly effected
     *
     * @param t input value between 0 and 1
     * @return input value, moved closer to 0 or 1 depending on its initial
     *         proximity
     */
    private static double smooth(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    /**
     * Weighted linear interpolation
     *
     * @param t first value to interpolate between
     * @param a second value to interpolate between
     * @param b weighting between 0 and 1 to determine whether interpolated
     *        result should be closer to a or b respectively
     * @return the weighted interpolation, lying between a and b
     */
    private static double interpolate(double t, double a, double b) {
        return a + t * (b - a);
    }

    /**
     * Calculates a deterministic random gradient based on hash (from the
     * options (1,1),(-1,1),(1,-1),(-1,-1) suggested by Ken Perlin in his
     * SIGGRAPH 2002 paper), and takes the dot product of that gradient vector
     * and the vector (x,y), returning that dot product which serves as an
     * influence value deciding how much influence the gradient has on the
     * pattern
     *
     * @param hash A hash based on the location of the grid plot containing
     *        point (x,y)
     * @param x x-coordinate of point within grid plot
     * @param y y-coordinate of point within grid plot
     * @return dot product of gradient and (x,y), scaling the influence of the
     *         gradient based on the location of point (x,y)
     */
    private static double gradientDotProduct(int hash, double x, double y) {
        // switch based on lowest 2 bits of hash, giving 4 possibilities
        // (00,01,10,11) determining which gradient to choose
        switch (hash & 0x3) {
            case 0x0:
                return x + y;
            case 0x1:
                return -x + y;
            case 0x2:
                return x - y;
            case 0x3:
                return -x - y;
            default:
                return 0; // never happens
        }
    }
}
