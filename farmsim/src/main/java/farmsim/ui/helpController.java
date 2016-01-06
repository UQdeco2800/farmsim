package farmsim.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class helpController  implements Initializable{

	@FXML private TabPane topBar;
	@FXML private Pane general;
	@FXML private AnchorPane base;
	@FXML private AnchorPane welcomeAnchor;
	@FXML private Label labelWelcome;
	@FXML private Pane welcomePane;
	@FXML private AnchorPane welcomeAnchor2;
	@FXML private TextArea welcomeTextArea;
	@FXML private Pane workPane;
	@FXML private Pane animalsPane;
	@FXML private Pane predatorsPane;
	@FXML private Pane cropsPane;
	@FXML private Pane weatherPane;
	@FXML private Tab menuWelcome;
	@FXML private Tab menuFarmAnimals;
	@FXML private Tab menuCropsPlants;
	@FXML private Tab menuPredators;
	@FXML private Tab menuWeather;
	@FXML private Tab menuWorkers;
	@FXML private Tab menuTools;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("helpController");
		
	}
	
}
