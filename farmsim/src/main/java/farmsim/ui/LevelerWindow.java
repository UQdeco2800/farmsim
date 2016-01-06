package farmsim.ui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Pos;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;

/**
 *
 * @author jack775544
 */
public class LevelerWindow extends BorderPane {
    // I hate JavaFX

    // The pane that stores the content of the leveler window
    private BorderPane windowPane;

    public LevelerWindowController controller;

    /**
     * Creates a leveler window
     * @param mainPane the pane that is the parent
     */
    public LevelerWindow(Pane mainPane) {
        URL location;
        FXMLLoader loader = new FXMLLoader();
        Pane levelerContent = null;
        location = getClass().getResource("/leveler/Leveler.fxml");
        loader.setLocation(location);
        controller = new LevelerWindowController(this);
        loader.setController(controller);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        try {
            levelerContent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        windowPane = new BorderPane();
        windowPane.setPickOnBounds(false);
        windowPane.relocate(2, 26);

        getChildren().add(windowPane);
        setContent(levelerContent);
        mainPane.getChildren().add(this);
    }

    public LevelerWindowController getController(){
        return controller;
    }

    /**
     * Sets the content of the leveler window
     * @param content A pane containing the content
     */
    private void setContent(Pane content) {
        windowPane.setCenter(content);
    }
}
