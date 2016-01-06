package farmsim.buildings;

import farmsim.Viewport;
import farmsim.entities.tileentities.objects.BaseObject;
import farmsim.tiles.Tile;
import farmsim.tiles.TileRegister;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * This tile entity is used to ensure tiles that a building is placed on aren't
 * also used for farming. This tile entity can also be used to get the building
 * placed on a tile.
 */
public class BuildingTileEntity extends BaseObject {

    private int tileX;
    private int tileY;
    private AbstractBuilding building;
    private Image constructionImage;

    public BuildingTileEntity(AbstractBuilding building, Tile tile) {
        super("building", tile, false);
        this.building = building;
        this.tileX = (int) tile.getWorldX();
        this.tileY = (int) tile.getWorldY();

        if (tileY == (int) (building.getWorldY() + building.getHeight() - 1)) {
            if (tileX % 2 == 0) {
                constructionImage = TileRegister.getInstance()
                        .getTileImage("buildingCaution");
            } else {
                constructionImage = TileRegister.getInstance()
                        .getTileImage("buildingCautionTape");
            }
        } else {
            Random random = new Random();
            switch (random.nextInt(3)) {
                case 1:
                    constructionImage = TileRegister.getInstance()
                            .getTileImage("buildingRP1");
                    break;
                case 2:
                    constructionImage = TileRegister.getInstance()
                            .getTileImage("buildingRP2");
                    break;
                default:
                    constructionImage = TileRegister.getInstance()
                            .getTileImage("buildingCautionBlank");
                    break;
            }
        }
    }

    /**
     * @return The building placed on this tile.
     */
    public AbstractBuilding getBuilding() {
        return building;
    }

    @Override
    public double getWorldX() {
        return building.getWorldX();
    }

    @Override
    public double getWorldY() {
        return building.getWorldY();
    }

    @Override
    public void draw(Viewport viewport, GraphicsContext ctx) {
        if (building.isBuilt()) {
            building.draw(viewport, ctx);
        } else {
            ctx.drawImage(constructionImage,
                    (tileX - viewport.getX()) * TileRegister.TILE_SIZE,
                    (tileY - viewport.getY()) * TileRegister.TILE_SIZE);
        }
    }
}
