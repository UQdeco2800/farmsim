package farmsim.buildings;

import farmsim.ui.PopUpWindow;
import farmsim.ui.PopUpWindowManager;
import farmsim.util.Point;
import farmsim.world.World;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The office is required to set up contracts
 *
 * @author wondertroy
 */
public class Office extends AbstractBuilding {
    public static final String SPRITE_LOCATION =
            "/buildings/office/office.png";
    public static final int WIDTH = 4;
    public static final int HEIGHT = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(Office.class);

    private OfficePopUp popup;

    /**
    * Is an office
    */
    public Office(World world) {
        super(world, WIDTH, HEIGHT, SPRITE_LOCATION);
    }

    /**
    * when double click load PopUp
    */
    @Override
    public void onDoubleClick(Point location) {
        if (popup == null) {
            try {
                loadPopUp();
            } catch (IOException e) {
                LOGGER.error("Error loading FXML", e);
                return;
            }
        }
    }

    /**
     * Loads the PopUp
     */
    private void loadPopUp() throws IOException {
    	PopUpWindow cPopUp = new PopUpWindow(645, 1000, 150, 50, "Contracts");
        Pane cPane = FXMLLoader.load(getClass().getClassLoader().getResource("contractWindow.fxml"));
        cPopUp.setContent(cPane);
        cPopUp.getStylesheets().add("css/contractstyle.css");
        PopUpWindowManager.getInstance().addPopUpWindow(cPopUp);
    }
}
