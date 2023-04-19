import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalMap {
    public static final GlobalMap INSTANCE = new GlobalMap();
    private final Map<Integer, List<Integer>> elevatorAccess = new HashMap<>();
    private final int[][] dist = new int [11][11];
    private final int[][] next = new int[11][11];

    private GlobalMap() {

    }

    public synchronized void addPath(int eleID, int mask) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            if ((mask & (1 << (i - 1))) > 0) {
                list.add(i);
            }
        }
        elevatorAccess.put(eleID, list);
        updatePath();
    }

    public synchronized void delPath(int eleID) {
        elevatorAccess.remove(eleID);
        updatePath();
    }

    private void updatePath() {
        for (int i = 0; i < 11; i++) {
            Arrays.fill(dist[i], 0x3f3f3f3f);
            Arrays.fill(next[i], -1);
        }
        for (List<Integer> list : elevatorAccess.values()) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                for (int j = i + 1; j < size; j++) {
                    int a = list.get(i) - 1;
                    int b = list.get(j) - 1;
                    dist[a][b] = (b - a) * 10 + 1;
                    dist[b][a] = (b - a) * 10 + 1;
                    next[a][b] = b;
                    next[b][a] = a;
                }
            }
        }
        for (int k = 0; k < 11; k++) {
            for (int i = 0; i < 11; i++) {
                for (int j = 0; j < 11; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
    }

    public synchronized int bestNextHop(PersonRequest req) {
        return next[req.getFromFloor() - 1][req.getToFloor() - 1] + 1;
    }
}
