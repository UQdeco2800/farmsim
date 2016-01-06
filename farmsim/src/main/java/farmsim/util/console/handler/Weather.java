package farmsim.util.console.handler;

import farmsim.util.console.Console;
import farmsim.world.World;
import farmsim.world.WorldManager;
import farmsim.world.weather.WeatherType;

/**
 * WeatherHandler.
 */
public class Weather extends BaseHandler implements BaseHandlerInterface {

    /**
     * Weather Command Handler.
     */
    public Weather() {
        super();
        this.addCommands();
        this.setName("Weather");
    }

    /**
     * adds the commands to the handler.
     */
    private void addCommands() {
        addSingleCommand("weather-get",
                "weather-get, returns the current " + "weather");
        addSingleCommand("weather-set", "weather-set [name], sets weather "
                + "according to name {raining, snowing, sunny}");
    }

    /**
     * Handler for commands of the weather system.
     * 
     * @param parameters the incoming command parameters.
     */
    @Override
    public void handle(String[] parameters) {
        if (parameters != null && parameters.length > 0
                && this.contains(parameters[0])) {
            switch (parameters[0]) {
                case "weather-get":
                    weatherGet();
                    break;
                case "weather-set":
                    weatherSet(parameters);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Gets current weather that's active.
     */
    private void weatherGet() {
        World world = WorldManager.getInstance().getWorld();
        Console.getInstance().println("weather: " + world.getWeatherName()
                + " enum: " + world.getWeatherType().toString());
    }

    /**
     * Sets the weather depending on incoming parameters.
     * 
     * @param parameters the weather settings.
     */
    private void weatherSet(String[] parameters) {
        WeatherType weather = null;
        World world = WorldManager.getInstance().getWorld();
        if (parameters.length == 2) {
            for (WeatherType weatherType : WeatherType.values()) {
                if (weatherType.toString().toLowerCase().equals(
                        parameters[1])) {
                    weather = weatherType;
                }
            }
            if (weather != null && !world.setWeather(weather)) {
                Console.getInstance().println("setting weather failed");
            }
            if (weather == null) {
                for (WeatherType weatherType : WeatherType.values()) {
                    Console.getInstance().println(
                            weatherType.toString().toLowerCase());
                }
            }
        }
    }
}
