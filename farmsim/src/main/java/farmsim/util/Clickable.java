package farmsim.util;

/**
 * An interface which allows the object to receive click events. Once the
 * Clickable interface is implemented into your object you will need to register
 * it with the {@link farmsim.GameManager}.
 */
public interface Clickable {
    /**
     * @return true iff the point is located within the object.
     */
    public boolean containsPoint(Point point);

    public void click(Point location);
}
