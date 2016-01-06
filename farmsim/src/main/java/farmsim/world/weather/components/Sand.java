package farmsim.world.weather.components;

import java.util.ArrayList;

import farmsim.particle.*;
import javafx.scene.image.Image;

/**
 * WeatherComponent - Sand.
 * <p>
 *     Applies visual effect on screen and applies tick damage.
 * </p>
 */
public class Sand extends WeatherComponent {
    private ArrayList<Image> images = new ArrayList<>();

    /**
     * Creates a component with a set level.
     * @param level to set the component.
     */
    public Sand(int level) {
        super("Sand", level);
        setImages();
    }

    /**
     * Creates a component with a level between the bounds inclusively.
     * @param lowerLevel lowest level.
     * @param higherLevel highest level.
     */
    public Sand(int lowerLevel, int higherLevel) {
        super("Sand", lowerLevel, higherLevel);
        setup();
    }

    private void setup() {
        setImages();
        for (Image image : images) {
            Emitter emitter = createEmitter(image);
            emitters.add(emitter);
            addToController(emitter);
        }
    }

    /**
     * Attaches the image variants to the component.
     */
    private void setImages() {
        this.images.add(new Image("/weather/sandstorm.png"));
    }

    private Emitter createEmitter(Image texture) {
        Emitter emitter = new Emitter(
                ParticleController.getInstance().getParent());
        emitter.setDeathCondition(DeathType.BOUND);
        emitter.setCords(new ParticleVector());
        emitter.setBounding(BoundType.WORLD);
        emitter.setPositionVarianceBounds(PositionVarianceType.WORLDHEIGHT);
        emitter.setPositionBounds(PositionType.TOPRIGHT);
        emitter.setVelocity(new ParticleVector(-(2 * getLevel() + 5), 0));
        emitter.setVelocityVariance(
                new ParticleVector(0, 0), new ParticleVector(0, 0));
        emitter.setTexture(texture);
        emitter.setSpawnRate(new ParticleVector(2 * Math.ceil(getLevel()/2.0), 1));
        return emitter;
    }
}
