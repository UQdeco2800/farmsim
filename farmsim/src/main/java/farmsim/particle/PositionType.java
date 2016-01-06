package farmsim.particle;

/**
 * The different bounding options for a Emitter.
 * <p>
 *     Type: x, y
 *     TOPRIGHT: screenWidth, 0
 *     TOPLEFT: 0, 0
 *     BOTTOMRIGHT: screenWidth, screenHeight
 *     BOTTOMLEFT: 0, screenHeight
 * </p>
 */
public enum PositionType {
    TOPRIGHT, TOPLEFT, BOTTOMRIGHT, BOTTOMLEFT
}
