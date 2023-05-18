import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.main.Person;
import exceptions.MyPathNotFoundException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class GraphUtils {
    private static final Map<Integer, Integer> DIST = new HashMap<>();
    private static final Set<Integer> VIS = new HashSet<>();
    private static final Queue<Node> QUEUE = new PriorityQueue<>(
            Comparator.comparingInt(n -> n.dis));
    private static final Map<Integer, Integer> PATH = new HashMap<>();

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

    public static int queryLeastMoments(Map<Integer, Person> people, Person person) throws
            PathNotFoundException {
        DIST.clear();
        VIS.clear();
        QUEUE.clear();
        PATH.clear();
        DIST.put(person.getId(), 0);
        QUEUE.add(new Node(person.getId(), 0));
        while (!QUEUE.isEmpty()) {
            Node node = QUEUE.poll();
            if (VIS.contains(node.id)) {
                continue;
            }
            VIS.add(node.id);
            for (Map.Entry<Integer, Integer> entry : ((MyPerson) people.get(node.id))
                    .getAcqValues().entrySet()) {
                int w = DIST.get(node.id) + entry.getValue();
                if (!DIST.containsKey(entry.getKey())
                        || DIST.get(entry.getKey()).compareTo(w) > 0) {
                    DIST.put(entry.getKey(), w);
                    QUEUE.add(new Node(entry.getKey(), w));
                    PATH.put(entry.getKey(), node.id);
                }
            }
        }
        Integer ans = null;
        for (Map.Entry<Integer, Integer> entry : ((MyPerson) person).getAcqValues().entrySet()) {
            if (!PATH.get(entry.getKey()).equals(person.getId())) {
                int w = DIST.get(entry.getKey()) + entry.getValue();
                if (ans == null || ans.compareTo(w) > 0) {
                    ans = w;
                }
            }
        }
        for (Map.Entry<Integer, Integer> e1 : DIST.entrySet()) {
            if (e1.getKey() == person.getId()) {
                continue;
            }
            for (Map.Entry<Integer, Integer> e2 : ((MyPerson) people.get(e1.getKey()))
                    .getAcqValues().entrySet()) {
                if (e2.getKey() != person.getId()
                        && find(e1.getKey(), person.getId()) != find(e2.getKey(), person.getId())) {
                    int w = people.get(e1.getKey()).queryValue(people.get(e2.getKey()))
                            + e1.getValue() + DIST.get(e2.getKey());
                    if (ans == null || ans.compareTo(w) > 0) {
                        ans = w;
                    }
                }
            }
        }
        if (ans == null) {
            throw new MyPathNotFoundException(person.getId());
        }
        return ans;
    }

    private static int find(int x, int s) {
        int res = x;
        while (!PATH.get(res).equals(s)) {
            res = PATH.get(res);
        }
        return res;
    }

    static class Node {
        private final int id;
        private final int dis;

        public Node(int id, int dis) {
            this.id = id;
            this.dis = dis;
        }
    }
}
