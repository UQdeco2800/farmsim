package farmsim.util.Animation;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpriteSheetAnimationTest {

    private SpriteSheetAnimation animation;
    private SpriteSheet spriteSheet;

    @Before
    public void setup() {
        spriteSheet = mock(SpriteSheet.class);
        when(spriteSheet.getDuration()).thenReturn(100);
        when(spriteSheet.numberOfImages()).thenReturn(3);
        when(spriteSheet.getWidth()).thenReturn(2);
        when(spriteSheet.getHeight()).thenReturn(2);
    }

    @Test
    public void testSimpleUpdate() throws Exception {
        animation = new SpriteSheetAnimation(spriteSheet);
        animation.start();
        assertTrue(animation.getFrameIndex() == 0);
        // Wait for 100 milliseconds so that second sprite should be displayed
        Thread.sleep(100);
        animation.update();
        assertTrue(animation.getFrameIndex() == 1);
    }

    @Test
    public void testUpdate() throws InterruptedException {
        animation = new SpriteSheetAnimation(spriteSheet);
        animation.start();
        assertTrue(animation.getFrameIndex() == 0);
        // Wait for 100 milliseconds so that second sprite should be displayed
        Thread.sleep(100);
        animation.update();
        assertTrue(animation.getFrameIndex() == 1);
        // Wait for 100 milliseconds so that third sprite should be displayed
        Thread.sleep(100);
        animation.update();
        assertTrue(animation.getFrameIndex() == 2);
    }

    @Test
    public void testUpdateWithLooping() throws Exception {
        // Looping on by default
        animation = new SpriteSheetAnimation(spriteSheet);
        animation.start();
        assertTrue(animation.getFrameIndex() == 0);
        // Wait for 300 milliseconds so that animation should have finished
        // and looped back to first sprite
        Thread.sleep(300);
        animation.update();
        assertTrue(!animation.isAnimationStopped());
    }

    @Test
    public void testNoLooping() throws Exception {
        // Turn looping off
        animation = new SpriteSheetAnimation(spriteSheet);
        animation.setLooping(false);
        animation.start();
        assertTrue(animation.getFrameIndex() == 0);
        // Wait for 300 milliseconds so that animation should have finished
        Thread.sleep(300);
        animation.update();
        assertTrue(animation.isAnimationStopped());
    }

    @Test
    public void testUpdateWithSpeed() throws Exception {
        // Set speed to 2
        animation = new SpriteSheetAnimation(spriteSheet, true, 2f);
        animation.start();
        assertTrue(animation.getFrameIndex() == 0);
        // Wait for 50 (100 / 2)  milliseconds so that second sprite should be displayed
        Thread.sleep(50);
        animation.update();
        assertTrue(animation.getFrameIndex() == 1);
        // Wait for 50 (100 /2) milliseconds so that third sprite should be displayed
        Thread.sleep(50);
        animation.update();
        assertTrue(animation.getFrameIndex() == 2);
    }

    @Test
    public void testUpdateWithSpeed2() throws Exception {
        // Set speed to 0.5
        animation = new SpriteSheetAnimation(spriteSheet, true, 0.5f);
        animation.start();
        assertTrue(animation.getFrameIndex() == 0);
        // Wait for 200 (100 / 0.5)  milliseconds so that second sprite should be displayed
        Thread.sleep(200);
        animation.update();
        assertTrue(animation.getFrameIndex() == 1);
        // Wait for 200 (100 /0.5) milliseconds so that third sprite should be displayed
        Thread.sleep(200);
        animation.update();
        assertTrue(animation.getFrameIndex() == 2);
    }

    @Test
         public void testUpdateWithStartIndex() throws InterruptedException {
        // set start index to 1
        animation = new SpriteSheetAnimation(spriteSheet, 1, 2);
        animation.start();
        assertTrue(animation.getFrameIndex() == 1);
        // Wait for 100 milliseconds so that second sprite should be displayed
        Thread.sleep(100);
        animation.update();
        assertTrue(animation.getFrameIndex() == 2);
    }


}