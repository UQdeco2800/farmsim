package farmsim.world.weather.components;

import farmsim.particle.DeathType;
import farmsim.particle.Emitter;
import farmsim.particle.ParticleController;
import farmsim.particle.ParticleVector;
import farmsim.particle.PositionVarianceType;

import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * WeatherComponent - Snow.
 * <p>
 *     Creates snow and applies snow particles on screen and snow tiles in game.
 * </p>
 */
public class Snow extends WeatherComponent {
    private ArrayList<Image> images = new ArrayList<>();

    /**
     * Creates a component with a set level.
     * @param level to set the component.
     */
    public Snow(int level) {
        super("Snow", level);
        setup();
    }

    /**
     * Creates a component with a level between the bounds inclusively.
     * @param lowerLevel lowest level.
     * @param higherLevel highest level.
     */
    public Snow(int lowerLevel, int higherLevel) {
        super("Snow", lowerLevel, higherLevel);
        setup();
    }

    private void setup() {
        setImages();
        for (Image image : images) {
            Emitter emitter = createEmitter(image);
            emitters.add(emitter);
            addToController(emitter);
        }
        if (this.getLevel() < 4) {
            Emitter emitter = createEmitter(
                    new Image("/weather/snowflake.png"));
            emitters.add(emitter);
            addToController(emitter);
        }
    }

    private Emitter createEmitter(Image texture) {
        Emitter emitter = new Emitter(
                ParticleController.getInstance().getParent());
        emitter.setDeathCondition(DeathType.BOUND);
        emitter.setCords(new ParticleVector());
        emitter.setPositionVarianceBounds(PositionVarianceType.SCREENWIDTH);
        emitter.setVelocity(new ParticleVector(0, 5));
        emitter.setVelocityVariance(
                new ParticleVector(5, 5), new ParticleVector(0, 3));
        emitter.setTexture(texture);
        emitter.setSpawnRate(new ParticleVector(getLevel(), 1));
        return emitter;
    }

    /**
     * Attaches the image variants to the component.
     */
    private void setImages() {
        this.images.add(new Image("/weather/snow1.png"));
        this.images.add(new Image("/weather/snow2.png"));
    }
}
