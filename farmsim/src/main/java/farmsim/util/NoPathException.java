package farmsim.util;

/**
 * This exists to indicate that a path could not be found to a target
 */
public class NoPathException extends Throwable {
    public NoPathException() {
        super("No path could be found between these points");
    }
}
