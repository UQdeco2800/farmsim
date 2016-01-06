package farmsim.world.weather.components;

import farmsim.particle.DeathType;
import farmsim.particle.Emitter;
import farmsim.particle.ParticleController;
import farmsim.particle.ParticleVector;
import farmsim.particle.PositionType;
import farmsim.particle.PositionVarianceType;

import java.util.ArrayList;

import farmsim.world.WorldManager;
import farmsim.world.weather.SeasonName;
import javafx.scene.image.Image;

/**
 * WeatherComponent - Wind.
 * <p>
 *     Applies visual effect on screen and if high level applies damage.
 * </p>
 */
public class Wind extends WeatherComponent {
    private ArrayList<Image> images = new ArrayList<>();

    /**
     * Creates a component with a set level.
     * @param level to set the component.
     */
    public Wind(int level) {
        super("Wind", level);
        setup();
    }

    /**
     * Creates a component with a level between the bounds inclusively.
     * @param lowerLevel lowest level.
     * @param higherLevel highest level.
     */
    public Wind(int lowerLevel, int higherLevel) {
        super("Wind", lowerLevel, higherLevel);
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
        String season = WorldManager.getInstance().getWorld().getSeason();
        if(SeasonName.WINTER.toString().equals(season)) {
            this.images.add(new Image("/weather/windleave_winter1.png"));
            this.images.add(new Image("/weather/windleave_winter2.png"));
        } else {
            this.images.add(new Image("/weather/wind_w_leaves.png"));
            this.images.add(new Image("/weather/wind_w_leaves2.png"));
        }
        if(SeasonName.SPRING.toString().equals(season)) {
            this.images.add(new Image("/weather/flower1.png"));
            this.images.add(new Image("/weather/flower2.png"));
        }
        if(SeasonName.AUTUMN.toString().equals(season)) {
            this.images.add(new Image("/weather/autumnleave1.png"));
            this.images.add(new Image("/weather/autumnleave2.png"));
            this.images.add(new Image("/weather/autumnleave3.png"));
        }
        this.images.add(new Image("/weather/wind.png"));
    }

    private Emitter createEmitter(Image texture) {
        Emitter emitter = new Emitter(
                ParticleController.getInstance().getParent());
        emitter.setDeathCondition(DeathType.BOUND);
        emitter.setCords(new ParticleVector());
        emitter.setPositionVarianceBounds(PositionVarianceType.SCREENHEIGHT);
        emitter.setPositionBounds(PositionType.TOPRIGHT);
        emitter.setVelocity(new ParticleVector(-(2 * getLevel() + 5), 0));
        emitter.setVelocityVariance(
                new ParticleVector(0.2, 0), new ParticleVector(0.2, 0.2));
        emitter.setTexture(texture);
        emitter.setSpawnRate(new ParticleVector(1, 90 * (1.0 / getLevel())));
        return emitter;
    }
}
