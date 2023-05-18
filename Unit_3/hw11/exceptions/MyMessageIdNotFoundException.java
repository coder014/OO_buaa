package exceptions;

import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static final Map<Integer, Integer> COUNT = new HashMap<>();
    private static int countSum = 0;
    private final int id;

    public MyMessageIdNotFoundException(int id) {
        COUNT.put(id, COUNT.getOrDefault(id, 0) + 1);
        countSum++;
        this.id = id;
    }

    @Override
    public void print() {
        System.out.printf("minf-%d, %d-%d\n", countSum, id, COUNT.get(id));
    }
}
