package farmsim.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Created by TeamFloyd on 20/10/2015.
 */
public class AnimalProcessingPopUp2 extends PopUpWindow {

    private static final int HEIGHT = 200;
    private static final int WIDTH = 200;
    /*
    public AnimalProcessingPopUp2(int xPosition, int yPosition) {

        super(HEIGHT, WIDTH, xPosition, yPosition, "TESTING");
    }*/

    private AnimalProcessingPopUpController2 controller = new AnimalProcessingPopUpController2();

    Label testLabel = new Label();
    public AnimalProcessingPopUp2(int xPos, int yPos){
        super(HEIGHT, WIDTH, xPos, yPos, "Testing");
        this.setContent(getContent());
    }

    public AnimalProcessingPopUp2(){
        super(HEIGHT,WIDTH,0,0,"Testing please work!!!");
        this.setContent(getContent());

    }

    private Node getContent() {

        HBox hBox = new HBox();
        testLabel = new Label("Testing Label");
        hBox.getChildren().addAll(testLabel);

        controller.changeColour(testLabel);

        controller.setHover(this);

        return hBox;
    }


}
