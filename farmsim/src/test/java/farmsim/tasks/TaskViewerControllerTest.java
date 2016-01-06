package farmsim.tasks;

import java.util.LinkedList;
import java.util.List;

import farmsim.tiles.TileProperty;
import farmsim.world.WorldManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import farmsim.entities.agents.Agent;
import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;
import farmsim.ui.tasks.TaskViewer;
import farmsim.ui.tasks.TaskViewerController;
import farmsim.util.Point;
import farmsim.world.World;

public class TaskViewerControllerTest {
    private TaskManager taskManager;
    private TaskManager mockManager;
    private TaskViewer taskViewer;
    private TaskViewerController taskViewerController;
    private World mockWorld;
    private Agent mockAgent;
    private Tile mockTile;
   
    @Before
    public void setup() {
    	mockTile = mock(Tile.class);
        when(mockTile.getTileEntity()).thenReturn(null);
        when(mockTile.getTileType()).thenReturn(1);
        TileRegister mockRegister = mock(TileRegister.class);
        when(mockRegister.getTileType(null)).thenReturn(10);
        TileRegister.setInstance(mockRegister);
        mockAgent = mock(Agent.class);
        Point mockLocation = mock(Point.class);
        when(mockLocation.getY()).thenReturn(5.0);
        when(mockLocation.getX()).thenReturn(5.0);
        when(mockAgent.getLocation()).thenReturn(mockLocation);
    	mockWorld = mock(World.class);
        when(mockWorld.getWidth()).thenReturn(10);
        when(mockWorld.getHeight()).thenReturn(10);
        when(mockWorld.getTile(anyObject())).thenReturn(mockTile);
        when(mockWorld.getTile(anyObject())).thenReturn(mockTile);
        taskViewer = mock(TaskViewer.class);
        
        //mockManager = TaskManager.getInstance();
        //mockManager.setInstance(taskManager);
        taskViewerController = new TaskViewerController();
        taskViewerController.setViewer(taskViewer);
    }
    @Test
    public void recordTaskTest() {
    	 MakeDirtTask dirtTask1 = new MakeDirtTask(1, 5, mockWorld);
    	 taskViewerController.startRecordingTasks();
    	 taskViewerController.addTaskToView(dirtTask1);
    	 taskViewerController.stopRecordingTasks();
    	 String dirt = "Make dirt";
    	 TaskManager.getInstance().refresh();
    	 taskViewerController.runTaskMacro(dirt);
    	 assertEquals("task list should have 1 task", TaskManager.getInstance().listTasks().size(), 1);
    	 assertEquals("macro list should have 1 macro", 
    			 1, taskViewerController.getMacroList().size()); 
    }
}
