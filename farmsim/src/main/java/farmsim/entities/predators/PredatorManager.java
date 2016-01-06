package farmsim.entities.predators;

import farmsim.Viewport;
import farmsim.ViewportNotSetException;
import farmsim.tasks.KillPredatorTask;
import farmsim.tasks.TaskManager;
import farmsim.util.Point;
import farmsim.world.DayNight;
import farmsim.world.World;
import farmsim.world.WorldManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Predator Manager Manages all predators in the game world
 *
 * @author r-portas
 */

public class PredatorManager {

	private int enabled = 1;
	
	// The maximum number of predators that can exist at any given time
	private int maxPredators = 6;

    private static final Logger logger =
            LoggerFactory.getLogger(PredatorManager.class);
    // The current game world
    private World world;
    // The viewport, used to spawn the predators only on the screen
    private Viewport viewport;
    // Set used to store current predators in the game world
    private List<Predator> predators;
    // A timer to count upwards to timerCounter
    private int timer;
    // The max value the timer reaches before resetting
    private int timerCutoff;
    // World size of the game, assuming world size is square
    private int worldSizeX;
    private int worldSizeY;
    // The random generator
    private Random randGenerator;
    // Handles converting the coordinates
    private ViewportTranslator translator;
    // Handles getting the locations to spawn
    private PredatorSpawner spawner;
    // Handles the config file
    private PredatorConfigLoader configLoader;

    /**
     * Creates a PredatorManager instance, note that only one instance should exist at any given time
     */
    public PredatorManager() {
        translator = new ViewportTranslator();
        spawner = new PredatorSpawner(translator);
        configLoader = new PredatorConfigLoader();
        configLoader.loadFile(
                "src/main/java/farmsim/entities/predators/predator.config");

        predators = Collections.synchronizedList(new ArrayList<Predator>());
        randGenerator = new Random();
        timer = 0;
        timerCutoff = 200;
        try {
            world = WorldManager.getInstance().getWorld();
            worldSizeX = world.getWidth();
            worldSizeY = world.getHeight();
        } catch (Exception e) {
            // This is caused if WorldManager cannot be loaded
            setWorldSize(2, 2);
        }

    }

    /*
     * Removes all predators from the PredatorManager
     */
    public void removeAllPredators() {
        predators = Collections.synchronizedList(new ArrayList<Predator>());
    }

    /**
     * Sets the viewport for the predator manager
     *
     * @param viewport The viewport to set
     */
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
        translator.setViewport(viewport);
    }

    /**
     * Disable predators
     */
    public void disablePredators() {
        enabled = 0;
    }

    /**
     * Enables predators
     */
    public void enablePredators() {
        enabled = 1;
    }

    /**
     * Gets the status of the predators
     * 1 if enabled, 0 if disabled
     *
     * @return enabled The status of the predators
     */
    public int getPredatorStatus() {
        return enabled;
    }

    /**
     * Gets the timer value, which represents the delay
     * between the random spawning function
     *
     * @return the timer value
     */
    public int getTimer() {
        return timerCutoff;
    }

    /**
     * Sets the timer for the random spawn delay is in ticks
     */
    public void setTimer(int delay) {
        timerCutoff = delay;
    }

    /**
     * Sets the bound for the world in respect to the random spawn.
     */
    public void setWorldSize(int x, int y) {
        worldSizeX = x;
        worldSizeY = y;
    }

    /**
     * Gets the world size from the world manager
     */
    public void refreshWorldSize() {
        worldSizeX = world.getWidth();
        worldSizeY = world.getHeight();
    }

    /**
     * This is used for junit testing so that we can test that the size is being set correctly
     *
     * @return worldSize
     */
    public List<Integer> getWorldSize() {
        List<Integer> out = new ArrayList<>();
        out.add(worldSizeX);
        out.add(worldSizeY);
        return out;
    }

    /**
     * Adds a predator to the manager
     */
    public void createPredator(Predator predator) {
        synchronized (predators) {
            predators.add(predator);
        }
        // Add new KillPredatorTask to TaskManager
        World world = WorldManager.getInstance().getWorld();
        int startingX = (int) Math.round(predator.getLocation().getX());
        int startingY = (int) Math.round(predator.getLocation().getY());
        try {
        KillPredatorTask task = new KillPredatorTask(predator, startingX,
                startingY, world, "Kill " + predator.toString());
        TaskManager.getInstance().addTask(task);
        } catch (Exception e) {
        	// Only an issue when unit testing
        }
    }

    /**
     * A simple method to create predators
     *
     * @param name The shortname of the predator, options are 'wolf', 'bear', 'mole', 'alligator' and 'rabbit'
     */
    public void spawnPredator(String name) {
        Predator pred;
        Point spawnLoc = spawner.getRandomSpawnLocation();
        switch (name) {
            case "rabbit":
                pred = new RabbitPredator(spawnLoc.getX(), spawnLoc.getY(), 100,
                        0.1);
                createPredator(pred);
                break;

            case "wolf":
                // Replace x and y with spawnLoc when fixed
                pred = new WolfPredator(spawnLoc.getX(), spawnLoc.getY(), 100,
                        0.1);
                createPredator(pred);
                break;
            case "bear":
                pred = new BearPredator(spawnLoc.getX(), spawnLoc.getY(), 100,
                        0.1);
                createPredator(pred);
                break;
            case "mole":
                pred = new MolePredator(spawnLoc.getX(), spawnLoc.getY(), 100,
                        0.1);
                createPredator(pred);
                break;
            case "alligator":
                pred = new AlligatorPredator(spawnLoc.getX(), spawnLoc.getY(),
                        100, 0.1);
                createPredator(pred);
                break;
            default:
                // No matching commands
                break;
        }
    }

    /**
     * Removes the given predator from the predator manager
     *
     * @param predator The predator to remove
     */
    public void removePredator(Predator predator) {
        synchronized (predators) {
            predators.remove(predator);
        }
    }

    /**
     * Removes old predators from the game
     * This is usually called within the tick of the predator manager
     */
    public void removeOldPredators() {
        synchronized (predators) {
            for (Predator predator : predators) {
                if (predator.canDestroy()) {
                    predators.remove(predator);
                }
            }
        }

    }

    /**
     * Gets the count of current predators in the game
     *
     * @return Returns the number of predators in the game
     */
    public int getPredatorCount() {
        return predators.size();
    }

    /**
     * Gets a set of all predators that are currently in the game
     *
     * @return Returns a list of predators
     */
    public List<Predator> getPredators() {
        return predators;
    }

    /**
     * Acts as a entry point for the test suite
     *
     * @return
     */
    public Point getDebugSpawn() {
        return spawner.getRandomSpawnLocation();
    }


    /**
     * Handles the ticking and the spawn checking
     */
    public void tick() throws ViewportNotSetException {
        if (enabled == 1) {
            if (viewport == null) {
                throw new ViewportNotSetException();
            }
    		// First clean up old predators
    		removeOldPredators();
    		
    		synchronized (predators) {
    			for (Predator predator : predators) {
                    
    	            predator.tick(viewport);
    	        }
			}
	        
	        // We will proabably want to implement the random spawn function here
	        if (timer > timerCutoff){
	            timer = 0;

	            int chance = 10; // 10% chance of spawning
//	            chance = 100; //here for testing	            
	            int var = randGenerator.nextInt(100);
	            
	            if (!WorldManager.getInstance().getWorld().getTimeManager().isDay()){
	            	chance = 20; // Double the chance of spawning when it's night
	            }

	            if (var < chance && getPredatorCount() < maxPredators){
	            	Point spawnloc = spawner.getRandomSpawnLocation();
	            	double x = spawnloc.getX();
	            	double y = spawnloc.getY();
	            	
	            	
	            	int whichAnimal = randGenerator.nextInt(4);
	            	
	            	Predator p;
	            	
	            	switch (whichAnimal){
	            	case 0:
	            		try {
	            			p = new BearPredator(x, y, 
	            					Integer.valueOf(configLoader.getPredatorAttribute("bear", "health")),
	            					Integer.valueOf(configLoader.getPredatorAttribute("bear", "speed")));
	            		} catch (Exception e) {
	            			p = new BearPredator(x, y, 100, 0.1);
	            		}
			            createPredator(p);
	            		break;
	            	case 1:
	            		try {
	            			p = new WolfPredator(x, y, 
	            					Integer.valueOf(configLoader.getPredatorAttribute("wolf", "health")),
	            					Integer.valueOf(configLoader.getPredatorAttribute("wolf", "speed")));
	            		} catch (Exception e) {
	            			p = new WolfPredator(x, y, 100, 0.1);
	            		}
			            createPredator(p);
	            		break;
	            	case 2:
						try {
							p = new RabbitPredator(x, y,
									Integer.valueOf(configLoader.getPredatorAttribute("rabbit", "health")),
									Integer.valueOf(configLoader.getPredatorAttribute("rabbit", "speed")));
						} catch (Exception e) {
							p = new RabbitPredator(x, y, 100, 0.1);
						}
	            		createPredator(p);
						break;
					case 3:
						try {
							p = new MolePredator(x, y,
									Integer.valueOf(configLoader.getPredatorAttribute("mole", "health")),
									Integer.valueOf(configLoader.getPredatorAttribute("mole", "speed")));
						} catch (Exception e) {
							p = new MolePredator(x, y, 100, 0.1);
						}
						createPredator(p);
						break;
	            	}
	            }
	        }
	        timer += 1;
    	}
    }
}
