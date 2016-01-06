package farmsim.entities.animals;

import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;

import java.util.Random;

public class Duck extends FarmAnimal {

    // Thresholds to determine when a pig will want to find food/drink as
    // opposed to wander
    private static final double HUNGER_THRESHOLD = 80;
    private static final double THIRST_THRESHOLD = 80;

    /* -------- PROCESSING INFORMATION -------- */
    public static final double MAX_DUCK_BREAST_QUALITY = 1;
    public static final double MIN_DUCK_BREAST_QUALITY = 0;
    public static final double MAX_WEIGHT = 1000;
    public static final double MIN_WEIGHT = 1;
    public static final double MAX_WEIGHT_INCREASE_RATE = 1;
    public static final double MIN_WEIGHT_INCREASE_RATE = 0;
    protected boolean edible; // Whether the pork from this animal is able to be
    // safely consumed
    protected double weight; // Weight of pig, to be used in determining amount
    // of pork
    protected double duckBreastQuality; // Quality of pork
    /* ---------------------------------------- */

    protected double weightIncreaseRate; // Speed at which pig's weight
    // increases

    public Duck(World world, double x, double y, double speed, double age, char
            sex, double weight) {
        super("duck", world, x, y, speed, age,
                sex);
        this.type = AnimalType.DUCK;
        this.weight = weight;
        this.wanderRange = 10;
        this.restRate = 0;
        this.thirstRate = 0;
        this.weightIncreaseRate = 0;
        this.duckBreastQuality = 0.5;
        this.edible = true;
    }

    /* GETTERS */
    public double getWeight() {
        return weight;
    }
    public boolean getEdible() {
        return edible;
    }
    public double getDuckBreastQuality() {
        return duckBreastQuality;
    }
    public double getWeightIncreaseRate() { return weightIncreaseRate;}

    /* SETTERS */
    public void setEdible(boolean edible) {
        this.edible = edible;
    }
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
    public void setDuckBreastQuality(double duckBreastQuality) {
        if (duckBreastQuality > MAX_DUCK_BREAST_QUALITY) {
            this.duckBreastQuality = MAX_DUCK_BREAST_QUALITY;
        } else if (duckBreastQuality < MIN_DUCK_BREAST_QUALITY) {
            this.duckBreastQuality = MIN_DUCK_BREAST_QUALITY;
        } else {
            this.duckBreastQuality = duckBreastQuality;
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
    }

    /**
     * state priorities: - if dead, goto DEAD state - sleep unless
     * food/drink
     * below threshold, in which case find food/drink
     */
    @Override
    public void determineState() {
        if (health <= MIN_HEALTH) {
            kill();
        } else if (restLevel <= MIN_REST_LEVEL) {
        } else if (foodLevel <= HUNGER_THRESHOLD
                || thirstLevel <= THIRST_THRESHOLD) {
            if (foodLevel <= thirstLevel) {
            } else {
            }
        } else {
                resetWanderDestination();
            }

        }
    }

    /**
     * Creates a baby duck at the location of the duck
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
        Duck newDuck = new Duck(WorldManager.getInstance().getWorld(),
                getLocation().getX(), getLocation().getY() ,0.07, 0, sex, 1);

        FarmAnimalManager.getInstance().addFarmAnimal(newDuck);
    }
