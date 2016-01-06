package farmsim.util.console;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

/**
 * An appender for outputting to the internal console which can be launched in
 * the game by using the debug option.
 */
public class ConsoleAppender extends AppenderSkeleton {
    /**
     * The Appender responsible for adding the logs to the relevant lists.
     * 
     * @param event the logging event.
     */
    @Override
    protected void append(LoggingEvent event) {
        Console console = Console.getInstance();
        if (event.getMessage() != null && event.getMessage() instanceof String
                && console != null) {
            PatternLayout layout = new PatternLayout(
                    "%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n");
            String string = layout.format(event);
            String level = event.getLevel().toString();
            console.addLog(level, string);
        }
    }

    /**
     * Cleanup for the appender (clearing buffers). No cleanup is needed for the
     * console appender as it doesnt have buffer.
     */
    @Override
    public void close() {
        // Do nothing because no cleanup needed
    }

    /**
     * Lets the logging framework know is a layout is required. For the console
     * no layout is needed as it is handled internally.
     * 
     * @return false always as not required.
     */
    @Override
    public boolean requiresLayout() {
        return false;
    }
}
