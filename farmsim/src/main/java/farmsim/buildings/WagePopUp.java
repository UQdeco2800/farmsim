package farmsim.buildings;

/**
 * Wage Popup Controller
 */

import farmsim.entities.agents.AgentManager;
import farmsim.ui.PopUpWindow;
import farmsim.ui.PopUpWindowManager;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;


import java.net.URL;
import java.util.ResourceBundle;

import static farmsim.ui.Notification.makeNotification;

public class WagePopUp extends PopUpWindow implements Initializable {
    public static final int HEIGHT = 100;
    public static final int WIDTH = 100;
    private String name = "";

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField input;

    public WagePopUp(){
        super(HEIGHT, WIDTH, 0, 0, "Edit Wage");
        getChildren().clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        getChildren().add(mainPane);

        confirmButton.setOnMouseClicked(event -> {
            if(processWageEdit(input.getText())){
                makeNotification("SUCCESS","Wage Changed, Please click the refresh button");
            } else{
                makeNotification("FAILED","Please enter a number between 50 and 400");
            }
        });
    }

    public void clickClose(Event event) {
        PopUpWindowManager.getInstance().removePopUpWindow(this);
    }

    public void show(String name) {
        this.name = name;
        if (!PopUpWindowManager.getInstance().containsPopUpWindow(this)) {
            PopUpWindowManager.getInstance().addPopUpWindow(this);
            mainPane.setMaxSize(WIDTH, HEIGHT);
            setMaxSize(WIDTH, HEIGHT);
        }
    }

    private boolean processWageEdit(String input){
        int inputNum = 0;
        AgentManager am = AgentManager.getInstance();
        try {
            inputNum = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }

        if (inputNum > 400 || inputNum < 50){
            return false;
        }

        return am.changeAgentWage(name, inputNum);
    }
}
