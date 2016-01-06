package farmsim.util.console.handler;

/**
 * Interface to force the children of Base Handler.
 */
public interface BaseHandlerInterface {
    /**
     * Handel interface for the console handlers.
     * @param parameters the command that was captured.
     */
    void handle(String[] parameters);
}
