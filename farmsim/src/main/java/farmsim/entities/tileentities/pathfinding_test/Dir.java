package farmsim.entities.tileentities.pathfinding_test;

import farmsim.entities.tileentities.objects.BaseObject;
import farmsim.tiles.Tile;

public class Dir extends BaseObject {

    public Dir(Tile parent, String type) {
        super(type, parent, false);
        super.setHasDirectionalSprites(true);
    }
}
