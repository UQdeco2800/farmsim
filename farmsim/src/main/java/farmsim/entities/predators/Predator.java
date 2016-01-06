package farmsim.entities.predators;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import farmsim.GameRenderer;
import farmsim.Viewport;
import farmsim.ViewportNotSetException;
import farmsim.entities.WorldEntity;
import farmsim.entities.agents.Agent;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import farmsim.world.World;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import farmsim.world.WorldManager;

/**
 * A base class for all predators
 *
 * @author r-portas
 */
public class Predator extends WorldEntity {
    // The current location of the predator
    Point location;
    // The destination that the predator wants to go
    Point destination;
    // The current world the predator resides in
    World world;
    // The speed which the predator moves at
    double speed;
    // The amount of health the predator has
    int health;
    // Tells the predator manager that it can destroy the predator now
    boolean canDestroy = false;
    // Stores the image to render
    String sprite;
    // Stores the direction that the predator is going in
    String direction;
    int animationStage;
    int currentAnimationTick;
    int animationSpeed;
    
    // Attack every 10 ticks
    int attackSpeed;
    int attackCounter = 0;
    
    public Predator(double x, double y,
            int health, double speed, int attackSpeed, String graphic){
        super(graphic, x, y);
        sprite = graphic;
    	this.location = new Point(x, y);        
        this.speed = speed;
        this.health = health;
        this.attackSpeed = attackSpeed;
        this.direction = "DownRight";
        this.animationStage = 0;
        this.currentAnimationTick = 0;
        this.animationSpeed = 20;
    }

    /**
     * Overrides the attackCounter
     * This should only be used for testing
     * @param num The number to set the counter to
     */
    public void overrideAttackCounter(int num){
    	attackCounter = num;
    }
    
    /**
     * Checks if the predator can be destroyed
     * @return true if the predator can be killed
     */
    public boolean canDestroy() {
    	return canDestroy;
    }
    
    /**
     * Sets a flag to kill the predator next update
     */
    public void killPredator(){
    	this.canDestroy = true;
    }
    
    /**
     * The AI code to tell the predators what to do Note: This will need to be
     * overriden in all sub classes
     */
    private void startTask() {
        moveTo(0, 0);
    }


    /**
     * Runs a tick on the predator
     * @param viewport The viewport of the game
     */
    public void tick(Viewport viewport) {
        // Do the tick stuff here
        // This will contain the AI component

        if (location != null && destination != null) {
            location.moveToward(destination, speed);
        }
    }

    /**
     * Attacks an animal/plant
     */
    private void attack(){
    	// The base predator cannot attack
    }
    
    /**
     * A very simple movement code to move the predator 
     * 
     * Returns 0 if it can move to the given position
     */
    public int moveTo(double x, double y){
        destination = new Point(x, y);
       
        return 0;
    }
    
    /**
     * Moves the predator to the specified point if it is not in a safe 
     * zone.
     * Also calculates the direction the animal should face
     * 
     * @author rachelcatchpoole
     * 
     * @param point the destination Point
     */
    public void moveToward(Point point) {
        String newDirection;
        if (destination.getY() >= getLocation().getY()) {
            newDirection = "Down";
        } else {
            newDirection = "Up";
        }
        if (destination.getX() >= getLocation().getX()) {
            newDirection += "Right";
        } else {
            newDirection += "Left";
        }
        direction = newDirection;

        World currentWorld = WorldManager.getInstance().getWorld();
        if (!currentWorld.getTile(point).getSafeZone()) {
            // move toward point if not in safe zone
            location.moveToward(point, speed);
        }
    }

    /**
     * Moves the predator off the screen to destroy it
     */
    public void moveOffScreen(Viewport viewport){
        canDestroy = true;

        double distToWest = getLocation().getX() - viewport.getX();
        double distToEast = viewport.getX() + viewport.getWidthTiles() - getLocation().getX();
        double distToNorth = getLocation().getY() - viewport.getY();
        double distToSouth = viewport.getY() + viewport.getHeightTiles() - getLocation().getY();

        double shortest = Collections.min(Arrays.asList(
                distToWest,
                distToEast,
                distToNorth,
                distToSouth
        ));

        if (Double.compare(shortest, distToWest) == 0) {
            moveTo(
                    viewport.getX() + viewport.getWidthTiles(),
                    this.getLocation().getY()
            );
        } else if (Double.compare(shortest, distToEast) == 0) {
            moveTo(viewport.getX(), this.getLocation().getY());
        } else if (Double.compare(shortest, distToNorth) == 0) {
            moveTo(this.getLocation().getX(), viewport.getY());
        } else {
            moveTo(
                    this.getLocation().getX(),
                    viewport.getY() + viewport.getHeightTiles()
            );
        }
    }
    
    public double getSpeed() {
        return speed;
    }

    @Override
    public Point getLocation() {
        return new Point(location);
    }

    /**
     * Retrieves the destination of where the predator is trying to move to
     * @return destination	the destination
     */
    public Point getDestination(){
        return new Point(destination);
    }

    /**
     * Gets the current health of the predator
     * @return health	the health of the animal
     */
    public int getHealth(){
        return health;
    }
    
    /**
     * A string representation of the predator
     */
    public String toString(){
    	return this.getClass().getSimpleName() + "(" + 
    this.getLocation().getX() + ", " + this.getLocation().getY() + ")";
    }

    /**
     * Gets the predator's x location
     */
    public double getWorldX() {
        return this.location.getX();
    }

    /**
     * Gets the predators's y location
     */
    public double getWorldY() {
        return this.location.getY();
    }

    /**
     * gets the predator's image name for animation
     */
    public String getImageName() {
        //Should look like predatorType, Y direction, x direction, number
        //e.g. bearUpLeft0
        String imageName = sprite;
        imageName += getDirection();

        //addLogic for 0 or 1 here
        if (currentAnimationTick == animationSpeed) {
            if (animationStage > 0) {
                animationStage = 0;
            } else {
                animationStage = 1;
            }
            currentAnimationTick = 0;
        } else {
            currentAnimationTick++;
        }

        imageName += Integer.toString(animationStage);
        return imageName;
    }

    /** Returns the direction of the Animal **/
    public String getDirection() {
        return direction;
    }
    
    /**
     * 
     * @param point
     * @return true if the point can be attacked by a predator, or false if it
     *  is in a scarecrow's safe zone.
     * 
     * @author rachelcatchpoole
     */
    public boolean attackable(Point point) {
        World currentWorld = WorldManager.getInstance().getWorld();
        boolean safe = currentWorld.getTile(point).getSafeZone();
        return !safe;
    }

    @Override
    public void draw(Viewport v, GraphicsContext g) {
        String texture;
        try {
            texture = getImageName();
        } catch (Exception e) {
            texture = sprite + "DownLeft0";
        }

        g.drawImage(TileRegister.getInstance().getTileImage(texture),
                (getLocation().getX() - v.getX()) * TileRegister.TILE_SIZE,
                (getLocation().getY() - v.getY()) * TileRegister.TILE_SIZE);
    }


	@Override
	public void tick() {
		
		
	}
	
	/**
	 * The predator's health decreases when attacked by a hunter.
	 * @author TheSpecialist4
	 */
	public void attackPredator() {
		 this.health -= 30;
		 if (health <= 10) {
			 killPredator();
		 }
	}

}
