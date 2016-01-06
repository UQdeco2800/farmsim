package farmsim;

import java.util.*;

import farmsim.buildings.BuildingPlacer;
import farmsim.entities.WorldEntity;
import farmsim.entities.agents.Agent;
import farmsim.entities.agents.AgentManager;
import farmsim.entities.animals.FarmAnimalManager;
import farmsim.entities.predators.Predator;
import farmsim.entities.predators.PredatorManager;
import farmsim.render.Drawable;
import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;
import farmsim.util.Point;
import farmsim.world.World;
import farmsim.world.WorldManager;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * GameRenderer renders the {@link World} and all {@link WorldEntity} instances.
 *
 * @author Leggy
 *
 */

public class GameRenderer extends AnimationTimer implements Observer {
    private GraphicsContext graphicsContext;
    private World world;
    private TileRegister tileRegister;
    private BuildingPlacer buildingPlacer;
    private int tileSize;

    private Viewport viewport;
    
    private boolean showWaterLevel = false;

    private List<List<? extends Drawable>> renderList;

    /**
     * The rendered size (in pixels) of a Tile.
     */

    public GameRenderer(Viewport viewport, GraphicsContext graphicsContext) {
        super();

        this.graphicsContext = graphicsContext;
        this.world = WorldManager.getInstance().getWorld();
        this.tileRegister = TileRegister.getInstance();
        this.buildingPlacer = world.getBuildingPlacer();
        this.tileSize = TileRegister.TILE_SIZE;
        this.viewport = viewport;
        this.renderList = new LinkedList<>();

        renderList.add(AgentManager.getInstance().getAgents());
        renderList.add(WorldManager.getInstance().getWorld().getPredatorManager().getPredators());
        renderList.add(FarmAnimalManager.getInstance().getFarmAnimals());
    }

    @Override
    public void handle(long arg0) {
        render();
    }

    public LinkedList<Drawable> sortDrawables(
            List<List<? extends Drawable>> drawables) {

        LinkedList<Drawable> result = new LinkedList<>();
        for (List<? extends Drawable> list : drawables) {
            synchronized (list) {
                for (Drawable drawable : list) {
                    boolean added = false;
                    for (int i = 0; i < result.size(); i++) {
                        double drawWorldY = drawable.getWorldY();
                        double drawWorldX = drawable.getWorldX();
                        double resWorldY = result.get(i).getWorldY();
                        double resWorldX = result.get(i).getWorldX();

                        if (!(Double.compare(drawWorldY, resWorldY) > 0)
                                && !(Double.compare(drawWorldX, resWorldX) > 0)) {

                            result.add(i, drawable);
                            added = true;
                            break;
                        }
                    }

                    if (!added) {
                        result.addLast(drawable);
                    }
                }
            }
        }

        return result;
    }

    private void render() {
        renderWorld();

        sortDrawables(renderList).forEach(drawable -> {
            if (viewport.isOnScreen(drawable)) {
                drawable.draw(viewport, graphicsContext);
            }
        });
        renderNight();
        renderSelection();
        buildingPlacer.draw(viewport, graphicsContext);
    }

    /**
     * Renders the world to the canvas.
     */
    private void renderWorld() {
        int viewportMinX = viewport.getX();
        int viewportMaxX = viewport.getX() + viewport.getWidthTiles();
        int viewportMinY = viewport.getY();
        int viewportMaxY = viewport.getY() + viewport.getHeightTiles();

        for (int y = viewportMinY; y < viewportMaxY; y++) {
            for (int x = viewportMinX; x < viewportMaxX; x++) {
                world.getTile(x, y).draw(viewport, graphicsContext);
            }
        }
    }

    /**
     * Renders the current selected tile outline.
     */
    private void renderSelection() {
        List<Point> selection = GameManager.getInstance().getSelection();

        /* Loop through the selection and display the water status bar for each
         selected tile
         */
        if(showWaterLevel) {
            for (Point selectedPoint : selection) {
                Tile tile = WorldManager.getInstance().getWorld()
                                .getTile(selectedPoint);
                float waterLevel = tile.getWaterLevel();
                graphicsContext.setFill(Color.BLUE);
                graphicsContext.fillRect(
                        selectedPoint.getX() * 32.0- viewport.getX() * 32.0,
                        selectedPoint.getY() * 32.0 - viewport.getY() * 32.0,
                        waterLevel * 32.0, 10.0);
    
            }
        }

        // Display the pollution bar for each tile
        for (Point selectedPoint : selection) {
            Tile tile = WorldManager.getInstance().getWorld()
                            .getTile(selectedPoint);
            double pollutionLevel = tile.getPollution();
            graphicsContext.setFill(Color.BROWN);
            graphicsContext.fillRect(
                    selectedPoint.getX() * 32.0 - viewport.getX() * 32.0,
                    (selectedPoint.getY() * 32.0 - viewport.getY() * 32.0) + 10,
                    pollutionLevel * 32.0, 10.0
            );
        }

        // One tile selected
        if (selection.size() == 1) {
            graphicsContext.drawImage(tileRegister.getTileImage("selectionS"),
                            (selection.get(0).getX() - viewport.getX())
                                            * tileSize, (selection.get(0)
                                            .getY() - viewport.getY())
                                            * tileSize);
            return;
        } else if (selection.isEmpty()) {
            return;
        }

        // selection size > 1
        Point topLeft = selection.get(0);
        double minX = topLeft.getX();
        double minY = topLeft.getY();
        double maxX = topLeft.getX();
        double maxY = topLeft.getY();

        // Gets maxX and Y points
        for (Point point : selection) {
            if (maxX < point.getX()) {
                maxX = point.getX();
            }
            if (maxY < point.getY()) {
                maxY = point.getY();
            }
        }

        // Check for single width column or row
        Image selectImage;
        if ((int) minX == (int) maxX) {
            // Single width Column
            for (Point point : selection) {
                if ((int) point.getY() == (int) minY) {
                    // Top
                    selectImage = tileRegister.getTileImage("selectionSCT");
                } else if ((int) point.getY() == (int) maxY) {
                    // Bottom
                    selectImage = tileRegister.getTileImage("selectionSCB");
                } else {
                    // Centre
                    selectImage = tileRegister.getTileImage("selectionSCC");
                }

                graphicsContext.drawImage(selectImage,
                                (point.getX() - viewport.getX()) * tileSize,
                                (point.getY() - viewport.getY()) * tileSize);
            }
        } else if ((int) minY == (int) maxY) {
            // Single width Row
            for (Point point : selection) {
                if ((int) point.getX() == (int) minX) {
                    // Left
                    selectImage = tileRegister.getTileImage("selectionSRL");
                } else if ((int) point.getX() == (int) maxX) {
                    // Right
                    selectImage = tileRegister.getTileImage("selectionSRR");
                } else {
                    // Centre
                    selectImage = tileRegister.getTileImage("selectionSRC");
                }

                graphicsContext.drawImage(selectImage,
                                (point.getX() - viewport.getX()) * tileSize,
                                (point.getY() - viewport.getY()) * tileSize);
            }
        } else {
            // Not column or row (ie. 2x2 selection or larger)
            for (Point point : selection) {
                if ((int) point.getX() == (int) minX
                                && (int) point.getY() == (int) minY) {
                    // Left Top
                    selectImage = tileRegister.getTileImage("selectionLT");
                } else if ((int) point.getX() == (int) maxX
                                && (int) point.getY() == (int) minY) {
                    // Right Top
                    selectImage = tileRegister.getTileImage("selectionRT");
                } else if ((int) point.getY() == (int) minY) {
                    // Centre Top
                    selectImage = tileRegister.getTileImage("selectionCT");
                } else if ((int) point.getX() == (int) minX
                                && (int) point.getY() == (int) maxY) {
                    // Left Bottom
                    selectImage = tileRegister.getTileImage("selectionLB");
                } else if ((int) point.getX() == (int) minX) {
                    // Left Centre
                    selectImage = tileRegister.getTileImage("selectionLC");
                } else if ((int) point.getX() == (int) maxX
                                && (int) point.getY() == (int) maxY) {
                    // Right Bottom
                    selectImage = tileRegister.getTileImage("selectionRB");
                } else if ((int) point.getX() == (int) maxX) {
                    // Right Centre
                    selectImage = tileRegister.getTileImage("selectionRC");
                } else if ((int) point.getY() == (int) maxY) {
                    // Centre Bottom
                    selectImage = tileRegister.getTileImage("selectionCB");
                } else {
                    // Centre
                    selectImage = tileRegister.getTileImage("selectionC");
                }

                graphicsContext.drawImage(selectImage,
                                (point.getX() - viewport.getX()) * tileSize,
                                (point.getY() - viewport.getY()) * tileSize);
            }
        }
    }
    
    
    /**
     *This renders a gradual darkness value by tile instead of by the entire pane. 
     * Please dont read/judge this code for the love of god. 
     * Tt does what it has to do and its not completely hacked together. 
     *
     * @author sc0urge
     *
     */
    private void renderNight(){
        int viewportMinX = viewport.getX();
        int viewportMaxX = viewport.getX() + viewport.getWidthTiles();
        int viewportMinY = viewport.getY();
        int viewportMaxY = viewport.getY() + viewport.getHeightTiles();

        Image zero = tileRegister.getTileImage("zero");
        Image ten = tileRegister.getTileImage("ten");
        Image twenty = tileRegister.getTileImage("twenty");
        Image thirty = tileRegister.getTileImage("thirty");
        Image forty = tileRegister.getTileImage("forty");
        Image fifty = tileRegister.getTileImage("fifty");
        Image sixty = tileRegister.getTileImage("sixty");
        Image seventy = tileRegister.getTileImage("seventy");
        Image eighty = tileRegister.getTileImage("eighty");
        Image ninety = tileRegister.getTileImage("ninety");
        Image full = tileRegister.getTileImage("full");

        int currentHour = WorldManager.getInstance().getWorld().getTimeManager().getHours();
        Image selectImage;
        switch (currentHour) {
            case 15:
                selectImage = zero;
                break;
            case 16:
                selectImage = ten;
                break;
            case 17:
                selectImage = twenty;
                break;
            case 18:
                selectImage = thirty;
                break;
            case 19:
                selectImage = forty;
                break;
            case 20:
                selectImage = fifty;
                break;
            case 21:
                selectImage = sixty;
                break;
            case 22:
                selectImage = seventy;
                break;
            case 23:
                selectImage = eighty;
                break;
            case 0:
                selectImage = seventy;
                break;
            case 1:
                selectImage = sixty;
                break;
            case 2:
                selectImage = fifty;
                break;
            case 3:
                selectImage = forty;
                break;
            case 4:
                selectImage = thirty;
                break;
            case 5:
                selectImage = twenty;
                break;
            case 6:
                selectImage = ten;
                break;
            default:
                selectImage = zero;
                break;
        }

        for (int y = viewportMinY; y < viewportMaxY; y++) {
            for (int x = viewportMinX; x < viewportMaxX; x++) {
                graphicsContext.drawImage(selectImage,
                    x * tileSize - viewport.getX() * tileSize,
                    y * tileSize - viewport.getY() * tileSize);
            }
        }
    }

    /**
     * Render agents/peons
     */
    private void renderAgents() {
        // TODO: expand this for all WorldEntities.
    	
    	List<Predator> predators = WorldManager.getInstance().getWorld().getPredatorManager().getPredators();
    	for (Predator predator : predators) {
    		double predX = predator.getLocation().getX();
            double predY = predator.getLocation().getY();

            int viewportMinX = viewport.getX();
            int viewportMinY = viewport.getY();
            int viewportMaxX = viewport.getX() + viewport.getWidthTiles();
            int viewportMaxY = viewport.getY() + viewport.getHeightTiles();

            if (predX + TileRegister.TILE_SIZE >= viewportMinX
                    && predX < viewportMaxX
                    && predY + TileRegister.TILE_SIZE >= viewportMinY
                    && predY < viewportMaxY) {

                graphicsContext.save();

                graphicsContext.drawImage(tileRegister.getTileImage("predator"),
                        (predX - viewportMinX) * TileRegister.TILE_SIZE,
                        (predY - viewportMinY) * TileRegister.TILE_SIZE);
                graphicsContext.restore();

            }
    	}

    }


    /**q33
     * Method to apply a rotate around point to graphicsContext. This is useful
     * for drawing rotated images.
     * 
     * @param angle
     *            The angle to rotate in degrees
     * @param xPivot
     *            The x coordinate to pivot/rotate around
     * @param yPivot
     *            The y coordinate to pivot/rotate around
     */
    private void rotateAroundPoint(double angle, double xPivot, double yPivot) {
        Rotate rotate = new Rotate(angle, xPivot, yPivot);
        graphicsContext.setTransform(rotate.getMxx(), rotate.getMyx(),
                        rotate.getMxy(), rotate.getMyy(), rotate.getTx(),
                        rotate.getTy());
    }
  
      @Override
      public void update(Observable arg0, Object arg1) {
          showWaterLevel = (boolean)arg1;
          
      }
}