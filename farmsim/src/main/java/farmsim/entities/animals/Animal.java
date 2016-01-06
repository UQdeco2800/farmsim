
import farmsim.entities.WorldEntity;
import farmsim.tasks.TaskManager;
import farmsim.util.NoPathException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public abstract class Animal extends WorldEntity {
    //Create slf4j logger
    private static final Logger LOGGER =
            LoggerFactory.getLogger(Animal.class);
    /**
     * Enum containing all possible species of Animal. If an animal need not
     * have a species, DEFAULT should be used. There are no guaranteed
        DEFAULT, COW, PIG, DUCK, CHICKEN, SHEEP, FISH

    protected String direction;
    public Animal(String type, double x, double y,
                  double speed, double health) {
        super(type, x, y);
        this.direction = "DownRight";
        this.health = 100;
    public AnimalType getAnimalType() {
    /** Returns true if Animal is alive **/
    /** Returns the direction of the Animal **/
    public String getDirection() {
        return direction;
    }


     * Sets the Animal's current health to the provided health value. Health
     * will not be lower than MIN_HEALTH or greater than MAX_HEALTH.
     * @param health The new health of the animal

     * @param delta The amount to change the health by
    }

    }

     * Sets the Animal's speed to the provided speed value. Speed will not be
     * lower than MIN_SPEED or greater than MAX_SPEED.
     * @param speed The new speed of the animal
     * Adds delta to the current speed. Speed will not be lower than MIN_SPEED
     * or greater than MAX_SPEED.
     * 
     * @param delta The amount to change the animal's speed by

     * Moves the Animal's current getLocation() toward the provided Point. The
     * Animal's speed dictates the distance moved. Also calculates what
     * direction the Animal should be facing.
     * 
     * @param destination The point to move towards
    public void moveToward(Point destination) {
//        try {
//    	    getLocation().pathToward(destination, speed);
//        } catch (IllegalArgumentException e) {
//            LOGGER.error(e.toString());
//        } catch (NoPathException e) {
////            TaskManager.getInstance().removeTask(task);
//        }
        getLocation().moveToward(destination, speed);

        String newDirection;
        if (destination.getY() >= getLocation().getY()) {
            newDirection = "Down";
        } else {
            newDirection = "Up";
        if (destination.getX() >= getLocation().getX()) {
            newDirection += "Right";
        } else {
            newDirection += "Left";
        }
        direction = newDirection;
    }
    @Override
    public void tick() {
    }

    public abstract double getAge();