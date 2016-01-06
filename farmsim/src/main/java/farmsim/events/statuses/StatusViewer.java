package farmsim.events.statuses;

import java.util.ArrayList;

import farmsim.ui.PopUpWindow;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class StatusViewer extends PopUpWindow {
	private static StatusHandler statusHandler = StatusHandler.getInstance();
	private static ArrayList<Status> statusList;

    /**
     * Creates StatusViewer with specified parameters
     *
     * @param height Height of the window
     * @param width Width of the window
     * @param xPosition initial x position of the top left corner
     * @param yPosition initial y position of the top left corner
     * @param title Title of the window
     */
    public StatusViewer() {
        super(150, 150, 0, 0, "Active Events");
        getStylesheets().add("css/StatusList.css");
        update();
    }
    
    public void update(){
    	statusList = statusHandler.allStatus();
    	this.setContent(addStatusItems());
    }
    
    private GridPane addStatusItems(){
		GridPane pane = new GridPane();
		
		if (statusList.size() == 0){
			pane.add(new Text("No Active Events"), 0, 0);
			return pane;
		}
		
		pane.add(new Text("Event"), 0, 0);
		pane.add(new Text("Level"), 1, 0);
		pane.add(new Text("Duration"), 2, 0);

		for (int i = 0; i < statusList.size(); i++){
			pane.add(new Text(statusList.get(i).getName().toString()), 0, i + 1);
			pane.add(new Text(String.valueOf(statusList.get(i).getLvl())), 1, i + 1);
			pane.add(new Text(String.valueOf(statusList.get(i).getDuration())), 2, i + 1);
		}
		return pane;
    }
}
