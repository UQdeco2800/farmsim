package farmsim.ui;

import farmsim.util.Point;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * Created by homer5677 on 20/10/2015.
 */
public class AnimalProcessingPopUpController2 {

    private AnimalProcessingPopUp2 view;



    int pointX=0;
    int pointY=0;

    public void setHeightWidth(Point point){
        pointX = (int) point.getX();
        pointY = (int) point.getY();
    }

    public int getPointX(){
        return pointX;
    }
    public int getPointY(){
        return pointY;
    }


    public void changeColour(Label label){
        label.setStyle("-fx-background-color: blue");
    }

    public void setHover(AnimalProcessingPopUp2 animalProcessingPopUp2) {
        animalProcessingPopUp2.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                animalProcessingPopUp2.testLabel.setStyle("-fx-background-color: yellow");
            }
        });
    }
}
