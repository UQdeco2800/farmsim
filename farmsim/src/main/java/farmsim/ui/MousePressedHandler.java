package farmsim.ui;

import farmsim.GameManager;
import farmsim.Viewport;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Handles Mouse press events.
 * 
 * @author Leggy
 *
 */
public class MousePressedHandler implements EventHandler<MouseEvent> {
    private Viewport viewport;

    public MousePressedHandler(Viewport viewport) {
        super();
        this.viewport = viewport;
    }

    @Override
    public void handle(MouseEvent event) {
        ((Pane) event.getSource()).requestFocus();
        Point tileLocation = new Point(
                event.getX() / TileRegister.TILE_SIZE + viewport.getX(),
                event.getY() / TileRegister.TILE_SIZE + viewport.getY());
        GameManager.getInstance().setPressed(tileLocation, event.isPrimaryButtonDown());

    }

}
