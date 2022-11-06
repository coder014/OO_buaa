import java.util.HashMap;
import java.util.Map;

public class JsonObject {
    private final Map<String, Object> attributes = new HashMap<>();

    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
