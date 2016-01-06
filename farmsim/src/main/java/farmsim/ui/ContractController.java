package farmsim.ui;

import farmsim.contracts.Contract;
import farmsim.contracts.ContractHandler;
import farmsim.contracts.ContractGenerator;
import farmsim.inventory.StorageManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import farmsim.world.*;



/**
 * Controls contract UI
 * Created by chrisj on 16/10/2015.
 */
public class ContractController implements Initializable{

    @FXML
    private Pane contract1;

    @FXML
    private Pane contract2;

    @FXML
    private Pane contract3;

    @FXML
    private Label contractGiver1;

    @FXML
    private Label cropType1;

    @FXML
    private Label amount1;

    @FXML
    private Label repeatCount1;

    @FXML
    private Label interval1;

    @FXML
    private Label reward1;

    @FXML
    private Label fine1;

    @FXML
    private Label description1;

    @FXML
    private Label expiryDate1;

    @FXML
    private Label contractGiver2;

    @FXML
    private Label cropType2;

    @FXML
    private Label amount2;

    @FXML
    private Label repeatCount2;

    @FXML
    private Label interval2;

    @FXML
    private Label reward2;

    @FXML
    private Label fine2;

    @FXML
    private Label description2;

    @FXML
    private Label expiryDate2;

    @FXML
    private Label contractGiver3;

    @FXML
    private Label cropType3;

    @FXML
    private Label amount3;

    @FXML
    private Label repeatCount3;

    @FXML
    private Label interval3;

    @FXML
    private Label reward3;

    @FXML
    private Label fine3;

    @FXML
    private Label description3;

    @FXML
    private Label expiryDate3;

    @FXML
    private Pane currContractInfo;

    @FXML
    private Label currContractGiver;

    @FXML
    private Label currCropType;

    @FXML
    private Label currAmount;

    @FXML
    private Label currRepeatCount;

    @FXML
    private Label currInterval;

    @FXML
    private Label currReward;

    @FXML
    private Label currFine;

    @FXML
    private Label currDescription;

    @FXML
    private Label currCropCount;

    @FXML
    private Label shipmentCropCount;

    @FXML
    private Label timeFromCurrentDate;

    @FXML
    private Label noShipmentsLeft;

    @FXML
    private Button currContract1;

    @FXML
    private Button currContract2;

    @FXML
    private Button currContract3;

    private ContractHandler availableContracts;
    private ContractHandler activeContracts;
    private StorageManager storageManager;
    private ArrayList<Contract> aContracts;

    /**
	 * Initialize the contract table with three random contracts, and make them invisible.
	 *
	 * @return void: nothing returned
	 */
    public void initialize(URL arg0, ResourceBundle arg1) {
		availableContracts = WorldManager.getInstance().getWorld().getAvailableContracts();
        activeContracts = WorldManager.getInstance().getWorld().getActiveContracts();
        storageManager = WorldManager.getInstance().getWorld().getStorageManager();
        contract1.setVisible(false);
        contract2.setVisible(false);
        contract3.setVisible(false);
        setContractLabels();
    }


    /**
	 * Set contractGiver, cropType, repeatCount, interval, reward, penalty, description
	 * and expiryDate for each of three contract, and make them visible.
	 *
	 * @return void: nothing returned.
	 *
	 * @require availableContracts.getContracts().entrySet() is not null, currentContract is not null,
	 * currentContract is not null.
	 *
	 * @ensure three contracts are visible, and are updated with values.
	 */
    private void setContractLabels() {
        int i = 1;
        Contract currentContract;
        if (availableContracts.getContracts().size() > 0) {
            currentContract = availableContracts.getContract(0);
            contract1.setVisible(true);
            contractGiver1.setText(currentContract.getContractGiver());
            cropType1.setText(currentContract.getResourceType().getType());
            amount1.setText(Integer.toString(currentContract.getAmount()));
            repeatCount1.setText(Integer.toString(currentContract.getRepeatCount()));
            interval1.setText(Integer.toString(currentContract.getInterval()));
            reward1.setText(Integer.toString(currentContract.getReward()));
            fine1.setText(Integer.toString(currentContract.getPenalty()));
            description1.setText(currentContract.getDescription());
            expiryDate1.setText(Integer.toString(currentContract.getExpiryDate()));

            currentContract = availableContracts.getContract(1);
            contract2.setVisible(true);
            contractGiver2.setText(currentContract.getContractGiver());
            cropType2.setText(currentContract.getResourceType().getType());
            amount2.setText(Integer.toString(currentContract.getAmount()));
            repeatCount2.setText(Integer.toString(currentContract.getRepeatCount()));
            interval2.setText(Integer.toString(currentContract.getInterval()));
            reward2.setText(Integer.toString(currentContract.getReward()));
            fine2.setText(Integer.toString(currentContract.getPenalty()));
            description2.setText(currentContract.getDescription());
            expiryDate2.setText(Integer.toString(currentContract.getExpiryDate()));

            currentContract = availableContracts.getContract(2);
            contract3.setVisible(true);
            contractGiver3.setText(currentContract.getContractGiver());
            cropType3.setText(currentContract.getResourceType().getType());
            amount3.setText(Integer.toString(currentContract.getAmount()));
            repeatCount3.setText(Integer.toString(currentContract.getRepeatCount()));
            interval3.setText(Integer.toString(currentContract.getInterval()));
            reward3.setText(Integer.toString(currentContract.getReward()));
            fine3.setText(Integer.toString(currentContract.getPenalty()));
            description3.setText(currentContract.getDescription());
            expiryDate3.setText(Integer.toString(currentContract.getExpiryDate()));

        }
    }

    @FXML
    public void contract1() {
        acceptContract(0);
    }

    @FXML
    public void contract2() {
        acceptContract(1);
    }

    @FXML
    public void contract3() {
        acceptContract(2);
    }

    @FXML
    public void decContract1() {
        declineContract(0);
    }

    @FXML
    public void decContract2() {
    	declineContract(1);
    }

    @FXML
    public void decContract3() {
    	declineContract(2);
    }

    /**
	 * When a contract is accepted, make it invisible in new contracts table, and remove it from
	 * available contracts table then add it into current contracts table.
	 *
	 * @param contractNumber: the contract is accepted.
	 *
	 * @return void: nothing returned
	 *
	 * @enquire availableContracts.getContracts().entrySet() is not null and currentContract is not null
	 *
	 * @ensure the chosen contract is not in available contracts table and is in current contracts table.
	 */
    private void acceptContract(int contractNumber) {

        Contract currentContract = availableContracts.getContract(contractNumber);
        if (WorldManager.getInstance().getWorld().getActiveContracts().addContract(currentContract) == false) {
            Dialog dialog = new Dialog("Error", "Only three contracts can be active at once.",
                    Dialog.DialogMode.ERROR);
            PopUpWindowManager.getInstance().addPopUpWindow(dialog);
            return;
        }
        switch (contractNumber) {
            case 0:
                contract1.setVisible(false);
                break;

            case 1:
                contract2.setVisible(false);
                break;

            case 2:
                contract3.setVisible(false);
                break;
        }
    }

    /**
	 * When a contract is declined, make it invisible in new contracts table, and remove it from
	 * available contracts table.
	 *
	 * @param contractNumber: the contract is declined.
	 *
	 * @return void: nothing returned
	 *
	 * @enquire availableContracts.getContracts().entrySet() is not null and currentContract is not null
	 *
	 * @ensure the chosen contract is not in available contracts table
	 */
    private void declineContract(int contractNumber) {
        switch (contractNumber) {
            case 0:
                contract1.setVisible(false);
                break;

            case 1:
                contract2.setVisible(false);
                break;

            case 2:
                contract3.setVisible(false);
                break;
        }
    }

    /**
	 * Initialize the current contract table with three contracts, and make them invisible.
	 *
	 * @return void: nothing returned
	 */
    @FXML
    public void initializeActive() {
        currContract1.setDisable(true);
        currContract2.setDisable(true);
        currContract3.setDisable(true);
        currContractInfo.setVisible(false);
        activeContracts = WorldManager.getInstance().getWorld().getActiveContracts();
        setCurrContractLabels();
    }

    /**
	 * Make all the current contract in activeContracts visible.
	 * Also, update the current contract information in the hashMap.
	 *
	 * @return void: nothing returned
	 *
	 * @require: activeContracts.getContracts().entrySet() is not null,
	 * currentContract is not null.
	 *
	 * @ensure: the current contract is updated.
	 */
    private void setCurrContractLabels() {
        switch (activeContracts.getContracts().size()) {
            case 1:
                currContract1.setDisable(false);
                break;
            case 2:
                currContract1.setDisable(false);
                currContract2.setDisable(false);
                break;
            case 3:
                currContract1.setDisable(false);
                currContract2.setDisable(false);
                currContract3.setDisable(false);
                break;
            case 0:
                return;
        }
        setCurrContract1();
    }

    /**
	 * Set the information of the first current contract,
	 * and make the information visible.
	 *
	 * @return void: nothing returned
	 *
	 * @require: activeContracts.getContracts().entrySet() is not null,
	 * currentContract is not null.
	 *
	 * @ensure: the current contract is updated.
	 */
    @FXML
    public void setCurrContract1() {
        Contract currentContract = activeContracts.getContract(0);
        currContractInfo.setVisible(true);
        int cropCount = storageManager.getCrops().getQuantity(currentContract.getResourceType());
        currContractGiver.setText(currentContract.getContractGiver());
        currCropType.setText(currentContract.getResourceType().getType());
        currAmount.setText(Integer.toString(currentContract.getAmount()));
        currRepeatCount.setText(Integer.toString(currentContract.getRepeatCount()));
        currInterval.setText(Integer.toString(currentContract.getInterval()));
        currReward.setText(Integer.toString(currentContract.getReward()));
        currFine.setText(Integer.toString(currentContract.getPenalty()));
        currDescription.setText(currentContract.getDescription());
        currCropCount.setText(Integer.toString(cropCount));
        shipmentCropCount.setText(Integer.toString(currentContract.getAmount()));
        timeFromCurrentDate.setText(Integer.toString(
                Math.abs(activeContracts.getDeliveryDate(currentContract))));
        noShipmentsLeft.setText(Integer.toString(currentContract.getRepeatCount()));
    }

    /**
	 * Set the information of the second current contract,
	 * and make the information visible.
	 *
	 * @return void: nothing returned
	 *
	 * @require: activeContracts.getContracts().entrySet() is not null,
	 * currentContract is not null.
	 *
	 * @ensure: the current contract is updated.
	 */
    @FXML
    public void setCurrContract2() {
        Contract currentContract = activeContracts.getContract(1);
        currContractInfo.setVisible(true);
        int cropCount = storageManager.getCrops().getQuantity(currentContract.getResourceType());
        currContractGiver.setText(currentContract.getContractGiver());
        currCropType.setText(currentContract.getResourceType().getType());
        currAmount.setText(Integer.toString(currentContract.getAmount()));
        currRepeatCount.setText(Integer.toString(currentContract.getRepeatCount()));
        currInterval.setText(Integer.toString(currentContract.getInterval()));
        currReward.setText(Integer.toString(currentContract.getReward()));
        currFine.setText(Integer.toString(currentContract.getPenalty()));
        currDescription.setText(currentContract.getDescription());
        currCropCount.setText(Integer.toString(cropCount));
        shipmentCropCount.setText(Integer.toString(currentContract.getAmount()));
        timeFromCurrentDate.setText(Integer.toString(
                Math.abs(activeContracts.getDeliveryDate(currentContract))));
        noShipmentsLeft.setText(Integer.toString(currentContract.getRepeatCount()));
    }

    /**
	 * Set the information of the third current contract,
	 * and make the information visible.
	 *
	 * @return void: nothing returned
	 *
	 * @require: activeContracts.getContracts().entrySet() is not null,
	 * currentContract is not null.
	 *
	 * @ensure: the current contract is updated.
	 */
    @FXML
    public void setCurrContract3() {
        Contract currentContract = activeContracts.getContract(2);
        currContractInfo.setVisible(true);
        int cropCount = storageManager.getCrops().getQuantity(currentContract.getResourceType());
        currContractGiver.setText(currentContract.getContractGiver());
        currCropType.setText(currentContract.getResourceType().getType());
        currAmount.setText(Integer.toString(currentContract.getAmount()));
        currRepeatCount.setText(Integer.toString(currentContract.getRepeatCount()));
        currInterval.setText(Integer.toString(currentContract.getInterval()));
        currReward.setText(Integer.toString(currentContract.getReward()));
        currFine.setText(Integer.toString(currentContract.getPenalty()));
        currDescription.setText(currentContract.getDescription());
        currCropCount.setText(Integer.toString(cropCount));
        shipmentCropCount.setText(Integer.toString(currentContract.getAmount()));
        timeFromCurrentDate.setText(Integer.toString(
                Math.abs(activeContracts.getDeliveryDate(currentContract))));
        noShipmentsLeft.setText(Integer.toString(currentContract.getRepeatCount()));
    }
}
