package farmsim.ui;


import farmsim.GameManager;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * EventHandler for when the mouse leaves the screen
 * 
 * @author Aidan
 *
 */
public class MouseExitedHandler implements EventHandler<MouseEvent> {

    /**
     * Cancel any movement of the game window
     */
    @Override
    public void handle(MouseEvent event) {
        GameManager manager = GameManager.getInstance();
        manager.setViewportDown(false);
        manager.setViewportLeft(false);
        manager.setViewportRight(false);
        manager.setViewportUp(false);
    }

}
