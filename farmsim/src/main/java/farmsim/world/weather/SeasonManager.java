package farmsim.world.weather;

import farmsim.world.WorldManager;
import farmsim.world.weather.seasons.Autumn;
import farmsim.world.weather.seasons.Spring;
import farmsim.world.weather.seasons.Summer;
import farmsim.world.weather.seasons.Winter;

import java.util.Observable;

/**
 * Season Manager.
 * A manager to control the loading and switching of the current active season.
 */
public class SeasonManager extends Observable {

    private Season currentSeason;
    private int startDay;
    private static final int SEASON_LENGTH = 30; // number of days in season

    /**
     * Creates a season manager. Intentionally empty (see comment).
     */
    public SeasonManager() {
        /* Constructor is intentionally empty. Formerly contained seasonStart()
        * method code, but after refactors in other classes, it became essential
        * that the code formerly here was called after completion of the 
        * World.java construction, where new SeasonManager() is called.
        */
    }

    /**
     * Starts a random season (to be called once after world starts).
     */
    public void seasonStart() {
        // Start a random season
        int ordinal;
        SeasonName toStart;

        ordinal = (int) Math.floor(Math.random() * 4);
        toStart = SeasonName.values()[ordinal];
        seasonSwitch(toStart);
    }

    /**
     * Gets current season as a string.
     * 
     * @return string name of current season
     */
    public String getSeason() {
        if (currentSeason == null) {
            return "None";
        }
        return currentSeason.getNameString();
    }
    
    public Season getSeasonObject() {
        return currentSeason;
    }

    /**
     * Switch to a specified season. Also notifies observers.
     * 
     * @param season The season to switch to.
     */
    public void seasonSwitch(SeasonName season) {
        switch (season) {
            case SUMMER:
                currentSeason = new Summer();
                break;
            case AUTUMN:
                currentSeason = new Autumn();
                break;
            case WINTER:
                currentSeason = new Winter();
                break;
            case SPRING:
                currentSeason = new Spring();
                break;
        }
        startDay = WorldManager.getInstance().getWorld().getTimeManager()
                .getDays();
        setChanged();
        notifyObservers(getSeason());
    }

    /**
     * Switch to the chronologically-next season. Must be called with a season
     * active (implementation is that a season will always be active)
     */
    public void seasonSwitch() {
        if (currentSeason != null) {
            seasonSwitch(currentSeason.getName().next());
        } else {
            seasonStart();
        }
    }

    
    /**
     * Get a string that when passed to JavaFX setStyle() will update the 
     * season icon to the current season. For use on the seasonIcon element in
     * FarmSimController.
     * @param season the season to check for
     * @param large if True, return the 64x64 variant, else the 16x16.
     * @return a valid string passable to to seasonIcon.setStyle().
     */
    public String getIconStyle(SeasonName season, Boolean large) {
        if (!large) {
            switch (season) {
                case SUMMER:
                    return "seasonSummer";
                case AUTUMN:
                    return "seasonAutumn";
                case WINTER:
                    return "seasonWinter";
                case SPRING:
                    return "seasonSpring";
            } 
        } else {
            switch (season) {
                case SUMMER:
                    return "forecastSummer";
                case AUTUMN:
                    return "forecastAutumn";
                case WINTER:
                    return "forecastWinter";
                case SPRING:
                    return "forecastSpring";
            }
        } 
        return null;    // unreachable
    }
    
    /**
     * Returns the filepath to the current season's icon.
     * @return the filepath
     */
    public String getIconPath() {
        return currentSeason.getPath();
    }
    
    /**
     * Gets the season end day.
     * @return the day the current season ends.
     */
    public int getSeasonEndDay() {
        return startDay + SEASON_LENGTH;
    }
    
    /**
     * Register seasonSwitch() as a monthly task with the time manager.
     */
    public void registerMonthlyTask() {
        WorldManager.getInstance().getWorld().getTimeManager()
                .registerMonthlyTask(() -> seasonSwitch());
    }
}
