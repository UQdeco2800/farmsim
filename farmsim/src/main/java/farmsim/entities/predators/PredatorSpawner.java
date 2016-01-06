package farmsim.entities.predators;

import java.util.Random;

import farmsim.Viewport;
import farmsim.util.Point;

/**
 * Handles predator spawning
 * @author r-portas
 *
 */
public class PredatorSpawner {
	private ViewportTranslator translator;
	private Random randGenerator;
	
	public PredatorSpawner(ViewportTranslator translator) {
		this.translator = translator;
		randGenerator = new Random();
	}
	
	/**
	 * Gets a random number in the range of the visible
	 * ingame x coordinates
	 * @return Integer representing x location
	 */
	private int getHorizontalSpawnRange() {
		int left = translator.getLeft();
		int right = translator.getRight();
		int offset = randGenerator.nextInt(right - left);
		int position = left + offset;
		return position;
	}
	
	/**
	 * Gets a random number in the range of the visible
	 * ingame y coordinates
	 * @return Integer representing y location
	 */
	private int getVerticalSpawnRange() {
		int top = translator.getTop();
		int bottom = translator.getBottom();
		int offset = randGenerator.nextInt(bottom - top);
		int position = top + offset;
		return position;
	}
	/**
	 * Gets the x and y coords to spawn at the top of the screen
	 * @return A point representing a spawn location
	 */
	private Point getTopPoint() {
		int y = translator.getTop();
		int x = getHorizontalSpawnRange();
		Point top = new Point(x, y);
		return top;
	}
	

	/**
	 * Gets the x and y coords to spawn at the bottom of the screen
	 * @return A point representing a spawn location
	 */
	private Point getBottomPoint() {
		int y = translator.getBottom();
		int x = getHorizontalSpawnRange();
		Point bottom = new Point(x, y);
		return bottom;
	}
	
	/**
	 * Gets the x and y coords to spawn at the left of the screen
	 * @return A point representing a spawn location
	 */
	private Point getLeftPoint() {
		int y = getVerticalSpawnRange();
		int x = translator.getLeft();
		Point left = new Point(x, y);
		return left;
	}

	/**
	 * Gets the x and y coords to spawn at the right of the screen
	 * @return A point representing a spawn location
	 */
	private Point getRightPoint() {
		int y = getVerticalSpawnRange();
		int x = translator.getRight();
		Point right = new Point(x, y);
		return right;
	}

	/**
	 * Gets a location for the predator to spawn at a random location
	 * @return A random Point on the screen
	 */
	public Point getRandomSpawnLocation() {
		int screenSide = randGenerator.nextInt(4);
		return getSpawnLocation(screenSide);
	}
	
	/**
	 * Gets a location to spawn on the given screenSide
	 * @param screenSide A value between 0 and 3 inclusive to tell which side to spawn on
	 * @return A Point representing the location
	 */
	public Point getSpawnLocation(int screenSide) {
		switch (screenSide) {
			case 0:
				// The top of the screen
				return getTopPoint();
			case 1:
				// The bottom of the screen
				return getBottomPoint();
			case 2:
				// The left of the screen
				return getLeftPoint();
			case 3:
				// The right of the screen
				return getRightPoint();
			default:
				return null;
				
		}
	}
}
