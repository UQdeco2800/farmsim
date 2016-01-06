package farmsim.util.Animation;

import farmsim.tiles.TileRegister;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

public class SpriteSheetTest {

    @Test
    public void testNumberOfImages() throws Exception {
        // Defaults to tile size images if no image width/height given
        // single image
        SpriteSheet spriteSheet = new SpriteSheet(null, 1, 1, 100);
        assertEquals(1, spriteSheet.numberOfImages());

        // 1 *2 sprite sheet
        spriteSheet = new SpriteSheet(null, 1, 2, 100);
        assertEquals(2, spriteSheet.numberOfImages());

        // 2 * 1 sprite sheet
        spriteSheet = new SpriteSheet(null, 2, 1, 100);
        assertEquals(2, spriteSheet.numberOfImages());

        // 3 *3 sprite sheet
        spriteSheet = new SpriteSheet(null, 3, 3, 100);
        assertEquals(9, spriteSheet.numberOfImages());
    }

    @Test
    public void testGetPointForIndex() throws Exception {
        // Defaults to tile size images if no image width given
        int tile = TileRegister.TILE_SIZE;

        // 2 * 2 sprite sheet
        SpriteSheet spriteSheet =
                new SpriteSheet(null, 2, 2, 100);

        // top left corner image
        assertEquals(new Point(0, 0), spriteSheet.getPointForIndex(0));
        // top right corner image
        assertEquals(new Point(tile, 0), spriteSheet.getPointForIndex(1));
        //bottom left
        assertEquals(new Point(0, tile), spriteSheet.getPointForIndex(2));
        // bottom right
        assertEquals(new Point(tile, tile), spriteSheet.getPointForIndex(3));
    }
}