import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class OperationDispacher {
    public static final OperationDispacher INSTANCE = new OperationDispacher();
    private final Map<Integer, Operation> operations = new HashMap<>();

    private OperationDispacher() {}

    public void register(int opID, Operation op) {
        operations.put(opID, op);
    }

    public void call(int opID) throws NoSuchElementException {
        Operation operation = operations.get(opID);
        if (operation != null) {
            operation.execute();
        } else {
            throw new NoSuchElementException("There's no operation registered with such type: "
                    + opID + ".");
        }
    }
}
