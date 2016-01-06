package farmsim.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The parent event which is accepted by the event manager. All events should
 * extend this.
 * 
 * @author bobri
 */
public abstract class AbstractEvent implements Event {

    private long duration = -1;
    private double level = 1;
    protected static final Logger LOGGER = 
			LoggerFactory.getLogger(AbstractEvent.class);

    /**
     * Sets the duration of this event
     * 
     * @param duration The new duration of this event > 0
     */
    protected void setDuration(long duration) {
        if (duration > 0) {
            this.duration = duration;
        }
        return;
    }

    /**
     * Sets the level of this event
     * 
     * @param increaseFactor The new level of this event > 0
     */
    protected void setLevel(double increaseFactor) {
        if (increaseFactor > 0) {
            this.level = increaseFactor;
        }
        return;
    }

    /**
     * Decreases the duration of this event by 1, if duration > 0.
     */
    protected void tickDuration() {
        if (duration > 0) {
            duration--;
        }
        return;
    }

    /**
     * Gets the duration of this event
     * 
     * @return The current duration of this event
     */
    @Override
    public long getDuration() {
        return duration;
    }

    /**
     * Gets the level of this event
     * 
     * @return The current level of this event
     */
    @Override
    public double getLevel() {
        return level;
    }

    /**
     * Indicates whether this event should be displayed as a status.
     * 
     * @return True if the duration is > 0. False otherwise.
     */
    @Override
    public boolean displayable() {
        if (duration > 0) {
            return true;
        }
        return false;
    }

    /**
     * Checks if 2 events are equal. Events are equal if they are the same type
     * of event.
     */
    @Override
    public boolean equals(Object o) {
        if (this.getClass() == o.getClass()) {
            return true;
        }
        return false;
    }

    /**
     * Override hashcode since equals is overridden - since equal objects must
     * have the same hashcode Equality is determined by getClass, so use the
     * hashcode method for class
     */
    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
