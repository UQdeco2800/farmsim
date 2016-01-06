package farmsim.world.modifiers;

import farmsim.util.Point;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ModifierManagerTest {
    private ModifierManager manager;
    private Point position = new Point(10, 20);

    @Before
    public void setup() {
        manager = new ModifierManager();
    }

    @Test
    public void testAddNewAttributeModifier() throws Exception {
        Map<String, String> settings = new HashMap<>();
        settings.put("tag", "Tests");
        settings.put("target", "Batman");
        settings.put("location", position.toString());

        manager.addNewAttributeModifier(settings);
        Assert.assertTrue(manager.count("Tests") == 1);
        Assert.assertTrue(manager.count("all") == 1);
        Assert.assertTrue(manager.count("Batman") == 0);
    }

    @Test
    public void testRemoveAttributeModifier() throws Exception {
        Map<String, String> settings = new HashMap<>();
        settings.put("tag", "Tests");
        settings.put("target", "Batman");
        settings.put("location", position.toString());

        manager.addNewAttributeModifier(settings);
        manager.removeAttributeModifier("Batman", "Tests", position);
        Assert.assertTrue(manager.count("Tests") == 1);
        Assert.assertTrue(manager.count("all") == 1);
    }

    @Test
    public void testPurge() throws Exception {
        Map<String, String> settings = new HashMap<>();
        settings.put("tag", "Tests");
        settings.put("target", "Batman");
        settings.put("location", position.toString());
        Map<String, String> settings2 = new HashMap<>();
        settings2.put("tag", "Robin");
        settings2.put("target", "Batman");
        settings2.put("location", position.toString());

        manager.addNewAttributeModifier(settings);
        manager.addNewAttributeModifier(settings2);
        manager.purge("Tests");
        Assert.assertTrue(manager.count("Tests") == 0);
        Assert.assertTrue(manager.count("all") == 1);
        manager.purge("all");
        Assert.assertTrue(manager.count("all") == 0);
    }
}