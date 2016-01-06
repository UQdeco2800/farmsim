package farmsim.buildings;

import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.ui.PopUpWindow;
import farmsim.ui.PopUpWindowManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Office PopUp
 */
public class OfficePopUp extends PopUpWindow implements Initializable {
    public static final int WIDTH = 850;
    public static final int HEIGHT = 500;

    PopUpWindow popup;

    @FXML
    private BorderPane mainPane;

    @FXML
    private BorderPane headerPane;
	
	/**
	* is an office PopUp
	*/
    public OfficePopUp() {
        super(HEIGHT, WIDTH, 0, 0, "Office");
        getChildren().clear();
    }

    /**
     * initalises office PopUp
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getChildren().add(mainPane);
        addDragging(headerPane);
        populateAgents();
    }
	
    /**
     * populates agents 
     */
    private void populateAgents() {
        List<Agent> agents = AgentManager.getInstance().getAgents();
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
        }
    }
	
    /**
    * shows the office PopUp
    */
    public void show() {
        if (!PopUpWindowManager.getInstance().containsPopUpWindow(this)) {
            PopUpWindowManager.getInstance().addPopUpWindow(this);
            mainPane.setMaxSize(WIDTH, HEIGHT);
            setMaxSize(WIDTH, HEIGHT);
        }
    }
	
    /**
    * closes office PopUpWindow
    */
    public void clickClose(Event e) {
        PopUpWindowManager.getInstance().removePopUpWindow(this);
    }
}
