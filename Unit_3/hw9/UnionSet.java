import java.util.HashMap;
import java.util.Map;

public class UnionSet {
    private final Map<Integer, Integer> tree = new HashMap<>();

    public int fa(int x) {
        if (!tree.containsKey(x)) {
            tree.put(x, x);
            return x;
        }
        int p = x;
        while (p != tree.get(p)) {
            p = tree.get(p);
        }
        int q = x;
        int next;
        while (q != p) {
            next = tree.get(q);
            tree.put(q, p);
            q = next;
        }
        return p;
    }

    public void union(int u, int v) {
        int x = fa(u);
        int y = fa(v);
        if (x != y) {
            tree.put(x, y);
        }
    }

    public boolean isConnected(int u, int v) {
        return fa(u) == fa(v);
    }
}
