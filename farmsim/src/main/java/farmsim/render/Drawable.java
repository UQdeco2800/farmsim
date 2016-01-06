package farmsim.render;

import farmsim.Viewport;

import javafx.scene.canvas.GraphicsContext;

/**
 * Objects positioned in the world that can be drawn.
 *
 * @author lightandlight
 */
public interface Drawable extends HasPosition {
    /**
     * Draws the object to the specified {@link GraphicsContext} given the
     * position of the {@link Viewport}.
     */
    void draw(Viewport viewport, GraphicsContext graphics);
}
