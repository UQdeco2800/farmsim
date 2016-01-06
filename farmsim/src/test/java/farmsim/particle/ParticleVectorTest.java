package farmsim.particle;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the Particle Vector.
 */
public class ParticleVectorTest {

    /**
     * Testing adding to particles together.
     */
    @Test
    public void testAdd() {
        ParticleVector vector1 = new ParticleVector(10, 10);
        ParticleVector vector2 = new ParticleVector(10, 10);

        vector1.add(vector2);
        Assert.assertEquals(new ParticleVector(20, 20), vector1);
    }

    /**
     * Test reassigning the particle vector values.
     */
    @Test
    public void testSet() {
        ParticleVector vector1 = new ParticleVector();
        Assert.assertEquals(new ParticleVector(), vector1);
        vector1.set(10, 10);
        Assert.assertEquals(new ParticleVector(10, 10), vector1);
    }

    /**
     * Test getting the first element.
     */
    @Test
    public void testGetX() {
        ParticleVector vector1 = new ParticleVector();
        Assert.assertEquals(0.0, vector1.getX(), 0.001);
        vector1 = new ParticleVector(10, 10);
        Assert.assertEquals(10, vector1.getX(), 0.001);
    }

    /**
     * Test getting the second element.
     */
    @Test
    public void testGetY() {
        ParticleVector vector1 = new ParticleVector();
        Assert.assertEquals(0.0, vector1.getY(), 0.001);
        vector1 = new ParticleVector(10, 10);
        Assert.assertEquals(10, vector1.getY(), 0.001);
    }

    /**
     * Tests checking the equality of a particle vector.
     */
    @Test
    public void testEquality() {
        ParticleVector vector1 = new ParticleVector();
        ParticleVector vector2 = new ParticleVector();
        ParticleVector vector3 = new ParticleVector(10, 10);
        String name = "Evan Hughes";

        Assert.assertEquals(vector1, vector2);
        Assert.assertTrue(!vector1.equals(name));
        Assert.assertEquals(vector1.hashCode(), vector2.hashCode());
        Assert.assertNotEquals(vector1.hashCode(), vector3.hashCode());
    }
}