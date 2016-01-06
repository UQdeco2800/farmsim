import java.util.Random;
    public static final double MIN_MILK_QUALITY = 0; // Milk unable to be safely
    public static final double MAX_MILK_PRODUCTION_RATE = 1;
    public static final double MIN_MILK_PRODUCTION_RATE = 0;
    public static final double MAX_WEIGHT_INCREASE_RATE = 1;
    public static final double MIN_WEIGHT_INCREASE_RATE = 0;

    protected boolean edible; // Whether the beef from this animal is able to be
    protected double weight; // Weight of cow, to be used in determining amount
    protected double weightIncreaseRate; // Speed at which cow's weight
    // increases
    public Cow(World world, double x, double y, double speed, double age, char
            sex,
               double weight) {
        super("cow", world, x, y, speed, age, sex);
        this.wanderRange = 20;
        this.restRate = 0;
        this.thirstRate = 0;
        this.milkProductionRate = 0;
        this.weightIncreaseRate = 0;
        this.milkQuantity = 0.5;
        this.beefQuality = 0.5;
        this.edible = true;
        this.milkable = true;
    /** Returns the weight increase rate of the cow **/
    /** Returns the milk production rate of the cow **/
    /** Returns whether the cow is pregnant **/
    /** Returns the weight of the cow **/
    public double getWeight() {
        return weight;
    }

    /** Returns whether the cow is milkable **/
    public boolean getMilkable() {
        return milkable;
    }

    /** Returns whether the cow is edible **/
    public boolean getEdible() {
        return edible;
    }

    /** Returns the milk quantity of the cow **/
    public double getMilkQuantity() {
        return milkQuantity;
    }

    /** Returns the milk quality of the Animal **/
    public double getMilkQuality() {
        return milkQuality;
    }

    /** Returns the beef quality of the cow **/
    public double getBeefQuality() {
        return beefQuality;
    }
    /**
     * Sets whether the cow is edible or not
     */
    /**
     * Sets whether the cow is milkable or not
     */
    /**
     * Sets the cow's weight
     */
    /**
     * Sets the cow's milk quantity
     */
    /**
     * Sets the cow's beef quality
     */
    public void setBeefQuality(double beefQuality) {
        if (beefQuality > MAX_BEEF_QUALITY) {
            this.beefQuality = MAX_BEEF_QUALITY;
        } else if (beefQuality < MIN_BEEF_QUALITY) {
            this.beefQuality = MIN_BEEF_QUALITY;
        } else {
            this.beefQuality = beefQuality;
        }
    }

    /**
     * Sets the cow's milk production rate
     */
    public void setMilkProductionRate(double milkProductionRate) {
        if (milkProductionRate > MAX_MILK_PRODUCTION_RATE) {
            this.milkProductionRate = MAX_MILK_PRODUCTION_RATE;
        } else if (milkProductionRate < MIN_MILK_PRODUCTION_RATE) {
            this.milkProductionRate = MIN_MILK_PRODUCTION_RATE;
        } else {
            this.milkProductionRate = milkProductionRate;
        }
    }

    /**
     * Sets the cow's weight increase rate
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
     * Sets the cow's milk quality
     */
    public void setMilkQuality(double milkQuality) {
        if (milkQuality > MAX_MILK_QUALITY) {
            this.milkQuality = MAX_MILK_QUALITY;
        } else if (milkQuality < MIN_MILK_QUALITY) {
            this.milkQuality = MIN_MILK_QUALITY;
        } else {
            this.milkQuality = milkQuality;
        }
    }

    /**
     * tickUpdate method should include all the changes to variables that occur
     * every tick (weight, milkQuantity etc.) determineState method should
     * include the logic for determining which state should be entered
     * findHandler is a case statement which calls the correct state handler.
     * All generic handlers are in FarmAnimal
    /**
     * state priorities: - if dead, goto DEAD state - sleep unless food/drink
     * below threshold, in which case find food/drink
    @Override
        } else if (readyToMate && getAvailableMate() != null) {
        } else if (targetMate != null) {
                resetWanderDestination();
    /**
     * Creates a baby cow at the location of the cow
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
        Cow newCow = new Cow(WorldManager.getInstance().getWorld(),
                getLocation().getX(), getLocation().getY() ,0.06, 0, sex, 1);

        FarmAnimalManager.getInstance().addFarmAnimal(newCow);
    }
