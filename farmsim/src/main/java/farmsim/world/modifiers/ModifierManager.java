package farmsim.world.modifiers;

import farmsim.util.Point;
import farmsim.util.Tickable;
import farmsim.util.console.Console;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Modifier Manager
 *<p>
 *     Allows the creation/addition and removal of modifiers in the game world.
 *</p>
 */
public class ModifierManager implements Tickable {

    private List<AttributeModifier> attributeModifiers;

    /**
     * Creates a new Modifier Manager to handle the adding and removal
     * of modifiers from the game.
     */
    public ModifierManager() {
        attributeModifiers = new ArrayList<>();
    }

    /**
     * Adds a new modifier to the game to be controlled with given settings
     * applied.
     * @param settings to apply to the modifier as key, value pairs.
     */
    public void addNewAttributeModifier(Map<String, String> settings) {
        attributeModifiers.add(new AttributeModifier(settings));
    }

    /**
     * Removes a modifier at a position with a target and tag as specified.
     * @param target of the modifier to be removed.
     * @param tag of the family the modifier belongs too.
     * @param position of the modifier in the world.
     */
    public void removeAttributeModifier(String target, String tag,
                                        Point position) {
        Iterator<AttributeModifier> iterator = attributeModifiers.iterator();
        while (iterator.hasNext()) {
            AttributeModifier modifier = iterator.next();
            if (modifier.getTag().equals(tag)
                    && modifier.getTarget().equals(target)
                    && modifier.getPosition().equals(position)) {
                iterator.remove();
            }
        }
    }

    /**
     * Removes modifiers with a specific tag or
     * all modifiers if "all".equals(tag).
     * @param tag to search for items.
     */
    public void purge(String tag) {
        if ("all".equals(tag)) {
            attributeModifiers = new ArrayList<>();
        } else {
            Iterator<AttributeModifier> iterator =
                    attributeModifiers.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getTag().equals(tag)) {
                    Console.getInstance().println("removed " + tag
                            + " modifier");
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Count Modifiers by tag or "all".
     * @param tag to search for items.
     */
    public Integer count(String tag) {
        int counter = 0;
        if ("all".equals(tag)) {
            counter = attributeModifiers.size();
        } else {
            for (AttributeModifier attributeModifier : attributeModifiers) {
                if (attributeModifier.getTag().equals(tag)) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Applies all modifiers effects currently stored onto the world.
     */
    @Override
    public void tick() {
        //not a parallel stream due to modifiers overlapping resources.
        attributeModifiers.stream().forEach(AttributeModifier::tick);
    }
}
