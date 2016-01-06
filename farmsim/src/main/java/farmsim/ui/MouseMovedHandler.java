package farmsim.ui;

import farmsim.GameManager;
import farmsim.Viewport;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * MouseMotionHandler for the main pane. Moves the viewport when 
 * the mouse approaches the edge of the screen, after a short delay
 * 
 * @author BlueDragon23
 *
 */
public class MouseMovedHandler implements EventHandler<MouseEvent> {
    
    // How long the game should wait before scrolling the screen
    private static int DELAY = 800;
    
    private Viewport viewport;
    
    private boolean mapMovingByMouseHorizontal = false;
    private boolean mapMovingByMouseVertical = false;
    private long mouseWaitingRight = -1;
    private long mouseWaitingLeft = -1;
    private long mouseWaitingUp = -1;
    private long mouseWaitingDown = -1;

    public MouseMovedHandler(Viewport viewport) {
        super();
        this.viewport = viewport;
    }

    @Override
    public void handle(MouseEvent event) {
                        
        GameManager manager = GameManager.getInstance();
        int tileSize = TileRegister.TILE_SIZE;
        
        Point tileLocation = new Point(
                event.getX() / tileSize + viewport.getX(),
                event.getY() / tileSize + viewport.getY());
        
        manager.setMouseMoved(tileLocation);

        if (event.getX() < 3 * tileSize) {
            // The mouse is on the left edge
            if (mouseWaitingLeft != -1 && System.currentTimeMillis() - mouseWaitingLeft > DELAY) {
                mapMovingByMouseHorizontal = true;
                manager.setViewportLeft(true);
                manager.setViewportRight(false);
            } else if (mouseWaitingLeft == -1) {
                mouseWaitingLeft = System.currentTimeMillis();
            }
            mouseWaitingRight = -1;
        } else if (event.getX() > viewport.getWidthPixels() - 3
                * tileSize) {
            // The mouse is on the right edge
            if (mouseWaitingRight != -1 && System.currentTimeMillis() - mouseWaitingRight > DELAY) {
                mapMovingByMouseHorizontal = true;
                manager.setViewportRight(true);
                manager.setViewportLeft(false);
            } else if (mouseWaitingRight == -1) {
                mouseWaitingRight = System.currentTimeMillis();
            }
            mouseWaitingLeft = -1;
        } else {
            if (mapMovingByMouseHorizontal) {
                /* 
                 * Prevents the mouse from disrupting keyboard 
                 * control of the viewport
                 */
                mapMovingByMouseHorizontal = false;
                manager.setViewportLeft(false);
                manager.setViewportRight(false);
            }
            mouseWaitingRight = -1;
            mouseWaitingLeft = -1;
        }
        
        if (event.getY() < 3 * tileSize) {
            // The mouse is on the top edge
            if (mouseWaitingUp != -1 && System.currentTimeMillis() - mouseWaitingUp > DELAY) {
                mapMovingByMouseVertical = true;
                manager.setViewportUp(true);
                manager.setViewportDown(false);
            } else if (mouseWaitingUp == -1) {
                mouseWaitingUp = System.currentTimeMillis();
            }
            mouseWaitingDown = -1;
        } else if (event.getY() > viewport.getHeightPixels() - 3
                * tileSize) {
            // The mouse is on the bottom edge
            if (mouseWaitingDown != -1 && System.currentTimeMillis() - mouseWaitingDown > DELAY) {
                mapMovingByMouseVertical = true;
                manager.setViewportDown(true);
                manager.setViewportUp(false);
            } else if (mouseWaitingDown == -1) {
                mouseWaitingDown = System.currentTimeMillis();
            }
            mouseWaitingUp = -1;
        } else {
            if (mapMovingByMouseVertical) {
                /* 
                 * Prevents the mouse from disrupting keyboard 
                 * control of the viewport
                 */
                mapMovingByMouseVertical = false;
                manager.setViewportUp(false);
                manager.setViewportDown(false);
            }
            mouseWaitingDown = -1;
            mouseWaitingUp = -1;
        }
    }
}
