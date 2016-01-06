package farmsim.ui;

import java.net.URL;
import java.util.ResourceBundle;

import common.Constants;
import common.client.GenericClient;
import common.exception.UnauthenticatedUserException;
import farmsim.entities.tileentities.TileEntity;
import farmsim.money.Money;
import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class DimensionsPopUpController implements Initializable {

    @FXML
    private BorderPane mainPane;

    @FXML
    private GridPane grid;

    @FXML
    private Label currentPrice;

    @FXML
    private StackPane mapPane;

    @FXML
    private Canvas canvas;

    @FXML
    private Button okButton;

    @FXML
    private Button closeButton;

    private static int MAP_SCALE = 2;

    private GraphicsContext graphicsContext;

    private Point maxSelected;

    private World world = WorldManager.getInstance().getWorld();

    /**
     * Attempts to purchase more land. Fails if the world doesn't exist, or the
     * user has insufficient funds to purchase the land
     * 
     * @param event
     */
    @FXML
    public void acceptDimensions(ActionEvent event) {
        if (world != null && maxSelected != null) {
            double price = calculatePrice();
            if (price == 0) {
                // You can't sell land
                exitDimensions();
                return;
            }
            Money money = WorldManager.getInstance().getWorld()
                            .getMoneyHandler();
            // Calculate the area being added and use that as the price
            boolean canAfford = money.subtractAmount((int) price);
            if (canAfford) {
                world.setDimensions(
                                (int) Math.max(maxSelected.getX(),
                                                world.getWidth()),
                                (int) Math.max(maxSelected.getY(),
                                                world.getHeight()));
                GenericClient client = WorldManager.getInstance().getWorld()
                                .getFarmClient();
                try {
                    client.setWallet((long) (client.getWallet() - price));
                } catch (UnauthenticatedUserException e) {
                    /* Just ignore it, wallet will update on next login */
                }
            } else {
                // Tell the player they need more money
                PopUpWindow popUp = new Dialog("Insufficient funds",
                                "You do not have enough " + Constants.DECOCOIN
                                                + "s to purchase that land",
                                Dialog.DialogMode.ERROR);
                PopUpWindowManager.getInstance().addPopUpWindow(popUp);
                return;
            }
        }
        exitDimensions();
    }

    /**
     * Exits the popup window without purchase
     * 
     * @param event
     */
    @FXML
    public void rejectDimensions(ActionEvent event) {
        exitDimensions();
    }

    /**
     * Closes the window, assuming this is the topmost window right now
     */
    private void exitDimensions() {
        PopUpWindowManager.getInstance().removeTopPopUpWindow();
    }

    @Override
    /**
     * Scales the canvas to the world size and draws the map initially
     */
    public void initialize(URL arg0, ResourceBundle arg1) {
        SelectionMouseListener listener = new SelectionMouseListener();
        canvas.setOnMousePressed(listener);
        canvas.setOnMouseDragged(listener);
        canvas.setOnMouseReleased(listener);
        canvas.setOnMouseClicked(listener);
        canvas.setWidth(MAP_SCALE * (world.getWidth() + 10));
        canvas.setHeight(MAP_SCALE * (world.getHeight() + 10));
        graphicsContext = canvas.getGraphicsContext2D();
        drawMap();

    }

    /**
     * Draw the tiles and tile entities to the map
     */
    private void drawMap() {
        Tile tile;
        TileEntity tileEntity;
        TileRegister register = TileRegister.getInstance();
        Color pixelColor;
        for (int x = 0; x < world.getWidth() + 10; x++) {
            for (int y = 0; y < world.getHeight() + 10; y++) {
                if (x < world.getWidth() && y < world.getHeight()) {
                    tile = world.getTile(x, y);
                    tileEntity = tile.getTileEntity();

                    if (tileEntity != null) {
                        pixelColor = register.getTileColour(
                                        tileEntity.getTileType());
                    } else {
                        pixelColor = register.getTileColour(register
                                        .getTileName(tile.getTileType()));
                    }
                } else {
                    /* Area outside of the current world, concealed in FOW */
                    pixelColor = Color.DARKGREY;
                }
                graphicsContext.setFill(pixelColor);
                graphicsContext.fillRect(MAP_SCALE * x, MAP_SCALE * y,
                                MAP_SCALE * 1, MAP_SCALE * 1);
            }
        }
    }

    /**
     * Display the users current selection on the map
     * 
     * @param pressedLocation
     *            the users initial press
     * @param currentLocation
     *            the current location of the mouse
     */
    private void drawSelection() {
        graphicsContext.setStroke(Color.YELLOW);
        graphicsContext.strokeLine(0, maxSelected.getY() * MAP_SCALE,
                        maxSelected.getX() * MAP_SCALE,
                        maxSelected.getY() * MAP_SCALE);
        graphicsContext.strokeLine(maxSelected.getX() * MAP_SCALE, 0,
                        maxSelected.getX() * MAP_SCALE,
                        maxSelected.getY() * MAP_SCALE);
    }

    /**
     * Calculate the price of land based on the users current selection
     * 
     * @return the price of land in \u0110ecocoins
     */
    private double calculatePrice() {
        if (maxSelected.getX() - world.getWidth() <= 0
                        && maxSelected.getY() - world.getHeight() <= 0) {
            return 0;
        } else if (maxSelected.getX() - world.getWidth() <= 0) {
            return (maxSelected.getY() - world.getHeight()) * world.getWidth();
        } else if (maxSelected.getY() - world.getHeight() <= 0) {
            return (maxSelected.getX() - world.getWidth()) * world.getHeight();
        } else {
            return maxSelected.getX() * maxSelected.getY()
                            - world.getWidth() * world.getHeight();
        }
    }

    private class SelectionMouseListener implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED) || event
                            .getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
                drawMap();
                maxSelected = new Point(
                                Math.max(event.getX() / MAP_SCALE,
                                                world.getWidth()),
                                Math.max(event.getY() / MAP_SCALE,
                                                world.getHeight()));
                drawSelection();
                currentPrice.setText(calculatePrice() + Constants.D);
            }

        }

    }
}
