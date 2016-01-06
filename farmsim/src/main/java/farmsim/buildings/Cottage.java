package farmsim.buildings;
import farmsim.world.World;



/**
 * A place to hang out you know? 
 * @author the one and only sc0urge
 */
public class Cottage extends AbstractBuilding {
    public static final int WIDTH = 3;
    public static final int HEIGHT = 3;
    public static final String SPRITE_LOCATION =
            "/buildings/cottage.png";

    public Cottage(World world) {
        super(world, WIDTH, HEIGHT, SPRITE_LOCATION);
    }
}

