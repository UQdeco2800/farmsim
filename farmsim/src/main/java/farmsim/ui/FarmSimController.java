package farmsim.ui;

import farmsim.ui.*;
import javafx.event.ActionEvent;
import farmsim.*;

import farmsim.buildings.BuildingsPopUp;
import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.entities.agents.AgentRole;

import farmsim.entities.animals.*;
import farmsim.entities.disease.Medicine;
import farmsim.entities.disease.Pesticide;
import farmsim.entities.fire.FireManager;
import farmsim.entities.machines.MachineType;
import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.crops.Crop;
import farmsim.entities.tileentities.objects.BaseObject;
import farmsim.entities.tileentities.objects.Fence;
import farmsim.entities.tools.ToolType;
import common.resource.SimpleResource;
import farmsim.inventory.Storage;
import farmsim.particle.ParticleController;
import farmsim.tasks.*;
import farmsim.tiles.Tile;
import farmsim.tiles.TileProperty;
import farmsim.tiles.TileRegister;
import farmsim.ui.Dialog.DialogMode;
import farmsim.ui.tasks.TaskViewer;
import farmsim.ui.tasks.TaskViewerController;
import farmsim.util.*;
import farmsim.util.console.Console;
import farmsim.world.DayNight;
import farmsim.world.World;
import farmsim.world.WorldManager;
import farmsim.world.generators.BasicWorldGenerator;

import javafx.fxml.JavaFXBuilderFactory;
import org.apache.log4j.Level;
import farmsim.world.weather.WeatherQueue;
import farmsim.world.weather.WeatherType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.*;

import javafx.collections.FXCollections;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class FarmSimController extends Observable implements Initializable {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FarmSimController.class);
    private static Thread musicThread;

    private static Thread soundEffectsThread;// = new Thread(new SoundEffects());

    @FXML
    public ProgressIndicator waterIndicator = new ProgressIndicator();
    @FXML
    public Label waterLabel = new Label();
    @FXML
    public Label dayNum = new Label();
    @FXML
    public Label rocksNo = new Label();

    @FXML
    private CheckBox showWater = new CheckBox();
    @FXML
    private StackPane gamePane;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Pane menuPane;
    @FXML
    private StackPane menuGrass;
    @FXML
    private Button menuButton;
    @FXML
    private ComboBox<String> plantChoice;
    @FXML
    private TextArea output = new TextArea();
    @FXML
    private Pane easyTool = new Pane();
    @FXML
    private Pane dayIndicator = new Pane();
    @FXML
    private Pane nightIndicator = new Pane();
    @FXML
    private Pane coin = new Pane();
    @FXML
    private Label wallet = new Label();
    @FXML
    private Label tps = new Label(); // ticks per second
    @FXML
    private Label season = new Label();
    @FXML
    private Pane seasonIcon = new Pane();
    @FXML
    private Slider slider = new Slider(0.0, 100, 50);
    @FXML
    private Label weather = new Label();
    @FXML
    private Label sliderOutput = new Label();
    @FXML 
    private Pane weatherIcon = new Pane();
    @FXML
    private ListView<String> inventoryAgentSelect = new ListView<String>();
    @FXML
    private HBox forecast = new HBox();
    @FXML
    private ListView<String> rucksackList = new ListView<String>();
    @FXML
    private Button rucksackFarmer, rucksackHunter, rucksackBuilder, rucksackMilker;
    @FXML
    private Button rucksackShearer, rucksackEggHandler, rucksackButcher, rucksackEquip;
    @FXML
    private Button rucksackBack, rucksackAddStorage;
    @FXML
    private Button appleBtn, cornBtn, lettuceBtn, strawberryBtn, pearBtn;
    @FXML
    private Button mangoBtn, cottonBtn, bananaBtn, sugarcaneBtn, lemonBtn;
    @FXML
    private Label totalInfectionLabel;
    @FXML
    private Label peonInfectionLabel;
    @FXML
    private Label cropInfectionLabel;
    @FXML
    private Label numTreatmentsLabel;
    @FXML
    private Button useTreatSmall;
    @FXML
    private Button useTreatMedium;
    @FXML
    private Button useTreatLarge;
    @FXML
    private Button createFence, clearFence, createGate, toggleGate;
    @FXML 
    TabPane gameMenu = new TabPane();

    @FXML
    public void help(ActionEvent event) {
        PopUpWindow dimensionsInput = new PopUpWindow(433, 853, 100, 30, "HELP");
        URL location = getClass().getResource("/helpPopUp.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        try {
            Parent root = fxmlLoader.load();
            dimensionsInput.setContent(root);
            PopUpWindowManager.getInstance().addPopUpWindow(dimensionsInput);
        } catch (IOException e) {
            LOGGER.error("Error loading FXML file helpPopUp", e);
            e.printStackTrace();
        }
    }
    
    // Preferred number of pixels to the left of the divider
    private ExecutorService executor;
    private long worldSeed = 3;
    private int initialWorldSizeX = 128;
    private int initialWorldSizeY = 62;
    private boolean running = false;
    private TileRegister tileRegister;
    private WorldManager worldManager;
    private ParticleController particleController;
    private TaskViewer taskViewer = null;
    private MiniMap minimap;
    private Canvas canvas;
    private Viewport viewport;
    private Canvas statusCanvas;
    private GraphicsContext graphicsContext;
    private AtomicBoolean quit;
    private FontAwesomeFXDemoPopUp fontAwesomePopUp;
    private AnimalProcessingSelectionPopUp animalProcessingSelectionPopUp;
    private AnimalProcessingSelectionController animalProcessingSelectionPopUpController;
    private CreditsPopUp creditsPopUp;

    private BuildingsPopUp buildingsPopUp;
    private static boolean muteSoundEffects = true;
    private static double soundEffectsVolume = 20;
    private static Music music = new Music();


    public static void initMusicThread() {
        musicThread = new Thread(music);
    }

    public static void startMusicThread() {
        musicThread.start();
    }

    public static void stopMusicThread() {
        if (musicThread != null && musicThread.isAlive()) {
            musicThread.interrupt();
        }
    }

    public static void initSoundEffectsThread() {
        soundEffectsThread = new Thread(new SoundEffects());
    }

    /**
     * Method for starting the persistent animal sound effects.
     */
    public static void startSoundEffectsThread() {
        soundEffectsThread.start();
    }


    public static void stopSoundEffectsThread() {
        if (soundEffectsThread != null && soundEffectsThread.isAlive()) {
            soundEffectsThread.interrupt();
        }
    }


    /**
     * Method for getting the current status of the animal sound effects.
     *
     * @return Returns true iff the gae sounds is muted, false otherwise.
     */
    public static boolean soundEffectsStatus() {
        return muteSoundEffects;
    }

    /**
     * Method for getting the current status of the volume slider in the
     * audio tab.
     *
     * @return Double which describes the value of the volume slider on the game
     * form 0.0 to 100.00
     */
    public static double getSoundEffectsVolume() {
        return soundEffectsVolume;
    }


    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tileRegister = TileRegister.getInstance();
        tileRegister.registerTiles();
        worldManager = WorldManager.getInstance();
        PopUpWindowManager.getInstance().setMainPane(mainPane);

        waterIndicator.setVisible(false);
        waterLabel.setVisible(false);

        output.setWrapText(true);
        output.setEditable(false);

        /*
         * load the different plants in from the database handler and put them
         * in the combobox
         */

        org.apache.log4j.Logger.getRootLogger().setLevel(Level.OFF);

        // Listen for Slider value changes
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            sliderOutput.setText("    Sound Effects Volume: " + newValue
                    .intValue() + " ");

            soundEffectsVolume = newValue.doubleValue();
            if (!slider.isValueChanging()) { // respond on slider click
                try {
                    musicThread.interrupt();
                    musicThread = new Thread(music);
                    musicThread.start();
                    soundEffectsThread.interrupt();
                } catch(Exception e){
                    LOGGER.error("Error in music volume");
                }
            }
        });

        slider.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(
                    ObservableValue<? extends Boolean> observableValue,
                    Boolean wasChanging,
                    Boolean changing) {
                if (!changing) { // respond on slider mouse drag
                    try {
                        musicThread.interrupt();
                        musicThread = new Thread(music);
                        musicThread.start();
                        soundEffectsThread.interrupt();
                    } catch(Exception e){
                        LOGGER.error("Error in music volume");
                    }
                }
            }
        });


        inventoryAgentSelect.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            populateRucksack(newValue);
        });
        switchRucksackView(0);
    }

    @FXML
    public void startGame(ActionEvent event) throws Exception {
        if (!running) {
            running = true;
            mainPane.setOnKeyPressed(new KeyPressHandler());
            mainPane.setOnKeyReleased(new KeyPressHandler());

            menuPane.setDisable(true);
            menuPane.setOpacity(0);
            menuGrass.setDisable(true);
            menuGrass.setOpacity(0);

            canvas = new Canvas(gamePane.getWidth(), gamePane.getHeight());

            createWorld();

            World world = WorldManager.getInstance().getWorld();
            viewport = new Viewport(
                    gamePane.getWidth(),
                    gamePane.getHeight(),
                    world
            );

            world.widthProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        viewport.setMaxWidthPixels(gamePane.getWidth());
                        viewport.setWidthTiles(newValue.intValue());
                        canvas.setWidth(viewport.getWidthPixels());
                    });

            world.heightProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        viewport.setMaxHeightPixels(gamePane.getHeight());
                        viewport.setHeightTiles(newValue.intValue());
                        canvas.setHeight(viewport.getHeightPixels());
                    });

            // Particle Pane (pane which particles are rendered)
            Canvas particlePane = new Canvas(gamePane.getWidth(),
                    gamePane.getHeight());
            gamePane.getChildren().add(particlePane);

            StackPane.setAlignment(canvas, Pos.TOP_LEFT);
            gamePane.getChildren().add(canvas);
            gamePane.widthProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        viewport.setMaxWidthPixels(newValue.doubleValue());
                        viewport.setWidthPixels(newValue.doubleValue());
                        canvas.setWidth(viewport.getWidthPixels());
                        particlePane.setWidth(newValue.doubleValue());
                    });
            gamePane.heightProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        viewport.setMaxHeightPixels(newValue.doubleValue());
                        viewport.setHeightPixels(newValue.doubleValue());
                        canvas.setHeight(viewport.getHeightPixels());
                        particlePane.setHeight(newValue.doubleValue());
                    });

            particlePane.toFront();

            this.statusCanvas = new Canvas();
            StackPane.setAlignment(statusCanvas, Pos.TOP_LEFT);
            statusCanvas.widthProperty().bind(gamePane.widthProperty());
            statusCanvas.heightProperty().bind(gamePane.heightProperty());
            graphicsContext = statusCanvas.getGraphicsContext2D();

            minimap = new MiniMap(world, viewport);
            minimap.setOnMousePressed(new MapMouseHandler(viewport, minimap));
            minimap.setOnMouseClicked(new MapMouseHandler(viewport, minimap));
            minimap.setOnMouseReleased(new MapMouseHandler(viewport, minimap));
            minimap.setOnMouseDragged(new MapMouseHandler(viewport, minimap));

            StackPane.setAlignment(minimap, Pos.BOTTOM_RIGHT);
            gamePane.getChildren().add(minimap);

            createAgents();
//            createFarmAnimals();

            gamePane.setOnMousePressed(new MousePressedHandler(viewport));
            gamePane.setOnMouseReleased(new MouseReleasedHandler(viewport));
            gamePane.setOnMouseDragged(new MouseDraggedHandler(viewport));
            gamePane.setOnMouseMoved(new MouseMovedHandler(viewport));
            gamePane.setOnMouseExited(new MouseExitedHandler());

            executor = Executors.newCachedThreadPool();

            quit = new AtomicBoolean(false);
            executor.execute(new GameLoop(quit, viewport, minimap, 50));

            GameRenderer gameRenderer =
                    new GameRenderer(viewport, canvas.getGraphicsContext2D());
            this.addObserver(gameRenderer);
            gameRenderer.start();

            this.particleController = ParticleController.getInstance();
            particleController.setParent(particlePane);
            particleController.setViewport(viewport);
            particleController.start();

            worldManager.getWorld().getMoneyHandler().addObserver(new MoneyObserver());
            worldManager.getWorld().getTimeManager().addObserver(new DayNightObserver());
            dayIndicator.setVisible(true);
            nightIndicator.setVisible(false);
            dayNum.setText("  1/1/2800    ");
            wallet.setText(" " + common.Constants.D +
                    worldManager.getWorld().getMoneyHandler().getAmount());
            season.setText(worldManager.getWorld().getSeason() + "    ");
            slider.setValue(soundEffectsVolume);
            openLevelerWindow();
            setSeedsQuantity();
            muteSoundEffects = false;
            
            // initialise task manager and task viewer popup
            TaskViewerController taskViewerController = new TaskViewerController();
            taskViewer = new TaskViewer();
            taskViewerController.setViewer(taskViewer);
            taskViewer.setController(taskViewerController);
            TaskManager.getInstance().setController(taskViewerController);
            
            // Update the forecast on tab click
            gameMenu.getSelectionModel().selectedItemProperty().addListener(
                    new ChangeListener<Tab>() {
                        @Override
                        public void changed(ObservableValue<? extends Tab>
                                value, Tab oldTab, Tab newTab) {
                            if (newTab.getText().equals("Weather Forecast")) {
                                Platform.runLater(() -> updateForecast());
                            }
                        }
                    }
            );
            running = true;
        }
    }

    @FXML
    public void startGameWithDebug(ActionEvent event) throws Exception {
        Console.getInstance().show();
        org.apache.log4j.Logger.getRootLogger().setLevel(Level.ALL);
        startGame(event);
    }

    public void quit() {
        System.exit(0);
    }


    public void stopGame() {
        if (executor != null || quit != null) {
            quit.set(true);
            executor.shutdown();
        }
    }
    
    @FXML
    public void openMarketplace(ActionEvent event) {
        PopUpWindow login = new PopUpWindow(300, 300, 300, 150, "Login");
        URL location = getClass().getResource("/Login.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        try {
            Parent root = fxmlLoader.load();
            login.setContent(root);
            PopUpWindowManager.getInstance().addPopUpWindow(login);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Error opening marketplace GUI", e);
        }
    }

    @FXML
    public void makeDirt(ActionEvent event) {
        /*
         * Gets the current tile selection, world and integer ID assigned to the
         * dirt tile.
         */
        List<Point> selection = GameManager.getInstance().getSelection();
        World world = worldManager.getWorld();
        for (Point point : selection) {
            TaskManager.getInstance()
                    .addTask(new MakeDirtTask(point, world, output));
        }
    }

    @FXML
    public void raiseLand(ActionEvent event) {
        List<Point> selection = GameManager.getInstance().getSelection();
        World world = worldManager.getWorld();
        for (Point point : selection) {
            TaskManager.getInstance()
                    .addTask(new RaiseLandTask(point, world, output));
        }
    }

    @FXML
    public void lowerLand(ActionEvent event) {
        List<Point> selection = GameManager.getInstance().getSelection();
        World world = worldManager.getWorld();
        for (Point point : selection) {
            TaskManager.getInstance()
                    .addTask(new LowerLandTask(point, world, output));
        }
    }

    @FXML
    public void strike(ActionEvent event) {
        /*
         * Sets the works to strike for a duration
         */
        World world = worldManager.getWorld();
        TaskManager.getInstance().addTask(new StrikeTask(15, 15, world, 500));
    }

    @FXML
    public void showDay() {
        /*
         * Shows the current day in stat bar
         */
        DayNight time = worldManager.getWorld().getTimeManager();
        dayNum.setText("  " +
                (time.getDays() % 28) + "/" +
                (time.getMonth() % 12) + "/" +
                (time.getYears() + 2800) + "    ");
    }

    @FXML
    public void showTps() {
        /*
         * Shows the current ticks per second in the stat bar
         */
        DecimalFormat df = new DecimalFormat("#.##");
        tps.setText("   Ticks/s: "
                + df.format(TicksPerSecond.getInstance()
                .getInstantaneousTPS()));
    }

    @FXML
    public void plough(ActionEvent event) {
        /*
         * Gets the current tile selection, world and integer ID assigned to the
         * grass and dirt tiles.
         */
        List<Point> selection = GameManager.getInstance().getSelection();
        World world = worldManager.getWorld();
        for (Point point : selection) {
            TaskManager.getInstance().addTask(new PloughTask(point, world, output));
            //moved code that was here to isValid function of PloughTask so that
            //invalid plough tasks queue successfully as intended.

        }

    }

    @FXML
    public void harvest(ActionEvent event) {
        /*
         * Gets the current tile selection, world and integer ID assigned to the
         * ploughed_dirt tile.
         */
        List<Point> selection = GameManager.getInstance().getSelection();
        World world = worldManager.getWorld();
        for (Point point : selection) {
            TaskManager.getInstance()
                    .addTask(new HarvestTask(point, world, output));
        }
    }

    /**
     * Check whether all instances of clearable AbstractObjects have been
     * removed from the grounds
     *
     * @return true is there are no instances of clearable AbstractObjects,
     * false otherwise
     */
    @Deprecated
    private boolean isCleared() {
        World world = worldManager.getWorld();
        TileEntity entity;
        for (int y = 0; y < world.getHeight(); y++) {
            for (int x = 0; x < world.getWidth(); x++) {
                entity = world.getTile(x, y).getTileEntity();
                if (entity instanceof BaseObject
                        && ((BaseObject) entity).isClearable()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Create a DimensionsPopUp
     */
    @FXML
    public void setDimensions(ActionEvent event) {
        if (worldManager.getWorld() != null) {
            PopUpWindow dimensionsInput = new PopUpWindow(300, 300, 200, 200,
                    "Set Dimensions");
            URL location = getClass().getResource("/DimensionsPopUp.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            Parent root = null;
            try {
                root = fxmlLoader.load(location.openStream());
            } catch (IOException e) {
                LOGGER.error("Error in loading FXML", e);
                e.printStackTrace();
            }
            // Scene scene = new Scene(root);
            dimensionsInput.setContent(root);
            PopUpWindowManager.getInstance().addPopUpWindow(dimensionsInput);
        }
    }
    
    @FXML
    public void openTechTree(ActionEvent event) throws IOException{
    	//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	if (running) {
            /*URL location;
            FXMLLoader loader = new FXMLLoader();
            Pane pane = null;
            location = getClass().getResource("/techtree/MultiLeafV2.7.fxml");
            loader.setLocation(location);
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            try {
                pane = loader.load();
                TechTreeController controller = new TechTreeController(pane);
                loader.setController(controller);
                controller.initMouseClick();
            } catch (IOException e) {
                e.printStackTrace();
            }
            PopUpWindow popUp = new PopUpWindow(500, 500, 0, 0, "Tech Tree");
            //Pane pane = FXMLLoader.load(getClass().getClassLoader().getResource("techtree/MultiLeafV2.7.fxml"));
            popUp.setContent(pane);
            PopUpWindowManager.getInstance().addPopUpWindow(popUp);*/
            WorldManager.getInstance().getWorld().getTechTree()
                    .createWindow(mainPane);
            WorldManager.getInstance().getWorld().getTechTree()
                    .getController().initMouseClick();
        }
    }

    /**
     * Toggle the visibility of the minimap
     */
    @FXML
    public void toggleMinimap(ActionEvent event) {
        minimap.toggleVisibility();
    }

    /**
     * Toggle the visibility of agents on the minimap
     */
    @FXML
    public void toggleMinimapAgents(ActionEvent event) {
        minimap.toggleAgentVisibility();
    }

    /**
     * Opens the Buildings PopUp Window.
     */
    @FXML
    public void showBuildingsPopUp(ActionEvent event) {
        if (buildingsPopUp == null) {
            buildingsPopUp = new BuildingsPopUp();
        } else if (PopUpWindowManager.getInstance()
                .containsPopUpWindow(buildingsPopUp)) {
            PopUpWindowManager.getInstance().removePopUpWindow(buildingsPopUp);
            return;
        }
        PopUpWindowManager.getInstance().addPopUpWindow(buildingsPopUp);
    }

    /**
     * Method for toggling the perisitant animal sounds.
     *
     * @param event The button click.
     */
    @FXML
    public void toggleGameSounds(ActionEvent event) {
        soundEffectsThread.interrupt();
        muteSoundEffects = !muteSoundEffects;
    }

    /**
     * Prompts a worker to water a selected tile.
     *
     * @param event the event associated with the button press
     */
    @FXML
    public void waterCrop(ActionEvent event) {
        List<Point> selection = GameManager.getInstance().getSelection();
        World world = WorldManager.getInstance().getWorld();
        for (Point point : selection) {
            TaskManager.getInstance()
                    .addTask(new WaterTask(point, world, output));
        }
    }

    /**
     * Sets initial value of seeds for player
     */
    public void setSeedsQuantity() {
    	Hashtable<String, String> hashtable = new Hashtable<String,String>(); 
    	Storage seeds = WorldManager.getInstance().getWorld()
    				.getStorageManager().getSeeds();
    	seeds.addObserver(new SeedObserver());
    	seeds.addItem(new SimpleResource("Apple Seeds", hashtable, 50));
    	seeds.addItem(new SimpleResource("Corn Seeds", hashtable, 50));
    	seeds.addItem(new SimpleResource("Lettuce Seeds", hashtable, 50));
    	seeds.addItem(new SimpleResource("Strawberry Seeds", hashtable, 50));
    	seeds.addItem(new SimpleResource("Pear Seeds", hashtable, 50));
    	seeds.addItem(new SimpleResource("Mango Seeds", hashtable, 50));
    	seeds.addItem(new SimpleResource("Cotton Seeds", hashtable, 50));
    	seeds.addItem(new SimpleResource("Banana Seeds", hashtable, 50));
    	seeds.addItem(new SimpleResource("Sugarcane Seeds", hashtable, 50));
    	seeds.addItem(new SimpleResource("Lemon Seeds", hashtable, 50));
    	
    	appleBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	cornBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	lettuceBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	strawberryBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	pearBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	mangoBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	cottonBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	bananaBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	sugarcaneBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	lemonBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	
    	
    }
    
    private void plantCrop(String seedType, String cropType) {
        Hashtable<String, String> hashtable = new Hashtable<String,String>(); 
        Storage seeds = WorldManager.getInstance().getWorld()
                .getStorageManager().getSeeds();
        List<Point> selection = GameManager.getInstance().getSelection();
        int enough = seeds.getQuantity(new SimpleResource(
                seedType, hashtable, 1));
        if (enough < selection.size()) {
            PopUpWindowManager.getInstance().addPopUpWindow(new Dialog("Not enough seeds", 
                            "You don't have enough " + seedType, DialogMode.ERROR));
        } else {
            World world = worldManager.getWorld();
            for (Point point : selection) {
                PlantTask a = new PlantTask(point, world, cropType, output);
                if (a.isValid()){
                    TaskManager.getInstance().addTask(a);
                }
            }
        }
    }
    
    /**
     * plants the apple from the button.
     * Planting from button.
     */
    @FXML
    public void plantApple() {
        plantCrop("Apple Seeds", "Apple");
    }

    /**
     * plants the apple from the button.
     * Planting from button.
     */
    @FXML
    public void plantCorn() {
        plantCrop("Corn Seeds", "Corn");
        
       /* if(numPlants == 1) {
            output.appendText("Planted 1 corn plant" + 
                    System.getProperty("line.separator"));
        } else {
            output.appendText("Planted " + numPlants + " corn plants" + 
                    System.getProperty("line.separator"));
        }*/
    }

    /**
     * plants the apple from the button.
     * Planting from button.
     */
    @FXML
    public void plantLettuce() {
        plantCrop("Lettuce Seeds", "Lettuce");
    }

    /**
     * plants the apple from the button.
     * Planting from button.
     */
    @FXML
    public void plantStrawberry() {
        plantCrop("Strawberry Seeds", "Strawberry");
    }

    /**
     * plants the apple from the button.
     * Planting from button.
     */
    @FXML
    public void plantPear() {
        plantCrop("Pear Seeds", "Pear");
    }

    /**
     * plants the apple from the button.
     * Planting from button.
     */
    @FXML
    public void plantMango() {
        plantCrop("Mango Seeds", "Mango");
    }

    /**
     * plants the apple from the button.
     * Planting from button.
     */
    @FXML
    public void plantCotton() {
        plantCrop("Cotton Seeds", "Cotton");
    }

    /**
     * plants the apple from the button.
     * Planting from button.
     */
    @FXML
    public void plantBanana() {
        plantCrop("Banana Seeds", "Banana");
    }

    /**
     * plants the apple from the button.
     * Planting from button.
     */
    @FXML
    public void plantSugarcane() {
        plantCrop("Sugarcane Seeds", "Sugarcane");
    }

    /**
     * plants the apple from the button.
     * Planting from button.
     */
    @FXML
    public void plantLemon() {
        plantCrop("Lemon Seeds", "Lemon");
    }

    /**
     * Notifies the game renderer that the player has changed preference
     * concerning the visibility of the water bars.
     */
    @FXML
    public void changedWaterStatus() {
        if (showWater.isSelected() &&
                GameManager.getInstance().getSelection().size() == 1) {
            waterIndicator.setVisible(true);
        } else {
            waterIndicator.setVisible(false);
        }
        setChanged();
        notifyObservers(showWater.isSelected());
    }

    /**
     * Create more agents (peons)
     */
    @FXML
    public void moarPeons(ActionEvent event) {
        Random random = new Random();
        if (running) {
            ArrayList<String> names = new ArrayList<>(Arrays.asList(
                    "Jim", "Jimmy", "Jdog", "James", "Jim II, First of his name"));
            names.forEach(this::createAgent);
        }
    }

    /**
     * Adds FishTask to the trainRole manager.
     */
    @FXML
    public void fish(ActionEvent event) {
        List<Point> selection = GameManager.getInstance().getSelection();
        if (worldManager.getWorld() != null) {
            World world = WorldManager.getInstance().getWorld();
            for (Point point : selection) {
                TaskManager.getInstance().addTask(
                        new FishTask(point, world));
            }
        }
    }


    /**
     * Adds ClearTreeTask to the trainRole manager.
     */
    @FXML
    public void clearTreeOrRocks(ActionEvent event) {
        List<Point> selection = GameManager.getInstance().getSelection();
        if (worldManager.getWorld() != null) {
            World world = WorldManager.getInstance().getWorld();
            for (Point point : selection) {
                TaskManager.getInstance().addTask(
                        new ClearTreeTask(point, world));

            }
        }
    }
    
    /**
     * Adds ClearSnowTask to the trainRole manager.
     */
    @FXML
    public void clearSnow(ActionEvent event) {
        List<Point> selection = GameManager.getInstance().getSelection();
        if (worldManager.getWorld() != null) {
            World world = WorldManager.getInstance().getWorld();
            for (Point point : selection) {
                TaskManager.getInstance().addTask(
                        new ClearSnowTask(point, world));
            }
        }
    }
    
    /**
     * Adds ClearRocksTask to the trainRole manager.
     */
    @FXML
    public void clearRocks(ActionEvent event) {
        List<Point> selection = GameManager.getInstance().getSelection();
        if (worldManager.getWorld() != null) {
            World world = WorldManager.getInstance().getWorld();
            for (Point point : selection) {
                TaskManager.getInstance().addTask(
                        new ClearRocksTask(point, world));
            }
        }
    }

    /**
     * Adds task manager pop up window to game window if it isn't already
     * displayed.
     */
    @FXML

    public void newTaskViewer(ActionEvent event) {
        if (running) {
            if (!PopUpWindowManager.getInstance().containsPopUpWindow(
                    taskViewer)) {
                Platform.runLater(() -> PopUpWindowManager.getInstance()
                        .addPopUpWindow(taskViewer));
            }
        }
    }

    /**
     * BUUUUUUUUUURN
     */
    @FXML
    public void setOnFire(ActionEvent event) {
        List<Point> selection = GameManager.getInstance().getSelection();
        FireManager fireManager = WorldManager.getInstance().getWorld()
                .getFireManager();
        for (Point point : selection) {
            fireManager.createFire((int) point.getX(),
                    (int) point.getY());
        }
    }

    /**
     * Create Fence
     */
    @FXML
    public void createFence(ActionEvent event) {
        List<Point> selection = GameManager.getInstance().getSelection();
        // selection size > 1
        Point topLeft = selection.get(0);
        double minX = topLeft.getX();
        double minY = topLeft.getY();
        double maxX = topLeft.getX();
        double maxY = topLeft.getY();

        // Gets maxX and Y points
        for (Point point : selection) {
            if (maxX < point.getX()) {
                maxX = point.getX();
            }
            if (maxY < point.getY()) {
                maxY = point.getY();
            }
        }

        World world = worldManager.getWorld();

        String location;
        if ((int) minX == (int) maxX) {
            // Single width Column
            for (Point point : selection) {
                if ((int) point.getY() == (int) minY) {
                    // Top
                    location = "T";
                } else if ((int) point.getY() == (int) maxY) {
                    // Bottom
                    location = "P";
                } else {
                    // Centre
                    location = "V";
                }
                Tile tile = world.getTile((int) point.getX(),
                        (int) point.getY());
                tile.setTileEntity(new Fence(tile, location, false));
                tile.setProperty(TileProperty.PASSABLE, false);
            }
        } else if ((int) minY == (int) maxY) {
            // Single width Row
            for (Point point : selection) {
                if ((int) point.getX() == (int) minX) {
                    // Left
                    location = "BL";
                } else if ((int) point.getX() == (int) maxX) {
                    // Right
                    location = "BR";
                } else {
                    // Centre
                    location = "H";
                }

                Tile tile = world.getTile((int) point.getX(),
                        (int) point.getY());
                tile.setTileEntity(new Fence(tile, location, false));
                tile.setProperty(TileProperty.PASSABLE, false);
            }
        } else {
            // Not column or row (ie. 2x2 selection or larger)
            for (Point point : selection) {
                if ((int) point.getX() == (int) minX
                        && (int) point.getY() == (int) minY) {
                    // Left Top
                    location = "TL";
                } else if ((int) point.getX() == (int) maxX
                        && (int) point.getY() == (int) minY) {
                    // Right Top
                    location = "TR";
                } else if ((int) point.getY() == (int) minY) {
                    // Centre Top
                    location = "H";
                } else if ((int) point.getX() == (int) minX
                        && (int) point.getY() == (int) maxY) {
                    // Left Bottom
                    location = "BL";
                } else if ((int) point.getX() == (int) minX) {
                    // Left Centre
                    location = "V";
                } else if ((int) point.getX() == (int) maxX
                        && (int) point.getY() == (int) maxY) {
                    // Right Bottom
                    location = "BR";
                } else if ((int) point.getX() == (int) maxX) {
                    // Right Centre
                    location = "V";
                } else if ((int) point.getY() == (int) maxY) {
                    // Centre Bottom
                    location = "H";
                } else {
                    // Centre, no fencing
                    continue;
                }

                Tile tile = world.getTile((int) point.getX(),
                        (int) point.getY());
                tile.setTileEntity(new Fence(tile, location, false));
                tile.setProperty(TileProperty.PASSABLE, false);
            }

        }
    }

    /**
     * Clear Fence
     */
    @FXML
    public void clearFence(ActionEvent event) {
        List<Point> selection = GameManager.getInstance().getSelection();
        if (worldManager.getWorld() != null) {
            World world = WorldManager.getInstance().getWorld();
            for (Point point : selection) {
                TaskManager.getInstance().addTask(
                        new ClearFenceTask(point, world));
            }
        }
    }

    /**
     * Create Gate
     */
    @FXML
    public void createGate(ActionEvent event) {
        List<Point> selection = GameManager.getInstance().getSelection();
        if (worldManager.getWorld() != null) {
            World world = WorldManager.getInstance().getWorld();
            for (Point point : selection) {
                Tile tile = world.getTile((int) point.getX(),
                        (int) point.getY());
                tile.setTileEntity(new Fence(tile, "", true));
                tile.setProperty(TileProperty.PASSABLE, false);
            }
        }
    }

    /**
     * Toggle Gate
     */
    @FXML
    public void toggleGate(ActionEvent event) {
        List<Point> selection = GameManager.getInstance().getSelection();
        if (worldManager.getWorld() != null) {
            World world = WorldManager.getInstance().getWorld();
            for (Point point : selection) {
                TileEntity entity = world.getTile(point).getTileEntity();
                if (entity instanceof Fence) {
                    ((Fence) entity).toggleGate();
                }
            }
        }
    }

    /**
     * Opens storage UI in a new pop up window
     *
     * @throws IOException
     */
    @FXML
    public void openStorage(ActionEvent event) throws IOException {
        if (running) {
            PopUpWindow popUp = new PopUpWindow(475, 800, 200, 200, "Storage");
            Pane pane = FXMLLoader.load(getClass().getClassLoader()
                    .getResource("Storage.fxml"));
            popUp.setContent(pane);
            PopUpWindowManager.getInstance().addPopUpWindow(popUp);
        }
    }
    
    /**
     * Method to handle the rucksack agent buttons
     * @author hankijord
     */
    @FXML
    public void handleRucksackButtonClick(ActionEvent event){
    	Object source = event.getSource();
    	if (source instanceof Button) {
    	    Button clickedBtn = (Button) source;
    	    if (clickedBtn.getId() != null) {
    	    	String type = new String();
    	    	switch (clickedBtn.getId()){
    	    		case "rucksackFarmer":
    	    			type = "Farmer";
    	    			break;
    	    		case "rucksackHunter":
    	    			type = "Hunter";
    	    			break;
    	    		case "rucksackBuilder":
    	    			type = "Builder";
    	    			break;
    	    		case "rucksackButcher":
    	    			type = "Butcher";
    	    			break;
    	    		case "rucksackShearer":
    	    			type = "Shearer";
    	    			break;
    	    		case "rucksackEggHandler":
    	    			type = "Egg handler";
    	    			break;
    	    		case "rucksackMilker":
    	    			type = "Milker";
    	    			break;
    	    		case "rucksackBack":
    	    			switchRucksackView(0);
    	    			return;
    	    	}
    	    	populateRucksackAgentList(type);
    	    	switchRucksackView(1);
    	    }
    	}
    }
    
    
    /**
     * Used for UI changes, to switch between rucksack views
     * @author hankijord
     * @param direction
     */
    private void switchRucksackView(int direction){
    	// 0 sets initial screen, 1 sets View2
    	Boolean dir;
    	if (direction == 0){ dir = true;} else {dir = false;}
       	
    	Button[] buttons = {rucksackFarmer, rucksackHunter, rucksackBuilder, rucksackButcher, rucksackShearer,
    						rucksackEggHandler, rucksackMilker, rucksackBack, rucksackEquip, rucksackAddStorage};
    	for (int i = 0; i < buttons.length; i++){
    		if (i < 7){
    			buttons[i].setVisible(dir);
    			buttons[i].setManaged(dir);
    		} else {
    			buttons[i].setVisible(!dir);
    			buttons[i].setManaged(!dir);
    		}
    	}
    	inventoryAgentSelect.setVisible(!dir);
	    rucksackList.setVisible(!dir);
	    
	    inventoryAgentSelect.setManaged(!dir);
	    rucksackList.setManaged(!dir);
    }
    
    /**
     * Removes the item from rucksack and places in storage
     * @author hankijord
     */
    @FXML
    public void rucksackAddToStorage(ActionEvent event){
    	String selectedItem = new String(rucksackList.getSelectionModel().getSelectedItem());
    	// Don't allow adding to storage of equipped tools
    	if (selectedItem.toLowerCase().contains("(equipped)")){return;}
    			
    	String tempString = selectedItem.replace(" (Equipped)", "");
    	String parts[] = tempString.split(":");
    	selectedItem = parts[0];
    	
    	Agent selectedAgent = getAgentFromListViewString(inventoryAgentSelect.getSelectionModel().getSelectedItem());
    	SimpleResource tempResource = selectedAgent.getRucksack().getResource(selectedItem);
		selectedAgent.getRucksack().removeFromRucksack(tempResource, tempResource.getQuantity());
		WorldManager.getInstance().getWorld().getStorageManager().addItem(tempResource);
		
    	populateRucksack(inventoryAgentSelect.getSelectionModel().getSelectedItem());
    }
    
    /**
     * Equips the selected item in rucksack
     * @author hankijord
     */
    @FXML
    public void rucksackEquipTool(ActionEvent event){   
    	// If item is already equipped return
    	String selectedItem = new String(rucksackList.getSelectionModel().getSelectedItem());
    	if (selectedItem.toLowerCase().contains("(equipped)")){
    		return;
    	}
    	
    	// Find the Agent object for the selected listview string
    	Agent selectedAgent = getAgentFromListViewString(inventoryAgentSelect.getSelectionModel().getSelectedItem());
    	    	
    	// Check if selectedItem in list view is a tool and get the tool type if it is
    	for (ToolType tool: ToolType.values()){
    		if (selectedItem.equals(tool.displayName())){
    		    if (selectedAgent.canEquip(tool)) {
    		        selectedAgent.equip(tool);
    		    }
    		}
    	}
    	 
    	populateRucksack(inventoryAgentSelect.getSelectionModel().getSelectedItem());
    }
    
    /**
     * Used to display workers of a specified type in the rucksack list
     * Accepts a String input with the type of worker
     * @author hankijord
     * @param type
     */
    
    public void populateRucksackAgentList(String type){
    	List<Agent> agentList = AgentManager.getInstance().getAgents();
    	ObservableList<String> inventoryAgentList = FXCollections.observableArrayList();
    	
    	// Adds the agents who have the selected role to the listview
    	for (Agent agent: agentList){
    		if (type == agent.getCurrentRoleType().displayName()){
    			inventoryAgentList.add(agent.getName() + ": Level " + agent.getLevelForRole(agent.getCurrentRoleType()));
    		}
    	}
    	// Display rucksack items
    	inventoryAgentSelect.setItems(inventoryAgentList);
    }
    
    /**
     * Used to display the rucksack of a selected worker in the 'inventoryAgentSelect' ListView
     * Accepts a String input as selectedWorker, which is the text of the selected row in the ListView
     * @author hankijord
     * @param selectedWorker
     */
    
    public void populateRucksack(String selectedWorker){
    	ObservableList<String> rucksackObservableList = FXCollections.observableArrayList();

       	if (selectedWorker != null){
    		// Find the agent class for the listview selected string
	    	Agent selectedAgent = getAgentFromListViewString(selectedWorker);
	    	if(selectedAgent.getRucksack().countObservers() == 0){
	    		selectedAgent.getRucksack().addObserver(new RucksackObserver());
	    	}

	    	// Find the rucksack items for the selected agent
	    	for (SimpleResource resource: selectedAgent.getRucksack().getList()){
	    		if (selectedAgent.getToolType() != null && 
	    		        selectedAgent.getToolType().displayName().equals(resource.getType())){
	    				rucksackObservableList.add(resource.getType() + " (Equipped)");
	    		} else {
	    			if (resource.getQuantity() > 1) {
	    				rucksackObservableList.add(resource.getType() + ": " + resource.getQuantity());
	    			} else {
	    				rucksackObservableList.add(resource.getType());
	    			}
	    		}
	    	}	
    	}
    	// Display rucksack items
    	rucksackList.setItems(rucksackObservableList);
    }
    
    /**
     * Helper method to return the Agent object from the selected item string in a ListView
     * @author hankijord
     * @param selectedAgent the ListView string of the selected row
     * @return Agent object
     */
    private Agent getAgentFromListViewString(String selectedAgent){
    	List<Agent> agentList = AgentManager.getInstance().getAgents();
    	Agent tempAgent = new Agent ("default", 0, 0, 0);
    	for (Agent agent: agentList){
    		String str = agent.getName() + ": Level " + agent.getLevelForRole(agent.getCurrentRoleType());
    		if (str.equals(selectedAgent)){
    			tempAgent = agent;
    		}
    	}
    	return tempAgent;
    }
    
    /**
     * Sets all peons to be in a tractor
     */
    @FXML
    private void equipTractor(ActionEvent event){
        List<Agent> agentList = AgentManager.getInstance().getAgents();
        for (Agent agent: agentList){
            agent.equipMachine(MachineType.TRACTOR);
        }
    }
    
    /**
     * Un equip all tractors from peons
     */
    @FXML
    private void removeTractor(ActionEvent event){
        List<Agent> agentList = AgentManager.getInstance().getAgents();
        for (Agent agent: agentList){
            agent.removeMachine();
        }
    }


    /**
     * Clears the window displaying the game output.
     *
     * @param event the button press event which calls the method
     */
    public void clearHistory(ActionEvent event) {
        output.clear();
    }

    private void createWorld() {
        // Generate a new world for us
        BasicWorldGenerator worldGen = new BasicWorldGenerator();
        int[] tiles = {tileRegister.getTileType("grass"),
                tileRegister.getTileType("grass2"),
                tileRegister.getTileType("grass3")};
        try {
            worldGen.configureWorld("World1", initialWorldSizeX,
                    initialWorldSizeY, worldSeed);
            worldGen.setBaseTileTypes(tiles);
            worldGen.setElevation();
            worldGen.setMoisture();
            worldGen.addLakes();
            worldGen.updateMoisture();
            worldGen.setTileWaterLevel();
            worldGen.setBiomes();
            worldGen.addBiomeEntities();
            worldGen.createRivers();
            worldGen.placeStaffHouse();
            worldManager.setWorld(worldGen.build());
        } catch (Exception e) {
            LOGGER.error("Create world", e.getMessage());
        }
        worldManager.getWorld().setTileObserver(new TileObserver());
        worldManager.getWorld().setSeasonObserver(new SeasonObserver());
        worldManager.getWorld().setWeatherObserver(new WeatherObserver());
        worldManager.getWorld().setPassibility();
        worldManager.getWorld().getTimeManager().addObserver(
                new ForecastObserver());
    }

    /**
     * Create initial agents
     */
    private void createAgents() {
        ArrayList<String> names = new ArrayList<>(Arrays.asList(
                "Steve", "Rick", "Jim", "Spock", "Bond"));
        names.forEach(this::createAgent);
        loadAgents();
    }

    private void loadAgents() {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(getClass().getResource("/peons.txt").toURI())))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.substring(0,1).equals( "#")) {
                    continue;
                }
                String[] attributes = line.split("\\|");
                Map<String, String> map = new HashMap<>();
                // name|age|gender|role|lore|language|editor|os|religion|animal|tabs|haircolour|eyebrows
                map.put("name", attributes[0]);
                map.put("age", attributes[1]);
                map.put("gender", attributes[2]);
                map.put("role", attributes[3]);
                map.put("lore", attributes[4]);
                map.put("language", attributes[5]);
                map.put("editor", attributes[6]);
                map.put("os", attributes[7]);
                map.put("religion", attributes[8]);
                map.put("animal", attributes[9]);
                map.put("tabs", attributes[10]);
                map.put("haircolour", attributes[11]);
                map.put("eyebrows", attributes[12]);

                if (map.get("animal").toLowerCase().equals("duck")) {
                    createAgent(map);
                } else {
                }
            }
        } catch (Exception e) {

        }
    }

    private void createAgent(String name) {
        World world = WorldManager.getInstance().getWorld();
        int x, y;

        Random random = new Random();
        do {
            x = random.nextInt(world.getWidth());
            y = random.nextInt(world.getHeight());
        } while (!Passable.passable(x, y));
        AgentManager.getInstance().addAgent(new Agent(name, x, y, .1));
    }

    private void createAgent(Map<String, String> map) {
        World world = WorldManager.getInstance().getWorld();
        int x, y;

        Random random = new Random();
        do {
            x = random.nextInt(world.getWidth());
            y = random.nextInt(world.getHeight());
        } while (!Passable.passable(x, y));

        AgentManager.getInstance().addAgent(new Agent(x, y, .1, map));
    }

    /**
     * Adds initial farm animals to the world. Note: the numbers/types of
     * starting farm animals may change in the future
     */
    private void createFarmAnimals() {
        for (int i = 0; i < 4; i++) {
            createFarmAnimal("cow");
            createFarmAnimal("pig");
            createFarmAnimal("sheep");
            createFarmAnimal("duck");
            createFarmAnimal("chicken");
        }
    }

    private void createFarmAnimal(String type) {
        World world = WorldManager.getInstance().getWorld();
        char gender;
        int x, y;

        if (Math.random() > 0.5) {
            gender = 'm';
        } else {
            gender = 'f';
        }

        Random random = new Random();
        do {
            x = random.nextInt(world.getWidth());
            y = random.nextInt(world.getHeight());
        } while (!Passable.passable(x, y));

        switch (type) {
            case "cow":
                Cow cow = new Cow(world, x, y, 0.06, 2000, gender, 0);
                FarmAnimalManager.getInstance().addFarmAnimal(cow);
                break;
            case "pig":
                Pig pig = new Pig(world, x, y, 0.07, 2000, gender, 0);
                FarmAnimalManager.getInstance().addFarmAnimal(pig);
                break;
            case "sheep":
                Sheep sheep = new Sheep(world, x, y, 0.07, 2000, gender, 0);
                FarmAnimalManager.getInstance().addFarmAnimal(sheep);
                break;
            case "duck":
                Duck duck = new Duck(world, x, y, 0.07, 2000, gender, 0);
                FarmAnimalManager.getInstance().addFarmAnimal(duck);
                break;
            case "chicken":
                Chicken chicken = new Chicken(world, x, y, 0.07, 2000, gender, 0);
                FarmAnimalManager.getInstance().addFarmAnimal(chicken);
                break;
            default:
                break;
        }
    }

    /**
     * Opens a leveler window. Pretty much does what it says on the tin
     *
     * @throws Exception when things go wrong ;)
     */
        WorldManager.getInstance().getWorld().getLeveler()
                .createWindow(mainPane);
        WorldManager.getInstance().getWorld().getLeveler()
                .getController().initMouseClick();

    }


    /*
    public ComboBox animalTypeSelection;*/


    public void openWorkerManagement(ActionEvent actionEvent) {
        World world = worldManager.getWorld();
        PopUpWindow agentRoleMangement = new PopUpWindow(520, 300, 200,200,
                "Worker role management");
        URL location = getClass().getResource("/AgentPopUp.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        Parent root = null;
        try {
            root = fxmlLoader.load(location.openStream());
        } catch (IOException e) {
            LOGGER.error("Error in loading FXML", e);
            e.printStackTrace();
        }
        // Scene scene = new Scene(root);
        agentRoleMangement.setContent((Pane) root);
        PopUpWindowManager.getInstance().addPopUpWindow(agentRoleMangement);
    }
    
    /**
     * Updates the display of infection numbers in disease tab
     */
    public void updateInfectionCounts() {
    	peonInfectionLabel.setText(String.valueOf(Agent.getTotalInfections()));
    	cropInfectionLabel.setText(String.valueOf(Crop.getTotalInfections()));
    	totalInfectionLabel.setText(String.valueOf(
    			Agent.getTotalInfections() + Crop.getTotalInfections()));
    	numTreatmentsLabel.setText(String.valueOf(
    			Medicine.getTreatmentPoints() + Pesticide.getTreatmentPoints()));
    }
    
    /**
     * Treat all infected crops and agents with up to 5 points 
     */
    @FXML
    public void treatmentSmallPressed() {
    	treatInfections(5);
    }
    
    /**
     * Treat all infected crops and agents with up to 10 points 
     */
    @FXML
    public void treatmentMediumPressed() {
    	treatInfections(10);
    }
    
    /**
     * Treat all infected crops and agents with up to 20 points 
     */
    @FXML
    public void treatmentLargePressed() {
    	treatInfections(20);
    }
    
    private void treatInfections(int treatmentPoints) {
    	int usablePoints = Medicine.getTreatmentPoints() + Pesticide.getTreatmentPoints();
    	int pointsUsed = (usablePoints - treatmentPoints > 0) ? treatmentPoints : usablePoints;
    	usablePoints -= pointsUsed;
    	int medUse = pointsUsed / 2;
    	int pestUse = pointsUsed - medUse;
    	Medicine.alterTreatmentPoints(-medUse);
    	Pesticide.alterTreatmentPoints(-pestUse);
    	Agent.setTreatmentRound(pointsUsed);
    	Crop.setTreatmentRound(pointsUsed);
    	Agent.setremainingTreatments(Agent.getTotalInfections());
    	Crop.setremainingTreatments(Crop.getTotalInfections());
    }

    /**
     * Opens a window which shows a range of FontAwesomeFx Examples
     * (NOT TO BE INCLUDED IN FINAL VERSION)
     */
    public void showFxExample(ActionEvent actionEvent) {
        if (fontAwesomePopUp == null) {
            fontAwesomePopUp = new FontAwesomeFXDemoPopUp();
        } else if (PopUpWindowManager.getInstance().containsPopUpWindow(fontAwesomePopUp)) {
            PopUpWindowManager.getInstance().removePopUpWindow(fontAwesomePopUp);
            return;
        }
        PopUpWindowManager.getInstance().addPopUpWindow(fontAwesomePopUp);
    }
    
    /**
     * Updates the weather forecast view with current information.
     */
    private void updateForecast() {
        ObservableList<Node> members = forecast.getChildren();
        ObservableList<Node> member;
        List<WeatherQueue.Element> forecastList; 
        int day;
        String name;
        
        forecastList = worldManager.getWorld().getForecast();
        for (int i = 0; i < members.size() - 1; i++) {
            // Update day number
            day = worldManager.getWorld().getTimeManager().getDays() + i + 1;
            member = ((VBox)members.get(i)).getChildren();
            if (((Label)(member.get(0))).getText().equals("Day " + day)) {
                day++;
            }
            ((Label)(member.get(0))).setText("Day " + day);
            
            // Update image
            if (member.get(1).getStyleClass().size() == 2) {
                member.get(1).getStyleClass().remove(1);
            }
            ((Pane) (member.get(1))).getStyleClass().add(
                    worldManager.getWorld().getForecastIconStyle(
                            forecastList.get(i).getWeather()));
            
            // Update weather name
            if (forecastList.get(i).getWeather() == WeatherType.DEFAULT) {
                ((Label)(member.get(2))).setText("Mostly fine");
            } else {
                name = forecastList.get(i).getWeather().toString();
                name = name.charAt(0) + name.substring(1).toLowerCase();
                ((Label)(member.get(2))).setText(name);
            }
        }
    }

    //Call controller first? which wil call this??
    //also put a check in to make sure that no buildings are currently being built
    public void openAnimalProcessingSelection(ActionEvent actionEvent) throws IOException {
        if (animalProcessingSelectionPopUp == null) {
            //animalProcessingSelectionController.checkBuildingsOnMap();
            animalProcessingSelectionPopUp = new AnimalProcessingSelectionPopUp();
        } else if (PopUpWindowManager.getInstance().containsPopUpWindow(animalProcessingSelectionPopUp)) {
            PopUpWindowManager.getInstance().removePopUpWindow(animalProcessingSelectionPopUp);
            return;
        }
        PopUpWindowManager.getInstance().addPopUpWindow(animalProcessingSelectionPopUp);
    }

    /**
     * Opens a window which shows the credits
     */
    public void showCredits(ActionEvent actionEvent) {
        if (creditsPopUp == null) {
            creditsPopUp = new CreditsPopUp();
        } else if (PopUpWindowManager.getInstance().containsPopUpWindow(creditsPopUp)) {
            PopUpWindowManager.getInstance().removePopUpWindow(creditsPopUp);
        }
    }

    /**
     * An observer that will be called when a single tile is selected and the
     * water indicator of the tile must be updated.
     *
     * @author Blake
     */
    public class TileObserver implements Observer {

        /**
         * If one tile is selected the current water level of the tile is
         * retrieved and the water indicator is updated to display it. If
         * multiple tiles are selected the water indicator is hidden.
         */
        @Override
        public void update(Observable tile, Object arg1) {
            float waterLevel = ((Tile) tile).getWaterLevel();
            if (GameManager.getInstance().getSelection().size() == 1 &&
                    showWater.isSelected()) {

                Platform.runLater(() -> {
                    waterIndicator.setProgress(waterLevel);
                    waterIndicator.setVisible(true);
                    waterLabel.setVisible(true);
                });

            } else {
                Platform.runLater(() -> {
                    waterIndicator.setVisible(false);
                    waterLabel.setVisible(false);
                });
            }
        }
    }

    /**
     * An observer that will be called when the users amount of money has
     * changed and the display needs to be updated.
     *
     * @author Jordan
     */
    public class MoneyObserver implements Observer {

        /**
         * updates the money display.
         */
        @Override
        public void update(Observable amount, Object arg1) {
            Platform.runLater(() ->
                wallet.setText(" " + common.Constants.D +
                		worldManager.getWorld().getMoneyHandler().getAmount())
            );
        }
    }

    /**
     * An observer that will be called when day has changed to night and vice
     * versa.
     *
     * @author Jordan
     */
    public class DayNightObserver implements Observer {

        /**
         * updates the display logo from a sun to moon and vice versa.
         */
        @Override
        public void update(Observable day, Object arg1) {
            if (worldManager.getWorld().getTimeManager().isDay()) {
                Platform.runLater(() -> {
                    showDay();
                    dayIndicator.setVisible(true);
                    nightIndicator.setVisible(false);
                });
            } else {
                Platform.runLater(() -> {
                    nightIndicator.setVisible(true);
                    dayIndicator.setVisible(false);
            
                });
            }
        }
    }

    /**
     * An observer called to update on change of season. On season change,
     * updates the season label and icon in the UI.
     * Also updates the season label and icon in the forecast tab.
     */
    public class SeasonObserver implements Observer {
        /**
         * On notification from observable (world.seasonManager), updates UI
         * season label and icon with new season.
         */
        @Override
        public void update(Observable seasonManager, Object seasonName) {
            VBox seasonInfo;
            String name = (String) seasonName;
            String lower = name.charAt(0) + name.substring(1).toLowerCase();
            seasonInfo = (VBox) (forecast.getChildren().get(
                    forecast.getChildren().size() - 1));
            
            Platform.runLater(() -> {
                season.setText(lower);
                if (seasonIcon.getStyleClass().size() == 2) {
                    seasonIcon.getStyleClass().remove(1);
                }
                seasonIcon.getStyleClass().add(
                        worldManager.getWorld().getSeasonIconStyle(
                                worldManager.getWorld().getSeasonObject()
                                        .getName(), false));
                ((Label) seasonInfo.getChildren().get(0)).setText(lower);
                if (seasonInfo.getChildren().get(1).getStyleClass()
                        .size() > 1){
                    seasonInfo.getChildren().get(1).getStyleClass().remove(1);
                }
                ((Pane) seasonInfo.getChildren().get(1)).getStyleClass().add(
                        worldManager.getWorld().getSeasonIconStyle(
                                worldManager.getWorld().getSeasonObject()
                                        .getName(), true));
                Notification.makeNotification("Season",
                        worldManager.getWorld().getSeasonManager()
                                .getIconPath(), "It is now " + lower + ".");
            });
        }
    }
    
    /**
     * An observer called to update on change of weather. On weather change,
     * updates the weather label and icon in the UI.
     */
    public class WeatherObserver implements Observer {
        /**
         * On notification from observable(world.weatherManager), updates UI
         * weather label and icon with new weather.
         */
        @Override
        public void update(Observable weatherManager, Object weatherName) {
            Platform.runLater(() -> {
                if (weatherName == "Default") {
                    weather.setText("");
                    weatherIcon.setVisible(false);
                } else {
                    weather.setText((String) weatherName);
                    if (weatherIcon.getStyleClass().size() == 2) {
                        weatherIcon.getStyleClass().remove(1);
                    }
                    weatherIcon.getStyleClass().add(
                            worldManager.getWorld().getWeatherIconStyle(
                                    worldManager.getWorld().getWeatherType()));
                    weatherIcon.setVisible(true);
                    Notification.makeNotification("Weather", worldManager
                            .getWorld().getWeatherManager().getIconPath(),
                            (String) weatherName);
                }
            });
        }
    }
    
    /** 
     * An observer called on change of day to update the forecast.
     */
    public class ForecastObserver implements Observer {
        
        @Override
        public void update(Observable day, Object arg1) {
            if ((worldManager.getWorld().getTimeManager().getHours() % 24 == 0) 
                    && (worldManager.getWorld().getTimeManager().getHours() 
                            > 23)) {
                worldManager.getWorld().updateForecast();
                Platform.runLater(() -> updateForecast());
                
                int remaining = worldManager.getWorld().getSeasonEndDay() 
                        - worldManager.getWorld().getTimeManager().getDays() 
                        - 1;
                VBox seasonInfo = (VBox) (forecast.getChildren().get(
                        forecast.getChildren().size() - 1));
                Platform.runLater(() -> 
                    ((Label) seasonInfo.getChildren().get(2)).setText(
                            "" + remaining + " days remaining"));
            }
        }
        
    }
    
    /**
     * An observer called to update on change of the rucksack. If contents of
     * the rucksack change, the list in the rucksack tab is updated.
     * @author hankijord
     */
    public class RucksackObserver implements Observer {
        /**
         * On notification from observable (agentManager), updates UI
         * rucksack listview with changed rucksack values.
         */
        @Override
        public void update(Observable agentManager, Object agent) {
            Platform.runLater(() -> {
            	populateRucksack(inventoryAgentSelect.getSelectionModel().getSelectedItem());
            });
        }
    }
    
    public class SeedObserver implements Observer {
        /**
         * On notification from something, updates UI
         * 
         */
        @Override
    public void update(Observable seedStorage, Object storage) {
        	Platform.runLater(() -> {
		    	Storage seeds = WorldManager.getInstance().getWorld()
						.getStorageManager().getSeeds();
		        for (int i = 0; i < seeds.getSize(); i++) {
		        	SimpleResource x = seeds.getList().get(i);
		        
		        	switch(x.getType()) {
		        	case "Apple Seeds":
		        		appleBtn.setText(Integer.toString(x.getQuantity()));
		        		break;
		        	case "Corn Seeds":
		        		cornBtn.setText(Integer.toString(x.getQuantity()));
		        		break;	
		        	case "Lettuce Seeds":
		        		lettuceBtn.setText(Integer.toString(x.getQuantity()));
		        		break;
		        	case "Strawberry Seeds":
		        		strawberryBtn.setText(Integer.toString(x.getQuantity()));
		        		break;
		        	case "Pear Seeds":
		        		pearBtn.setText(Integer.toString(x.getQuantity()));
		        		break;
		        	case "Mango Seeds":
		        		mangoBtn.setText(Integer.toString(x.getQuantity()));
		        		break;
		        	case "Cotton Seeds":
		        		cottonBtn.setText(Integer.toString(x.getQuantity()));
		        		break;
		        	case "Banana Seeds":
		        		bananaBtn.setText(Integer.toString(x.getQuantity()));
		        		break;
		        	case "Sugarcane Seeds":
		        		sugarcaneBtn.setText(Integer.toString(x.getQuantity()));
		        		break;
		        	case "Lemon Seeds":
		        		lemonBtn.setText(Integer.toString(x.getQuantity()));
		        		break;
		        	}
		        }
            }); 
        }
    }
}

