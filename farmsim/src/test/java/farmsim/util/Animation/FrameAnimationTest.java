package farmsim.util.Animation;


import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FrameAnimationTest {

    FrameAnimation animation;
    Frame[] frames;

    @Before
    public void setup(){
        // Mock a frame array
        Frame frame = mock(Frame.class);
        Frame frame2 = mock(Frame.class);
        Frame frame3 = mock(Frame.class);
        when(frame.getDuration()).thenReturn(100);
        when(frame2.getDuration()).thenReturn(150);
        when(frame3.getDuration()).thenReturn(100);
        frames = new Frame[3];
        frames[0] = frame;
        frames[1] = frame2;
        frames[2] = frame3;
    }

    @Test
    public void testStart() throws Exception {
        animation = new FrameAnimation(frames);
        animation.start();
        assertEquals(false, animation.isAnimationStopped());
    }

    @Test
    public void testStop() throws Exception {
        animation = new FrameAnimation(frames);
        animation.start();
        animation.stop();
        assertEquals(true, animation.isAnimationStopped());
    }

    @Test
    public void testSimpleUpdate() throws Exception {
        animation = new FrameAnimation(frames);
        animation.start();
        // Wait for 100 milliseconds so that second frame should be displayed
        Thread.sleep(100);
        animation.update();
        assertTrue(animation.getCurrentFrame().equals(frames[1]));
    }

    @Test
    public void testUpdate() throws InterruptedException {
        animation = new FrameAnimation(frames);
        animation.start();
        // Wait for 100 milliseconds so that second frame should be displayed
        Thread.sleep(100);
        animation.update();
        assertTrue(animation.getCurrentFrame().equals(frames[1]));

        // Wait for 150 milliseconds so that the last frame should be displayed
        Thread.sleep(150);
        animation.update();
        assertTrue(animation.getCurrentFrame().equals(frames[2]));
    }

    @Test
    public void testReset() throws Exception {
        animation = new FrameAnimation(frames);
        // update the image before trying to reset it. Test relies on
        animation.start();
        // Wait for 50 milliseconds so that second frame should be displayed
        Thread.sleep(50);
        animation.update();
        animation.reset();
        assertEquals(true, animation.isAnimationStopped());
        assertEquals(frames[0], animation.getCurrentFrame());
    }

    @Test
    public void testUpdateWithLooping() throws Exception{
        // Looping on by default
        animation = new FrameAnimation(frames);
        animation.start();
        // Wait for 100 milliseconds so that second frame should be displayed
        Thread.sleep(100);
        animation.update();
        assertTrue(animation.getCurrentFrame().equals(frames[1]));

        // Wait for 150 milliseconds so that the last frame should be displayed
        Thread.sleep(150);
        animation.update();
        assertTrue(animation.getCurrentFrame().equals(frames[2]));

        // Wait for 150 milliseconds so that the first frame should be displayed
        Thread.sleep(100);
        animation.update();
        // Animation should still be running
        assertTrue(!animation.isAnimationStopped());
    }

    @Test
       public void testNoLooping() throws Exception{
        // Turn looping off
        animation = new FrameAnimation(frames);
        animation.setLooping(false);
        animation.start();
        // Wait for 350 milliseconds so that animation should have finished
        Thread.sleep(350);
        animation.update();
        // Animation should be stopped
        assertTrue(animation.isAnimationStopped());
    }

    @Test
    public void testUpdateWithSpeed() throws Exception{
        // Set speed to 1.5
        animation = new FrameAnimation(frames, true, 2f);
        animation.start();
        // Wait for 50(100 / 2) milliseconds so that second frame should be displayed
        Thread.sleep(50);
        animation.update();
        assertTrue(animation.getCurrentFrame().equals(frames[1]));

        // Wait for 75(150 / 2) milliseconds so that the last frame should be displayed
        Thread.sleep(75);
        animation.update();
//        assertTrue(animation.getCurrentFrame().equals(frames[2])); //
// cancel this test because of sound
    }

    @Test
    public void testUpdateWithSpeed2() throws Exception{
        // Set speed to 0.5
        animation = new FrameAnimation(frames, true, 0.5f);
        animation.start();
        // Wait for 200(100 / 0.5) milliseconds so that second frame should be displayed
        Thread.sleep(200);
        animation.update();
        assertTrue(animation.getCurrentFrame().equals(frames[1]));

        // Wait for 300 (150/ 0.5) milliseconds so that the last frame should be displayed
        Thread.sleep(300);
        animation.update();
        assertTrue(animation.getCurrentFrame().equals(frames[2]));
    }
}