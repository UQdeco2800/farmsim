package farmsim.resource;


import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class SimpleResource {

    private Integer id = null;
    private String type = "";
    private Map<String, String> attributes = new HashMap<>();
    private Integer quantity = 0;

    public SimpleResource(Integer id, String type, Map<String, String> attributes, Integer quantity) {
        this.id = id;
        this.type = type;
        if (attributes != null) {
            this.attributes = attributes;
        }
        if (quantity <= 0 ) {
            this.quantity = 1;
        } else {
            this.quantity = quantity;
        }
    }

    public SimpleResource(String type, Map<String, String> attributes, Integer quantity) {
        this(null, type, attributes, quantity);
    }

    public SimpleResource(String type, Map<String, String> attributes) {
        this(null, type, attributes, 1);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public boolean equalsType(SimpleResource item) {
        return type.equals(item.getType());
    }

    public void addToStack(SimpleResource item) {
        if (this.equalsType(item)) {
            this.quantity += item.getQuantity();
        }
    }

    public SimpleResource split(int quantity) {
        if (quantity >= this.quantity || quantity <= 0) {
            return null;
        }
        this.quantity -= quantity;
        return new SimpleResource(type, new Hashtable<>(attributes), quantity);
    }
}
