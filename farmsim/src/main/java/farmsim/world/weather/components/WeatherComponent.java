package farmsim.world.weather.components;

import farmsim.particle.Emitter;
import farmsim.particle.ParticleController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Random;

import javafx.application.Platform;

/**
 * WeatherComponent.
 * <p>
 *     A single aspect that forms a weather.
 * </p>
 */
public class WeatherComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            WeatherComponent.class);
    private String name = "Default";
    private int level = 1;
    protected ArrayList<Emitter> emitters = new ArrayList<>();

    /**
     * Creates a component with a name.
     * @param name of the component.
     */
    public WeatherComponent(String name) {
        this.name = name;
    }

    /**
     * Component with a name and a set level.
     * @param name of the component.
     * @param level level of the component.
     */
    public WeatherComponent(String name, int level) {
        this.name = name;
        this.setLevel(level);
    }

    /**
     * Creates a component with a name and a level between the range
     * inclusively.
     * @param name of the component.
     * @param lowerLevel lowest level range.
     * @param higherLevel highest level range.
     */
    public WeatherComponent(String name, int lowerLevel, int higherLevel) {
        this.name = name;
        Random rand = new Random();
        if (lowerLevel <= higherLevel) {
            this.setLevel(
                    rand.nextInt((higherLevel - lowerLevel) + 1) + lowerLevel);
        }
    }

    /**
     * String representation of the component.
     * @return the name of the component.
     */
    @Override
    public String toString() {
        return this.getName();
    }

    /**
     * The name of the component.
     * @return the string of the name of the component.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Resets the error of the level.
     * @param newLevel the level to set the component too.
     */
    public void setLevel(int newLevel) {
        if (newLevel > 0 && newLevel < 6) {
            this.level = newLevel;
        } else {
            this.errorLevelRange();
        }
    }

    /**
     * The level the component is currently set to.
     * @return the level of the component.
     */
    public int getLevel() {
        return this.level;
    }

    protected void errorLevelRange() {
        LOGGER.error("component range invalid");
    }

    /**
     * Adds an emitter to the ParticleController.
     * @param emitter to be added to the particle controller.
     */
    protected void addToController(Emitter emitter) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ParticleController.getInstance().addParticleSet(emitter);
            }
        });
    }

    /**
     * Kills all emitters that the component spawned. Also removes it from the
     * memory of the component.
     */
    public void kill() {
        for (Emitter emitter : emitters) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    emitter.kill();
                }
            });
        }
        emitters = new ArrayList<>();
    }
}
