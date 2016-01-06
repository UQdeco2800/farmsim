package farmsim.util.console;

import org.junit.Test;

/**
 * Tests for the custom command handler exception.
 */
public class ConsoleHandlerExceptionTest {

    /**
     * Tests creating an empty Console Handler Exception.
     */
    @Test(expected = ConsoleHandlerException.class)
    public void createException() {
        throw new ConsoleHandlerException();
    }

    /**
     * Tests creating an Console Handler Exception with a passing Exception.
     */
    @Test(expected = ConsoleHandlerException.class)
    public void createExceptionFromError() {
        try {
            int setToFail = 9001 / 0;
        } catch (Exception e) {
            throw new ConsoleHandlerException(e);
        }
    }
}
