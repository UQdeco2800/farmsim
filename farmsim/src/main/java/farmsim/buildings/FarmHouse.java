package farmsim.buildings;
import farmsim.world.World;



/**
 * A place to hang out you know? 
 * @author the one and only sc0urge
 */
public class FarmHouse extends AbstractBuilding {
    public static final int WIDTH = 4;
    public static final int HEIGHT = 4;
    public static final String SPRITE_LOCATION =
            "/buildings/farmHouse.png";

    public FarmHouse(World world) {
        super(world, WIDTH, HEIGHT, SPRITE_LOCATION);
    }
}

