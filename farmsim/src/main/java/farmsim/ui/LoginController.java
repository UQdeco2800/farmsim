package farmsim.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.client.GenericClient;
import farmsim.ui.Dialog.DialogMode;
import farmsim.world.WorldManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    private GenericClient client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = WorldManager.getInstance().getWorld().getFarmClient();
    }

    @FXML
    public void login(ActionEvent event) {
        boolean connected = client.authNew(username.getText(), password.getText());
        if (!connected) {
            connected = client.authExist(username.getText(), password.getText());
            if (!connected) {
                /* Invalid login */
                PopUpWindowManager.getInstance()
                        .addPopUpWindow(new Dialog("Invalid login", "Invalid username or password", DialogMode.ERROR));
                return;
            }
        }
        WorldManager.getInstance().getWorld().getMoneyHandler().update();
        /* Remove myself */
        PopUpWindowManager.getInstance().removeTopPopUpWindow();
        /* Login successful, proceed to MarketplaceController */
        PopUpWindow marketplace = new PopUpWindow(300, 300, 300, 150, "Marketplace");
        URL location = getClass().getResource("/Marketplace.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        try {
            Parent root = fxmlLoader.load();
            marketplace.setContent(root);
            PopUpWindowManager.getInstance().addPopUpWindow(marketplace);
        } catch (IOException e) {
            LOGGER.error("Error opening marketplace GUI", e);
        }
    }
    
    @FXML
    public void exit(ActionEvent event) {
        PopUpWindowManager.getInstance().removeTopPopUpWindow();
    }

}
