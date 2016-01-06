package farmsim.ui;

import farmsim.buildings.AbstractAnimalProcessingBuilding;
import farmsim.buildings.AbstractBuilding;
import farmsim.buildings.StaffHouse;
import farmsim.buildings.process.*;

import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.entities.agents.AgentRole;
import farmsim.entities.animals.FarmAnimal;
import farmsim.entities.animals.FarmAnimalManager;
import farmsim.tasks.AbstractTask;
import farmsim.tasks.BuildTask;
import farmsim.tasks.TaskManager;
import farmsim.tasks.animalProcessing.*;
import farmsim.world.World;
import farmsim.world.WorldManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;

import java.net.URL;
import java.util.*;

/**
 * The Animal Processing Selection GUI Controller/Animal Processing Task Selection and execution.
 *
 * @author homer5677
 */
public class AnimalProcessingSelectionController implements Initializable {
    private final String RESPONSE_GENERAL_BUY_BUILDING = "To process an animal, buy the relevant processing building!";
    private final String DEFAULT_AGENT_COMBO_PROMPT = "Select an Agent";
    private final String RANDOM_PROMPT = "Random Agent";
    private final String CLOSE_PROMPT = "Close Agent";
    @FXML
    public BorderPane mainPane;
    @FXML
    public BorderPane contentBorderPane;
    @FXML
    public Label titleContent;
    @FXML
    public VBox MainContent; //All the stuff is in here
    //Top
    @FXML
    public VBox agentSelectionArea;
    @FXML
    public VBox agentSelectionVBox;//Top Left
    @FXML
    public ComboBox agentEntitySelection;
    @FXML
    public RadioButton randomAgentRadio;
    @FXML
    public RadioButton closestAgentRadio; //to staffhouse
    @FXML
    public VBox agentSelectionInfo; //Top Right
    @FXML
    public Label agentSelectedName;
    @FXML
    public VBox agentSelectionLevels; //Container for next 4 labels
    @FXML
    public Label agentSelectedButcherLvl;
    @FXML
    public Label agentSelectedMilkingLvl;
    @FXML
    public Label agentSelectedEggHandlingLvl;
    @FXML
    public Label agentSelectedShearingLvl;
    @FXML
    public Label agentSelectedOtherInfo; //Other info
    //Middle
    @FXML
    public HBox processSelection; //Container for Buttons
    @FXML
    public Label selectProcessLabel; //ToDo remove?
    @FXML
    public ToggleButton processAbattoir; //Buttons
    @FXML
    public ToggleButton processEggCoop;
    @FXML
    public ToggleButton processShearingShed;
    @FXML
    public ToggleButton processMilkingBarn;
    //Bottom
    @FXML
    public HBox animalSelections;
    @FXML
    public VBox animalSelectionVBox;  //bottom left
    @FXML
    public HBox animalSelectionVBoxTop; //Container for next 2
    @FXML
    public ComboBox animalTypeSelection;
    @FXML
    public RadioButton animalTypeSelectionRandom;
    @FXML
    public HBox animalSelectionVBoxBottom; //Container for next 5
    @FXML
    public Button animalEntitySelectionRandom;
    @FXML
    public ListView animalEntitySelection;
    @FXML
    public RadioButton entityClosestToAgent;
    @FXML
    public RadioButton entityClosestToProcessBuilding;
    @FXML
    public RadioButton entityClosestToStaffHouse;
    @FXML
    public VBox animalSelectionInfo; //bottom right
    @FXML
    public Label animalSelectedName;
    @FXML
    public Label animalSelectedAge;
    @FXML
    public Label animalSelectedDistance;
    @FXML
    public HBox processExecution; //Container for go button
    @FXML
    public Button goButton;
    //Very Bottom
    @FXML
    public Label responseText;
    @FXML
    public ToggleGroup agentSelectionRadioGroup; //For "Random Agents"
    @FXML
    public ToggleGroup animalLocationRadioGroup; //For "AnimalLocation"
    @FXML
    public ToggleGroup processButtonsGroup;



    private WorldManager worldManager;
    private StaffHouse staffHouse;
    private boolean agentIsSelected = false;

    private Agent agentSelected;
    private Agent.RoleType roleSelected;



    //check
    private HashSet<AbstractAnimalProcessingBuilding> processingBuildings = new HashSet<AbstractAnimalProcessingBuilding>();
    private ArrayList<Agent> agentArrayList = new ArrayList<Agent>();
    private ArrayList<FarmAnimal> farmAnimalArrayList = new ArrayList<FarmAnimal>();
    private Agent.RoleType selectedRole;
    private HashMap<Agent.RoleType, AbstractAnimalProcessingBuilding> roleBuildingMap = new HashMap<Agent.RoleType, AbstractAnimalProcessingBuilding>();
    private FarmAnimal selectedAnimal;
    private AbstractAnimalProcessingBuilding selectedBuilding;
    private ArrayList<Agent.RoleType> processingRoles = new ArrayList<Agent.RoleType>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        worldManager = WorldManager.getInstance();
        checkBuildingsOnMap();
        processSelection.setDisable(true);
        animalSelections.setDisable(true);
    }

    /**
     * This method checks all buildings on the map for buildings which are processing buildings
     * (instances of AbstactAnimalProcessingBuilding) and displays the relevant animal processing building buttons.
     * Whether these buttons are enabled or not is determined in another method. A specific agent also has no influence
     * on anything as buildings can be purchased and placed in game non determinate of processing skills.
     */
    public void checkBuildingsOnMap() {
        HashSet<ToggleButton> processingButtons = new HashSet<ToggleButton>(Arrays.asList(processAbattoir, processEggCoop, processMilkingBarn, processShearingShed));
        for (ToggleButton button : processingButtons) {
            button.setVisible(false); //Ensuring all buttons are not visible to begin with/reset to not visible.
        }
        responseText.setText(RESPONSE_GENERAL_BUY_BUILDING);
        processingBuildings.clear();
        Set<AbstractBuilding> buildingsInWorld = worldManager.getWorld().getBuildings();
        for (AbstractBuilding building : buildingsInWorld) {
            if (building.isBuilt()) {
                if (building instanceof AbstractAnimalProcessingBuilding) { //all animal processing buildings
                    processingBuildings.add((AbstractAnimalProcessingBuilding) building);
                }
            }
        }

        ConcreteAnimalProcessingBuildingVisitor v = new ConcreteAnimalProcessingBuildingVisitor();
        for (AbstractAnimalProcessingBuilding processingBuilding : processingBuildings) {
            if (processingBuilding instanceof AbattoirBuilding) {
                changeButtonDisplay(processAbattoir);
                selectedBuilding = processingBuilding;
            } else if (processingBuilding instanceof ShearingShed) {
                changeButtonDisplay(processShearingShed);
                selectedBuilding = processingBuilding;

            } else if (processingBuilding instanceof AnimalCoop) {
                changeButtonDisplay(processEggCoop);
                selectedBuilding = processingBuilding;

            } else if (processingBuilding instanceof BarnBuilding) {
                changeButtonDisplay(processMilkingBarn);
                selectedBuilding = processingBuilding;

            }
            // changeButtonDisplay(processingBuilding.accept(v));
            // processingBuilding.accept(v);

        }




        //Radiobtn listener - //reset all changes on press
        agentSelectionRadioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (agentSelectionRadioGroup.getSelectedToggle() != null) {
                    //agentEntitySelection.hoverProperty().addListener(new PromptChange());
                    agentEntitySelection.getSelectionModel().clearSelection();
                    agentEntitySelection.getItems().clear();
                    agentEntitySelection.selectionModelProperty().addListener(new PromptChange());
                    if (agentSelectionRadioGroup.getSelectedToggle().equals(randomAgentRadio)) {
                        Agent selectedAgent = selectRandomAgent();
                        loadSelectedAgentInfo(selectedAgent);
                        agentEntitySelection.setPromptText(RANDOM_PROMPT);
                    } else {
                        Agent selectedAgent = selectClosestAgent();
                        loadSelectedAgentInfo(selectedAgent);
                        agentEntitySelection.setPromptText(CLOSE_PROMPT);
                    }
                }
            }
        });







        //ToggleProcessListener
/*
        processButtonsGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if (processButtonsGroup.getSelectedToggle() != null){
                    if (!(processButtonsGroup.getSelectedToggle().isSelected())== true){
                        System.out.println(oldValue);
                        System.out.println(newValue);
                    }
                }
            }
        });
*/


    }

    /**
     * Gets a random agent currently on the map.
     * @return An random agent from the map
     */
    private Agent selectRandomAgent() {
        AgentManager agentManager = AgentManager.getInstance();
        List<Agent> agentList = agentManager.getAgents();
        Agent randomAgent = agentList.get(new Random().nextInt(agentList.size()));
        return randomAgent;
    }

    /**
     * Gets the agent on the map that is currently closest to the staff house (when method is called)
     * @return An agent that is closest to the staff house
     */
    private Agent selectClosestAgent() {
        WorldManager worldManager = WorldManager.getInstance();
        Set<AbstractBuilding> buildingsInWorld = worldManager.getWorld().getBuildings();
        boolean containsStaffHouse = false;
        for (AbstractBuilding building : buildingsInWorld) { //visitor could used also
            if (building instanceof StaffHouse) {
                this.staffHouse = (StaffHouse) building;
                containsStaffHouse = true;
            }
        }
        AgentManager agentManager = AgentManager.getInstance();
        List<Agent> agentList = agentManager.getAgents();
        Agent closeAgent = agentList.get(0);
        if (containsStaffHouse) {
            for (Agent agent : agentList) {
                if (agent.getLocation().distance(staffHouse.getLocation()) < closeAgent.getLocation().distance(staffHouse.getLocation())) {
                    closeAgent = agent;
                }
            }
        }
        return closeAgent;
    }

    /**
     * Loads the selected Agents information into the infomation boxes.
     * Also double checks that agent is "selected" visually and sets it to be selected, also removes the "selected"
     * from other agents on the map
     * @param selectedAgent the Agent that is selected to show information
     */
    private void loadSelectedAgentInfo(Agent selectedAgent) {
        AgentManager agentManager = AgentManager.getInstance();
        List<Agent> agentList = agentManager.getAgents();
        for (Agent agent : agentList) {
            if (agent.isSelected()) {
                agent.setSelected(false);
            }
        }
        selectedAgent.setSelected(true);

        agentSelectedName.setText(selectedAgent.getName());
        Integer butcherLevel = new Integer(selectedAgent.getLevelForRole(Agent.RoleType.BUTCHER));
        Integer milkingLevel = new Integer(selectedAgent.getLevelForRole(Agent.RoleType.MILKER));
        Integer eggHandlingLevel = new Integer(selectedAgent.getLevelForRole(Agent.RoleType.EGG_HANDLER));
        Integer shearingLevel = new Integer(selectedAgent.getLevelForRole(Agent.RoleType.SHEARER));
        agentSelectedButcherLvl.setText(butcherLevel.toString());
        agentSelectedMilkingLvl.setText(milkingLevel.toString());
        agentSelectedEggHandlingLvl.setText(eggHandlingLevel.toString());
        agentSelectedShearingLvl.setText(shearingLevel.toString());
        agentSelectedOtherInfo.setText(selectedAgent.toString());

        agentSelected = selectedAgent;
        processSelection.setDisable(false);
    }

    /**
     * Calls the set agent info method for the agent selected in the combo
     * @param actionEvent Combobox selection
     */
    public void loadAgentCombo(ActionEvent actionEvent) {
        AgentManager agentManager = AgentManager.getInstance();
        List<Agent> agentList = agentManager.getAgents(); //A list of all agents in the game
        if (agentEntitySelection.getValue() != null) {
            String currentSelectedAgent = agentEntitySelection.getValue().toString();
            for (Agent agent : agentList) {
                if (currentSelectedAgent.equals(agent.getName())) {
                    Agent selectedAgent = agent;
                    loadSelectedAgentInfo(selectedAgent);
                }
            }
        }
    }


    /**
     * This loads all the current agents on the map into the combobox.
     * It also serves as a 'reset' when other options are selected used and then this needs to be used again.
     * @param event - mouseclick
     */
    public void resetAgentCombo(Event event) {
        agentEntitySelection.getItems().clear();
        //agentSelectionRadioGroup.getSelectedToggle().setSelected(false);

        AgentManager agentManager = AgentManager.getInstance();
        List<Agent> agentList = agentManager.getAgents(); //A list of all agents in the game
        if (agentEntitySelection.getItems().isEmpty()) {
            for (Agent agent : agentList) {
                agentEntitySelection.getItems().add(agent.getName());//Adding the agent's to the combobox
            }
            agentEntitySelection.setPromptText(DEFAULT_AGENT_COMBO_PROMPT);
        }
    }



    /**
     * Changes the buttons for building selection/role selection
     *
     * @param buttonToChange
     */
    private void changeButtonDisplay(ToggleButton buttonToChange) {
        buttonToChange.setVisible(true);
    }







    /**
     * Abattoir Button Pressed
     *
     * @param actionEvent
     */
    @FXML
    public void loadProcessAbattoir(ActionEvent actionEvent) {
        if (processAbattoir.isSelected()){
            agentSelectionArea.setDisable(true);
            roleSelected = Agent.RoleType.BUTCHER;
            loadAvailableAnimalsForAgent(agentSelected,roleSelected);
        } else {
            agentSelectionArea.setDisable(false);
            animalSelections.setDisable(true);

        }
    }


    /**
     * Coop button pressed
     * @param actionEvent
     */
    public void loadEggProcessCoop(ActionEvent actionEvent) {
        if (processEggCoop.isSelected()){
            agentSelectionArea.setDisable(true);
            roleSelected = Agent.RoleType.EGG_HANDLER;
            loadAvailableAnimalsForAgent(agentSelected,roleSelected);
        } else {
            agentSelectionArea.setDisable(false);
            animalSelections.setDisable(true);


        }
    }


    /**
     * Shearing Shed Button Pressed
     *
     * @param actionEvent
     */
    @FXML
    public void loadProcessShearingShead(ActionEvent actionEvent) {
        if (processShearingShed.isSelected()){
            agentSelectionArea.setDisable(true);
            roleSelected = Agent.RoleType.SHEARER;
            loadAvailableAnimalsForAgent(agentSelected, roleSelected);
        } else {
            agentSelectionArea.setDisable(false);
            animalSelections.setDisable(true);

        }
    }

    /**
     * Milking Barn Button Pressed
     *
     * @param actionEvent
     */
    @FXML
    public void loadProcessMilkingBarn(ActionEvent actionEvent) {
        if (processMilkingBarn.isSelected()){
            agentSelectionArea.setDisable(true);
            roleSelected = Agent.RoleType.MILKER;
            loadAvailableAnimalsForAgent(agentSelected,roleSelected);
        } else {
            agentSelectionArea.setDisable(false);
            animalSelections.setDisable(true);
        }
    }




    /**
     * Loads animals to combo for particular building
     * @param role , the skill which this building faciliates
     */
    private void loadAgentForProcessing(Agent.RoleType role) {
        selectedRole = role;
        //All the potential processing roles
        for (Agent.RoleType roleType : Agent.RoleType.values()) {
            if (!((role.equals(Agent.RoleType.BUILDER)) || (role.equals(Agent.RoleType.HUNTER)) || (role.equals(Agent.RoleType.FARMER)))) {
                processingRoles.add(role);
            }
        }

        //role building map
        for (Agent.RoleType roleType : processingRoles) {
            roleBuildingMap.put(role, getBuildingForRole(role));
        }

    }



    private void loadAvailableAnimalsForAgent(Agent agentSelected, Agent.RoleType role){
        animalSelections.setDisable(false);
        animalTypeSelection.getItems().clear(); //resets any values already in the combobox
        FarmAnimalManager farmAnimalManager = FarmAnimalManager.getInstance();
        List<FarmAnimal> farmAnimalList = farmAnimalManager.getFarmAnimals(); //A list of all farm animals in the game
        for (FarmAnimal farmAnimal : farmAnimalList) {
            farmAnimalArrayList.add(farmAnimal);
            if (!(animalTypeSelection.getItems().contains(farmAnimal.getAnimalType().name()))) { //Show the combo only each animal type
                animalTypeSelection.getItems().add(farmAnimal.getAnimalType().name());
            }
        }
        animalTypeSelection.setPromptText("Select an Animal");
    }



    /**
     * Loads the all available animals in the game which are AnimalType selected in the combobox into the listview
     * (Animal Combobox Selection)
     *
     * @param actionEvent Comboxclick
     */
    public void loadSelectedAnimalTypeEntities(ActionEvent actionEvent) {
        if (animalTypeSelection.getItems().isEmpty() == false) {
            if (animalTypeSelection.getSelectionModel().getSelectedItem() != null) { //model since could be updated to account for new animals
                String currentSelectedAnimalType = animalTypeSelection.getSelectionModel().getSelectedItem().toString();
                animalEntitySelection.getItems().clear();
                loadAvailableAnimalsOfTypeHelper(currentSelectedAnimalType);
                processExecution.setVisible(true);
            } else {
                responseText.setText("Select an animal");
            }
        } else {
            closePopUp();
        }
    }
    /**
     * Clean and resets everything
     */
    private void closePopUp() {
        PopUpWindowManager manager = PopUpWindowManager.getInstance();
        manager.removeTopPopUpWindow();
    }

    /**
     * Loads the animal into list box
     *
     * @param currentSelectedAnimalType (string)
     */
    private void loadAvailableAnimalsOfTypeHelper(String currentSelectedAnimalType) {
        for (FarmAnimal farmAnimal : farmAnimalArrayList){
            if (currentSelectedAnimalType.equals(farmAnimal.getAnimalType().name())){
                animalEntitySelection.getItems().add(farmAnimal);
                //animalEntitySelection.getItems().add(farmAnimal.getAnimalType().name() + " (" + farmAnimalArrayList.indexOf(farmAnimal) + ") ");
            }
        }
        //change listener
        animalEntitySelection.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //new value is the current selected value
                //System.out.println(newValue.toString());
                selectedAnimal = (FarmAnimal) newValue;
                FarmAnimal oldAnimal = (FarmAnimal) oldValue;
                animalSelectedName.setText(selectedAnimal.getAnimalType().name());
                animalSelectedAge.setText(String.valueOf(selectedAnimal.getAge()));
                animalSelectedDistance.setText(String.valueOf(selectedAnimal.getLocation().distance(agentSelected.getLocation())));
                selectedAnimal.setSelected(true);
                if (oldAnimal == null) {
                    return;
                }
                oldAnimal.setSelected(false);
            }
        });

    }

    /**
     * Loads the animal and agent for processing (TASK WILL GO HERE)
     *
     * @param actionEvent Execute Button click
     */
    public void executeProcessing(ActionEvent actionEvent) {
        World world = worldManager.getWorld(); //for task

        boolean animalIsSelected = false;
        boolean agentIsSelected = false;
        boolean roleIsCorrect = false;
        boolean buildingRoleAssociationCorrect = false;
        animalSelections.setDisable(true);
        processSelection.setDisable(true);



        /*
         * In order to start task
         *      1. Type of process must be chosen (buttons), this determines the agent role and building
         *      2. The particular agent must be chosen (cbo), this determines what agent for xp gathered later on
         *      3. The particular animal type to process must be chosen, this is determined on what building is chosen and the level of the selected agent
         *      4. Choose the particular animal of that type, this influences yield
         *      5.
         *
         */

        if ((animalEntitySelection.getItems().isEmpty() == false) && (agentEntitySelection.getItems().isEmpty() == false)) { //agent and animal boxes loaded
            if ((animalEntitySelection.getSelectionModel().getSelectedItem() != null) && (agentEntitySelection.getValue() != null)) { //agent and animal selected


                //Final Processing Variables
                Agent agentForProcessingTask = agentSelected;
                Agent.RoleType roleForProcessingTask = selectedRole;
                FarmAnimal farmAnimalForProcessingTask = selectedAnimal;

                //AbstractAnimalProcessingBuilding sel = getBuildingForRole(selectedRole);
                //AbattoirBuilding buildingForProcessingTask2 = selectedBuilding3;
                //AbstractAnimalProcessingBuilding buildingForProcessingTask = getBuildingForRole(selectedRole);
                AbstractAnimalProcessingBuilding buildingForProcessingTask = selectedBuilding;
                System.out.println("Final " + buildingForProcessingTask);

                //working
/*
                System.out.println("::Selections::\n" +
                                "Agent:: \t ID: " + agentForProcessingTask.toString() + "\t Name: " + agentForProcessingTask.getName() + "\n" +
                                "Agent.Role:: \t Role: " + roleForProcessingTask.toString() + "\n" +
                                "Farm Animal:: \t ID: " + farmAnimalForProcessingTask.toString() + "\t Type: " + farmAnimalForProcessingTask.getAnimalType().name() + "\t Age: " + farmAnimalForProcessingTask.getAge() + "\n" +
                                "Building:: \t Building: " + buildingForProcessingTask.toString() + "\t Location: " + buildingForProcessingTask.getLocation() + "\n"
                );*/


                //***DOUBLE CHECKS BEFORE EXECUTION ****/

                //Checks current role is inded a processing role (as map is made from that) and association with building is correct
                if (roleBuildingMap.containsKey(selectedRole)) {
                    roleIsCorrect = true;
                    if (roleBuildingMap.containsValue(getBuildingForRole(selectedRole))) {
                        buildingRoleAssociationCorrect = true;
                    }
                }


                //animal
                for (FarmAnimal animal : farmAnimalArrayList) {
                    if (selectedAnimal.equals(animal)) {
                        animalIsSelected = true;
                    }
                }

                //agent
                for (Agent agent : agentArrayList) {
                    if (agentSelected.equals(agent)) {
                        agentIsSelected = true;
                    }
                }






                //Start Task
                if ((animalIsSelected == true) && (agentIsSelected == true) && (roleIsCorrect == true) && (buildingRoleAssociationCorrect == true)) {

                    //Start Task
                    //agentForProcessingTask.setCurrentRoleType(roleForProcessingTask);
                    //higher priority = higher number
                    // GotoAnimalTask goToAnimal = new GotoAnimalTask(farmAnimalForProcessingTask.getLocation(), world, 5, "Getting Animal", "GettingAnimal", farmAnimalForProcessingTask, buildingForProcessingTask);
                    // MoveAnimalTask moveAnimalTask = new MoveAnimalTask(buildingForProcessingTask.getLocation(), world, 5, "Moving To Building", "Going to building", farmAnimalForProcessingTask);
                    // MilkCowTask milkCowTask = new MilkCowTask(buildingForProcessingTask.getLocation(),5, world, 5, "STring", "string", farmAnimalForProcessingTask,buildingForProcessingTask);
                    TestTask testTask = new TestTask(buildingForProcessingTask,farmAnimalForProcessingTask, agentForProcessingTask,this);



                    agentForProcessingTask.setTask(testTask);
                    agentForProcessingTask.getTask().startTask();


                }

            }
        }
        closePopUp();

    }



    //Again instance of could be used --> change from string
    private AbstractAnimalProcessingBuilding getBuildingForRole(Agent.RoleType role) {

        AbstractAnimalProcessingBuilding buildingForRole = null;
        String buildingName = new String();
        switch (role) {
            case BUTCHER:
                buildingName = "AbattoirBuilding";
                break;
            case SHEARER:
                buildingName = "ShearingShed";
                break;
            case EGG_HANDLER:
                buildingName = "AnimalCoop";
                break;
            case MILKER:
                buildingName = "BarnBuilding";
                break;
        }
        for (AbstractAnimalProcessingBuilding processingBuilding : processingBuildings) {
            if (processingBuilding.getClass().getSimpleName().equals(buildingName)) {
                buildingForRole = processingBuilding;
            }
        }
        return buildingForRole;
    }


    /**
     * also has to disable the other list box and random enty btn
     */
    public void loadRandomEntity(ActionEvent actionEvent) {

    }


    /* this just randomises what is selected in the list box, essentially just forcing a different click*/
    public void setRandomSelectedEntity(ActionEvent actionEvent) {

    }


    public void overideUserClosestAgent(ActionEvent actionEvent) {

    }

    public void overideUserProcessingBuilding(ActionEvent actionEvent) {

    }


    public void overideUserStaffHouse(ActionEvent actionEvent) {
    }



    public void randomAgent(ActionEvent actionEvent) {
    }

    public void closestAgentToStaffHouse(ActionEvent actionEvent) {
    }

    public void resetButton(ActionEvent actionEvent) {
        PopUpWindowManager manager = PopUpWindowManager.getInstance();
        manager.removeTopPopUpWindow();
    }


    public class PromptChange implements ChangeListener<Boolean> {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if (agentSelectionRadioGroup.getSelectedToggle().isSelected()) { //toggle button is currently selected
                String oldPromptText = new String();
                if (agentSelectionRadioGroup.getSelectedToggle().equals(randomAgentRadio)) {
                    oldPromptText = RANDOM_PROMPT;
                } else if (agentSelectionRadioGroup.getSelectedToggle().equals(closestAgentRadio)) {
                    oldPromptText = CLOSE_PROMPT;
                }

                if (agentEntitySelection.isHover()) {
                    agentEntitySelection.setPromptText(DEFAULT_AGENT_COMBO_PROMPT);
                } else if (!agentEntitySelection.isHover()) {
                    agentEntitySelection.setPromptText(oldPromptText);
                }
            }
        }
    }


}




