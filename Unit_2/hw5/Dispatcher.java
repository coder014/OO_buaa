import com.oocourse.elevator1.PersonRequest;

import java.util.HashSet;
import java.util.Set;

public class Dispatcher {
    private final Object notifier = new Object();
    private final Set<PersonRequest> requests = new HashSet<>();
    private boolean finished = false;

    public void addRequest(PersonRequest req) {
        synchronized (requests) {
            requests.add(req);
        }
        synchronized (notifier) {
            notifier.notifyAll();
        }
    }

    /* Remember to use synchronized !! */
    public Set<PersonRequest> getRequests() {
        return requests;
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
