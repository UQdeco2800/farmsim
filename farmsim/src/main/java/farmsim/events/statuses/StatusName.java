package farmsim.events.statuses;

/**
 * All valid status' that can be accepted by the StatusHandler
 * 
 * ###voidstatus should not be directly set. voidstatus is used as the undefined
 * status and in JUnity tests only.###
 * 
 * @author bobri
 */
public enum StatusName {
    STRIKE, DROUGHT, RAIN, SPEEDCHANGE, VOIDSTATUS, UFO, Christmas, FORESTFIRE
}
