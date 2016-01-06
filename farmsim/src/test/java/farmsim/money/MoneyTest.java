package farmsim.money;

import common.client.FarmClient;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Woody
 *
 */
public class MoneyTest {
    Money money;

	@Before
    public void setupMoney() {
        FarmClient client = mock(FarmClient.class);
        when(client.isAuthenticated()).thenReturn(false);
        money = new Money(client, 1000);
    }
	
    @Test
    public void testInit() {
        assertEquals(money.getAmount(), 1000);
    }


    @Test
    public void testAdd() {
        money.addAmount(500);
        assertEquals(money.getAmount(), 1500);
    }

    @Test
    public void testBadSubtract() {
        money.subtractAmount(9999999);
        assertEquals(money.getAmount(), 1000);
    }

    @Test
    public void testGoodSubtract() {
        assertTrue(money.subtractAmount(500));
        assertEquals(money.getAmount(), 500);
    }
}
