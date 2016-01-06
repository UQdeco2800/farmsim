package farmsim.world.modifiers;

import farmsim.entities.tileentities.TileEntity;
import farmsim.entities.tileentities.crops.Crop;
import farmsim.entities.tileentities.objects.Snow;
import farmsim.tiles.Tile;
import farmsim.util.Point;
import farmsim.util.Tickable;
import farmsim.world.World;
import farmsim.world.WorldManager;
import farmsim.world.weather.SeasonName;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 * A modifier which changes attributes of tiles including tick count.
 */
public class AttributeModifier implements Tickable {

    /* Default Settings and Core Values */
    private String target = "Tile";
    private String tag = "Modifier";
    private Point position = new Point(0, 0);
    private String shape = "square";
    private World world;

    private Map<String, String> settings;

    /**
     * Creates a modifier with the settings given as a key value store.
     * @param settings to be applied to the modifier.
     */
    public AttributeModifier(Map<String, String> settings) {
        this.settings = settings;
        this.target = settings.getOrDefault("target", target);
        this.tag = settings.getOrDefault("tag", tag);
        this.shape = settings.getOrDefault("shape", shape);
        this.world = WorldManager.getInstance().getWorld();
        //set the position
        this.position = new Point(
                settings.getOrDefault("position", position.toString()));
    }

    /**
     * Gets a group of tiles.
     * @return list of tiles to affect.
     */
    private ArrayList<Tile> getTiles() {
        switch (shape) {
            case "square":
                return getTilesSquare();
            case "circle":
                return getTilesRadial();
            default:
                return getTilesSquare();
        }
    }

    /**
     * Gets a radial selection of tiles around a point, assumes that a
     * radius setting is stored in the settings config or will default
     * to a radius of 1.
     * @return list of tiles in a circle around the center point.
     */
    private ArrayList<Tile> getTilesRadial() {
        int radius = Integer.parseInt(
                this.settings.getOrDefault("radius", "1"));
        ArrayList<Tile> tiles = new ArrayList<>();
        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {
                radialTileCheck(i, j, radius, tiles);
            }
        }
        return tiles;
    }

    /**
     * Using the formula x^2 + y^2 <= radius^2 tiles are added accordingly.
     * @param i x dimension of the circle
     * @param j y dimension of the circle
     * @param radius of the circle.
     * @param tiles list to store the selected tiles.
     */
    private void radialTileCheck(int i, int j, int radius,
                                 ArrayList<Tile> tiles) {
        if (Math.pow(i, 2) + Math.pow(j, 2) <= Math.pow(radius, 2)) {
            Tile tile = world.getTile(i + (int)position.getX(),
                    j + (int)position.getY());
            if ("all".equals(target)) {
                tiles.add(tile);
            } else if (tile.getTileEntity() != null
                    && tile.getTileEntity().getTileType()
                    .equals(this.target)) {
                tiles.add(tile);
            }
        }
    }

    /**
     * Gets tiles in a square shape, assumes the user set the finish point and
     * that tiles exist between them. If not set then will return the tile
     * under the point.
     * @return list of tiles in square from position to end points.
     */
    private ArrayList<Tile> getTilesSquare() {
        // may be backwards to cast to string back to point but easier to see.
        Point end = new Point(settings.getOrDefault("end",
                this.getPosition().toString()));
        ArrayList<Tile> tiles = new ArrayList<>();

        for (double i = this.position.getX(); i < end.getX(); i++) {
            for (double j = this.position.getY(); j < end.getY(); j++) {
                // Add to list if searching for all or the tile target
                Tile tile = world.getTile((int) i, (int) j);
                if ("all".equals(target)) {
                    tiles.add(tile);
                } else if (tile.getTileEntity() != null
                        && tile.getTileEntity().getTileType()
                        .equals(this.target)) {
                    tiles.add(tile);
                }
            }
        }
        return tiles;
    }

    /**
     * Gets the Tag.
     * @return string of the Tag.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Gets the target.
     * @return string of the Target.
     */
    public String getTarget() {
        return target;
    }

    /**
     * Gets the position.
     * @return a point which is the start position.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Gets the tiles that the modifier should affect and handles them (applies
     * the attribute change).
     */
    @Override
    public void tick() {
        //get all the tiles we want to change the attribute of
        ArrayList<Tile> tiles = getTiles();
        //add the delta to the attribute
        handleTiles(tiles);
    }

    /**
     * Handles the attribute change that set under attribute in the settings
     * applied.
     * @param tiles list to be affected.
     */
    private void handleTiles(ArrayList<Tile> tiles) {
        switch (settings.get("attribute")) {
            case "moisture":
                changeMoisture(tiles);
                break;
            case "growth":
                applyGrowth(tiles);
                break;
            case "season":
                setSeason(tiles);
                break;
            default:
                break;
        }
    }

    /**
     * Adjusts the moisture on the set of tiles with no exclusives.
     * @param tiles to have the moisture changed.
     */
    private void changeMoisture(ArrayList<Tile> tiles) {
        tiles.stream().parallel().forEach(e -> e.setWaterLevel(
                e.getWaterLevel()
                + Float.parseFloat(
                        this.settings.getOrDefault("effect", "0.0"))));
    }

    /**
     * Applies a growth tick to a plant.
     * @param tiles list of tiles to affect if type Crop.
     */
    private void applyGrowth(ArrayList<Tile> tiles) {
        Random random = new Random();
        Double chance;
        for (Tile tile : tiles) {
            chance = random.nextDouble();
            if(tile.getTileEntity() instanceof Crop && chance < 1) {
                singleGrowthChange(tile,
                        Integer.parseInt(settings.getOrDefault("effect", "1")));
            }
        }
    }

    /**
     * Applies a multi seasonal modifier to the world depending on the currently
     * active season.
     * @param tiles that the seasonal modifier should be applied to.
     */
    private void setSeason(ArrayList<Tile> tiles) {
        Random random = new Random();
        Double chance;
        String season = WorldManager.getInstance().getWorld().getSeason();
        Map<String, Integer> weatherComponents = WorldManager.getInstance()
                .getWorld().getWeatherComponents();
        //If its winter then place snow and change water to ice
        //has to be snowing for snow
        for (Tile tile : tiles) {
            chance = random.nextDouble();
            //Set tiles to the season
            if (chance < 0.0005) {
                tile.setActiveSeason(season);
            }
            // Winter specific
            winterEffects(tile, weatherComponents, chance, season);
            //Summer and Spring specific
            if ((SeasonName.SPRING.toString().equals(season)
                    || SeasonName.SUMMER.toString().equals(season))
                    && chance < 0.5) {
                singleGrowthChange(tile, 1);
            }
        }
    }

    /**
     * Increase the growth of a plant tile.
     * @param tile to increase growth
     */
    private void singleGrowthChange(Tile tile, long time) {
        TileEntity tileEntity = tile.getTileEntity();
        if (tileEntity != null && tileEntity instanceof Crop) {
            ((Crop) tileEntity).advanceGrowth(time);
        }
    }


    /**
     * Applies winter effects or removes them.
     * @param tile the individual tile.
     * @param weatherComponents current weather components.
     * @param chance the probability of the event to occur.
     * @param season the current season.
     */
    private void winterEffects(Tile tile,
                               Map<String, Integer> weatherComponents,
                               double chance,
                               String season) {
        String seasonName = SeasonName.WINTER.toString();
        if (seasonName.equals(season)) {
            if (weatherComponents.containsKey("Snow")
                    && chance < (0.0001 * weatherComponents.get("Snow"))
                    && tile.getTileEntity() == null) {
                tile.setTileEntity(new Snow(tile));
            }
            if (tile.getTileEntity() instanceof Crop) {
                singleGrowthChange(tile, -1);
            }

        } else {
            if (chance < 0.002
                    && tile.getTileEntity() != null
                    && "snow".equals(tile.getTileEntity().getTileType())) {
                tile.setTileEntity(null);
            }
        }
    }
}
