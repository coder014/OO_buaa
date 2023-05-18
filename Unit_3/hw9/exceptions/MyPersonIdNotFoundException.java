package exceptions;

import com.oocourse.spec1.exceptions.PersonIdNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static final Map<Integer, Integer> COUNT = new HashMap<>();
    private static int countSum = 0;
    private final int id;

    public MyPersonIdNotFoundException(int id) {
        COUNT.put(id, COUNT.getOrDefault(id, 0) + 1);
        countSum++;
        this.id = id;
    }

    @Override
    public void print() {
        System.out.printf("pinf-%d, %d-%d\n", countSum, id, COUNT.get(id));
    }
}
