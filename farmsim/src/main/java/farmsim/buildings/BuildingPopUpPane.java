package farmsim.buildings;

import farmsim.Constants;
import farmsim.money.Money;
import farmsim.ui.Dialog;
import farmsim.ui.PopUpWindowManager;
import farmsim.world.World;
import farmsim.world.WorldManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;


/**
 * A pane for the BuildingsPopUp that gives information on a specific type of
 * building, and allows the user to place the building given some settable
 * restrictions.
 */
public class BuildingPopUpPane extends TitledPane {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            BuildingPopUpPane.class);
    private String name;
    private String category;
    private Class<? extends AbstractBuilding> buildingClass;
    private String sprite;
    private String description;
    private int maxCount;
    private int cost;

    private boolean rotate;


    /**
     * Create a pane for a specific type of building in the buildings popup
     * window.
     * Handles placing the building given set restrictions.
     * By default the building can be placed for free, and an unlimited number
     * can be placed in the world.
     *
     * @param name The name of the type of building.
     * @param buildingClass The java class of the building to create.
     */
    public BuildingPopUpPane(String name,
            Class<? extends AbstractBuilding> buildingClass) {
        super();
        this.name = name;
        this.buildingClass = buildingClass;
        this.sprite = null;
        this.description = null;
        this.maxCount = -1;
        this.cost = 0;
    }

    /**
     * Set the image to use to preview this building.
     */
    public void setSprite(String location) {
        this.sprite = location;
    }

    /**
     * Set the building's description.
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Set a restriction on the number of buildings of this type that can be
     * placed in the world.
     */
    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    /**
     * Set how much this building costs the user to place.
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Setup the pane's content.
     */
    public void setupContent() {
        this.setText(this.name);
        this.setPrefWidth(BuildingsPopUp.WIDTH - 6 - 16); // width - border - scrollbar
        BorderPane content = new BorderPane();
        if (this.description != null) {
            Label lblDescription = new Label(this.description);
            lblDescription.getStyleClass().add("building-description");
            content.setTop(lblDescription);
        }

        if (this.sprite != null) {
            Image imgBuilding = new Image(this.sprite);
            ImageView ivBuilding = new ImageView();
            ivBuilding.setImage(imgBuilding);
            if (imgBuilding.getWidth() > 100) {
                ivBuilding.setFitWidth(100);
            }
            ivBuilding.setPreserveRatio(true);
            ivBuilding.setSmooth(true);
            content.setLeft(ivBuilding);
        }
        
        Button btnPlace = new Button("Place");
        btnPlace.getStyleClass().add("place-button");
        registerPlaceClickEvent(btnPlace);
        content.setRight(btnPlace);
        content.setAlignment(btnPlace, Pos.CENTER);

        content.setCenter(new Label("Cost: " + Constants.D + cost));

        this.setContent(content);
    }

    /**
     * Start placing the building when the given button is pressed.
     * Make sure cost/count restrictions are met.
     */
    private void registerPlaceClickEvent(Button btn) {
        btn.setOnMouseClicked(new PlaceClickHandler());
    }

    /**
     * @return true if the max count property is not violated,
     *         otherwise return false and show an error dialog.
     */
    private boolean checkMaxCount() {
        if (maxCount > 0) {
            int count = 0;
            for (AbstractBuilding building
                    : WorldManager.getInstance().getWorld().getBuildings()) {
                if (buildingClass.isAssignableFrom(building.getClass())) {
                    count++;
                }
            }
            if (count >= maxCount) {
                showErrorDialog("Only " + maxCount
                        + " of those buildings can be placed in the world.");
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if the player has enough money to place the building,
     *         otherwise return false and show an error dialog.
     */
    private boolean checkBalance() {
        Money money = WorldManager.getInstance().getWorld().getMoneyHandler();
        if (money.getAmount() < cost) {
            showErrorDialog("Not enough funds!");
            return false;
        }
        return true;
    }

    /**
     * Show an error dialog popup with the given message.
     */
    private void showErrorDialog(String message) {
        Dialog dialog = new Dialog("Error", message,
                Dialog.DialogMode.ERROR);
        PopUpWindowManager.getInstance().addPopUpWindow(dialog);
    }

    private class PlaceClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            if (!checkMaxCount() || !checkBalance()) {
                return;
            }

            AbstractBuilding building;
            World world = WorldManager.getInstance().getWorld();
            try {
                Constructor<? extends AbstractBuilding> ctor =
                        buildingClass.getConstructor(World.class);
                building = ctor.newInstance(world);
            } catch (NoSuchMethodException | InvocationTargetException
                    | InstantiationException | IllegalAccessException e) {
                showErrorDialog("Failed to create building: " + name);
                LOGGER.error("Failed to create an instance of "
                        + buildingClass.getName(), e);
                return;
            }

            world.getBuildingPlacer().startPlacingBuilding(building,
                    new PlacerHandler());
        }
    }

    private class PlacerHandler implements BuildingPlacerEventHandler {
        @Override
        public void onPlace(AbstractBuilding building) {
            if (!checkBalance()) {
                building.removeFromWorld();
                return;
            }
            WorldManager.getInstance().getWorld().getMoneyHandler()
                    .subtractAmount(cost);
        }

        @Override
        public void onCancel(AbstractBuilding building) {
            // we haven't withdrawn any money, so do nothing.
        }
    }
}
