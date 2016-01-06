package farmsim.buildings;

/**
 * Handler for building placer events.
 *
 * @author llste
 */
public interface BuildingPlacerEventHandler {
    /**
     * The placing of the given building was successful.
     */
    void onPlace(AbstractBuilding building);

    /**
     * The placing of the given building was canceled by the player.
     */
    void onCancel(AbstractBuilding building);
}
