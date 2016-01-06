package farmsim.entities.animals;

import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;

import java.util.Random;

public class Sheep extends FarmAnimal {

    /* -------- PROCESSING INFORMATION -------- */
    public static final double MAX_WOOL_QUALITY = 1; // God-tier wool
    public static final double MIN_WOOL_QUALITY = 0; // wool unsuitable for use
    public static final double MAX_LAMB_QUALITY = 1;
    public static final double MIN_LAMB_QUALITY = 0;
    public static final double MAX_WOOL_QUANTITY = 100;
    public static final double MIN_WOOL_QUANTITY = 0;
    public static final double MAX_WEIGHT = 2000;
    public static final double MIN_WEIGHT = 1;
    public static final double MAX_WOOL_PRODUCTION_RATE = 1;
    public static final double MIN_WOOL_PRODUCTION_RATE = 0;
    public static final double MAX_WEIGHT_INCREASE_RATE = 1;
    public static final double MIN_WEIGHT_INCREASE_RATE = 0;
    // Thresholds to determine when a sheep will want to enter a certain state
    private static final double HUNGER_THRESHOLD_L = 70;
    private static final double HUNGER_THRESHOLD_H = 100;
    private static final double THIRST_THRESHOLD_L = 70;
    private static final double THIRST_THRESHOLD_H = 100;
    private static final double REST_THRESHOLD_H = 20;
    protected boolean edible; // Whether the lamb from this animal is able to be
    // safely consumed
    protected double weight; // Weight of sheep, to be used in determining amount
    // of lamb
    protected double lambQuality; // Quality of lamb
    // increases
    /* ---------------------------------------- */
    protected boolean shearable; // Whether the sheep is able to be sheared or
    // not
    protected double woolQuantity; // Quantity of wool available for shearing
    protected double woolQuality; // Quality of wool produced by the sheep
    protected double woolProductionRate; // Speed at which sheep produces wool
    protected double weightIncreaseRate; // Speed at which sheep's weight

    public Sheep(World world, double x, double y, double speed, double age, char
            sex, double weight) {
        super("sheep", world, x, y, speed, age,
                sex);
        this.type = AnimalType.SHEEP;
        this.weight = weight;
        this.shearable = true;
        this.edible = true;
        this.woolQuantity = 0.5;
        this.woolQuality = 0.5;
        this.woolProductionRate = 0;
        this.lambQuality = 0.5;
        this.weightIncreaseRate = 0;
        this.wanderRange = 20;
        this.restRate = 0;
        this.thirstRate = 0;
    }

    /* GETTERS */

    /**
     * Returns the weight increase rate of the sheep
     **/
    public double getWeightIncreaseRate() {
        return weightIncreaseRate;
    }

    /**
     * Returns the wool production rate of the sheep
     **/
    public double getWoolProductionRate() {
        return woolProductionRate;
    }

    /**
     * Returns whether the sheep is pregnant
     **/
    public boolean getPregnant() {
        return pregnant;
    }

    /**
     * Returns the weight of the sheep
     **/
    public double getWeight() {
        return weight;
    }

    /**
     * Returns whether the sheep is shearable
     **/
    public boolean getShearable() {
        return shearable;
    }

    /**
     * Returns whether the sheep is edible
     **/
    public boolean getEdible() {
        return edible;
    }

    /**
     * Returns the wool quantity of the sheep
     **/
    public double getWoolQuantity() {
        return woolQuantity;
    }

    /**
     * Returns the wool quality of the sheep
     **/
    public double getWoolQuality() {
        return woolQuality;
    }

    /**
     * Returns the lamb quality of the sheep
     **/
    public double getLambQuality() {
        return lambQuality;
    }
    /* SETTERS */

    /**
     * Sets whether the sheep is shearable or not
     */
    public void setShearable(boolean shearable) {
        this.shearable = shearable;
    }

    /**
     * Sets whether the sheep is edible or not
     */
    public void setEdible(boolean edible) {
        this.edible = edible;
    }

    /**
     * Sets the sheep's weight
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
     * Sets the sheep's wool quantity
     */
    public void setWoolQuantity(double woolQuantity) {
        if (woolQuantity > MAX_WOOL_QUANTITY) {
            this.woolQuantity = MAX_WOOL_QUANTITY;
        } else if (woolQuantity < MIN_WOOL_QUANTITY) {
            this.woolQuantity = MIN_WOOL_QUANTITY;
        } else {
            this.woolQuantity = woolQuantity;
        }
    }

    /**
     * Sets the sheep's wool quality
     */
    public void setWoolQuality(double woolQuality) {
        if (woolQuality > MAX_WOOL_QUALITY) {
            this.woolQuality = MAX_WOOL_QUALITY;
        } else if (woolQuality < MIN_WOOL_QUALITY) {
            this.woolQuality = MIN_WOOL_QUALITY;
        } else {
            this.woolQuality = woolQuality;
        }
    }

    /**
     * Sets the sheep's lamb quality
     */
    public void setLambQuality(double lambQuality) {
        if (lambQuality > MAX_LAMB_QUALITY) {
            this.lambQuality = MAX_LAMB_QUALITY;
        } else if (lambQuality < MIN_LAMB_QUALITY) {
            this.lambQuality = MIN_LAMB_QUALITY;
        } else {
            this.lambQuality = lambQuality;
        }
    }

    /**
     * Sets the sheep's wool production rate
     */
    public void setWoolProductionRate(double woolProductionRate) {
        if (woolProductionRate > MAX_WOOL_PRODUCTION_RATE) {
            this.woolProductionRate = MAX_WOOL_PRODUCTION_RATE;
        } else if (woolProductionRate < MIN_WOOL_PRODUCTION_RATE) {
            this.woolProductionRate = MIN_WOOL_PRODUCTION_RATE;
        } else {
            this.woolProductionRate = woolProductionRate;
        }
    }

    /**
     * Sets the sheep's weight increase rate
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
     * every tick (weight, woolQuantity etc.) determineState method should
     * include the logic for determining which state should be entered
     * findHandler is a case statement which calls the correct state handler.
     * All generic handlers are in FarmAnimal
     */
    @Override
    protected void tickUpdate() {
        super.tickUpdate();
        setWoolQuantity(woolQuantity + woolProductionRate);
        setWeight(weight + weightIncreaseRate);
    }

    /**
     * state priorities: - if dead, goto DEAD state - sleep unless food/drink
     * below threshold, in which case find food/drink
     */
    @Override
    public void determineState() {
        if (health <= MIN_HEALTH) {
        } else if (foodLevel <= HUNGER_THRESHOLD_L
                || thirstLevel <= THIRST_THRESHOLD_L) {
            if (foodLevel <= thirstLevel) {
            } else {
            }
        } else {
                resetWanderDestination();
            }

        }
    }

    /**
     * Creates a baby sheep at the location of the sheep
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
        Sheep newSheep = new Sheep(WorldManager.getInstance().getWorld(),
                getLocation().getX(), getLocation().getY() ,0.07, 0, sex, 1);

        FarmAnimalManager.getInstance().addFarmAnimal(newSheep);
    }

}