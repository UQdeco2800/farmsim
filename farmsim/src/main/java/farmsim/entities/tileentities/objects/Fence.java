package farmsim.entities.tileentities.objects;

import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;

/**
 *
 * @author hoyland6
 *
 *         Fence
 *
 */

public class Fence extends BaseObject {

    private String location;
    private boolean isGate;
    private boolean gateOpen;

    /**
     * Create a new Fence
     */
    public Fence(Tile parent, String location, boolean isGate) {
        super("fence", parent);
        this.location = location;
        this.isGate = isGate;
        gateOpen = false;
    }

    public void toggleGate() {
        if (gateOpen) {
            gateOpen = false;
            getParent().setProperty(TileProperty.PASSABLE, false);
        }
        else {
            gateOpen = true;
            getParent().setProperty(TileProperty.PASSABLE, true);
        }
    }

    public boolean isGateOpen() {
        if (gateOpen) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String getTileType() {
        if (!isGate) {
            if (location == "BL") {
                return "fenceBL";
            } else if (location == "BR") {
                return "fenceBR";
            } else if (location == "H") {
                return "fenceH";
            } else if (location == "P") {
                return "fenceP";
            } else if (location == "TL") {
                return "fenceTL";
            } else if (location == "TR") {
                return "fenceTR";
            } else if (location == "V") {
                return "fenceV";
            } else if (location == "T") {
                return "fenceT";
            } else {
                return "fenceP";
            }
        }
        else {
            if (gateOpen) {
                return "fenceGateOpen";
            }
            else {
                return "fenceGateClosed";
            }
        }
    }
}
