package farmsim.buildings;

import java.util.List;

import farmsim.entities.tileentities.objects.Water;
import farmsim.tiles.Tile;
import farmsim.world.World;
import farmsim.world.WorldManager;

/**
 * A jetty is necessary for peons to go fishing.
 * 
 * @author rachelcatchpoole for Team Adleman
 */
public class Jetty extends AbstractBuilding {
    public static int WIDTH = 3;
    public static int HEIGHT = 1;
    public static String SPRITE_LOCATION = "/buildings/jetty.png";
    // the fishable radius around a jetty
    private int radius = 10;
    // the tile of the jetty that sits on the land
    private Tile land;
    // the middle tile of the jetty
    private Tile middle;
    // the end of the jetty from which peon's fish
    private Tile end;

    /**
     * Creates a jetty in the world
     * @param world
     *      The world containing the jetty
     */
    public Jetty(World world) {
        super(world, WIDTH, HEIGHT, SPRITE_LOCATION);
    }
    
    /**
     * Creates a jetty 
     * @param world
     *      The world containing the jetty
     * @param width
     *      The width of the jetty
     * @param height
     *      The height of the jetty
     * @param spriteLocation
     *      The jetty's sprite location
     */
    public Jetty(World world, int width, int height, String spriteLocation) {
        super(world, width, height, spriteLocation);
    }
    
    /**
     * @return True iff the jetty is in a valid location.
     */
    @Override
    public boolean isLocationValid() {
        List<Tile> tiles = getTiles();
        Tile tile1 = tiles.get(0);
        middle = tiles.get(1);
        Tile tile3 = tiles.get(2);
        
        // the middle tile must be water
        if (!(middle.getTileEntity() instanceof Water)) {
            return false;
        }
        
        // calculate which end of the jetty is in the water
        if (tile1.getTileEntity() == null && tile3.getTileEntity() instanceof 
                Water) {
            land = tile1;
            end = tile3;
        } else if (tile1.getTileEntity() instanceof Water && 
                tile3.getTileEntity() == null) {
            land = tile3;
            end = tile1;
        } else {
            return false;
        }
        
        return true;
    }
    
    /**
     * Returns the end tile of the jetty
     * @return
     *      the end tile of the jetty from which peons can fish
     */
    public Tile getEnd() {
        return end;
    }
    
    /**
     * Returns the radius around a jetty that can be fished
     * @return the radius around the end point of the jetty
     */
    public int getRadius() {
        return radius;
    }
}

