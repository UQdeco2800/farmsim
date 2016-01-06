package farmsim.entities.animals;

import farmsim.Viewport;
import farmsim.world.World;
import javafx.scene.canvas.GraphicsContext;
/**
 * Provides an implementation for the core functionality of a farm animal. Farm
 * animals are state-based entities. This class should not be instantiated.
 *
 *
 */

     * Enum containing all the possible states that a FarmAnimal can be in. A
     * class extending FarmAnimal need not implement all of these states.
    protected int ageOfAdulthood; // age in ticks when animal becomes an adult
    protected int ageOfDeath; // age in ticks when the animal will die

    protected int animationStage;
    protected boolean isMoving;

    // number of ticks per animation frame
    protected int animationSpeed;
    protected int currentAnimationTick;
    protected Point wanderDestination;
    protected int wanderRange;


    protected boolean readyToMate = false;
    protected boolean pregnant = false;
    protected int ticksSincePregnant = 0;
    protected int ticksSinceHadBaby = 0;
    protected FarmAnimal targetMate = null;
    protected static final int PREGNANCY_TIME = 1;
    protected int mateCooldown;
    protected static final int MATE_COOLDOWN_MAX = 2000; //2000
    protected static final int MATE_COOLDOWN_MIN = 1000; //1000


    public FarmAnimal(String type, World world, double x, double y, double
            speed, double age, char sex) {
        super(type, x, y, speed, MAX_HEALTH);
        this.sex = sex;
        this.animationStage = 0;
        this.isMoving = false;
        this.restLevel = MAX_REST_LEVEL;
        this.foodLevel = MAX_FOOD_LEVEL;
        this.thirstLevel = MAX_THIRST_LEVEL;
        this.wanderRange = 20;
        this.animationSpeed = (int) (0.8/speed);
        this.world = world;
        resetMateCooldown();
        Random randomGenerator = new Random(identifier.getLeastSignificantBits() *
                System.currentTimeMillis());
        this.ageOfDeath = randomGenerator.nextInt(4000) + 4000; // ~200-400 sec
        this.ageOfAdulthood = 2000;
    }


    public double getAge() {
        return age;
    }

    public char getSex() {
        return sex;
    }

     * @return whether the animal is an adult
     */
    public boolean getAdultStatus() {
        if (age >= ageOfAdulthood) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
    public double getRestLevel() {
        return restLevel;
    }

    public double getFoodLevel() {
        return foodLevel;
    }

    public double getThirstLevel() {
        return thirstLevel;
    }

    public double getTimeDead() {
        return timeDead;
    }

    public boolean getReadyToMate() {
        return readyToMate;
    }

    public boolean hasTargetMate() {
        if (targetMate != null) {
            return true;
        }
        else {
            return false;
        }
    }






    public void setReadyToMate(boolean status) {
        readyToMate = status;
    }

    public void setPregnant(boolean status) {
        pregnant = status;
    }

    public void setTargetMate(FarmAnimal farmAnimal) {
        targetMate = farmAnimal;
    }

     * Tick method that should not be overridden in subclasses. All subclasses
     * should implement tickUpdate, determineState and findHandler.
    @Override
     * Updates the time-dependent variables that are common to all FarmAnimals.


    public String getImageName() {
        String imageName = getType();
        if (this.isAlive) {
            if (!getAdultStatus() && (getAnimalType() == AnimalType.COW ||
                    getAnimalType() == AnimalType.PIG || getAnimalType() ==
                    AnimalType.SHEEP)) {
                imageName += "Baby";
            }
            imageName += getDirection();
            if (isMoving) {
                if (currentAnimationTick == animationSpeed) {
                    if (animationStage == 0) {
                        animationStage = 1;
                    } else {
                        animationStage = 0;
                    }
                    currentAnimationTick = 0;
                } else {
                    currentAnimationTick++;
                }
            } else {
                animationStage = 0;
            }
            imageName += Integer.toString(animationStage);
        }
        else if (!this.isAlive) {
            imageName += "Dead";
            if (currentAnimationTick == animationSpeed) {
                if (animationStage >= 2) {
                    animationStage = 0;
                } else {
                    animationStage++;
                }
                currentAnimationTick = 0;
            } else {
                currentAnimationTick++;
            }
            imageName += Integer.toString(animationStage);
        }

        return imageName;
    }

        return wanderDestination;
    }

    protected FarmAnimal getAvailableMate() {
        for (FarmAnimal x : FarmAnimalManager.getInstance().getFarmAnimals()) {
            if (x.getAnimalType() == this.getAnimalType() &&
                    x.getReadyToMate() && x != this &&
                    x.getSex() != this.getSex()) {
                if (getLocation().distance(x.getLocation()) <=
                        FIND_MATE_RANGE) {
                    return x;
                }
            }
        }
        return null;
    }




        if (getAdultStatus()) {
            if (pregnant) {
                ticksSincePregnant++;
            }
            if (ticksSincePregnant >= PREGNANCY_TIME) {
                pregnant = false;
                ticksSincePregnant = 0;
                createBaby();
            }

            if (!pregnant && !hasTargetMate() && !readyToMate &&
                    ticksSinceHadBaby >= mateCooldown) {
                readyToMate = true;
                ticksSinceHadBaby = 0;
                resetMateCooldown();
            } else {
                ticksSinceHadBaby++;
            }
        }
    }

    protected void resetMateCooldown() {
        Random randomGenerator = new Random(identifier.getLeastSignificantBits() *
                System.currentTimeMillis());
        mateCooldown = randomGenerator.nextInt(MATE_COOLDOWN_MAX -
                MATE_COOLDOWN_MIN) + MATE_COOLDOWN_MIN;
    }

    @Override
    public void kill() {
        FarmAnimalManager farmAnimalManager = FarmAnimalManager.getInstance();
        farmAnimalManager.removeFarmAnimal(this);
    }

    protected void createBaby() {
    }

    }

    @Override
    public void draw(Viewport v, GraphicsContext g) {
        TileRegister tileRegister = TileRegister.getInstance();
        g.drawImage(tileRegister.getTileImage(getImageName()),
                (getLocation().getX() - v.getX()) * TileRegister.TILE_SIZE,
                (getLocation().getY() - v.getY()) * TileRegister.TILE_SIZE);
        drawSelection(v,g);
    }

}
