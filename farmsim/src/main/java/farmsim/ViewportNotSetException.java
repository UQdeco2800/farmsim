package farmsim;

/**
 * This exception exists to provide compatibility with all the damn singletons.
 *
 * <p>It only exists as a temporary solution until we get rid of the
 * singletons</p>
 *
 * @author lightandlight
 */
public class ViewportNotSetException extends Exception {
    public ViewportNotSetException() {
        super("setViewport() must be called before this method");
    }
}
