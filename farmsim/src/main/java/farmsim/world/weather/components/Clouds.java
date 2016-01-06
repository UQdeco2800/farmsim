package farmsim.world.weather.components;

import farmsim.particle.*;

import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * WeatherComponent - Clouds.
 * <p>
 *     Applies clouds to be displayed on the games ui.
 * </p>
 */
public class Clouds extends WeatherComponent {
    private ArrayList<Image> images = new ArrayList<>();

    /**
     * Creates a component with a set level.
     * @param level to set the component.
     */
    public Clouds(int level) {
        super("Clouds", level);
        setup();
    }

    /**
     * Creates a component with a level between the bounds inclusively.
     * @param lowerLevel lowest level.
     * @param higherLevel highest level.
     */
    public Clouds(int lowerLevel, int higherLevel) {
        super("Clouds", lowerLevel, higherLevel);
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
        this.images.add(new Image("/weather/cloud1.png"));
        this.images.add(new Image("/weather/cloud2.png"));
        this.images.add(new Image("/weather/cloud3.png"));
    }

    private Emitter createEmitter(Image texture) {
        int level = getLevel();
        Emitter emitter = new Emitter(
                ParticleController.getInstance().getParent());
        emitter.setBounding(BoundType.WORLD);
        emitter.setDeathCondition(DeathType.BOUND);
        emitter.setCords(new ParticleVector());
        emitter.setPositionVarianceBounds(PositionVarianceType.WORLDWIDTH);
        emitter.setVelocity(new ParticleVector(0, 0.1));
        emitter.setVelocityVariance(
                new ParticleVector(0, 0),
                new ParticleVector(0.1, 0.1*level));
        emitter.setTexture(texture);
        emitter.setSpawnRate(new ParticleVector(Math.ceil((double)level/2),
                500));
        return emitter;
    }
}
