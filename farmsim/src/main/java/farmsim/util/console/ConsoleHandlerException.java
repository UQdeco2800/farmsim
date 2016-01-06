package farmsim.util.console;

/**
 * A general purpose exception for the console to use when the handler couldn't
 * process the command in a error safe way.
 */
public class ConsoleHandlerException extends RuntimeException {
    /**
     * Exception for when the Handlers cannot process the command without
     * causing an error.
     */
    public ConsoleHandlerException() {
        super();
    }

    /**
     * Exception for when the Handlers cannot process the command without
     * causing an error allowing the error to be passed through.
     * 
     * @param exception that was thrown in the Handler.
     */
    public ConsoleHandlerException(Exception exception) {
        super(exception);
    }
}
