package farmsim.entities.tileentities.objects;

import farmsim.entities.tileentities.TileEntity;
import farmsim.tiles.Tile;

/**
 * @author BlueDragon23
 * 
 *         Represents a TileEntity that is an object. If a tile has an
 *         BaseObject tile entity, the tile is not available for farming.
 * 
 */
public class BaseObject extends TileEntity {
    private boolean isClearable;
    private boolean hasDirectionalSprites = false;

    /**
     * Initalises a new object tile entity.
     *
     * @param type The tile type
     * @param parent The parent tile
     * @param isClearable whether the object is clearable with the
     *        ClearLandTask.
     */
    public BaseObject(String type, Tile parent, boolean isClearable) {
        super(type, parent);
        this.isClearable = isClearable;
    }

    /**
     * Create a new object tile entity. Defaults to being clearable
     * 
     * @param type The tile type
     * @param parent the parent tile
     */
    public BaseObject(String type, Tile parent) {
        this(type, parent, true);
    }

    /**
     * @return whether this object is clearable with the ClearLandTask.
     */
    public boolean isClearable() {
        return isClearable;
    }

    public void tick() {}

    public void setHasDirectionalSprites(boolean value) {
        hasDirectionalSprites = value;
    }

    public boolean hasDirectionalSprites() {
        return hasDirectionalSprites;
    }

    /**
     * Get different sprite names if the tile is on the edge of a lake/river
     * 
     * @param north true if there is water to the north of this tile
     * @param east true if there is water to the east of this tile
     * @param south true if there is water to the south of this tile
     * @param west true if there is water to the west of this tile
     * @return the name of the sprite to render
     */
    public String getSpriteName(boolean north, boolean east, boolean south,
            boolean west) {
        if (!north && !east && !south && !west) {
            return getTileType() + "NESW";
        }

        if (!east && !south && !west) {
            return getTileType() + "ESW";
        }
        if (!north && !south && !west) {
            return getTileType() + "NSW";
        }
        if (!north && !east && !west) {
            return getTileType() + "NEW";
        }
        if (!north && !east && !south) {
            return getTileType() + "NES";
        }

        if (!north && !east) {
            return getTileType() + "NE";
        }
        if (!north && !south) {
            return getTileType() + "NS";
        }
        if (!north && !west) {
            return getTileType() + "NW";
        }
        if (!east && !south) {
            return getTileType() + "ES";
        }
        if (!east && !west) {
            return getTileType() + "EW";
        }
        if (!south && !west) {
            return getTileType() + "SW";
        }

        if (!north) {
            return getTileType() + "N";
        }
        if (!east) {
            return getTileType() + "E";
        }
        if (!south) {
            return getTileType() + "S";
        }
        if (!west) {
            return getTileType() + "W";
        }

        return getTileType() + "";
    }
}
