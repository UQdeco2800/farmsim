package farmsim.particle;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

/**
 * Tests the Particle Emitter.
 */
public class EmitterTest {

    /**
     * Tests spawning the emitter and killing children and itself.
     */
    @Test
    public void testParticleDieingFromBounds() {
        Canvas canvas = new Canvas(0, 0);

        Emitter emitter = new Emitter(canvas);
        emitter.setBounding(BoundType.FREE);
        emitter.setCords(new ParticleVector());
        emitter.setVelocity(new ParticleVector(1, 1));
        emitter.setSpawnRate(new ParticleVector(1, 1));
        emitter.setPositionVariance(new ParticleVector(), new ParticleVector());
        emitter.setPositionVarianceBounds(PositionVarianceType.SCREEN);
        emitter.setVelocityVariance(new ParticleVector(), new ParticleVector());
        emitter.setTexture(new Image("/nyanDuck.png"));

        // Test the death condition.
        emitter.setDeathCondition(DeathType.TIME, 1);
        Assert.assertEquals(DeathType.TIME, emitter.getDeath());
        emitter.setDeathCondition(DeathType.BOUND);
        Assert.assertEquals(DeathType.BOUND, emitter.getDeath());

        // Spawn some children and kill them in every way.
        emitter.spawn();
        emitter.spawn();
        Assert.assertTrue(!emitter.getChildren().isEmpty());
        emitter.tick();
        emitter.killDeadChildren();
        Assert.assertTrue(emitter.getChildren().isEmpty());
        emitter.setDeathCondition(DeathType.TIME, 1);
        emitter.spawn();
        emitter.spawn();
        emitter.getChildren().stream().parallel().forEach(ParticleElement::age);
        Assert.assertTrue(!emitter.getChildren().isEmpty());
        emitter.killDeadChildren();
        Assert.assertTrue(emitter.getChildren().isEmpty());
        Assert.assertTrue(!emitter.isDead());
        emitter.kill();
        Assert.assertTrue(emitter.isDead());
    }
}