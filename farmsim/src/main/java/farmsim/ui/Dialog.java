package farmsim.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;

/**
 * A simple Dialog to display messages or errors. Intended for use with
 * {@link PopUpWindowManager}
 * 
 * @author BlueDragon23
 *
 */
public class Dialog extends PopUpWindow {

    private static int HEIGHT = 150;
    private static int WIDTH = 300;
    private static int XPOS = 200;
    private static int YPOS = 100;

    public enum DialogMode {
        ERROR, MESSAGE, NONE, INPUT
    };
    
    private TextField input;

    /**
     * Create a new dialog that can be used with the {@link PopUpWindowManager}
     * 
     * @param title
     *            the title for the window
     * @param message
     *            the main text
     * @param mode
     *            the type of message
     */
    public Dialog(String title, String message, DialogMode mode) {
        super(HEIGHT, WIDTH, XPOS, YPOS, title);

        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10, 10, 10, 10));

        /* Create header */
        FlowPane header = new FlowPane();
        switch (mode) {
            case ERROR:
                addHeader(header, "/icons/error.png", "Error");
                break;
            case MESSAGE:
                addHeader(header, "/icons/message.png", "Message");
                break;
            case INPUT:
                addHeader(header, "/icons/message.png", "Input");
            default:
                addHeader(header, "/icons/message.png", "Message");
                break;
        }
        if (!mode.equals(DialogMode.INPUT)) {
            /* Create center */
            Label text = new Label(message);
            text.setWrapText(true);
            text.setTextAlignment(TextAlignment.CENTER);
            pane.setCenter(text);
        } else {
            addInput(pane, message);
        }

        /* Create ok button */
        Button ok = new Button("OK");
        ok.setCancelButton(true);
        ok.setDefaultButton(true);
        ok.setAlignment(Pos.BOTTOM_CENTER);
        ok.setMaxWidth(Double.MAX_VALUE);
        ok.setOnMouseClicked(e ->
                PopUpWindowManager.getInstance().removePopUpWindow(this));
        pane.setBottom(ok);
        setContent(pane);
    }

    /**
     * Add a header to the pane, containing an icon and a title
     * 
     * @param headerPane
     *            the pane for the header to go in
     * @param iconPath
     *            the path to the desired icon
     * @param text
     *            the title for the header
     */
    private void addHeader(Pane headerPane, String iconPath, String text) {
        HBox box = new HBox();
        box.setMaxWidth(Double.MAX_VALUE);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(12.0);
        // Add icon
        Image icon = new Image(iconPath);
        ImageView iconView = new ImageView(icon);
        iconView.setPreserveRatio(true);
        iconView.setFitHeight(32.0);
        Label label = new Label("Error");
        box.getChildren().addAll(iconView, label);

        HBox.setHgrow(iconView, Priority.ALWAYS);
        HBox.setHgrow(label, Priority.ALWAYS);
        label.setMaxWidth(Double.MAX_VALUE);
        // Add HBox to header
        headerPane.getChildren().add(box);
    }
    
    private void addInput(BorderPane pane, String message) {
        GridPane grid = new GridPane();
        grid.add(new Label(message), 0, 0);
        input = new TextField();
        grid.add(input, 0, 1);
    }
    
    public String getInput() {
        return input.getText();
    }
}
