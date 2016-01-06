package farmsim.world.weather.components;

import farmsim.particle.DeathType;
import farmsim.particle.Emitter;
import farmsim.particle.ParticleController;
import farmsim.particle.ParticleVector;
import farmsim.particle.PositionVarianceType;

import java.util.ArrayList;
import java.util.HashMap;

import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;
import javafx.scene.image.Image;

/**
 * WeatherComponent - Rain.
 */
public class Rain extends WeatherComponent {
    private ArrayList<Image> images = new ArrayList<>();

    /**
     * Creates a component with a set level.
     * @param level to set the component.
     */
    public Rain(int level) {
        super("Rain", level);
        setup();
    }

    /**
     * Creates a component with a level between the bounds inclusively.
     * @param lowerLevel lowest level.
     * @param higherLevel highest level.
     */
    public Rain(int lowerLevel, int higherLevel) {
        super("Rain", lowerLevel, higherLevel);
        setup();
    }

    private void setup() {
        setImages();
        if (getLevel() == 5) {
            Emitter emitter = createEmitter(new Image("/weather/lv5rain.png"));
            emitters.add(emitter);
            addToController(emitter);
        } else {
            for (Image image : images) {
                Emitter emitter = createEmitter(image);
                emitters.add(emitter);
                addToController(emitter);
            }
        }
        addModifier();
    }

    /**
     * Attaches the image variants to the component.
     */
    private void setImages() {
        this.images.add(new Image("/weather/rain1.png"));
        this.images.add(new Image("/weather/rain2.png"));
        this.images.add(new Image("/weather/rain3.png"));
    }

    private Emitter createEmitter(Image texture) {
        Emitter emitter = new Emitter(
                ParticleController.getInstance().getParent());
        emitter.setDeathCondition(DeathType.BOUND);
        emitter.setCords(new ParticleVector());
        emitter.setPositionVarianceBounds(PositionVarianceType.SCREENWIDTH);
        emitter.setVelocity(new ParticleVector(0, 30));
        emitter.setVelocityVariance(
                new ParticleVector(5, 0), new ParticleVector(0, 10));
        emitter.setTexture(texture);
        emitter.setSpawnRate(new ParticleVector(10 * getLevel(), 1));
        return emitter;
    }

    private void addModifier() {
        World world = WorldManager.getInstance().getWorld();
        //creates a modifier
        HashMap<String, String> modifierSettings = new HashMap<>();
        modifierSettings.put("target", "all");
        modifierSettings.put("tag", "Weather");
        modifierSettings.put("position", (new Point(0, 0)).toString());
        modifierSettings.put("end", (
                new Point(world.getWidth(),world.getHeight())).toString());
        modifierSettings.put("shape", "square");
        modifierSettings.put("attribute", "moisture");
        modifierSettings.put("effect", "0.0005");

        //make the thing
        WorldManager.getInstance().getWorld().getModifierManager()
                .addNewAttributeModifier(modifierSettings);
    }
}
