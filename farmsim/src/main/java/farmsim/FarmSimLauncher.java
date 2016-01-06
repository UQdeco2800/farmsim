package farmsim;

import java.net.URL;

import farmsim.entities.tileentities.crops.DatabaseHandler;
import farmsim.ui.FarmSimController;
import farmsim.util.TicksPerSecond;
import farmsim.util.console.Console;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Launcher class for FarmSim.
 *
 * @author Leggy
 */
public class FarmSimLauncher extends Application {
    private String version = Double.toString(Math.PI/10);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the plant data from the database so it can be accessed without
        // future database reads
        DatabaseHandler.getInstance().importData();

        URL location = getClass().getResource("/FarmSim.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);

        Parent root = fxmlLoader.load(location.openStream());
        FarmSimController farmSimController = fxmlLoader.getController();
        Scene scene = new Scene(root);
        scene.getStylesheets().add("css/style.css");
        primaryStage.setTitle("DecoFarm - A Farming Simulator v" + version
                + ": Now with moar ducks!");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("/icons/icon.png"));
        primaryStage.setOnCloseRequest(e -> farmSimController.stopGame());
        primaryStage.setMinWidth(1280);
        primaryStage.setMinHeight(720);
        primaryStage.show();
        primaryStage.setMaximized(false);

        Stage console = startConsole(primaryStage);
        Console.getInstance().attachGui(console);

        TicksPerSecond.getInstance().setFarmSimController(farmSimController);

        //FarmSimController.initSoundEffectsThread();
        //FarmSimController.startSoundEffectsThread();

        FarmSimController.initMusicThread();
        FarmSimController.startMusicThread();
    }

    /**
     * @param parent the stage to bind the console too (if it closes we close)
     * @throws Exception If the Console.fxml couldn't be found.
     */
    public Stage startConsole(Stage parent) throws Exception {
        URL location = getClass().getResource("/util/console/Console.fxml");
        Parent root = FXMLLoader.load(location);

        final Stage console = new Stage();
        Scene scene = new Scene(root, 740, 480);
        scene.getStylesheets().add("css/console.css");

        console.setTitle("Rubber Duck");
        console.getIcons().add(new Image("/icons/icon.png"));
        console.initModality(Modality.NONE);
        console.initOwner(parent);
        console.setScene(scene);

        return console;
    }

    @Override
    public void stop() {
        FarmSimController.stopMusicThread();
        FarmSimController.stopSoundEffectsThread();
    }
}
