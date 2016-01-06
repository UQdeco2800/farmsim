package farmsim.entities.animals;

import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;

import java.util.Random;

public class Chicken extends FarmAnimal {

    // Thresholds to determine when a chicken will want to find food/drink as
    // opposed to wander
    private static final double HUNGER_THRESHOLD = 80;
    private static final double THIRST_THRESHOLD = 80;

    /* -------- PROCESSING INFORMATION -------- */
    public static final double MAX_FEATHER_QUALITY = 1; // God-tier milk
    public static final double MIN_FEATHER_QUALITY = 0; // Milk unable to be safely
    // consumed
    public static final double MAX_CHICKEN_BREAST_QUALITY = 1;
    public static final double MIN_CHICKEN_BREAST_QUALITY = 0;
    public static final double MAX_EGG_QUALITY = 1;
    public static final double MIN_EGG_QUALITY = 0;
    public static final double MAX_EGG_QUANTITY = 100;
    public static final double MIN_EGG_QUANTITY = 0;
    public static final double MAX_WEIGHT = 1000;
    public static final double MIN_WEIGHT = 1;
    public static final double MAX_WEIGHT_INCREASE_RATE = 1;
    public static final double MIN_WEIGHT_INCREASE_RATE = 0;
    protected boolean edible; // Whether the beef from this animal is able to be
    // safely consumed
    protected double weight; // Weight of cow, to be used in determining amount
    // of beef
    protected double chickenBreastQuality; // Quality of beef
    protected double featherQuality;
    protected boolean eggable; // Whether the cow is able to be milked or not
    protected double eggQuantity; // Quantity of milk available for milking
    protected double eggQuality; // Quality of milk produced by the cow
    /* ---------------------------------------- */

    protected double eggProductionRate; // Speed at which cow produces milk
    protected double weightIncreaseRate; // Speed at which cow's weight
    // increases

    public Chicken(World world, double x, double y, double speed, double age, char
            sex, double weight) {
        super("chicken", world, x, y, speed, age,
                sex);
        this.type = AnimalType.CHICKEN;
        this.weight = weight;
        this.wanderRange = 10;
        this.restRate = 0;
        this.thirstRate = 0;
        this.weightIncreaseRate = 0;
        this.chickenBreastQuality = 0.5;
        this.featherQuality = 0.5;
        this.edible = true;
        this.eggable = true;
    }

    /* GETTERS */

    public double getWeight() {
        return weight;
    }
    public boolean getEggable() {
        return eggable;
    }
    public boolean getEdible() {
        return edible;
    }
    public double getEggQuantity() {
        return eggQuantity;
    }
    public double getEggQuality() {
        return eggQuality;
    }
    public double getChickBreastQuality() {
        return chickenBreastQuality;
    }
    public double getFeatherQuality() {
        return featherQuality;
    }
    public double getWeightIncreaseRate() { return weightIncreaseRate;}


    /* SETTERS */
    public void setEdible(boolean edible) {
        this.edible = edible;
    }
    public void setEggable(boolean milkable) {
        this.eggable = milkable;
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
    public void setEggQuantity(double eggQuantity) {
        if (eggQuantity > MAX_EGG_QUANTITY) {
            this.eggQuantity = MAX_EGG_QUANTITY;
        } else if (eggQuantity < MIN_EGG_QUANTITY) {
            this.eggQuantity = MIN_EGG_QUANTITY;
        } else {
            this.eggQuantity = eggQuantity;
        }
    }

    public void setEggQuality(double eggQuality) {
        if (eggQuality > MAX_EGG_QUALITY) {
            this.eggQuality = MAX_EGG_QUALITY;
        } else if (eggQuality < MIN_EGG_QUALITY) {
            this.eggQuality = MIN_EGG_QUALITY;
        } else {
            this.eggQuality = eggQuality;
        }
    }

    public void setFeatherQuality(double featherQuality) {
        if (featherQuality > MAX_FEATHER_QUALITY) {
            this.featherQuality = MAX_FEATHER_QUALITY;
        } else if (featherQuality < MIN_FEATHER_QUALITY) {
            this.featherQuality = MIN_FEATHER_QUALITY;
        } else {
            this.featherQuality = featherQuality;
        }
    }


    /**
     */
    public void setChickenBreastQuality(double chickenBreastQuality) {
        if (chickenBreastQuality > MAX_CHICKEN_BREAST_QUALITY) {
            this.chickenBreastQuality = MAX_CHICKEN_BREAST_QUALITY;
        } else if (chickenBreastQuality < MIN_CHICKEN_BREAST_QUALITY) {
            this.chickenBreastQuality = MIN_CHICKEN_BREAST_QUALITY;
        } else {
            this.chickenBreastQuality = chickenBreastQuality;
        }
    }

    /**
     * Sets the chicken's weight increase rate
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
        setEggQuantity(eggQuantity + eggProductionRate);
        setWeight(weight + weightIncreaseRate);
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
        } else {
                resetWanderDestination();
            }

        }
    }

    /**
     * Creates a baby chicken at the location of the chicken
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
        Chicken newChicken = new Chicken(WorldManager.getInstance().getWorld(),
                getLocation().getX(), getLocation().getY() ,0.07, 0, sex, 1);

        FarmAnimalManager.getInstance().addFarmAnimal(newChicken);
    }
}