package farmsim.ui;

import farmsim.GameManager;
import farmsim.buildings.BuildingPlacer;
import farmsim.world.WorldManager;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * @author Isaac Elliott
 */
public class KeyPressHandler implements EventHandler<KeyEvent> {
    @Override
    public void handle(KeyEvent event) {
        EventType eventType = event.getEventType();
        if (eventType == KeyEvent.KEY_PRESSED) {
            keyPressed(event.getCode());
        } else if (eventType == KeyEvent.KEY_RELEASED) {
            keyReleased(event.getCode());
        }
    }

    private void keyPressed(KeyCode code) {
        GameManager gameManager = GameManager.getInstance();
        switch (code) {
            case A:
                gameManager.setViewportLeft(true);
                break;

            case D:
                gameManager.setViewportRight(true);
                break;

            case W:
                gameManager.setViewportUp(true);
                break;

            case S:
                gameManager.setViewportDown(true);
                break;

            default:
                break;
        }
    }

    private void keyReleased(KeyCode code) {
        GameManager gameManager = GameManager.getInstance();
        switch (code) {
            case A:
                gameManager.setViewportLeft(false);
                break;

            case D:
                gameManager.setViewportRight(false);
                break;

            case W:
                gameManager.setViewportUp(false);
                break;

            case S:
                gameManager.setViewportDown(false);
                break;

            case ESCAPE:
                BuildingPlacer buildingPlacer = WorldManager.getInstance()
                        .getWorld().getBuildingPlacer();
                if (buildingPlacer.isPlacingBuilding()) {
                    buildingPlacer.stopPlacingBuilding();
                    break;
                } else if (buildingPlacer.isBulldozing()) {
                    buildingPlacer.stopBulldozing();
                    break;
                }
                PopUpWindowManager.getInstance().removeTopPopUpWindow();
                break;

            default:
                break;
        }
    }
}
