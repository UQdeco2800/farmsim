package farmsim.entities.animals;

/**
 * A fish animal. Fish can be caught by peons who go fishing.
 *
 * @author rachelcatchpoole for Team Adleman
 */
public class Fish extends Animal {

    private double health = 0;
    private double speed = 0;
    private double age = 1;

    /**
     * Create a fish at point x, y
     * @param x
     *      The x-coordinate of the fish
     * @param y
     *      The y-coordinate of the fish
     */
    public Fish(double x, double y) {
        super("fish", x, y, 0.0, 0.0);
        this.type = AnimalType.FISH;
        this.kill();
        this.setHealth(health);
        this.setSpeed(speed);
    }

    /**
     * Returns the health of the fish
     * @return the health of the fish
     */
    public double getHealth() {
        return health;
    }
    
    /**
     * Returns the age of the fish
     */
    public double getAge() {
        return age;
    }
   
}