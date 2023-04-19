import com.oocourse.elevator2.MaintainRequest;
import com.oocourse.elevator2.PersonRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class Dispatcher {
    private final Object notifier = new Object();
    private final Set<PersonRequest> requests = new HashSet<>();
    private final Map<Integer, CountDownLatch> maintainTable = new HashMap<>();
    private boolean finished = false;

    public void addPersonRequest(PersonRequest req) {
        synchronized (requests) {
            requests.add(req);
        }
        synchronized (notifier) {
            notifier.notifyAll();
        }
    }

    public void addMaintainRequest(MaintainRequest req) {
        synchronized (maintainTable) {
            maintainTable.put(req.getElevatorId(), new CountDownLatch(1));
        }
        synchronized (notifier) {
            notifier.notifyAll();
        }
    }

    /* Remember to use synchronized !! */
    public Set<PersonRequest> getRequests() {
        return requests;
    }

    /* Remember to use synchronized !! */
    public Map<Integer, CountDownLatch> getMaintainTable() {
        return maintainTable;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinish() {
        synchronized (notifier) {
            finished = true;
            notifier.notifyAll();
        }
    }

    public Object getNotifier() {
        return notifier;
    }
}
