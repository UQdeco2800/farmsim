package farmsim.ui;

import farmsim.world.WorldManager;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class TechTreeController implements Initializable {
    TechTreeWindow topPane;
    @FXML
    Pane techPane;
    @FXML
    ImageView topLevelTech; //actually topLevelKek
    @FXML
    ImageView growthOneImage, growthTwoImage, growthThreeImage, closeTech,
            resistOneImage, resistTwoImage, resistThreeImage, outputOneImage,
            outputTwoImage, outputThreeImage;
			resistOneGrayImage, resistTwoGrayImage, resistThreeGrayImage,
			outputOneGrayImage, outputTwoGrayImage, outputThreeGrayImage;
    Label skill1, skill2, skill3, skill4, skill5, skill6, skill7, skill8,
			skill9;
    public TechTreeController(TechTreeWindow pane) {
        topPane = pane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("donE!");
        setSkillPoints(WorldManager.getInstance().getWorld().getTechTree()
				.getSkillPointCount());
        setTotalXP(WorldManager.getInstance().getWorld().getLeveler()
				.getTotalXP());
        setNeedXP(WorldManager.getInstance().getWorld().getLeveler()
				.getNeedXP());
        initMouseClick();
    private class CloseTechClickedHandler implements EventHandler<MouseEvent> {
        public void handle(MouseEvent event){
            topPane.rip();
        }
    }
}
