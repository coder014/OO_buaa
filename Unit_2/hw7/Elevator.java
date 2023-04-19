import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.TimableOutput;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Elevator implements Runnable {
    enum State {
        IDLE, OPENING, CLOSING, MOVING
    }

    private final Dispatcher dispatcher = Dispatcher.INSTANCE;
    private final Object notifier;
    private final Set<PersonRequest> requests;
    private final int id;
    private boolean isMaintaining = false;
    private int floor;
    private int target = 0;
    private final int capacity;
    private final long moveSpeed;
    private final int reachableMask;
    private final Set<PersonRequest> passengers = new HashSet<>();
    private final Set<PersonRequest> assigned = new HashSet<>();
    private final Map<Integer, Integer> targetMap = new HashMap<>();
    private boolean takeOnly;
    private State state = State.IDLE;

    public Elevator(int id) {
        this(id, 400, 6, 1, 0x7FF);
    }

    public Elevator(int id, long speed, int capacity, int start, int mask) {
        this.id = id;
        this.notifier = dispatcher.getNotifier();
        this.requests = dispatcher.getRequests();
        this.moveSpeed = speed;
        this.capacity = capacity;
        this.floor = start;
        this.reachableMask = mask;
        GlobalMap.INSTANCE.addPath(id, mask);
    }

    @Override
    public void run() {
        boolean needExit = false;
        while (!needExit) {
            switch (state) {
                case IDLE:
                    needExit = idle();
                    break;
                case MOVING:
                    moving();
                    break;
                case OPENING:
                    opening();
                    break;
                case CLOSING:
                    closing();
                    break;
                default:
                    break;
            }
        }
    }

    private boolean idle() {
        synchronized (notifier) {
            getAssignment();
            if (isMaintaining) {
                TimableOutput.println(String.format("MAINTAIN_ABLE-%d", id));
                return true;
            } else if (state != State.IDLE) {
                return false;
            }
            if (dispatcher.isAllDone() && dispatcher.isFinished()) {
                return true;
            }
            try {
                notifier.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void moving() {
        checkMaintain();
        if (target == floor || isMaintaining) {
            if (passengers.isEmpty() && assigned.isEmpty()) {
                state = State.IDLE;
            } else {
                state = State.OPENING;
            }
        } else {
            try {
                Thread.sleep(moveSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            floor += (target > floor) ? 1 : -1;
            TimableOutput.println(String.format("ARRIVE-%d-%d", floor, id));
        }
    }

    private void opening() {
        takeOnly = (!isMaintaining) || passengers.isEmpty();
        for (PersonRequest req : passengers) {
            if (targetMap.get(req.getPersonId()) == floor) {
                takeOnly = false;
                break;
            }
        }
        dispatcher.servingAcquire(floor);
        if (takeOnly) {
            dispatcher.takeonlyAcquire(floor);
        }
        TimableOutput.println(String.format("OPEN-%d-%d", floor, id));
        synchronized (requests) {
            for (Iterator<PersonRequest> it = passengers.iterator(); it.hasNext(); ) {
                PersonRequest req = it.next();
                if (targetMap.get(req.getPersonId()) == floor) {
                    TimableOutput.println(String.format("OUT-%d-%d-%d",
                            req.getPersonId(), floor, id));
                    if (floor == req.getToFloor()) {
                        dispatcher.checkout(req.getPersonId());
                    } else {
                        PersonRequest newReq = new PersonRequest(floor,
                                req.getToFloor(), req.getPersonId());
                        requests.add(newReq);
                    }
                    it.remove();
                }
            }
        }
        if (isMaintaining) {
            synchronized (requests) {
                for (Iterator<PersonRequest> it = passengers.iterator(); it.hasNext(); ) {
                    PersonRequest req = it.next();
                    TimableOutput.println(String.format("OUT-%d-%d-%d",
                            req.getPersonId(), floor, id));
                    PersonRequest newReq = new PersonRequest(floor,
                            req.getToFloor(), req.getPersonId());
                    requests.add(newReq);
                    it.remove();
                }
            }
        } else {
            takeInPassengers();
        }
        synchronized (notifier) {
            notifier.notifyAll();
        }
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        state = State.CLOSING;
    }

    private void closing() {
        TimableOutput.println(String.format("CLOSE-%d-%d", floor, id));
        if (takeOnly) {
            dispatcher.takeonlyRelease(floor);
        }
        dispatcher.servingRelease(floor);
        getAssignment();
        if (isMaintaining) {
            if (!passengers.isEmpty()) {
                state = State.OPENING; // Re-open the door to avoid restriction
            }
        } else {
            //takeInPassengers();
            updateTarget();
        }
    }

    private boolean checkMaintain() {
        if (isMaintaining) {
            state = State.IDLE;
            return true;
        }
        isMaintaining = dispatcher.needMaintain(id);
        if (isMaintaining) {
            GlobalMap.INSTANCE.delPath(id);
            synchronized (requests) {
                requests.addAll(assigned);
            }
            synchronized (notifier) {
                notifier.notifyAll(); // check again
            }
            assigned.clear();
            state = State.IDLE;
        }
        return isMaintaining;
    }

    private void getAssignment() {
        if (checkMaintain()) {
            return;
        }
        int direction = getDirection();
        // Have no passengers or assignments
        if (direction == 0) {
            PersonRequest nearest = null;
            synchronized (requests) {
                synchronized (GlobalMap.INSTANCE) {
                    for (PersonRequest req : requests) {
                        if (nearest == null ||
                                (Math.abs(req.getFromFloor() - target)
                                        < Math.abs(nearest.getFromFloor() - target))) {
                            if (canReach(req.getFromFloor())
                                    && canReach(GlobalMap.INSTANCE.bestNextHop(req))) {
                                nearest = req;
                            }
                        }
                    }
                    if (nearest == null) { // No new requests
                        state = State.IDLE;
                        return;
                    } else {
                        state = State.MOVING;
                        target = nearest.getFromFloor();
                        assigned.add(nearest);
                        targetMap.put(nearest.getPersonId(),
                                GlobalMap.INSTANCE.bestNextHop(nearest));
                        requests.remove(nearest);
                        if (target != floor) {
                            return;
                        } else {
                            direction = targetMap.get(nearest.getPersonId()) < floor ? -1 : 1;
                        }
                    }
                }
            }
        }
        // Have passengers or assignments
        getAssignment1(direction);
    }

    private void getAssignment1(int direction) {
        state = State.MOVING;
        int remain = capacity - assigned.size() - passengers.size();
        synchronized (requests) {
            synchronized (GlobalMap.INSTANCE) {
                for (Iterator<PersonRequest> it = requests.iterator(); it.hasNext(); ) {
                    PersonRequest req = it.next();
                    int bestNext = GlobalMap.INSTANCE.bestNextHop(req);
                    boolean chosen = false;
                    if (remain > 0) {
                        if (direction < 0) { // downwards
                            if (canReach(req.getFromFloor()) && req.getFromFloor() <= floor
                                    && canReach(bestNext) && bestNext < req.getFromFloor()) {
                                chosen = true;
                            }
                        } else { // upwards
                            if (canReach(req.getFromFloor()) && req.getFromFloor() >= floor
                                    && canReach(bestNext) && bestNext > req.getFromFloor()) {
                                chosen = true;
                            }
                        }
                    } else {
                        break;
                    }
                    if (chosen) {
                        remain -= 1;
                        targetMap.put(req.getPersonId(), bestNext);
                        assigned.add(req);
                        it.remove();
                    }
                }
            }
        }
    }

    private int getDirection() {
        int direction = 0;
        for (PersonRequest req : passengers) {
            if (targetMap.get(req.getPersonId()) < floor) {
                direction = -1;
            } else {
                direction = 1;
            }
            break;
        }
        if (direction == 0) {
            for (PersonRequest req : assigned) {
                if (req.getFromFloor() < floor) {
                    direction = -1;
                } else {
                    direction = 1;
                }
                break;
            }
        }
        return direction;
    }

    private void updateTarget() {
        int direction = getDirection();
        if (direction == 0) {
            return;
        }
        if (direction < 0) {
            target = 0;
            for (PersonRequest req : passengers) {
                target = Math.max(target, targetMap.get(req.getPersonId()));
            }
            for (PersonRequest req : assigned) {
                target = Math.max(target, req.getFromFloor());
            }
        } else {
            target = 128;
            for (PersonRequest req : passengers) {
                target = Math.min(target, targetMap.get(req.getPersonId()));
            }
            for (PersonRequest req : assigned) {
                target = Math.min(target, req.getFromFloor());
            }
        }
    }

    private void takeInPassengers() {
        for (Iterator<PersonRequest> it = assigned.iterator(); it.hasNext();) {
            PersonRequest req = it.next();
            if (req.getFromFloor() == floor) {
                TimableOutput.println(String.format("IN-%d-%d-%d",
                        req.getPersonId(), floor, id));
                passengers.add(req);
                it.remove();
            }
        }
    }

    private boolean canReach(int floor) {
        return (reachableMask & (1 << (floor - 1))) > 0;
    }
}
