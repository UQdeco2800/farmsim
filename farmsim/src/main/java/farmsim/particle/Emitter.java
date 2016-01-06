package farmsim.particle;

import farmsim.tiles.TileRegister;
import farmsim.util.Tickable;
import farmsim.world.World;
import farmsim.world.WorldManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

/**
 * Creates and displays particles.
 * <p>
 * Given an image, frequency, spread, velocity and variance it will create a
 * node to display particles. Will recycle the particles.
 * </p>
 * <p>
 * Only creates permanent particles atm. As in the particles must be killed by
 * going off the screen or by the emitter dieing. Particles cannot fade with
 * time as of yet.
 * </p>
 * <p>
 * Emitter has a bound type.
 * Emitter has cords.
 * Emitter has particles/per tick or particles/instance per tick is for bound
 *     types that arnt panning per instance is the panning type
 * Emitter particle death type particles either die from bounds or from time
 * Emitter has variance in the position (radius)
 * Emitter has variance in the velocity
 * Emitter has a image
 * </p>
 */
public class Emitter implements Tickable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Emitter.class);

    private Canvas parent;
    private List<ParticleElement> children;
    private Random random = new Random();
    private int tickRateCounter = 1;

    // emitter settings
    private BoundType bounding;
    private PositionVarianceType positionVarianceBounds;
    private PositionType positionBounds;
    private DeathType death;
    private int deathTime = 0;
    private ParticleVector position;
    private ParticleVector velocity;
    private ParticleVector rate;
    private ParticleVector positionVarianceX;
    private ParticleVector positionVarianceY;
    private ParticleVector velocityVarianceX;
    private ParticleVector velocityVarianceY;
    private Image texture;
    private boolean spawnChildren = true;

    /**
     * A Particle Emitter.
     *
     * @param parent the parent to attach particles to.
     */
    public Emitter(Canvas parent) {
        if (parent != null) {
            this.parent = parent;
        } else {
            LOGGER.error("Bad parent");
        }
        children = Collections.synchronizedList(new ArrayList<>());
        position = new ParticleVector();
        velocity = new ParticleVector();
        rate = new ParticleVector(1, 1);
        positionVarianceX = new ParticleVector();
        positionVarianceY = new ParticleVector();
        velocityVarianceX = new ParticleVector();
        velocityVarianceY = new ParticleVector();
        bounding = BoundType.FREE;
    }

    /**
     * Applies a bounding to the Emitter (changes default)
     *
     * @param bounding to set.
     */
    public void setBounding(BoundType bounding) {
        if (bounding != null) {
            this.bounding = bounding;
        } else {
            LOGGER.error("Tried to set null bounding");
        }
    }

    /**
     * Applies a position cords to the Emitter.
     *
     * @param position to set.
     */
    public void setCords(ParticleVector position) {
        if (position != null) {
            this.position.set(position.getX(), position.getY());
        }
    }

    /**
     * Applies a velocity to the Emitter.
     *
     * @param velocity to set.
     */
    public void setVelocity(ParticleVector velocity) {
        if (velocity != null) {
            this.velocity = velocity;
        }
    }

    /**
     * Sets the spawn rate of the emitter.
     * <p>
     * The rate is amount/tick, so if wanted to skip a tick [20,2] would create
     * 20 particles every second tick.
     *
     * rate[0] = particles/tick. rate[1] = on every X tick.
     * </p>
     */
    public void setSpawnRate(ParticleVector rate) {
        if (rate != null) {
            this.rate.set(rate.getX(), rate.getY());
        }
    }

    /**
     * Sets the death condition for the Emitter.
     *
     * @param death to set.
     */
    public void setDeathCondition(DeathType death) {
        if (death != null) {
            this.death = death;
        }
    }

    /**
     * Sets the death condition with optional integer parameter, useful for some
     * death conditions.
     *
     * @param death to set.
     * @param time  to apply.
     */
    public void setDeathCondition(DeathType death, int time) {
        setDeathCondition(death);
        this.deathTime = time;
    }

    /**
     * Applies the positional variance to the emitter.
     *
     * @param varianceX the x variance to set.
     * @param varianceY the y variance to set.
     */
    public void setPositionVariance(ParticleVector varianceX,
                                    ParticleVector varianceY) {
        if (varianceX != null && varianceY != null) {
            this.positionVarianceX.set(varianceX.getX(), varianceX.getY());
            this.positionVarianceY.set(varianceY.getX(), varianceY.getY());
        }
    }

    /**
     * Position Bounds for setting the location of the emitter to a corner of
     * the screen instead of pre defined coordinates.
     * <p>
     * attaches the emitter to a location on the screen which may change
     * when screen is resized.
     * </p>
     * @param positionBounds to bound the emitter too.
     */
    public void setPositionBounds(PositionType positionBounds) {
        this.positionBounds = positionBounds;
    }

    /**
     * Position variance bounds for setting the variance of the emitter to fill
     * and area of the screen instead of pre defined values.
     * <p>
     * this allows the emitter to expand its spawn area when the game screen
     * increases.
     * </p>
     * @param positionBounds to bound the emitter too.
     */
    public void setPositionVarianceBounds(PositionVarianceType positionBounds) {
        this.positionVarianceBounds = positionBounds;
    }

    /**
     * Applies the velocity variance to the emitter.
     *
     * @param varianceX to set for X axis.
     * @param varianceY to set for Y axis.
     */
    public void setVelocityVariance(ParticleVector varianceX,
                                    ParticleVector varianceY) {
        if (varianceX != null && varianceY != null) {
            this.velocityVarianceX.set(varianceX.getX(), varianceX.getY());
            this.velocityVarianceY.set(varianceY.getX(), varianceY.getY());
        }
    }

    /**
     * Sets the texture element of the particle.
     *
     * @param texture to be applied.
     */
    public void setTexture(Image texture) {
        if (texture != null) {
            this.texture = texture;
        }
    }

    /**
     * Get list of children.
     * note: is mutable.
     * @return list of all particle elements.
     */
    public List<ParticleElement> getChildren() {
        return children;
    }

    /**
     * Creates a child.
     */
    private void createNewChildren() {
        ParticleElement particle = createSingleChild();
        children.add(particle);
    }

    /**
     * Creates a single Child.
     *
     * @return a single child with initial settings.
     */
    private ParticleElement createSingleChild() {
        return new ParticleElement(
                new ParticleVector(
                        elementCalcPosX(),
                        elementCalcPosY()),
                new ParticleVector(
                        elementCalcVelX(),
                        elementCalcVelY()
                ),
                texture.getWidth(),
                texture.getHeight(),
                texture);
    }

    /**
     * Calculates the X position of the particles with randomness with
     * the variance if set and incorporates all bounding's set.
     * @return x position of particle.
     */
    private double elementCalcPosX() {
        if (this.positionVarianceBounds != null
                && (positionVarianceBounds == PositionVarianceType.SCREENWIDTH
                    || positionVarianceBounds == PositionVarianceType.SCREEN)) {
            positionVarianceX = new ParticleVector(0, this.parent.getWidth());
        }
        if (this.positionVarianceBounds != null
                && (positionVarianceBounds == PositionVarianceType.WORLDWIDTH
                || positionVarianceBounds == PositionVarianceType.WORLD)) {
            World world = WorldManager.getInstance().getWorld();
            positionVarianceX = new ParticleVector(0,
                    world.getWidth() * TileRegister.TILE_SIZE);
        }
        if (this.positionBounds != null) {
            if (positionBounds == PositionType.TOPLEFT
                    || positionBounds == PositionType.BOTTOMLEFT) {
                position.set(0, position.getY());
            } else if (positionBounds == PositionType.TOPRIGHT
                    || positionBounds == PositionType.BOTTOMRIGHT) {
                if(this.bounding == BoundType.FREE) {
                    position.set(parent.getWidth(), position.getY());
                } else {
                    World world = WorldManager.getInstance().getWorld();
                    position.set((double) world.getWidth()
                                    * TileRegister.TILE_SIZE,
                            position.getY());
                }

            }
        }
        return (random.nextDouble() * positionVarianceX.getX() * (-1))
                + (random.nextDouble() * positionVarianceX.getY())
                + position.getX();
    }

    /**
     * Calculates the Y position of the particles with randomness with
     * the variance if set and incorporates all bounding's set.
     * @return Y position of particle.
     */
    private double elementCalcPosY() {
        if (this.positionVarianceBounds != null
                && (positionVarianceBounds == PositionVarianceType.SCREENHEIGHT
                    || positionVarianceBounds == PositionVarianceType.SCREEN)) {
            positionVarianceY = new ParticleVector(0, this.parent.getHeight());
        }
        if (this.positionVarianceBounds != null
                && (positionVarianceBounds == PositionVarianceType.WORLDHEIGHT
                || positionVarianceBounds == PositionVarianceType.WORLD)) {
            World world = WorldManager.getInstance().getWorld();
            positionVarianceY = new ParticleVector(0,
                    world.getHeight() * TileRegister.TILE_SIZE);
        }
        if (this.positionBounds != null) {
            if (positionBounds == PositionType.TOPLEFT
                    || positionBounds == PositionType.TOPRIGHT) {
                position.set(position.getX(), 0);
            } else if (positionBounds == PositionType.BOTTOMLEFT
                    || positionBounds == PositionType.BOTTOMRIGHT) {
                if(this.bounding == BoundType.FREE) {
                    position.set(position.getX(), parent.getHeight());
                } else {
                    World world = WorldManager.getInstance().getWorld();
                    position.set(position.getX(),
                            (double) world.getHeight()
                                    * TileRegister.TILE_SIZE);
                }
            }
        }
        return (random.nextDouble() * positionVarianceY.getX() * (-1))
                + (random.nextDouble() * positionVarianceY.getY())
                + position.getY();
    }

    /**
     * Calculates the X velocity of the particles with randomness with
     * the variance if set.
     * @return x velocity of particle.
     */
    private double elementCalcVelX() {
        return (random.nextDouble() * velocityVarianceX.getX() * (-1))
                + (random.nextDouble() * velocityVarianceX.getY())
                + velocity.getX();
    }

    /**
     * Calculates the Y velocity of the particles with randomness with
     * the variance if set.
     * @return Y velocity of particle.
     */
    private double elementCalcVelY() {
        return (random.nextDouble() * velocityVarianceY.getX() * (-1))
                + (random.nextDouble() * velocityVarianceY.getY())
                + velocity.getY();
    }

    /**
     * Animation tick (Moves the Particles).
     */
    @Override
    public void tick() {
        children.stream().parallel().forEach(ParticleElement::move);
    }

    /**
     * Create new particles if not set to die.
     */
    public void spawn() {
        if (spawnChildren) {
            populateChildren();
        }
    }

    /**
     * Create children according to the rate.
     */
    private void populateChildren() {
        if (this.tickRateCounter >= this.rate.getY()) {
            for (int i = 0; i < this.rate.getX(); i++) {
                createNewChildren();
            }
            tickRateCounter = 1;
        } else {
            tickRateCounter++;
        }
    }

    /**
     * Kills any dead particles (either out of bounds or time based).
     */
    public void killDeadChildren() {
        Iterator<ParticleElement> iterator = children.iterator();
        while (iterator.hasNext()) {
            if (death == DeathType.BOUND) {
                killDeadBoundChildren(iterator);
            } else {
                if (iterator.next().isDead(deathTime)) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Removes children which are bound but the current view or world view.
     * @param iterator current iteration pointer of the children.
     */
    private void killDeadBoundChildren(Iterator<ParticleElement> iterator) {
        if (bounding == BoundType.WORLD) {
            World world = WorldManager.getInstance().getWorld();
            if (iterator.next().isDead(
                    (double) world.getWidth() * TileRegister.TILE_SIZE,
                    (double) world.getHeight() * TileRegister.TILE_SIZE)) {
                iterator.remove();
            }
        } else {
            if (iterator.next().isDead(parent.getWidth(),
                    parent.getHeight())) {
                iterator.remove();
            }
        }
    }

    /**
     * Get the death type associated with the emitter.
     * @return death type applied to the emitter (bounds is default).
     */
    public DeathType getDeath() {
        return death;
    }

    /**
     * Kills the emitter (stops from spawning children).
     * Once all children (particles) are dead it will be killed by the Particle
     * Controller, which allows a transitional effect of dead emitters.
     */
    public void kill() {
        this.spawnChildren = false;
    }

    /**
     * Gets the state of the emitter of whether it is dead or alive.
     * @return true if no children and cant spawn children, false otherwise.
     */
    public boolean isDead() {
        return !this.spawnChildren && this.children.isEmpty();
    }

    public BoundType getBounding() {
        return bounding;
    }
}
