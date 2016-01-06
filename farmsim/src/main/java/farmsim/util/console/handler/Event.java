package farmsim.util.console.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import farmsim.events.CropCircleEvent;
import farmsim.events.CropCircleStyle;
import farmsim.events.DroughtEvent;
import farmsim.events.EventManager;
import farmsim.events.FireEvent;
import farmsim.events.RandomEventCreator;
import farmsim.events.SpeedChangeEvent;
import farmsim.events.StrikeEvent;
import farmsim.events.statuses.StatusHandler;
import farmsim.util.Point;
import farmsim.util.console.Console;
import farmsim.world.WorldManager;

/**
 * Event and Modifier Command Handler.
 */
public class Event extends BaseHandler implements BaseHandlerInterface {

	private static final Logger LOGGER = LoggerFactory.getLogger(RandomEventCreator.class);
	
    /**
     * Handler for all building console commands.
     */
    public Event() {
        super();
        this.addCommands();
        this.setName("Event & Modifier");
    }

    /**
     * Adds the commands to the Handler.
     */
    private void addCommands() {
        addSingleCommand("event", "strike [proportion] [duration]\n"
                + "cropCircle [x] [y] [circle style (0, 1, 2)]\n"
                + "speed [change factor] [duration]\n"
                + "fire [severity]");
        addSingleCommand("statusList",
                "Lists all current statusees in StatusHandler");
        addSingleCommand("toggleRandomEvents",
                "Toggles random events on or off");
    }

    /**
     * Handles the incoming command parameters.
     * 
     * @param parameters the incoming command to be processed.
     */
    @Override
    public void handle(String[] parameters) {
        if (parameters != null && parameters.length > 0
                && this.contains(parameters[0])) {
            switch (parameters[0]) {
                case "event":
                    events(parameters);
                    break;
                case "statusList":
                    list();
                    break;
                case "toggleRandomEvents":
                    toggle();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Opens up the status handler gui.
     */
    private void list() {
        StatusHandler statusHandler = StatusHandler.getInstance();
        statusHandler.popupStatus();
    }

    /**
     * Toggles if events will occur.
     */
    private void toggle() {
        EventManager eventManager = EventManager.getInstance();
        Console.getInstance()
                .println(String.valueOf(eventManager.toggleRanomEvents()));
    }

    /**
     * Loads the parameters to the specific event.
     * 
     * @param commandParameters the command parameters.
     */
    private void events(String[] commandParameters) {
        switch (commandParameters[1]) {
            case "strike":
                eventStrike(commandParameters);
                break;
            case "cropCircle":
                eventCropCircle(commandParameters);
                break;
            case "speed":
                eventSpeed(commandParameters);
                break;
            case "fire":
            	eventFire(commandParameters);
            	break;
            default:
                break;
        }
    }
    
    private void eventFire(String[] commandParameters) {
    	EventManager eventManager = EventManager.getInstance();
    	FireEvent fireEvent = new FireEvent(Integer.parseInt(commandParameters[2]));
    	eventManager.beginEvent(fireEvent);
    }

    /**
     * creates an event strike event.
     * 
     * @param commandParameters the command parameters.
     */
    private void eventStrike(String[] commandParameters) {
        EventManager eventManager = EventManager.getInstance();
        
        int proportion = Integer.parseInt(commandParameters[2]);
        int duration = Integer.parseInt(commandParameters[3]);

        Point staffHouseLocation = WorldManager.getInstance().getWorld().getStaffHouseLocation();
		if (staffHouseLocation != null) {
			StrikeEvent strike = new StrikeEvent((int) staffHouseLocation.getX(), 
					(int) staffHouseLocation.getY(), proportion, duration);
			eventManager.beginEvent(strike);
		} else {
			StrikeEvent strike = new StrikeEvent(15, 
					15, proportion, duration);
			eventManager.beginEvent(strike);
		}
    }

    /**
     * creates an drought event.
     * 
     * @param commandParameters the command parameters.
     */
    private void eventDrought(String[] commandParameters) {
        EventManager eventManager = EventManager.getInstance();
        DroughtEvent drought =
                new DroughtEvent(Integer.parseInt(commandParameters[2]));
        eventManager.beginEvent(drought);
    }

    /**
     * creates a crop circle event.
     * 
     * @param commandParameters the command parameters.
     */
    private void eventCropCircle(String[] commandParameters) {
        EventManager eventManager = EventManager.getInstance();
        CropCircleStyle style;
        switch (Integer.parseInt(commandParameters[4])) {
            case 1:
                style = CropCircleStyle.circle1;
                break;
            case 2:
                style = CropCircleStyle.circle2;
                break;
            default:
                style = CropCircleStyle.circle0;
        }
        CropCircleEvent cropCircle =
                new CropCircleEvent(Integer.parseInt(commandParameters[2]),
                        Integer.parseInt(commandParameters[3]), style);
        eventManager.beginEvent(cropCircle);
    }

    /**
     * changes the speed of the game event.
     * 
     * @param commandParameters the command parameters.
     */
    private void eventSpeed(String[] commandParameters) {
        EventManager eventManager = EventManager.getInstance();
        SpeedChangeEvent speedChange =
                new SpeedChangeEvent(Float.parseFloat(commandParameters[2]),
                        Long.parseLong(commandParameters[3]));
        eventManager.beginEvent(speedChange);
    }
}
