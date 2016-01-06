package farmsim.ui;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

/**
 * Created by jack775544 on 25/10/2015.
 */
public class TechTreeWindow extends BorderPane{
    private BorderPane windowPane;
    private TechTreeController controller;

    public TechTreeWindow(Pane mainPane){
        URL location;
        FXMLLoader loader = new FXMLLoader();
        Pane levelerContent = null;
        loader.setLocation(location);
        controller = new TechTreeController(this);
        loader.setController(controller);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        try {
            levelerContent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        windowPane = new BorderPane();
        windowPane.setPickOnBounds(false);
        windowPane.relocate(30, 30);

        getChildren().add(windowPane);
        setContent(levelerContent);
        mainPane.getChildren().add(this);
    }

    /**
     * Sets the content for the tech tree window
     * @param content the pane to place inside the window
     */
    private void setContent(Pane content) {
        windowPane.setCenter(content);
    }

    /**
     * Gets the controller for the tech tree window
     * @return
     */
    public TechTreeController getController(){
        return controller;
    }

    /**
     * Alas poor TechTreeWindow, I knew it well
     * Kills the window
     */
    public void rip(){
        //such rip, much wow, very sad
        ((AnchorPane) TechTreeWindow.this.getParent()).getChildren()
                .remove(TechTreeWindow.this);
    }
}
