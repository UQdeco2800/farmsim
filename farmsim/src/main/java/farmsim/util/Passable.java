package farmsim.util;

import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.world.WorldManager;
import farmsim.world.terrain.Biomes;

public class Passable {

    /**
     * Checks if a point is passable
     * @param point
     *          the point to check
     * @return
     *          true if the point is passable, otherwise false
     */
	public static boolean passable(Point point) {
        return passable((int) point.getX(), (int) point.getY());
	}

    /**
     * Check if a point is passable
     * @param x
     *          the x-coordinate of the point
     * @param y
     *          the y-coordinate of the point
     * @return
     *          true if the point is passable, otherwise false
     */
	public static boolean passable(int x, int y) {
        Tile tile = WorldManager.getInstance().getWorld().getTile(x, y);
        if (!tile.hasProperty(TileProperty.IS_BUILDING)) {
            return (boolean) tile.getProperty(TileProperty.PASSABLE);
        }
        return !(boolean) tile.getProperty(TileProperty.IS_BUILDING) &&
                (boolean) tile.getProperty(TileProperty.PASSABLE);
    }

    /**
     * Gives the pathfinding 'weight' of a point. This weight reflects how
     * 'hard' it is to move through that point, with a higher weight indicating
     * harder movement, and a less desirable point for pathfinding.
     *
     * Weight is currently determined by Biome:
     * - Grassland = 1
     * - Arid = 1
     * - Forest = 4
     * - Rocky = 9
     * - Marsh = 9
     *
     * @param point
     *          the point to check
     * @return
     *          -1 if the Biome property is not properly defined, else the
     *          weight of the tile at that point
     */
    public int weight(Point point) {
        return weight((int) point.getX(), (int) point.getY());
    }

    /**
     * Gives the pathfinding 'weight' of a point. This weight reflects how
     * 'hard' it is to move through that point, with a higher weight indicating
     * harder movement, and a less desirable point for pathfinding.
     *
     * Weight is currently determined by Biome:
     * - Grassland = 1
     * - Arid = 1
     * - Forest = 4
     * - Rocky = 9
     * - Marsh = 9
     *
     * @param x
     *          the x-coordinate of the point
     * @param y
     *          the y-coordinate of the point
     * @return
     *          -1 if the Biome property is not properly defined, else the
     *          weight of the tile at that point
     */
    public int weight(int x, int y) {
        Tile tile = WorldManager.getInstance().getWorld().getTile(x, y);
        Biomes biome = (Biomes) tile.getProperty(TileProperty.BIOME);
        if (biome == null) {
            return -1;
        } else {
            if (biome == Biomes.GRASSLAND || biome == Biomes.ARID) {
                return 1;
            } else if (biome == Biomes.FOREST) {
                return 4;
            } else if (biome == Biomes.ROCKY || biome == Biomes.MARSH) {
                return 9;
            } else {
                return -1;
            }
        }
    }
}
