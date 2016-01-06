package farmsim.buildings;

import farmsim.world.World;

/**
 * A jetty with north-south orientation
 * 
 * @author rachelcatchpoole for Team Adleman
 */
public class JettyVertical extends Jetty {
    public static final int WIDTH = 1;
    public static final int HEIGHT = 3;
    public static final String SPRITE_LOCATION = "/buildings/jettyVertical.png";

    /**
     * Creates a north-south jetty in the world
     * @param world
     *      The world containing the jetty
     */
    public JettyVertical(World world) {
        super(world, WIDTH, HEIGHT, SPRITE_LOCATION);
    }
    
}

