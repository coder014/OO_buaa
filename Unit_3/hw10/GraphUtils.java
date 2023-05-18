import com.oocourse.spec2.main.Person;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class GraphUtils {
    public static boolean isConnected(MyPerson p1, MyPerson p2) {
        if (p1.equals(p2)) {
            return true;
        }
        Map<Integer, Boolean> visit = new HashMap<>();
        Queue<MyPerson> queue = new LinkedList<>();
        queue.add(p1);
        queue.add(p2);
        visit.put(p1.getId(), false);
        visit.put(p2.getId(), true);
        while (!queue.isEmpty()) {
            MyPerson person = queue.poll();
            boolean bool = visit.get(person.getId());
            for (Person p : person.getAcquaintance()) {
                if (visit.containsKey(p.getId())) {
                    if (!visit.get(p.getId()).equals(bool)) {
                        return true;
                    }
                } else {
                    queue.add((MyPerson) p);
                    visit.put(p.getId(), bool);
                }
            }
        }
        return false;
    }
}
