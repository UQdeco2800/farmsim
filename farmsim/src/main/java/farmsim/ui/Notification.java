package farmsim.ui;

import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * 
 * Simple notification class
 * @author BlueDragon23
 *
 */
public class Notification {
    
    private static int height = 100;
    private static int width = 200;
    private static int time = 3;
    private static int numToWrap = 3;
    private static int numNotifications = 0;
    
    private static int xPosition = 300;
    private static int yPosition = 20;

    /**
     * Create a new notification
     * @param title The title for the window
     * @param imagePath Path to the icon to be displayed
     * @param message The message to display 
     */
    public static void makeNotification(String title, String imagePath, String message) {
        PopUpWindow window = new PopUpWindow(height, width, xPosition, yPosition, title);
        placement(true);
        BorderPane pane = new BorderPane();
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(12.0);
        
        Label label = new Label(message);
        pane.setCenter(box);
        ImageView icon = new ImageView(imagePath);
        icon.setPreserveRatio(true);
        icon.setFitHeight(32.0);
        box.getChildren().addAll(icon, label);
        window.setContent(pane);
        PopUpWindowManager.getInstance().addPopUpWindow(window);
        PauseTransition delay = new PauseTransition(Duration.seconds(time));
        delay.setOnFinished( event -> {PopUpWindowManager.getInstance().removePopUpWindow(window); placement(false);});
        delay.play();
    }
    
    /**
     * Create a notification with a default icon
     * @param title The title for the window
     * @param message The message to display
     */
    public static void makeNotification(String title, String message) {
        makeNotification(title, "icons/icon.png", message);
    }
    
    /**
     * Adjust the placement for the next notification
     * @param mode True if a notification has been added, false if it has been removed
     */
    private static void placement(boolean mode) {
        if (mode) {
            /* Increment */
            numNotifications++;
            if (numNotifications % numToWrap == 0) {
                yPosition = 20;
                xPosition += width;
            } else {
                yPosition += height;
            }
        } else {
            /* Decrement */
            numNotifications--;
            if (numNotifications % numToWrap == numToWrap - 1) {
                yPosition = height * (numToWrap - 1) + 20; // Might need to be -1
                xPosition -= width;
            } else {
                yPosition -= height;
            }
        }
    }

}
