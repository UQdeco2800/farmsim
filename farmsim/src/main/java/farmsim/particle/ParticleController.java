package farmsim.particle;

import farmsim.Viewport;
import farmsim.tiles.TileRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Particle Controller for javafx.
 *
 * <p>
 * This Particle controller/generator
 * </p>
 */
public class ParticleController extends AnimationTimer {

    private static ParticleController INSTANCE = new ParticleController();
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ParticleController.class);
    private Canvas parent;
    private List<Emitter> emitters = new ArrayList<>();
    private boolean animate = true;
    private GraphicsContext graphicsContext;
    private Viewport viewport;

    /**
     * Creates the Particle Controller (Singleton Structure).
     */
    private ParticleController() {
        // private to prevent implicit constructor.
    }

    /**
     * Causes the particle engine to animate the particles.
     * @param now the time of the animation.
     */
    @Override
    public void handle(long now) {
        if (animate) {
            // spawn new particles.
            emitters.stream().parallel().forEach(Emitter::spawn);
            // move all particles.
            emitters.stream().parallel().forEach(Emitter::tick);
            // clear the screen.
            graphicsContext.clearRect(0, 0,
                    parent.getWidth(), parent.getHeight());
            // draw all the new particles.
            for (Emitter emitter : emitters) {
                if (emitter.getBounding() == BoundType.WORLD) {
                    emitter.getChildren().forEach(this::worldParticle);
                } else {
                    emitter.getChildren().stream().forEach(
                            particle ->
                                    graphicsContext.drawImage(
                                            particle.getTexture(),
                                            particle.getLocation().getX(),
                                            particle.getLocation().getY())
                    );
                }
            }

            // kill dead particles.
            emitters.stream().parallel().forEach(Emitter::killDeadChildren);
            // Kill dead emitters.
            killDeadChildren();
            // Tick age of remaining particles if emitter is age dependent.
            emitters.stream().parallel().forEach(emitter -> {
                    if (emitter.getDeath() == DeathType.TIME) {
                        emitter.getChildren().stream().parallel().forEach(
                            ParticleElement::age);
                    }
            });
        }
    }

    /**
     * Draws particles that are bounded by the world.
     * @param particle to be drawn.
     */
    private void worldParticle(ParticleElement particle) {
        if (particle.getLocation().getX() >=
                (viewport.getX() * TileRegister.TILE_SIZE)
                && particle.getLocation().getY() >=
                (viewport.getY() * TileRegister.TILE_SIZE)) {
            graphicsContext.drawImage(particle.getTexture(),
                    particle.getLocation().getX()
                            - (viewport.getX() * TileRegister.TILE_SIZE),
                    particle.getLocation().getY()
                            - (viewport.getY() * TileRegister.TILE_SIZE));
        }
    }

    /**
     * Returns the global instance of the Particle Engine.
     * @return global accessible particle engine.
     */
    public static ParticleController getInstance() {
        return INSTANCE;
    }

    /**
     * Sets the parent pane to attach too.
     * @param parent pane to display too.
     */
    public void setParent(Canvas parent) {
        if (parent != null) {
            this.parent = parent;
            this.graphicsContext = parent.getGraphicsContext2D();
        } else {
            LOGGER.error("Bad parent");
        }
    }

    /**
     * Creates a demo to display on screen of the current particle
     * implementation.
     */
    public void mangoes() {
        if (this.parent != null) {
            LOGGER.info("Enabled the Particle Demo");
            particleExample1();
            particleExample2();
            particleExample3();
        }
    }

    /**
     * displays a emitter with a wide width variance.
     */
    public void particleExample1() {
        Emitter emitter = new Emitter(parent);
        emitter.setDeathCondition(DeathType.TIME, 20);
        emitter.setCords(new ParticleVector(100, 0));
        emitter.setPositionVariance(new ParticleVector(50, 50),
                new ParticleVector(0, 0));
        emitter.setVelocity(new ParticleVector(0, 5));
        emitter.setVelocityVariance(new ParticleVector(), new ParticleVector());
        emitter.setTexture(new Image("/farmanimals/cowsprite.png"));
        emitter.setSpawnRate(new ParticleVector(3, 1));
        emitters.add(emitter);
    }

    /**
     * displays an emitter with no variance.
     */
    public void particleExample2() {
        Emitter emitter = new Emitter(parent);
        emitter.setDeathCondition(DeathType.BOUND);
        emitter.setCords(new ParticleVector(500, 0));
        emitter.setPositionVariance(new ParticleVector(), new ParticleVector());
        emitter.setVelocity(new ParticleVector(0, 2));
        emitter.setVelocityVariance(new ParticleVector(), new ParticleVector());
        emitter.setTexture(new Image("/environment/rocks.png"));
        emitter.setSpawnRate(new ParticleVector(1, 30));
        emitters.add(emitter);
    }

    /**
     * displays an emitter with a vector variance.
     */
    public void particleExample3() {
        Emitter emitter = new Emitter(parent);
        emitter.setBounding(BoundType.WORLD);
        emitter.setDeathCondition(DeathType.BOUND);
        emitter.setCords(new ParticleVector(320, 100));
        emitter.setPositionVariance(new ParticleVector(), new ParticleVector());
        emitter.setVelocity(new ParticleVector(0, 2));
        emitter.setVelocityVariance(new ParticleVector(2, 2),
                new ParticleVector(0, 2));
        emitter.setTexture(new Image("/crops/mango3.png"));
        emitter.setSpawnRate(new ParticleVector(2, 1));
        emitters.add(emitter);
    }

    /**
     * For the one and only @leggy (spawns nyanDucks).
     */
    public void nyanDuck() {
        Emitter emitter = new Emitter(parent);
        emitter.setDeathCondition(DeathType.BOUND);
        emitter.setPositionBounds(PositionType.TOPLEFT);
        emitter.setPositionVarianceBounds(PositionVarianceType.SCREENHEIGHT);
        emitter.setVelocity(new ParticleVector(5, 0));
        emitter.setVelocityVariance(new ParticleVector(0, 10),
                new ParticleVector(5, 5));
        emitter.setTexture(new Image("/nyanDuck.png"));
        emitter.setSpawnRate(new ParticleVector(10, 1));
        emitters.add(emitter);
    }

    /**
     * Will enable the ticking of the emitters.
     */
    public void startAnimation() {
        this.animate = true;
    }

    /**
     * Will disable the emitting of the emitters.
     */
    public void stopAnimation() {
        this.animate = false;
    }

    /**
     * Get the parent canvas which particles will be drawn too.
     * @return parent canvas.
     */
    public Canvas getParent() {
        return this.parent;
    }


    /**
     * Adds an emitter.
     */
    public void addParticleSet(Emitter emitter) {
        if (emitter != null) {
            emitters.add(emitter);
        }
    }

    /**
     * Removes all children (emitters) that qualify as dead
     * (no children and cant spawn children).
     */
    private void killDeadChildren() {
        Iterator<Emitter> iterator = emitters.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isDead()) {
                iterator.remove();
            }
        }
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public Viewport getViewport() {
        return viewport;
    }

}
