package farmsim.entities.predatorTest;

import farmsim.Viewport;
import farmsim.entities.predators.ViewportTranslator;
import farmsim.util.Point;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Test;

public class ViewportTranslatorTest {
	private ViewportTranslator translator;
	
	@Test
	public void testDefault() {
		translator = new ViewportTranslator();
		
		assertEquals(translator.getLeft(), 0);
		assertEquals(translator.getRight(), 100);
		
		assertEquals(translator.getTop(), 0);
		assertEquals(translator.getBottom(), 100);
	}
	
	@Test
	public void testDefaultPoints() {
		translator = new ViewportTranslator();
		
		assertTrue(translator.getBottomRight().equals(new Point(100, 100)));
		assertTrue(translator.getTopLeft().equals(new Point(0, 0)));
	}
	
	@Test
	public void testViewport() {
		translator = new ViewportTranslator();
		
		// Attempt to set a null viewport
		translator.setViewport(null);

		// Check that the points are still at default values
		assertTrue(translator.getBottomRight().equals(new Point(100, 100)));
		assertTrue(translator.getTopLeft().equals(new Point(0, 0)));
		
		
    	Viewport vp = mock(Viewport.class);
        when(vp.getWidthTiles()).thenReturn(1);
        when(vp.getHeightTiles()).thenReturn(1);
        when(vp.getX()).thenReturn(5);
        when(vp.getY()).thenReturn(5);
        
        translator.setViewport(vp);
        
        
		assertEquals(translator.getLeft(), 5);
		assertEquals(translator.getRight(), 6);
		
		assertEquals(translator.getTop(), 5);
		assertEquals(translator.getBottom(), 6);
	}

	@Test
	public void testViewportPoints() {
		translator = new ViewportTranslator();
    	Viewport vp = mock(Viewport.class);
        when(vp.getWidthTiles()).thenReturn(1);
        when(vp.getHeightTiles()).thenReturn(1);
        when(vp.getX()).thenReturn(5);
        when(vp.getY()).thenReturn(5);
        
        translator.setViewport(vp);
        
		assertTrue(translator.getBottomRight().equals(new Point(6, 6)));
		assertTrue(translator.getTopLeft().equals(new Point(5, 5)));
	}
}
