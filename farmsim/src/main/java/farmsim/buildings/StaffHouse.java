package farmsim.buildings;

import farmsim.util.Point;
import farmsim.world.World;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;

/**
 * The Staff House is a building that allows users to view and control their
 * workers.
 *
 * @author llste
 */
public class StaffHouse extends AbstractBuilding {
    public static final String SPRITE_LOCATION =
            "/buildings/staffHouse/sprite.png";
    public static final int WIDTH = 4;
    public static final int HEIGHT = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(
            StaffHouse.class);

    private StaffHousePopUp popup;

    public StaffHouse(World world) {
        super(world, WIDTH, HEIGHT, SPRITE_LOCATION);
        setBuildTime(0);
    }

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
        popup.show();
    }


    private void loadPopUp() throws IOException {
        URL location = getClass().getResource(
                "/buildings/staffHouse/popUp.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.load(location.openStream());
        popup = fxmlLoader.getController();
    }
}
