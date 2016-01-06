package farmsim.events;

import farmsim.events.statuses.StatusName;

/**
 * Methods that all events need for the EventManager to work correctly
 * 
 * @author bobri
 *
 */
public interface Event {

    /**
     * Begins the event
     */
    public void begin();

    /**
     * Updates the event.
     * 
     * @return True if the event should continue to be updated, false if it no
     *         longer requires to be updated.
     */
    public boolean update();

    /**
     * Gets the duration remaining of this event
     * 
     * @return The remaining duration of this event. -1 if this event has no
     *         duration associated with it.
     */
    public long getDuration();

    /**
     * Specifies whether the EventManager should tick this event or not
     * 
     * @return True if this event should be ticked, otherwise false.
     */
    public boolean needsTick();

    /**
     * Gets the name of this status.
     * 
     * @return StatusName of status
     */
    public StatusName getName();

    /**
     * Gets the level/Severity of this event
     * 
     * @return The level or severity. Return 1 if a status level has not been
     *         explicitly defined
     */
    public double getLevel();

    /**
     * Indicates whether the event should be displayed as a status
     * 
     * @return True if this event should be displayed as a status, false if it
     *         should not be displayed.
     */
    public boolean displayable();

}
