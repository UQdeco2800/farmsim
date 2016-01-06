package farmsim.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * Internal pop up window that can contain a content pane and be moved by
 * dragging the title bar with the mouse and closed by clicking the exit button.
 *
 */
public class PopUpWindow extends Region {

    // pane containing pop up window contents
    private BorderPane windowPane;

    // to store initial position of mouse when moving window by dragging
    private double dragX;
    private double dragY;

    /**
     * Creates PopUpWindow with specified parameters.
     *
     * @param height Height of the window
     * @param width Width of the window
     * @param xPosition initial x position of the top left corner
     * @param yPosition initial y position of the top left corner
     * @param title Title of the window
     */
    public PopUpWindow(int height, int width, int xPosition, int yPosition,
            String title) {
        getStylesheets().add("css/popUpWindow.css");

        windowPane = new BorderPane();
        windowPane.relocate(xPosition, yPosition);
        windowPane.setPrefSize(width, height);
        windowPane.getStyleClass().add("window");

        addTitleBar(windowPane, title);

        this.setOnMouseClicked(event -> toFront());
        this.setPickOnBounds(false);

        getChildren().add(windowPane);
    }

    /**
     * Helper function for PopUpWindow creation. Adds a title bar with the
     * specified text to the top of the input pane.
     *
     * @param pane Pane to add the title bar to
     * @param title Text to be displayed in the title bar
     */
    private void addTitleBar(BorderPane pane, String title) {
        BorderPane titleBar = new BorderPane();
        titleBar.setPrefHeight(30);
        titleBar.getStyleClass().add("title-bar");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("title");
        titleBar.setCenter(titleLabel);

        addExitButton(titleBar);
        addDragging(titleBar);

        pane.setTop(titleBar);
    }

    /**
     * Adds exit button to right of specified BorderPane.
     *
     * @param pane BorderPane to add exit button to
     */
    private void addExitButton(BorderPane pane) {
        ImageView exitSymbol = new ImageView();
        exitSymbol.getStyleClass().add("exit-symbol");
        exitSymbol.setFitHeight(20);
        exitSymbol.setPreserveRatio(true);

        exitSymbol.setOnMouseClicked(
                event -> ((Pane) PopUpWindow.this.getParent()).getChildren()
                        .remove(PopUpWindow.this));
        exitSymbol.setPickOnBounds(true);

        BorderPane exitButtonPane = new BorderPane();
        exitButtonPane.getStyleClass().add("exit-container");
        exitButtonPane.setCenter(exitSymbol);

        pane.setRight(exitButtonPane);
    }

    /**
     * Allows the specified pane to be used to drag the popUpWindow with the
     * mouse.
     *
     * @param pane Pane that can be dragged to move the popUpWindow
     */
    protected void addDragging(Pane pane) {
        pane.setOnMousePressed(event -> {
                dragX = getLayoutX() - event.getScreenX();
                dragY = getLayoutY() - event.getScreenY();
            });

        pane.setOnMouseDragged(event -> {
                setLayoutX(dragX + event.getScreenX());
                setLayoutY(dragY + event.getScreenY());
            });
    }

    /**
     * Sets content pane to be displayed in pop up window.
     *
     * @param content Pane to be displayed in pop up window
     */
    public void setContent(Node content) {
        windowPane.setCenter(content);
    }
    
    public void setDimensions(int width, int height) {
        windowPane.setPrefSize(width, height);
    }

}
