package farmsim.entities.predators;

/**
 * A Wolf animal to attack farm animals
 * 
 * @author r-portas
 */
public class WolfPredator extends Predator {
	
    public WolfPredator(double x, double y, int health, double speed) {
        super(x, y, health, speed, 50, "wolf");
    }
}
