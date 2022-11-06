import java.util.Map;

public class Attribute implements Map.Entry<String, Object> {
    private final String name;
    private final Object obj;

    public Attribute(String name, Object obj) {
        this.name = name;
        this.obj = obj;
    }

    @Override
    public String getKey() {
        return name;
    }

    @Override
    public Object getValue() {
        return obj;
    }

    @Override
    public Object setValue(Object value) {
        throw new UnsupportedOperationException();
    }
}
