package farmsim.render;

/**
 * Objects that have a position in the world.
 *
 * @author lightandlight
 */
public interface HasPosition {
    /**
     * The object's x coordinate.
     *
     * @return A number between 0 and the maximum world width
     */
    double getWorldX();

    /**
     * The object's y coordinate.
     *
     * @return A number between 0 and the maximum world height
     */
    double getWorldY();
}
