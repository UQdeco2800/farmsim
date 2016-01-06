package farmsim.events;

import java.util.ArrayList;

import farmsim.events.statuses.StatusHandler;
import farmsim.util.Tickable;

/**
 * Manages and initiates game events. Access the EventManager using
 * getInstance();
 * 
 * @author bobri
 */

public class EventManager implements Tickable {
	private static final EventManager INSTANCE = new EventManager();
	private RandomEventCreator rEC = RandomEventCreator.getInstance();
	private StatusHandler statusHandler = StatusHandler.getInstance();
	private ArrayList<AbstractEvent> tickableEvents = new ArrayList<>();

	/**
	 * Gets the instance of the EventManager.
	 * 
	 * @return Returns the event manager.
	 */
	public static EventManager getInstance() {
		return INSTANCE;
	}

	/**
	 * Initiates an event
	 * 
	 * @param event
	 *            AbstractEvent to be initiated
	 */
	public void beginEvent(AbstractEvent event) {
		if (tickableEvents.contains(event)) {
			if (!duplicateExceptions(event)) {
				return;
			}
		}
		if (event.needsTick()) {
			tickableEvents.add(event);
		}

		if (event.displayable()) {
			if (event.getDuration() == -1) {
				statusHandler.addStatus(event.getName(), event.getLevel());
			} else {
				statusHandler.addStatus(event.getName(), event.getLevel(), event.getDuration());
			}
		}
		event.begin();
	}

	/**
	 * Toggles random events on or off.
	 * 
	 * @return Bool indicating if random events are active.
	 */
	public boolean toggleRanomEvents() {
		return rEC.toggleRandomEvents();
	}

	/**
	 * Events with durations that are allowed to have duplicates running
	 * 
	 * @param event
	 *            The event to check for duplicates
	 * @return True if duplicates of this event are allowed
	 */
	private boolean duplicateExceptions(AbstractEvent event) {
		switch (event.getName()) {
		case SPEEDCHANGE:
			return true;
		default:
			return false;
		}
	}

	/**
	 * Updates all events that require to be ticked
	 */
	@Override
	public void tick() {
		AbstractEvent e;
		for (int i = 0; i < tickableEvents.size(); i++) {
			e = tickableEvents.get(i);
			statusHandler.tickDuration(e.getName());
			if (!e.update()) {
				if (e.displayable()) {
					statusHandler.removeStatus(e.getName());
				}
				tickableEvents.remove(i);
				i--;
			}
		}
	}
}
