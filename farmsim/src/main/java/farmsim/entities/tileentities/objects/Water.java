package farmsim.entities.tileentities.objects;

import farmsim.tiles.Tile;

public class Water extends BaseObject {

    public Water(Tile parent) {
        super("water", parent, false);
        super.setHasDirectionalSprites(true);
    }
    
    public Water(Tile parent, String type) {
        super(type, parent, false);
        super.setHasDirectionalSprites(true);
    }

    /**
     * Get the Water tile type (water or ice)
     *
     * @return the type currently active.
     */
    @Override
    public String getTileType() {
        if(getParent() != null
                && "WINTER".equals(getParent().getActiveSeason())) {
            return "ice";
        } else {
            return super.getTileType();
        }
    }
}
