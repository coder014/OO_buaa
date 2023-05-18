package exceptions;

import com.oocourse.spec2.exceptions.EqualRelationException;

import java.util.HashMap;
import java.util.Map;

public class MyEqualRelationException extends EqualRelationException {
    private static final Map<Integer, Integer> COUNT = new HashMap<>();
    private static int countSum = 0;
    private final int id1;
    private final int id2;

    public MyEqualRelationException(int id1, int id2) {
        COUNT.put(id1, COUNT.getOrDefault(id1, 0) + 1);
        countSum++;
        if (id1 != id2) {
            COUNT.put(id2, COUNT.getOrDefault(id2, 0) + 1);
        }
        this.id1 = Integer.min(id1, id2);
        this.id2 = Integer.max(id1, id2);
    }

    @Override
    public void print() {
        System.out.printf("er-%d, %d-%d, %d-%d\n",
                countSum, id1, COUNT.get(id1), id2, COUNT.get(id2));
    }
}
