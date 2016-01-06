package farmsim.ui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

/**
 * Allows events outside the FarmSimController to add/remove popup windows.
 *
 * @author llste
 */
public class PopUpWindowManager {
    private static final PopUpWindowManager INSTANCE = new PopUpWindowManager();

    private AnchorPane mainPane;

    public static PopUpWindowManager getInstance() {
        return INSTANCE;
    }

    /**
     * Gives the manager the game's main AnchorPane. This should only be called
     * when the FarmSimController is initalised.
     */
    public void setMainPane(AnchorPane mainPane) {
        this.mainPane = mainPane;
    }

    /**
     * @return The game's main AnchorPane.
     */
    public AnchorPane getMainPane() {
        return mainPane;
    }

    /**
     * Adds the given popup window to the game.
     */
    public void addPopUpWindow(PopUpWindow popUp) {
        if (mainPane != null) {
            if (popUp.getLayoutX() < 0) {
                popUp.setLayoutX(0);
            } else if (popUp.getLayoutX() + popUp.getWidth()
                    > mainPane.getWidth()) {
                popUp.setLayoutX(mainPane.getWidth() - popUp.getWidth());
            }
            if (popUp.getLayoutY() < 0) {
                popUp.setLayoutY(0);
            } else if (popUp.getLayoutY() + popUp.getHeight()
                    > mainPane.getHeight()) {
                popUp.setLayoutY(mainPane.getHeight() - popUp.getHeight());
            }
            mainPane.getChildren().add(popUp);
        }
    }

    /**
     * Removes the given popup window from the game.
     */
    public void removePopUpWindow(PopUpWindow popUp) {
        if (mainPane != null) {
            mainPane.getChildren().remove(popUp);
        }
    }

    /**
     * @return true iff the game contains the given popup window.
     */
    public boolean containsPopUpWindow(PopUpWindow popUp) {
        return mainPane != null && mainPane.getChildren().contains(popUp);
    }

    /**
     * Remove the popup window that is on the top.
     * 
     * @return True iff a popup window was removed.
     */
    public boolean removeTopPopUpWindow() {
        if (mainPane != null) {
            List<Node> children = new LinkedList<Node>(mainPane.getChildren());
            Collections.reverse(children);
            for (Node node : children) {
                if (node instanceof PopUpWindow) {
                    mainPane.getChildren().remove(node);
                    return true;
                }
            }
        }
        return false;
    }
}
