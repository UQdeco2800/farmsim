package farmsim.ui;

import farmsim.MiniMap;
import farmsim.Viewport;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Handler to allow movement of the viewport via the minimap
 * 
 * @author BlueDragon23
 *
 */
public class MapMouseHandler implements EventHandler<MouseEvent> {

    private MiniMap minimap;
    private Viewport viewport;

    public MapMouseHandler(Viewport viewport, MiniMap minimap) {
        super();
        this.minimap = minimap;
        this.viewport = viewport;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED || 
                        event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            event.consume();
        }
        double x = event.getX();
        double y = event.getY();
        viewport.setPosition((int) (x / minimap.getTileSize() - viewport
                        .getWidthTiles() / 2),
                        (int) (y / minimap.getTileSize() - viewport
                                        .getHeightTiles() / 2));
        event.consume();
    }

}
