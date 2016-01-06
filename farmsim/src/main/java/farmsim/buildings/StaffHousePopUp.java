package farmsim.buildings;

import common.client.FarmClient;
import common.resource.SimpleOrder;
import common.resource.SimpleResource;
import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.ui.PopUpWindow;
import farmsim.ui.PopUpWindowManager;
import farmsim.world.WorldManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static farmsim.ui.Notification.makeNotification;

/**
 * Controller for the Staff House PopUp
 */
public class StaffHousePopUp extends PopUpWindow implements Initializable {
    public static final int WIDTH = 850;
    public static final int HEIGHT = 500;

    private FarmClient farmClient;
    private WagePopUp wagePopUp;
    private SelectionModel selection = new SelectionModel();

    @FXML
    private BorderPane mainPane;

    @FXML
    private BorderPane headerPane;

    @FXML
    private Tab currentTab;

    @FXML
    private Tab hireTab;

    @FXML
    private Tab helpTab;

    @FXML
    private VBox agents;

    @FXML
    private VBox hireAgents;

    @FXML
    private VBox helpBox;

    @FXML
    private Button hireButton;

    @FXML
    private Button fireButton;

    @FXML
    private Button editWage;

    @FXML
    private Button refreshButton;

    public StaffHousePopUp() {
        super(HEIGHT, WIDTH, 0, 0, "Staff House");
        getChildren().clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        farmClient = WorldManager.getInstance().getWorld().getFarmClient();
        getChildren().add(mainPane);
        addDragging(headerPane);
        setupContent();
        hireButton.setDisable(true);

        fireButton.setOnMouseClicked(event -> {
            if (selection.getCurrentSelection() != null) {
                AgentManager am = AgentManager.getInstance();
                if (am.removeAgent(selection.getSelectionName())) {
                    populateCurrentTable();
                    makeNotification("SUCCESS", "firing succesful");
                    // TODO: send fired workers back to marketplace
                } else {
                    makeNotification("ERROR", "firing failed");
                }
            }
        });

        // not implemented
        hireButton.setOnMouseClicked(event -> {

        });

        editWage.setOnMouseClicked(event -> {
            showWagePopUp();
        });

        refreshButton.setOnMouseClicked(event -> {
            selection.remove(selection.getCurrentSelection());
            setupContent();
        });

        currentTab.setOnSelectionChanged(event -> {
            if(currentTab.isSelected()){
                fireButton.setDisable(false);
                hireButton.setDisable(true);
                editWage.setDisable(false);
                refreshButton.setDisable(false);
                selection.remove(selection.getCurrentSelection());
            }
        });

        hireTab.setOnSelectionChanged(event -> {
            if(hireTab.isSelected()){
                fireButton.setDisable(true);
                hireButton.setDisable(false);
                editWage.setDisable(true);
                refreshButton.setDisable(false);
                selection.remove(selection.getCurrentSelection());
            }
        });

        helpTab.setOnSelectionChanged(event -> {
            if(helpTab.isSelected()){
                fireButton.setDisable(true);
                hireButton.setDisable(true);
                editWage.setDisable(true);
                refreshButton.setDisable(true);
                selection.remove(selection.getCurrentSelection());
            }
        });
    }

    public void show() {
        if (!PopUpWindowManager.getInstance().containsPopUpWindow(this)) {
            PopUpWindowManager.getInstance().addPopUpWindow(this);
            mainPane.setMaxSize(WIDTH, HEIGHT);
            setMaxSize(WIDTH, HEIGHT);
            setupContent();
        }
    }

    public void setupContent() {
        populateCurrentTable();
        populateHireTable();
        fillHelpTab();
    }

    public void clickClose(Event event) {
        PopUpWindowManager.getInstance().removePopUpWindow(this);
    }

    /**
     * get staff list, iterate through them and put the info in Vboxes.
     */
    private void populateCurrentTable() {

        agents.getChildren().clear();

        VBox headerRow = new VBox();
        Text headerText = new Text(0, 0, "Name\t\t\t\t\tGender\t:)\tWage\tBLD\tBCR\tEGH\tFRM\tHNT\tMLK\tSHR");
        headerRow.getChildren().add(headerText);
        addRowToCurrent(headerRow);

        AgentManager am = AgentManager.getInstance();

        List<Agent> staffList = am.getAgents();
        Iterator<Agent> iterateList = staffList.iterator();


        while (iterateList.hasNext()) {
            String agentInfo;
            Agent next = iterateList.next();
            List<String> roleNames = next.getRoleTypeNames();
            String skillDetail = "";

            Iterator<String> iterateRole = roleNames.iterator();
            while (iterateRole.hasNext()) {
                Agent.RoleType roleType = next.getRoleTypeFromString(
                        iterateRole.next());
                skillDetail = skillDetail + "\t " + next.getLevelForRole(roleType);
            }

            String agentName = next.getName();
            agentName = agentName.replaceAll(" ", "_");

            while (agentName.length() < 5) {
                agentName = agentName + " ";
            }
            agentName = agentName + " ";


            String agentGender = next.getGender();
            while (agentGender.length() < 7) {
                agentGender = agentGender + " ";
            }

            int wage = next.getWage();

            if (agentName.length() >= 20) {
                agentInfo = agentName + "\t" + agentGender + "\t " + next.getHappiness() + "\t " + wage + "\t" + skillDetail;
            } else if (agentName.length() >= 16) {
                agentInfo = agentName + "\t\t" + agentGender + "\t " + next.getHappiness() + "\t " + wage + "\t" + skillDetail;
            } else if (agentName.length() >= 12) {
                agentInfo = agentName + "\t\t\t" + agentGender + "\t " + next.getHappiness() + "\t " + wage + "\t" + skillDetail;
            } else if (agentName.length() >= 8) {
                agentInfo = agentName + "\t\t\t\t" + agentGender + "\t " + next.getHappiness() + "\t " + wage + "\t" + skillDetail;
            } else {
                agentInfo = agentName + "\t\t\t\t\t" + agentGender + "\t " + next.getHappiness() + "\t "+wage+ "\t" + skillDetail;
            }

            Text text = new Text(0, 0, agentInfo);
            VBox temp = new VBox();
            temp.getChildren().add(text);

            temp.setOnMouseClicked(event -> {
                if (selection.contains(temp)) {
                    selection.remove(temp);
                } else {
                    selection.remove(selection.getCurrentSelection());
                    selection.add(temp);
                }
            });

            addRowToCurrent(temp);
        }
    }


    private void populateHireTable() {
        hireAgents.getChildren().clear();

        VBox headerRow = new VBox();
        Text headerText = new Text(0, 0, "Name\t\t\t\t\tGender\t:)\tCost\t\tBLD\tBCR\tEGH\tFRM\tHNT\tMLK\tSHR");
        headerRow.getChildren().add(headerText);
        addRowToHire(headerRow);

        getMarketPeon();
    }

    private void fillHelpTab() {
        helpBox.getChildren().clear();

        Text text = new Text(0, 0, "Short Guide to the Staff House TM");
        Text text2 = new Text(0, 0, "Click a name and click a button to do stuff to it");
        Text text3 = new Text(0, 0, "BLD = Builder\t BCR = BUTCHER \t EGH = EGG HANDLER");
        Text text4 = new Text(0, 0, "FRM = Farmer\t HNT = HUNTER\t MLK = MILKER\t SHR = SHEARER");

        VBox temp = new VBox();
        temp.getChildren().add(text);
        addRowToHelp(temp);

        VBox temp2 = new VBox();
        temp.getChildren().add(text2);
        addRowToHelp(temp2);

        VBox temp3 = new VBox();
        temp.getChildren().add(text3);
        addRowToHelp(temp3);

        VBox temp4 = new VBox();
        temp.getChildren().add(text4);
        addRowToHelp(temp4);

    }

    public void addRowToCurrent(Node node) {
        agents.getChildren().add(node);
    }

    public void addRowToHire(Node node) {
        hireAgents.getChildren().add(node);
    }

    public void addRowToHelp(Node node) {
        helpBox.getChildren().add(node);
    }

    /**
     * selection for agent list
     */
    private class SelectionModel {
        VBox selection = null;

        public void add(VBox node) {
            node.setStyle("-fx-effect: dropshadow(three-pass-box, pink, 10, 10, 0, 0);");
            selection = node;
        }

        public void remove(VBox node) {
            if (node != null) {
                node.setStyle("-fx-effect: none;");
            }
            selection = null;
        }

        public boolean contains(VBox node) {
            return node.equals(selection);
        }

        public VBox getCurrentSelection() {
            return selection;
        }

        public String getSelectionName() {
            if (selection != null) {
                List<Node> children = selection.getChildren();
                Iterator<Node> iterateChildren = children.iterator();

                while (iterateChildren.hasNext()) {
                    Node node = iterateChildren.next();
                    if (node instanceof Text && node.toString().contains(" ")) {
                        return node.toString().substring(11,
                                node.toString().indexOf(" "));
                    }
                }
            }
            return null;
        }
    }

    private void showWagePopUp(){
        if (wagePopUp == null) {
            try {
                loadWagePopUp();
            } catch (IOException e) {
                Logger LOGGER = LoggerFactory.getLogger(getClass());
                LOGGER.error("Error loading FXML", e);
                return;
            }
        }

        if (selection.getCurrentSelection() != null) {
            wagePopUp.show(selection.getSelectionName());
        }
    }

    private void loadWagePopUp() throws IOException {
        URL location = getClass().getResource(
                "/buildings/staffHouse/wagePopUp.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.load(location.openStream());
        wagePopUp = fxmlLoader.getController();
    }

    public void getMarketPeon(){
        String row;
        if(!farmClient.isAuthenticated()) {
            return;
        }
        //normally it should be getSellOrders("peon")
        //getting test values from the database
        List<SimpleOrder> orderList = farmClient.getSellOrders("test");
        Iterator<SimpleOrder> iterateOrder = orderList.iterator();
        while (iterateOrder.hasNext()) {
            SimpleOrder order = iterateOrder.next();
            SimpleResource resource = order.getResource();
            Map<String, String> attributes = resource.getAttributes();

            long price = order.getPrice();
            int quantity = resource.getQuantity();

            if (quantity < 1 || attributes.isEmpty()) {
                continue;
            }

            String skillDetail = "";
            Agent agent = new Agent(0, 0, 1, attributes);

            List<String> roleNames = agent.getRoleTypeNames();

            Iterator<String> iterateRole = roleNames.iterator();
            while (iterateRole.hasNext()) {
                Agent.RoleType roleType = agent.getRoleTypeFromString(
                        iterateRole.next());
                skillDetail = skillDetail + "\t " + agent.getLevelForRole(roleType);
            }

            String agentName = agent.getName();
            agentName = agentName.replaceAll(" ", "_");

            while (agentName.length() < 5) {
                agentName = agentName + " ";
            }
            agentName = agentName + " ";


            String agentGender = agent.getGender();
            while (agentGender.length() < 7) {
                agentGender = agentGender + " ";
            }

            long wage = price;

            if (agentName.length() >= 20) {
                row = agentName + "\t" + agentGender + "\t " + agent.getHappiness() + "\t " + wage + "\t" + skillDetail;
            } else if (agentName.length() >= 16) {
                row = agentName + "\t\t" + agentGender + "\t " + agent.getHappiness() + "\t " + wage + "\t" + skillDetail;
            } else if (agentName.length() >= 12) {
                row = agentName + "\t\t\t" + agentGender + "\t " + agent.getHappiness() + "\t " + wage + "\t" + skillDetail;
            } else if (agentName.length() >= 8) {
                row = agentName + "\t\t\t\t" + agentGender + "\t " + agent.getHappiness() + "\t " + wage + "\t" + skillDetail;
            } else {
                row = agentName + "\t\t\t\t\t" + agentGender + "\t " + agent.getHappiness() + "\t "+wage+ "\t" + skillDetail;
            }

            Text text = new Text(0, 0, row);
            VBox temp = new VBox();
            temp.getChildren().add(text);

            temp.setOnMouseClicked(event -> {
                if (selection.contains(temp)) {
                    selection.remove(temp);
                } else {
                    selection.remove(selection.getCurrentSelection());
                    selection.add(temp);
                }
            });

            addRowToHire(temp);
        }
    }
}
