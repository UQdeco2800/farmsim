package farmsim.world.weather.components;

import farmsim.particle.DeathType;
import farmsim.particle.Emitter;
import farmsim.particle.ParticleController;
import farmsim.particle.ParticleVector;
import farmsim.particle.PositionVarianceType;

import java.util.ArrayList;

import javafx.scene.image.Image;

/**
 * WeatherComponent - Hail.
 * <p>
 *     Damages entities in game and display hail rocks on the screen.
 * </p>
 */
public class Hail extends WeatherComponent {
    private ArrayList<Image> images = new ArrayList<>();

    /**
     * Creates a component with a set level.
     * @param level to set the component.
     */
    public Hail(int level) {
        super("Hail", level);
        setup();
    }

    /**
     * Creates a component with a level between the bounds inclusively.
     * @param lowerLevel lowest level.
     * @param higherLevel highest level.
     */
    public Hail(int lowerLevel, int higherLevel) {
        super("Hail", lowerLevel, higherLevel);
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
        this.images.add(new Image("/weather/hale1.png"));
        this.images.add(new Image("/weather/hale2.png"));
    }

    private Emitter createEmitter(Image texture) {
        Emitter emitter = new Emitter(
                ParticleController.getInstance().getParent());
        emitter.setDeathCondition(DeathType.BOUND);
        emitter.setCords(new ParticleVector());
        emitter.setPositionVarianceBounds(PositionVarianceType.SCREENWIDTH);
        emitter.setVelocity(new ParticleVector(0, 10));
        emitter.setVelocityVariance(
                new ParticleVector(5, 5), new ParticleVector(0, 10));
        emitter.setTexture(texture);
        emitter.setSpawnRate(new ParticleVector((2 * getLevel()) + 1, 1));
        return emitter;
    }
}
