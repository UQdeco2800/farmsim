package farmsim.entities.animals;

import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;

import java.util.Random;

public class Pig extends FarmAnimal {

    // Thresholds to determine when a pig will want to find food/drink as
    // opposed to wander
    private static final double HUNGER_THRESHOLD = 80;
    private static final double THIRST_THRESHOLD = 80;

    /* -------- PROCESSING INFORMATION -------- */
    public static final double MAX_PORK_QUALITY = 1;
    public static final double MIN_PORK_QUALITY = 0;
    public static final double MAX_WEIGHT = 1500;
    public static final double MIN_WEIGHT = 1;
    public static final double MAX_WEIGHT_INCREASE_RATE = 1;
    public static final double MIN_WEIGHT_INCREASE_RATE = 0;

    protected boolean edible; // Whether the pork from this animal is able to be
    // safely consumed
    protected double weight; // Weight of pig, to be used in determining amount
    // of pork
    protected double porkQuality; // Quality of pork
    /* ---------------------------------------- */

    protected double weightIncreaseRate; // Speed at which pig's weight
    // increases

    public Pig(World world, double x, double y, double speed, double age, char
            sex, double weight) {
        super("pig", world, x, y, speed, age, sex);
        this.type = AnimalType.PIG;
        this.weight = weight;
        this.wanderRange = 10;
        this.restRate = 0;
        this.thirstRate = 0;
        this.weightIncreaseRate = 0;
        this.porkQuality = 0.5;
        this.edible = true;
    }

    /* GETTERS */

    public double getWeight() {
        return weight;
    }

    public boolean getEdible() {
        return edible;
    }

    public double getPorkQuality() {
        return porkQuality;
    }

    /* SETTERS */

    /**
     */
    public void setEdible(boolean edible) {
        this.edible = edible;
    }

    /**
     */
    public void setWeight(double weight) {
        if (weight > MAX_WEIGHT) {
            this.weight = MAX_WEIGHT;
        } else if (weight < MIN_WEIGHT) {
            this.weight = MIN_WEIGHT;
        } else {
            this.weight = weight;
        }
    }

    /**
     */
    public void setPorkQuality(double porkQuality) {
        if (porkQuality > MAX_PORK_QUALITY) {
            this.porkQuality = MAX_PORK_QUALITY;
        } else if (porkQuality < MIN_PORK_QUALITY) {
            this.porkQuality = MIN_PORK_QUALITY;
        } else {
            this.porkQuality = porkQuality;
        }
    }

    /**
     */
    public void setWeightIncreaseRate(double weightIncreaseRate) {
        if (weightIncreaseRate > MAX_WEIGHT_INCREASE_RATE) {
            this.weightIncreaseRate = MAX_WEIGHT_INCREASE_RATE;
        } else if (weightIncreaseRate < MIN_WEIGHT_INCREASE_RATE) {
            this.weightIncreaseRate = MIN_WEIGHT_INCREASE_RATE;
        } else {
            this.weightIncreaseRate = weightIncreaseRate;
        }
    }

    /**
     * tickUpdate method should include all the changes to variables that occur
     * every tick (weight etc.) determineState method should
     * include the logic for determining which state should be entered
     * findHandler is a case statement which calls the correct state handler.
     * All generic handlers are in FarmAnimal
     */
    @Override
    protected void tickUpdate() {
        super.tickUpdate();
        setWeight(weight + weightIncreaseRate);
        pregnancyUpdate();
    }

    /**
     * state priorities: - if dead, goto DEAD state - sleep unless food/drink
     * below threshold, in which case find food/drink
     */
    @Override
    public void determineState() {
        if (health <= MIN_HEALTH) {
        } else if (restLevel <= MIN_REST_LEVEL) {
        } else if (foodLevel <= HUNGER_THRESHOLD
                || thirstLevel <= THIRST_THRESHOLD) {
            if (foodLevel <= thirstLevel) {
            } else {
            }
        } else if (readyToMate && getAvailableMate() != null) {
        } else if (targetMate != null) {
        }else {
                resetWanderDestination();
            }
        }
    }

    /**
     * Creates a baby pig at the location of the pig
     */
    @Override
    protected void createBaby() {
        Random randomGenerator = new Random(identifier.getLeastSignificantBits() *
                System.currentTimeMillis());
        char sex;
        if (randomGenerator.nextInt() < 0.5) {
            sex = 'f';
        }
        else {
            sex = 'm';
        }
        Pig newPig = new Pig(WorldManager.getInstance().getWorld(),
                getLocation().getX(), getLocation().getY() ,0.07, 0, sex, 1);

        FarmAnimalManager.getInstance().addFarmAnimal(newPig);
    }

}