package farmsim.events;

/**
 * Contains definitions of the shapes of crop circles. This class should not be
 * instantiated. It should only be accessed by the static methods.
 * 
 * A crop circle is an int[9][9] which should be returned by a static method.
 */
public class CropCircleDefinitions {
	private static final int[][] CIRCLE0 = { 
		{ 0, 0, 0, 1, 1, 1, 0, 0, 0 },
		{ 0, 0, 1, 0, 1, 0, 1, 0, 0 }, 
		{ 0, 1, 1, 0, 1, 0, 1, 1, 0 },
		{ 1, 0, 0, 1, 1, 1, 0, 0, 1 }, 
		{ 1, 1, 1, 1, 0, 1, 1, 1, 1 },
		{ 1, 0, 0, 1, 1, 1, 0, 0, 1 }, 
		{ 0, 1, 1, 0, 1, 0, 1, 1, 0 },
		{ 0, 0, 1, 0, 1, 0, 1, 0, 0 }, 
		{ 0, 0, 0, 1, 1, 1, 0, 0, 0 } };
	private static final int[][] CIRCLE1 = { 
		{ 0, 0, 1, 1, 1, 1, 1, 0, 0 },
		{ 0, 1, 0, 0, 0, 0, 0, 1, 0 }, 
		{ 1, 0, 1, 0, 0, 0, 1, 0, 1 },
		{ 1, 0, 0, 1, 0, 1, 0, 0, 1 }, 
		{ 1, 0, 0, 0, 1, 0, 0, 0, 1 },
		{ 1, 0, 0, 1, 0, 1, 0, 0, 1 }, 
		{ 1, 0, 1, 0, 0, 0, 1, 0, 1 },
		{ 0, 1, 0, 0, 0, 0, 0, 1, 0 }, 
		{ 0, 0, 1, 1, 1, 1, 1, 0, 0 } };
	private static final int[][] CIRCLE2 = { 
		{ 0, 0, 0, 1, 1, 1, 0, 0, 0 },
		{ 0, 0, 1, 1, 1, 1, 1, 0, 0 }, 
		{ 0, 1, 1, 1, 1, 1, 1, 1, 0 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		{ 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
		{ 0, 1, 1, 1, 1, 1, 1, 1, 0 },
		{ 0, 0, 1, 1, 1, 1, 1, 0, 0 }, 
		{ 0, 0, 0, 1, 1, 1, 0, 0, 0 } };

	/**
	 * Gets a crop circle
	 * 
	 * @param style
	 *            The crop circle number to return
	 * @return An int[9][9] with the definition of a crop circle
	 */
	public static int[][] getCircle(CropCircleStyle style) {
		switch (style) {
		case circle0:
			return CIRCLE0.clone();
		case circle1:
			return CIRCLE1.clone();
		case circle2:
			return CIRCLE2.clone();
		default:
			return CIRCLE0.clone();
		}

	}
}
