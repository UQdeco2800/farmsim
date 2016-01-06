package farmsim.particle;

import javafx.scene.image.Image;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.image.Image;

/**
 * Particle Element Tests.
 */
public class ParticleElementTest {

    /**
     * Can we move the particle.
     */
    @Test
    public void testMove() {
        ParticleElement particle = new ParticleElement(
                new ParticleVector(),
                new ParticleVector(1, 1),
                20,
                20,
                null);

        particle.move();
        Assert.assertEquals(new ParticleVector(1, 1), particle.getLocation());
        particle.move();
        Assert.assertEquals(new ParticleVector(2, 2), particle.getLocation());
    }

    /**
     * Can we get the texture of the particle.
     */
    @Test
    public void testGetTexture() {
        ParticleElement particle = new ParticleElement(
                new ParticleVector(),
                new ParticleVector(1, 1),
                20,
                20,
                null);
        Assert.assertNull(particle.getTexture());

        Image nyanDuck = new Image("/nyanDuck.png");
        particle = new ParticleElement(
                new ParticleVector(),
                new ParticleVector(1, 1),
                20,
                20,
                nyanDuck);
        Assert.assertEquals(nyanDuck, particle.getTexture());
    }

    /**
     * Can the particle figure out when its out of bounds.
     */
    @Test
    public void testIsDeadBounds() {
        ParticleElement particle = new ParticleElement(
                new ParticleVector(10, 10),
                new ParticleVector(1, 1),
                20,
                20,
                null);
        Assert.assertTrue(particle.isDead(1, 1));
        Assert.assertTrue(!particle.isDead(20, 20));

        particle = new ParticleElement(
                new ParticleVector(-10, 1),
                new ParticleVector(1, 1),
                20,
                20,
                null);
        Assert.assertTrue(particle.isDead(1, 1));

        particle = new ParticleElement(
                new ParticleVector(1, -1),
                new ParticleVector(1, 1),
                20,
                20,
                null);
        Assert.assertTrue(particle.isDead(1, 1));

        particle = new ParticleElement(
                new ParticleVector(2, 1),
                new ParticleVector(1, 1),
                20,
                20,
                null);
        Assert.assertTrue(particle.isDead(1, 1));

        particle = new ParticleElement(
                new ParticleVector(1, 2),
                new ParticleVector(1, 1),
                20,
                20,
                null);
        Assert.assertTrue(particle.isDead(1, 1));
    }

    /**
     * Can particle figure out when its too old.
     */
    @Test
    public void testIsDeadAge() {
        ParticleElement particle = new ParticleElement(
                new ParticleVector(),
                new ParticleVector(),
                0,
                0,
                null);
        particle.age();
        particle.age();
        particle.age();
        Assert.assertTrue(particle.isDead(2));
        Assert.assertTrue(!particle.isDead(20));
    }

    /**
     * Particle elements should not be resizable.
     */
    @Test
    public void testIsResizable() {
        ParticleElement particle = new ParticleElement(
                new ParticleVector(),
                new ParticleVector(),
                20,
                20,
                null);

        Assert.assertFalse(particle.isResizable());
    }
}