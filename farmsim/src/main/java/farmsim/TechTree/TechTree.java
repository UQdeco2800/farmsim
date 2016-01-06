package farmsim.TechTree;

import farmsim.ui.TechTreeController;
import farmsim.ui.TechTreeWindow;
import farmsim.util.Leveler;
import farmsim.world.WorldManager;
import javafx.scene.layout.Pane;

import java.util.ArrayList;


/**
 * Created by jack775544 on 22/10/2015.
 */
public class TechTree {
    private TechTreeController windowController;
    private ArrayList<String> cropList;
    private GrowthRateSkill growthRateSkill = new GrowthRateSkill();
    private WeatherSkill weatherSkill = new WeatherSkill();
    private MoneySkill moneySkill = new MoneySkill();
    private Leveler leveler;

    public TechTree(Leveler l){
        //Leveler leveler = WorldManager.getInstance().getWorld().getLeveler();
        leveler = l;
        cropList = new ArrayList<>(leveler.getCrops());
    }

    public TechTreeWindow createWindow(Pane pane){
        TechTreeWindow window = new TechTreeWindow(pane);
        windowController = window.getController();
        return window;
    }

    public TechTreeController getController(){
        return windowController;
    }

    private int evaluatePoints(){
        /*ArrayList crops = new ArrayList<>(leveler.getCrops());
        for (String crop : crops){

        }*/
        return 0;
    }

    public int getSkillPointCount(){
        return points;
    }
}
