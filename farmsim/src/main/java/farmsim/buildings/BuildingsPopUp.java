package farmsim.buildings;

import farmsim.buildings.process.AbattoirBuilding;
import farmsim.buildings.process.AnimalCoop;
import farmsim.buildings.process.BarnBuilding;
import farmsim.buildings.process.ShearingShed;
import farmsim.ui.PopUpWindow;
import farmsim.world.WorldManager;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashSet;

/**
 * PopUp window that lists and places buildings.
 */
public class BuildingsPopUp extends PopUpWindow {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 500;

    VBox buildingsBox;

    /**
     * Create the buildings popup window.
     */
    public BuildingsPopUp() {
        super(HEIGHT, WIDTH, 0, 0, "Buildings");
        this.setLayoutY(20);
        setupView();

        registerBuildings();
    }

    /**
     * Register different building types and their cost/restrictions.
     * Note: The building must have a constructor that only takes the world to
     * place the building in.
     */
    public void registerBuildings() {
        // Scarecrow
        BuildingPopUpPane scarecrow = new BuildingPopUpPane(
                "Scarecrow", Scarecrow.class);
        scarecrow.setDescription("Keep the predators away.");
        scarecrow.setSprite(Scarecrow.SPRITE_LOCATION);
        scarecrow.setMaxCount(10);
        scarecrow.setCost(50);
        registerBuilding(scarecrow);
        
        // Jetty
        BuildingPopUpPane jetty = new BuildingPopUpPane(
                "Jetty", Jetty.class);
        jetty.setDescription("Let farmers go fishing.");
        jetty.setSprite(Jetty.SPRITE_LOCATION);
        jetty.setMaxCount(3);
        jetty.setCost(100);
        registerBuilding(jetty);
        
        // Jetty Vertical
        BuildingPopUpPane jettyVertical = new BuildingPopUpPane(
                "Jetty (North-South)", JettyVertical.class);
        jettyVertical.setDescription("Let farmers go fishing.");
        jettyVertical.setSprite(JettyVertical.SPRITE_LOCATION);
        jettyVertical.setMaxCount(3);
        jettyVertical.setCost(100);
        registerBuilding(jettyVertical);
        
        //Staff House
        BuildingPopUpPane staffHouse = new BuildingPopUpPane(
                "Staff House", StaffHouse.class);
        staffHouse.setDescription("Manage and hire agents.");
        staffHouse.setSprite(StaffHouse.SPRITE_LOCATION);
        staffHouse.setMaxCount(1);
        registerBuilding(staffHouse);

        //Storage
        BuildingPopUpPane storage = new BuildingPopUpPane(
                "Storage", StorageBuilding.class);
        storage.setSprite(StorageBuilding.SPRITE_LOCATION);
        storage.setMaxCount(1);
        storage.setCost(0);
        registerBuilding(storage);

        //Office
        BuildingPopUpPane office = new BuildingPopUpPane(
                "Office", Office.class);
        office.setDescription("Manage Contracts and accept new ones.");
        office.setSprite(Office.SPRITE_LOCATION);
        office.setMaxCount(2);
        office.setCost(0);
        registerBuilding(office);

        // Abattoir Building
        BuildingPopUpPane abattoirBuilding = new BuildingPopUpPane(
                "Abattoir Building", AbattoirBuilding.class);
        abattoirBuilding.setSprite(AbattoirBuilding.SPRITE_LOCATION);
        abattoirBuilding.setDescription("The Abattoir can be used to process various animals into their respective meats.");
        abattoirBuilding.setMaxCount(1);
        abattoirBuilding.setCost(100);
        registerBuilding(abattoirBuilding);

        // Shearing Shed
        BuildingPopUpPane shearingShed = new BuildingPopUpPane(
                "Shearing Shed", ShearingShed.class);
        shearingShed.setSprite(ShearingShed.SPRITE_LOCATION);
        shearingShed.setDescription("The Shearing Shed can be used to obtain wool as well as feather from chickens and ducks.");
        shearingShed.setMaxCount(1);
        shearingShed.setCost(150);
        registerBuilding(shearingShed);

        // Barn
        BuildingPopUpPane barnBuilding = new BuildingPopUpPane(
                "Barn", BarnBuilding.class);
        barnBuilding.setSprite(BarnBuilding.SPRITE_LOCATION);
        barnBuilding.setDescription("The Barn can be used to obtain milk from a cow.");
        barnBuilding.setMaxCount(1);
        barnBuilding.setCost(200);
        registerBuilding(barnBuilding);

        // Animal Coop
        BuildingPopUpPane animalProcessor = new BuildingPopUpPane(
                "Animal Coop", AnimalCoop.class);
        animalProcessor.setSprite(AnimalCoop.SPRITE_LOCATION);
        animalProcessor.setDescription("The Animal Coop can be used to obtain eggs from chickens and ducks.");
        animalProcessor.setMaxCount(1);
        animalProcessor.setCost(50);
        registerBuilding(animalProcessor);

        
        // Farm House
        BuildingPopUpPane farmHouse = new BuildingPopUpPane(
                "Farm House", FarmHouse.class);
        farmHouse.setSprite(FarmHouse.SPRITE_LOCATION);
        farmHouse.setMaxCount(30);
        farmHouse.setCost(0);
        registerBuilding(farmHouse);

        // Cottage
        BuildingPopUpPane cottage = new BuildingPopUpPane(
                "Cottage", Cottage.class);
        cottage.setSprite(Cottage.SPRITE_LOCATION);
        cottage.setMaxCount(30);
        cottage.setCost(0);
        registerBuilding(cottage);


        // Leggy Shrine
        BuildingPopUpPane leggyShrine = new BuildingPopUpPane(
                "Leggy Shrine", LeggyShrine.class);
        leggyShrine.setSprite(LeggyShrine.SPRITE_LOCATION);
        leggyShrine.setMaxCount(1);
        leggyShrine.setCost(500);
        registerBuilding(leggyShrine);
        
    }

    /**
     * Register a building pane.
     */
    public void registerBuilding(BuildingPopUpPane pane) {
        pane.setupContent();
        buildingsBox.getChildren().add(pane);
    }

    /**
     * Setup the popup's view.
     */
    public void setupView() {
        getStylesheets().add("/css/buildingsPopUp.css");
        BorderPane content = new BorderPane();

        // buildings
        buildingsBox = new VBox(5);
        ScrollPane scrollPane = new ScrollPane(buildingsBox);
        content.setCenter(scrollPane);

        // buttons
        HBox buttonsBox = new HBox(10);
        buttonsBox.setPadding(new Insets(5));
        Button remove = new Button("Bulldoze Building");
        remove.getStyleClass().add("bulldoze-button");
        remove.setOnMouseClicked(e ->
                WorldManager.getInstance().getWorld().startBulldozing());
        buttonsBox.getChildren().add(remove);
        content.setBottom(buttonsBox);

        this.setContent(content);
    }
}
