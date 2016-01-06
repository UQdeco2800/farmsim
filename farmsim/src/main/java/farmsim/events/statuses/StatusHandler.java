package farmsim.events.statuses;

import java.util.ArrayList;
import java.util.Hashtable;

import farmsim.ui.PopUpWindowManager;

/**
 * Handles the adding, removing and updating of statuses.
 * 
 * @author bobri
 */

public class StatusHandler {

    private static Hashtable<StatusName, Status> statusList =
            new Hashtable<StatusName, Status>();
    private static ArrayList<StatusName> durationStatus =
            new ArrayList<StatusName>();
    private static final StatusHandler INSTANCE = new StatusHandler();
    private PopUpWindowManager popUpManager = PopUpWindowManager.getInstance();
    private StatusViewer statusViewer;
    
    public static StatusHandler getInstance() {
        return INSTANCE;
    }

    /**
     * Adds a new status
     * 
     * @param status The status name (from enum StatusName)
     * @param lvl The level of the status >= 1
     * @return 1 upon success, 0 otherwise
     */
    public int addStatus(StatusName status, double lvl) {
        if ((lvl <= 0) || (statusList.containsKey(status))) {
            return 0;
        }
        statusList.put(status, new Status(status, lvl));
        return 1;
    }

    /**
     * Adds a new status with a duration
     * 
     * @param status The status name (from enum StatusName)
     * @param lvl The status level >= 1
     * @param duration The duration in ticks >= 1
     * @return 1 upon success, 0 otherwise
     */
    public int addStatus(StatusName status, double lvl, long duration) {
        if (duration <= 0) {
            return 0;
        }
        addStatus(status, lvl);
        if (statusList.get(status).setDuration(duration) == 0) {
            removeStatus(status);
            return 0;
        }
        durationStatus.add(status);
        return 1;
    }

    /**
     * Removes a status
     * 
     * @param status The status name (from enum StatusName)
     * @return 1 upon successful removal, 0 otherwise
     */
    public int removeStatus(StatusName status) {
        if (statusList.containsKey(status)) {
            statusList.remove(status);
            if (durationStatus.contains(status)) {
                durationStatus.remove(status);
            }
            return 1;
        }
        return 0;
    }

    /**
     * Remove a status
     * 
     * @param status The status name (from enum StatusName)
     * @param lvl The status level
     * @return 1 upon successful removal, 0 otherwise
     */
    public int removeStatus(StatusName status, double lvl) {
        if (checkStatus(status, lvl)) {
            return removeStatus(status);
        }
        return 0;
    }

    /**
     * Increases the level of an existing status
     * 
     * @param status The status name (from enum StatusName)
     * @param lvl Number of levels to increase by
     * @return The new level upon success, 0 otherwise
     */
    public double increaseStatus(StatusName status, double lvl) {
        if (lvl <= 0) {
            return 0;
        }
        if (statusList.containsKey(status)) {
            return statusList.get(status)
                    .changeLvl(statusList.get(status).getLvl() + lvl);
        }
        return 0;
    }

    /**
     * Decrease the level of an existing status
     * 
     * @param status The status name (from enum StatusName)
     * @param lvl Number of levels to decrease by
     * @return The new level upon success, 0 otherwise
     */
    public double decreaseStatus(StatusName status, double lvl) {
        if (lvl <= 0) {
            return 0;
        }
        if (statusList.containsKey(status)
                && (statusList.get(status).getLvl() > lvl)) {
            return statusList.get(status)
                    .changeLvl(statusList.get(status).getLvl() - lvl);
        }
        return 0;
    }

    /**
     * Checks if a status is active
     * 
     * @param status The status name (from enum StatusName)
     * @return true if status is active, false otherwise
     */
    public boolean checkStatus(StatusName status) {
        if (statusList.containsKey(status)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a status at a certain level is present
     * 
     * @param status The status name (from enum StatusName)
     * @param lvl the level of the status
     * @return true if status is active, false otherwise
     */
    public boolean checkStatus(StatusName status, double lvl) {
        if (statusList.containsKey(status)) {
            if (statusList.get(status).getLvl() == lvl) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reduces the duration of a status by 1
     * 
     * @param name The name of the status to tick
     */
    public void tickDuration(StatusName name) {
        if (statusList.containsKey(name)) {
            statusList.get(name)
                    .setDuration(statusList.get(name).getDuration() - 1);
        }
    }

    /**
     * Returns all currently active statuses
     * 
     * @return An ArrayList<Status> of all current statuses
     */
    public ArrayList<Status> allStatus() {
        return new ArrayList<Status>(statusList.values());
    }

    /**
     * Returns all statuses with a given level
     * 
     * @param lvl The level of statuses to return
     * @return An ArrayList<Status> of all active statuses with level lvl
     */
    public ArrayList<Status> allStatus(double lvl) {
        ArrayList<Status> returnList = allStatus();
        for (int i = 0; i < returnList.size(); i++) {
            if (returnList.get(i).getLvl() != lvl) {
                returnList.remove(i);
                i--;
            }
        }
        return returnList;
    }

    /**
     * Creates a popUp window with all current statuses in it.
     */

    public void popupStatus() {
    	if (statusViewer == null){
    		statusViewer = new StatusViewer();
    	} else if (popUpManager.containsPopUpWindow(statusViewer)){
    		statusViewer.update();
    		return;
    	}
    	popUpManager.addPopUpWindow(statusViewer);
    }
}
