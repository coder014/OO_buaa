package exceptions;

import com.oocourse.spec3.exceptions.RelationNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static final Map<Integer, Integer> COUNT = new HashMap<>();
    private static int countSum = 0;
    private final int id1;
    private final int id2;

    public MyRelationNotFoundException(int id1, int id2) {
        COUNT.put(id1, COUNT.getOrDefault(id1, 0) + 1);
        COUNT.put(id2, COUNT.getOrDefault(id2, 0) + 1);
        countSum++;
        this.id1 = Integer.min(id1, id2);
        this.id2 = Integer.max(id1, id2);
    }

    @Override
    public void print() {
        System.out.printf("rnf-%d, %d-%d, %d-%d\n",
                countSum, id1, COUNT.get(id1), id2, COUNT.get(id2));
    }
}
