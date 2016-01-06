package farmsim.ui;


import farmsim.world.WorldManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;


/**
 * @author homer5677
 */
public class AnimalProcessingSelectionPopUp extends PopUpWindow{
    public static final int WIDTH = 600;
    public static final int HEIGHT = 500;
    private WorldManager worldManager;

    private AnimalProcessingSelectionController controller;

    //check on throw exception
    public AnimalProcessingSelectionPopUp() throws IOException {
        super(HEIGHT, WIDTH, 0, 0, "Animal Processing Selection");
        getStylesheets().add("css/animalProcessing.css");


        Pane pane = FXMLLoader.load(getClass().getClassLoader()
                        .getResource("animal_processing/AnimalProcessingSelection.fxml"));

        this.setContent(pane);
    }





}
