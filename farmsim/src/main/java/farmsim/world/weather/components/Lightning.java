package farmsim.world.weather.components;

import java.util.ArrayList;

import farmsim.particle.*;
import javafx.scene.image.Image;

/**
 * WeatherComponent - Lightning.
 * <p>
 *     Applies visual effect on screen and can start fires.
 * </p>
 */
public class Lightning extends WeatherComponent {
    private ArrayList<Image> images = new ArrayList<>();

    /**
     * Creates a component with a set level.
     * @param level to set the component.
     */
    public Lightning(int level) {
        super("Lightning", level);
        setup();
    }

    /**
     * Creates a component with a level between the bounds inclusively.
     * @param lowerLevel lowest level.
     * @param higherLevel highest level.
     */
    public Lightning(int lowerLevel, int higherLevel) {
        super("Lightning", lowerLevel, higherLevel);
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
        this.images.add(new Image("/weather/thunderstorm1.png"));
        this.images.add(new Image("/weather/thunderstorm2.png"));
    }

    private Emitter createEmitter(Image texture) {
        int level = getLevel();
        Emitter emitter = new Emitter(
                ParticleController.getInstance().getParent());
        emitter.setBounding(BoundType.WORLD);
        emitter.setDeathCondition(DeathType.TIME, 10);
        emitter.setCords(new ParticleVector());
        emitter.setPositionVarianceBounds(PositionVarianceType.WORLD);
        emitter.setTexture(texture);
        emitter.setSpawnRate(new ParticleVector(level, 200));
        return emitter;
    }
}
