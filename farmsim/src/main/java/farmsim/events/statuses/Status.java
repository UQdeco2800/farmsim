package farmsim.events.statuses;

/**
 * A status object for use with the status/modifiers system.
 * 
 * @author bobri
 */

public class Status {

    private StatusName statusName = StatusName.VOIDSTATUS;
    private double statusLevel = 1;
    private long statusDuration = -1;

    /**
     * Creates a new Status with a StatusName and a level. If init fails
     * StatusName will be voidstatus and level will be 1
     * 
     * @param status The name of the status (from enum StatusName).
     * @param lvl The level of the status >= 1.
     */
    public Status(StatusName status, double lvl) {
        if (lvl <= 0) {
            return;
        }
        statusName = status;
        statusLevel = lvl;
        return;
    }

    /**
     * Changes the level of the status.
     * 
     * @param lvl The new level of the status. Must by >= 1.
     * @return The new level upon success, otherwise 0.
     */
    public double changeLvl(double lvl) {
        if (lvl > 0) {
            statusLevel = lvl;
            return lvl;
        }
        return 0;
    }

    /**
     * Set the duration of the status.
     * 
     * @param duration The new duration of the status. Must be >= 1.
     * @return Upon success the new duration, otherwise 0;
     */
    public long setDuration(long duration) {
        if (duration <= 0) {
            return 0;
        }
        statusDuration = duration;
        return duration;
    }

    /**
     * Gets the duration of the status in ticks
     * 
     * @return The duration of the status or -1 if no duration has been set.
     */
    public long getDuration() {
        return statusDuration;
    }

    /**
     * Gets the level of the status.
     * 
     * @return The level of the status.
     */
    public double getLvl() {
        return statusLevel;
    }

    /**
     * Gets the name of the status.
     * 
     * @return The StatusName of the status (from enum StatusName).
     */
    public StatusName getName() {
        return statusName;
    }

    @Override
    public String toString() {
        StringBuilder rtnString = new StringBuilder();
        rtnString.append(statusName.toString());
        rtnString.append("    Level: ");
        rtnString.append(statusLevel);

        if (statusDuration > 0) {
            rtnString.append("    Duration: ");
            rtnString.append(statusDuration / 1000);
        }

        return rtnString.toString();
    }
}
