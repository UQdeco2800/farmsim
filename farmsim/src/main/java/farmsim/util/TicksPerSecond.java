package farmsim.util;

import farmsim.FarmSimLauncher;
import farmsim.ui.FarmSimController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to calculate and return the current ticks per second of the game
 *
 * @author Harry Hoyland (hoyland6)
 *
 */
public class TicksPerSecond {

    private static final TicksPerSecond INSTANCE = new TicksPerSecond();

    public static TicksPerSecond getInstance() {
        return INSTANCE;
    }

    private double currentTPS;
    private double averageTPS;

    private List<Double> pastTPS = new ArrayList<>();

    private long tickStartTime;

    private FarmSimController farmSimController;

    public void tickStart() {
        tickStartTime = System.nanoTime();
    }

    public void tickEnd() {
        long tickEndTime = System.nanoTime();
        long tickTimeDiff = tickEndTime - tickStartTime;

        currentTPS = 1000000000/(double) tickTimeDiff ;

        pastTPS.add(0, currentTPS);
        if (pastTPS.size() > 100) {
            pastTPS.remove(100);
        }
    }

    public double getInstantaneousTPS() {
        return currentTPS;
    }

    public double getAverageTPS() {
        double sumTPS = 0;
        for (double tps: pastTPS) {
            sumTPS += tps;
        }
        averageTPS = sumTPS/pastTPS.size();
        return averageTPS;
    }

    public void updateTPSCounter() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                farmSimController.showTps();
            }
        });
    }


    public void setFarmSimController(FarmSimController farmSimController) {
        this.farmSimController = farmSimController;
    }

}
