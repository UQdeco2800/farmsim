package farmsim.world.modifiers;

import farmsim.util.Point;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class AttributeModifierTest {
    private AttributeModifier modifier;
    private String tag = "Tests";
    private String target = "Batman";
    private Point position = new Point(10, 20);

    @Before
    public void setup() {
        Map<String, String> settings = new HashMap<>();
        settings.put("tag", tag);
        settings.put("target", target);
        settings.put("position", position.toString());

        modifier = new AttributeModifier(settings);
    }

    @Test
    public void testGetTag() throws Exception {
        Assert.assertEquals(tag, modifier.getTag());
    }

    @Test
    public void testGetTarget() throws Exception {
        Assert.assertEquals(target, modifier.getTarget());
    }

    @Test
    public void testGetPosition() throws Exception {
        Assert.assertEquals(position, modifier.getPosition());
    }
}