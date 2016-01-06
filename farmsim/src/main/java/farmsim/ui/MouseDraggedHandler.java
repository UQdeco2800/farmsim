package farmsim.ui;

import farmsim.GameManager;
import farmsim.Viewport;
import farmsim.ViewportNotSetException;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Handles Mouse drag events.
 */
public class MouseDraggedHandler implements EventHandler<MouseEvent> {
    private Viewport viewport;
    public MouseDraggedHandler(Viewport viewport) {
        super();
        this.viewport = viewport;
    }

    @Override
    public void handle(MouseEvent event) {
        GameManager gameManager = GameManager.getInstance();
        Point tileLocation = new Point(
                event.getX() / TileRegister.TILE_SIZE + viewport.getX(),
                event.getY() / TileRegister.TILE_SIZE + viewport.getY());

        try {
            gameManager.setDragged(tileLocation);
        } catch (ViewportNotSetException e) {
            gameManager.setViewport(viewport);
        }
    }

}
