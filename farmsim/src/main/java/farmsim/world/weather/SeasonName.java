package farmsim.world.weather;

/**
 * Enumerated type to represent a season. Members are Summer, Autumn, Winter,
 * Spring.
 */
public enum SeasonName {
    SUMMER, AUTUMN, WINTER, SPRING;

    /**
     * Returns the chronologically-next season.
     * @return a SeasonName for the next season.
     */
    public SeasonName next() {
        return values()[(this.ordinal() + 1) % 4];
    }
}
