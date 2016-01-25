package farmsim.ui;

import farmsim.GameManager;
import farmsim.Viewport;
import farmsim.ViewportNotSetException;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Handles Mouse release events.
 * 
 * @author Leggy
 *
 */
public class MouseReleasedHandler implements EventHandler<MouseEvent> {
    private Viewport viewport;

    public MouseReleasedHandler(Viewport viewport) {
        super();
        this.viewport = viewport;
    }

    @Override
    public void handle(MouseEvent event) {
        Point tileLocation = new Point(
                event.getX() / TileRegister.TILE_SIZE + viewport.getX(),
                event.getY() / TileRegister.TILE_SIZE + viewport.getY());

        GameManager gameManager = GameManager.getInstance();

        try {
            gameManager.setReleased(tileLocation);
        } catch (ViewportNotSetException e) {
            gameManager.setViewport(viewport);
        }
    }

}
