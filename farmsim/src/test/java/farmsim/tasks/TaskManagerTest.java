package farmsim.tasks;

import java.util.LinkedList;
import java.util.List;

import farmsim.tiles.TileProperty;
import farmsim.world.WorldManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import farmsim.entities.agents.Agent;
import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;
import farmsim.ui.tasks.TaskViewerController;
import farmsim.util.Point;
import farmsim.world.World;

@PrepareForTest({WorldManager.class})
@RunWith(PowerMockRunner.class)
public class TaskManagerTest {
    private TaskManager taskManager;
    private World mockWorld;
    private Agent mockAgent;
    private Tile mockTile;
   
    @Before
    public void setup() {
        mockWorld = PowerMockito.mock(World.class);
        when(mockWorld.getWidth()).thenReturn(10);
        when(mockWorld.getHeight()).thenReturn(10);
        TileRegister mockRegister = mock(TileRegister.class);
        when(mockRegister.getTileType(null)).thenReturn(10);
        TileRegister.setInstance(mockRegister);

        TaskViewerController taskViewerController = mock(TaskViewerController.class);
        mockTile = mock(Tile.class);
        when(mockTile.getTileEntity()).thenReturn(null);
        when(mockTile.getTileType()).thenReturn(1);
        when(mockWorld.getTile(anyObject())).thenReturn(mockTile);
        mockAgent = mock(Agent.class);
        Point mockLocation = mock(Point.class);
        when(mockLocation.getY()).thenReturn(5.0);
        when(mockLocation.getX()).thenReturn(5.0);
        when(mockAgent.getLocation()).thenReturn(mockLocation);
        TaskManager.getInstance().refresh();
        taskManager = TaskManager.getInstance();
        taskManager.setController(taskViewerController);
    }

    @Test
    public void listTaskTest() {
//        List<AbstractTask> taskList = taskManager.listTasks();
//        MakeDirtTask dirtTask = new MakeDirtTask(1, 1, mockWorld);
//
//        assertEquals("List of tasks should be empty", taskList,
//                new LinkedList<AbstractTask>());

//        assertEquals("The TaskManager contains method should agree with this",
//                taskManager.contains(dirtTask), false);
//        taskManager.addTask(dirtTask);
//        taskList = taskManager.listTasks();
//        assertEquals("Now a list of the tasks should contain the task",
//                taskList.contains(dirtTask), true);
//        assertEquals("And the contains function should agree with it.",
//                taskManager.contains(dirtTask), true);
    }

    @Test
    public void conflictingTaskTest() {
//        WorldManager mockWorldManager = PowerMockito.mock(WorldManager.class);
//        PowerMockito.mockStatic(WorldManager.class);
//        PowerMockito.when(WorldManager.getInstance()).thenReturn(mockWorldManager);
//        when(mockWorldManager.getWorld()).thenReturn(mockWorld);
//        when(mockTile.getProperty(TileProperty.PASSABLE)).thenReturn(true);
////        MakeDirtTask dirtTask = new MakeDirtTask(1, 1, mockWorld);
//        MakeDirtTask nonConflictingDirtTask = new MakeDirtTask(1, 2, mockWorld);
//        when(mockAgent.isRequiredRole(dirtTask)).thenReturn(true);
//        when(mockAgent.hasRequiredTool(dirtTask)).thenReturn(true);
//        taskManager.addTask(dirtTask);
//        taskManager.getTask(mockAgent);
//        taskManager.conflictingTasks(dirtTask);
//        when(mockTile.getTileType()).thenReturn(1);

//        assertEquals("Another Dirt task on the same tile should conflict with "
//                + "this dirt Task", taskManager.conflictingTasks(dirtTask),
//                true);
        
//        assertEquals("Dirt task on a new tile should not conflict with this"
//                + " dirt Task",
//                taskManager.conflictingTasks(nonConflictingDirtTask), false);
    }

    @Test
    public void moveTaskTest() {
//        Tile mockTile = mock(Tile.class);
//        when(mockTile.getTileEntity()).thenReturn(null);
//        when(mockTile.getTileType()).thenReturn(1);
//        when(mockWorld.getTile(anyObject())).thenReturn(mockTile);
//        MakeDirtTask dirtTask = new MakeDirtTask(1, 1, mockWorld);
//        MakeDirtTask nonConflictingDirtTask = new MakeDirtTask(1, 2, mockWorld);
//        taskManager.addTask(dirtTask);
//        taskManager.addTask(nonConflictingDirtTask);
//        assertEquals("dirtTask should be first",
//        		taskManager.listTasks().get(0), dirtTask);
//        taskManager.moveTask(nonConflictingDirtTask, "up");
//        assertEquals("nonConflictingDirtTask should now be on top", taskManager
//                .listTasks().get(0), nonConflictingDirtTask);
//        taskManager.moveTask(nonConflictingDirtTask, "down");
//        assertEquals("dirtTask should be first again", taskManager.listTasks()
//                .get(0), dirtTask);
    }
    
    @Test
    public void getTaskTest() {
//        MakeDirtTask dirtTask1 = new MakeDirtTask(1, 5, mockWorld);
//        MakeDirtTask dirtTask2 = new MakeDirtTask(5, 1, mockWorld);
//        MakeDirtTask dirtTask3 = new MakeDirtTask(5, 6, mockWorld);
//        when(mockAgent.isRequiredRole(dirtTask1)).thenReturn(true);
//        when(mockAgent.isRequiredRole(dirtTask2)).thenReturn(true);
//        when(mockAgent.isRequiredRole(dirtTask3)).thenReturn(true);
//        when(mockAgent.hasRequiredTool(dirtTask1)).thenReturn(true);
//        when(mockAgent.hasRequiredTool(dirtTask2)).thenReturn(true);
//        when(mockAgent.hasRequiredTool(dirtTask3)).thenReturn(true);
//        taskManager.addTask(dirtTask1);
//        taskManager.addTask(dirtTask2);
//        taskManager.addTask(dirtTask3);
//        assertEquals("adjacent below task should be first",
//        		taskManager.getTask(mockAgent), dirtTask3);
//        assertEquals("no adjacents so chronological order now",
//        		taskManager.getTask(mockAgent), dirtTask1);
//        assertEquals("last task",
//        		taskManager.getTask(mockAgent), dirtTask2);
//
//        taskManager.getTask(mockAgent);
//        taskManager.getTask(mockAgent);
//        taskManager.getTask(mockAgent);
    	
    }

    @Test
    public void removeTaskTest() {
//        Tile mockTile = mock(Tile.class);
//        when(mockTile.getTileEntity()).thenReturn(null);
//        when(mockTile.getTileType()).thenReturn(1);
//        when(mockWorld.getTile(anyObject())).thenReturn(mockTile);
//        MakeDirtTask dirtTask = new MakeDirtTask(1, 1, mockWorld);
//        taskManager.addTask(dirtTask);
//        assertEquals("dirtTask should be in list",
//                taskManager.listTasks().get(0), dirtTask);
//        taskManager.removeTask(dirtTask);
//        assertEquals("TaskManager should be empty", taskManager.listTasks(),
//                new LinkedList<AbstractTask>());
    }
}
