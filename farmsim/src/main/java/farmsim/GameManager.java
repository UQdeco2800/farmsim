package farmsim;

import farmsim.entities.WorldEntity;
import farmsim.entities.agents.AgentManager;
import farmsim.entities.animals.FarmAnimalManager;

import farmsim.tiles.Tile;
import farmsim.util.Clickable;
import farmsim.util.Point;
import farmsim.world.WorldManager;

import java.util.*;

/**
 * Manages the interaction between the user and the game.
 * 
 * @author Leggy
 *
 */
public class GameManager extends Observable {

    private static final GameManager INSTANCE = new GameManager();

    private List<Point> selection;

    private List<Clickable> clickables;

    /**
     * While the mouse is being pressed, this is the location where press
     * started. If the mouse is not being pressed, this should be null.
     */
    private Point pressedLocation;


    /**
     * Whether or not the left mouse button is pressed
     */
    private boolean isPrimaryButtonDown;


    /**
     * While the mouse is being pressed, this is the current location of the
     * mouse. If the mouse is not being pressed, this should be null.
     */
    private Point dragLocation;

    /**
     * The current location of the mouse.
     */
    private Point mouseLocation;

    /**
     * Whether to move the viewport left, right, up or down
     */
    private boolean viewportLeft, viewportRight, viewportUp, viewportDown;

    private Viewport viewport;

    private GameManager() {
        //selection = new LinkedList<>();
        selection = Collections.synchronizedList(new LinkedList<>());
        clickables = new ArrayList<>();
        pressedLocation = null;
        dragLocation = null;
        isPrimaryButtonDown = false;

        viewportLeft = false;
        viewportRight = false;
        viewportUp = false;
        viewportDown = false;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    /**
     * Returns the instance of {@link GameManager}.
     *
     * @return Returns an instance of GameManager.
     */
    public static GameManager getInstance() {
        return INSTANCE;
    }


    /**
     * Register the given object to receive a callback to onClick when it is
     * clicked.
     */
    public void registerClickable(Clickable object) {
        clickables.add(object);
    }

    /**
     * Unregister the given object from receiving onClick callbacks.
     */
    public void unregisterClickable(Clickable object) {
        if (clickables.contains(object)) {
            clickables.remove(object);
        }
    }

    /**
     * Signify the start of a mouse press.
     * 
     * @param location the location of the mouse.
     */
    public void setPressed(Point location, boolean isPrimaryButtonDown) {
        pressedLocation = location;
        isPrimaryButtonDown = isPrimaryButtonDown;
    }

    /**
     * Update the location of the mouse while it is being pressed.
     * 
     * @param location the location of the mouse.
     */
    public void setDragged(Point location) throws ViewportNotSetException {
        // only update the selection if the mouse moves to another tile.
        if (dragLocation == null
                || (int) dragLocation.getX() != (int) location.getX()
                || (int) dragLocation.getY() != (int) location.getY()) {
            dragLocation = location;
            if (pressedLocation != null) {
                updateSelection(pressedLocation, dragLocation);
            }
        }
        mouseLocation = location;
    }

    /**
     * Signify the end of a mouse press.
     * 
     * @param location the location of the mouse.
     */
    public void setReleased(Point location) throws ViewportNotSetException {
        // pass the click event onto any Clickable objects
        for (Clickable object : new ArrayList<>(clickables)) {
            Point point = new Point(location);
            if (object.containsPoint(point)) {
                object.click(point);
            }
        }
        if (pressedLocation != null) {
            updateSelection(pressedLocation, location);
        }
        pressedLocation = null;
        dragLocation = null;
        isPrimaryButtonDown = false;
        if (selection.size() == 1) {
            Tile selectedTile = WorldManager.getInstance().getWorld()
                    .getTile(selection.get(0));
            selectedTile.handleObservers();
        }
        setChanged();
        notifyObservers(false);
    }

    /**
     * Update the location of the mouse while it is being moved.
     */
    public void setMouseMoved(Point location) {
        mouseLocation = location;
    }

    /**
     * @return the current location of the mouse.
     */
    public Point getMouseLocation() {
        return mouseLocation;
    }

    /**
     * @return A list of tile locations that are currently selected.
     */
    public List<Point> getSelection() {
        return Collections.synchronizedList(new LinkedList<>(selection));
    }
    
    /**
     * 
     * Set the selection on screen to this specific list of points.
     * @param points
     * 		The points to be shown as the selection onscreen.
     */
    public void setSelection(List<Point> points) {
    	selection = Collections.synchronizedList(new LinkedList<>());
    	for (Point point: points) {
    		selection.add(point);
    	}
    	setChanged();
        notifyObservers(false);
    }
    


    /**
     * Update the tile selection.
     * 
     * @param point1 the initial selection location.
     * @param point2 the end selection location.
     */
    private void updateSelection(Point point1, Point point2)
            throws ViewportNotSetException {

        if (viewport == null) {
            throw new ViewportNotSetException();
        }

        selection = Collections.synchronizedList(new LinkedList<>());

        int minX = (int) Math.min(point1.getX(), point2.getX());
        int maxX = (int) Math.max(point1.getX(), point2.getX());

        int minY = (int) Math.min(point1.getY(), point2.getY());
        int maxY = (int) Math.max(point1.getY(), point2.getY());

        // Make sure points are within the world.
        int worldRight = WorldManager.getInstance().getWorld().getWidth() - 1;
        int worldBottom = WorldManager.getInstance().getWorld().getHeight() - 1;
        minX = Math.min(Math.max(minX, 0), worldRight);
        maxX = Math.min(Math.max(maxX, 0), worldRight);
        minY = Math.min(Math.max(minY, 0), worldBottom);
        maxY = Math.min(Math.max(maxY, 0), worldBottom);

        // Make sure points are within the viewport
        int viewRight = viewport.getWidthTiles() - 1;
        int viewBottom = viewport.getHeightTiles() - 1;
        minX = Math.min(
                Math.max(minX, viewport.getX()),
                viewRight + viewport.getX()
        );
        maxX = Math.min(
                Math.max(maxX, viewport.getX()),
                viewRight + viewport.getX()
        );
        minY = Math.min(
                Math.max(minY, viewport.getY()),
                viewBottom + viewport.getY()
        );
        maxY = Math.min(
                Math.max(maxY, viewport.getY()),
                viewBottom + viewport.getY()
        );

        if (!isPrimaryButtonDown){
            List<WorldEntity> entities = new LinkedList<>();
            entities.addAll(AgentManager.getInstance().getAgents());
            entities.addAll(FarmAnimalManager.getInstance().getFarmAnimals());
            for (int i=0; i<entities.size();i++){
                WorldEntity entity = entities.get(i);
                if (entity.getWorldX() >= minX && entity.getWorldY() <= maxX + 1
                        && entity.getWorldY() >= minY
                        && entity.getWorldY() <= maxY + 1){
                    entity.setSelected(true);
                } else {
                    entity.setSelected(false);
                }
            }
        }

        for (int j = minY; j < maxY + 1; j++) {
            for (int i = minX; i < maxX + 1; i++) {
                selection.add(new Point(i, j));
            }
        }
    }

    /**
     * @param b Move viewport left
     */
    public void setViewportLeft(boolean b) {
        viewportLeft = b;
    }

    /**
     * @param b Move viewport right
     */
    public void setViewportRight(boolean b) {
        viewportRight = b;
    }

    /**
     * @param b Move viewport up
     */
    public void setViewportUp(boolean b) {
        viewportUp = b;
    }

    /**
     * @param b Move viewport down
     */
    public void setViewportDown(boolean b) {
        viewportDown = b;
    }

    /**
     * @return Whether to move viewport left
     */
    public boolean moveViewportLeft() {
        return viewportLeft;
    }

    /**
     * @return Whether to move viewport right
     */
    public boolean moveViewportRight() {
        return viewportRight;
    }

    /**
     * @return Whether to move viewport up
     */
    public boolean moveViewportUp() {
        return viewportUp;
    }

    /**
     * @return Whether to move viewport down
     */
    public boolean moveViewportDown() {
        return viewportDown;
    }

    public Point getSelectionFirst() {
        return new LinkedList<>(selection).getFirst();
    }

    public Point getSelectionLast() {
        return new LinkedList<>(selection).getLast();
    }
}
