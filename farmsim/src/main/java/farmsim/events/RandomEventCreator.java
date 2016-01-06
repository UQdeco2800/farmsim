package farmsim.events;

import java.util.Random;
import farmsim.ui.Notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import farmsim.util.Point;
import farmsim.util.Tickable;
import farmsim.world.World;
import farmsim.world.WorldManager;
import javafx.application.Platform;

/**
 * Initiates random game events. Access the RandomEventCreator using
 * getInstance();
 * 
 * @author bobri
 */

public class RandomEventCreator implements Tickable {
	private static final RandomEventCreator INSTANCE = new RandomEventCreator();
	private static final Logger LOGGER = LoggerFactory.getLogger(RandomEventCreator.class);
	private EventManager eventManager = EventManager.getInstance();
	private Random randGenerator = new Random();
	private World world = WorldManager.getInstance().getWorld();

	private static final int NOEVENTS = 3;
	private static final int FREQUENCY = 5000;

	private int randResult;
	private int tryTime = 10;
	private boolean enabled = true;

	/**
	 * Gets the instance of the RandomEventCreator.
	 * 
	 * @return Returns the random event creator.
	 */
	public static RandomEventCreator getInstance() {
		return INSTANCE;
	}

	/**
	 * Toggles random events on or off.
	 * 
	 * @return Boolean indicating if random events are active.
	 */
	public boolean toggleRandomEvents() {
		if (enabled) {
			LOGGER.info("Random event disabled");
			enabled = false;
		} else {
			LOGGER.info("Random event enabled");
			enabled = true;
		}
		return enabled;
	}

	/**
	 * Initiates the agent strike event.
	 * 
	 * @param x
	 *            The x location of the strike center.
	 * @param y
	 *            The y location of the strike center.
	 */
	private void beginStrike() {
		Point staffHouseLocation = world.getStaffHouseLocation();
		if (staffHouseLocation != null) {
			LOGGER.info("Strike: Peons going on strike at staffhouse for duration 30000");
			StrikeEvent strike = new StrikeEvent((int) staffHouseLocation.getX(), 
					(int) staffHouseLocation.getY(), 1, 30000);
			eventManager.beginEvent(strike);
		} else {
			staffHouseLocation = new Point(
					randGenerator.nextInt() % world.getWidth(), 
					randGenerator.nextInt() % world.getHeight());
			LOGGER.info("Strike: Peons going on strike for duration 30000");
			StrikeEvent strike = new StrikeEvent((int) staffHouseLocation.getX(), 
					(int) staffHouseLocation.getY(), 1, 30000);
			eventManager.beginEvent(strike);
		}
	}
	
	/**
	 * Generates a random integer that is in the range 
	 * between max and min inclusive
	 * 
	 * @param min 
	 * 				The minimum number in the range
	 * @param max
	 * 				The maximum number in the range
	 * @return
	 */
	private int randomBound(int min, int max) {
		int randNum = min + (int)(Math.random()*((max-min) + 1));
		return randNum;
	}
	
	/**
	 *  Generates a random location to place the crop
	 *  circle and randomly selects the style of the 
	 *  crop circle
	 */
	private void beginCropCircle() {
		CropCircleStyle style;
		int min = 4;
		int maxWidth = world.getWidth()-4;
		int maxHeight = world.getHeight()-4;
		int randX = randomBound(min, maxWidth);
		int randY = randomBound(min, maxHeight);
		int randStyle = randomBound(0, 2);
        switch (randStyle) {
        case 1:
            style = CropCircleStyle.circle1;
            break;
        case 2:
            style = CropCircleStyle.circle2;
            break;
        default:
            style = CropCircleStyle.circle0;
    }
       CropCircleEvent cropCircle = new CropCircleEvent(randX, randY, style);
       eventManager.beginEvent(cropCircle);
	}
	
	private void beginFire() {
		FireEvent fireEvent = new FireEvent(30000);
		eventManager.beginEvent(fireEvent);
		LOGGER.info("A fire event has begun");
	}

	@Override
	public void tick() {
		if ((tryTime > 0) || (!enabled)) {
			tryTime--;
			return;
		}
		tryTime = 10;
		randResult = randGenerator.nextInt(FREQUENCY);
		if (randResult < 80) {
			switch (randResult % NOEVENTS + 1) {
			case 1:
				Platform.runLater(() -> {
				Notification.makeNotification("Strike", "Your workers have gone"
						+ " on Strike");
				beginStrike();
				});
				break;
			case 2:
				Platform.runLater(() -> {
				Notification.makeNotification("Crop Circle", "A Crop Circle"
						+ " has appeared!");
				beginCropCircle();
				});
				break;
			case 3:
				Platform.runLater(() -> {
				Notification.makeNotification("FIRE!!!!", "A Fire has started");
				beginFire();
				});
				break;
			default:
				LOGGER.info("No event occured");
				break;
			}
		}
		return;
	}
}
