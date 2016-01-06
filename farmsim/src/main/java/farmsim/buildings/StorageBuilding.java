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
 * The Storage is required to store items
 *
 * @author hankijord
 */
public class StorageBuilding extends AbstractBuilding {
    public static final String SPRITE_LOCATION =
            "/buildings/storeHouse.png";
    public static final int WIDTH = 4;
    public static final int HEIGHT = 3;

    private PopUpWindow storagePopup;
	
	/**
	* Storage Building to open storage popup
	*/
    public StorageBuilding(World world) {
        super(world, WIDTH, HEIGHT, SPRITE_LOCATION);
    }

	/**
	* when double click load PopUp
	*/
    @Override
    public void onDoubleClick(Point location) {
        if (storagePopup == null) {
            try {
                loadPopUp();
            } catch (IOException e) {
                Logger l = LoggerFactory.getLogger(StorageBuilding.class);
                l.error("Error loading FXML", e);
                return;
            }
        }
    }
	
	/**
	* Loads the PopUp
	*/
    private void loadPopUp() throws IOException {
        PopUpWindow popUp = new PopUpWindow(475, 800, 200, 200, "Storage");
        Pane pane = FXMLLoader.load(getClass().getClassLoader()
                .getResource("Storage.fxml"));
        popUp.setContent(pane);
        PopUpWindowManager.getInstance().addPopUpWindow(popUp);
    }
}
