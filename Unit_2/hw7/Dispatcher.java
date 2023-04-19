import com.oocourse.elevator3.MaintainRequest;
import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class Dispatcher {
    private final Object notifier = new Object();
    private final Set<PersonRequest> requests = new HashSet<>();
    private final Set<Integer> maintainTable = new HashSet<>();
    private final Set<Integer> checkoutTable = new HashSet<>();
    private final List<Semaphore> servQueue = new ArrayList<>(12);
    private final List<Semaphore> takeQueue = new ArrayList<>(12);
    private boolean finished = false;
    public static final Dispatcher INSTANCE = new Dispatcher();

    private Dispatcher() {
        servQueue.add(null);
        takeQueue.add(null);
        for (int i = 1; i <= 11; i++) {
            servQueue.add(new Semaphore(4, true));
            takeQueue.add(new Semaphore(2, true));
        }
    }

    public void servingAcquire(int floor) {
        try {
            servQueue.get(floor).acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void servingRelease(int floor) {
        servQueue.get(floor).release();
    }

    public void takeonlyAcquire(int floor) {
        try {
            takeQueue.get(floor).acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void takeonlyRelease(int floor) {
        takeQueue.get(floor).release();
    }

    public void addPersonRequest(PersonRequest req) {
        synchronized (checkoutTable) {
            checkoutTable.add(req.getPersonId());
        }
        synchronized (requests) {
            requests.add(req);
        }
        synchronized (notifier) {
            notifier.notifyAll();
        }
    }

    public void addMaintainRequest(MaintainRequest req) {
        synchronized (maintainTable) {
            maintainTable.add(req.getElevatorId());
        }
        synchronized (notifier) {
            notifier.notifyAll();
        }
    }

    /* Remember to use synchronized !! */
    public Set<PersonRequest> getRequests() {
        return requests;
    }

    public void checkout(int id) {
        synchronized (checkoutTable) {
            checkoutTable.remove(id);
        }
    }

    public boolean isAllDone() {
        synchronized (checkoutTable) {
            return checkoutTable.isEmpty();
        }
    }

    public boolean needMaintain(int id) {
        synchronized (maintainTable) {
            return maintainTable.contains(id);
        }
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
