package farmsim.entities.predators;

import farmsim.Viewport;
import farmsim.util.Point;

/**
 * Translates the viewport values into a form that is easier
 * to fit with the predator manager
 * @author r-portas
 *
 */
public class ViewportTranslator {
	private Viewport viewport;
	
	public ViewportTranslator() {
		// This will act as a fallback in case the viewport cannot be retrieved
		viewport = null;
	}

	/**
	 * Sets the viewport of the translator
	 * @param viewport The viewport to set to
	 */
	public void setViewport(Viewport viewport) {
		if (viewport != null) {
			this.viewport = viewport;
		}
	}

	/**
	 * Returns the top value
	 * @return The top value
	 */
	public int getTop() {
		if (viewport != null) {
			return viewport.getY();
		} else {
			return 0;
		}
	}

	/**
	 * Returns the bottom value
	 * @return The bottom value
	 */
	public int getBottom() {
		if (viewport != null) {
			return viewport.getY() + viewport.getHeightTiles();
		} else {
			return 100;
		}
	}

	/**
	 * Returns the right value
	 * @return The right value
	 */
	public int getRight() {
		if (viewport != null) {
			return viewport.getX() + viewport.getWidthTiles();
		} else {
			return 100;
		}
	}

	/**
	 * Returns the left value
	 * @return The left value
	 */
	public int getLeft() {
		if (viewport != null) {
			return viewport.getX();
		} else {
			return 0;
		}
	}

	/**
	 * Returns a point representing the bottom left
	 * @return A point
	 */
	public Point getBottomRight() {
		if (viewport != null) {
			return new Point(viewport.getX() + viewport.getWidthTiles(), viewport.getY() + viewport.getHeightTiles());
		} else {
			return new Point(100, 100);
		}
	}
	
	/**
	 * Returns a point representing the top right
	 * @return A point
	 */
	public Point getTopLeft() {
		if (viewport != null) {
			return new Point(viewport.getX(), viewport.getY());
		} else {
			return new Point(0, 0);
		}
	}

}
