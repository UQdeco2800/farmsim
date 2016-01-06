package farmsim.particle;

/**
 * The different bounding options for a Emitter.
 * <p>
 *     SCREEN: variance for the particles is both the screen height and width.
 *     SCREENHEIGHT: particles y pos variance is only the height of the screen.
 *     SCREENWIDTH: particles x pos variance is only the width of the screen.
 * </p>
 */
public enum PositionVarianceType {
    SCREENHEIGHT, SCREENWIDTH, SCREEN, WORLD, WORLDHEIGHT, WORLDWIDTH
}
