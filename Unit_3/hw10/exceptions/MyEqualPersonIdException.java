package exceptions;

import com.oocourse.spec2.exceptions.EqualPersonIdException;

import java.util.HashMap;
import java.util.Map;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private static final Map<Integer, Integer> COUNT = new HashMap<>();
    private static int countSum = 0;
    private final int id;

    public MyEqualPersonIdException(int id) {
        COUNT.put(id, COUNT.getOrDefault(id, 0) + 1);
        countSum++;
        this.id = id;
    }

    @Override
    public void print() {
        System.out.printf("epi-%d, %d-%d\n", countSum, id, COUNT.get(id));
    }
}
