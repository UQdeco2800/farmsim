package farmsim.events;

import farmsim.events.statuses.StatusName;

/**
 * Changes weather to snow
 * Some sprite changes(?)
 * 
 * @author bobri
 */
public class ChristmasEvent extends AbstractEvent{
	
	/**
	 * Initializes the event
	 */
	ChristmasEvent(){
		//A new Christmas Event
	}

	/**
	 * Entry point for the Christmas Event
	 */
	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Updates the Christmas Event
	 */
	@Override
	public boolean update() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needsTick() {
		return false;
	}

	/**
	 * Returns the StatusName of this event.
	 */
	@Override
	public StatusName getName() {
		return StatusName.Christmas;
	}

}
