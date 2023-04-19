import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.TimableOutput;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class Elevator implements Runnable {
    enum State {
        IDLE, OPENING, CLOSING, MOVING
    }

    private final Dispatcher dispatcher;
    private final Object notifier;
    private final Set<PersonRequest> requests;
    private final Map<Integer, CountDownLatch> maintainTable;
    private final int id;
    private boolean isMaintaining = false;
    private boolean maintainAcked = false;
    private int floor;
    private int target = 0;
    private final int capacity;
    private final long moveSpeed;
    private final Set<PersonRequest> passengers = new HashSet<>();
    private final Set<PersonRequest> assigned = new HashSet<>();
    private State state = State.IDLE;

    public Elevator(int id, Dispatcher dispatcher) {
        this(id, dispatcher, 400, 6, 1);
    }

    public Elevator(int id, Dispatcher dispatcher, long speed, int capacity, int start) {
        this.id = id;
        this.dispatcher = dispatcher;
        this.notifier = dispatcher.getNotifier();
        this.requests = dispatcher.getRequests();
        this.maintainTable = dispatcher.getMaintainTable();
        this.moveSpeed = speed;
        this.capacity = capacity;
        this.floor = start;
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
            if (!maintainAcked) {
                getAssignment();
                if (isMaintaining) {
                    maintainAcked = true;
                    TimableOutput.println(String.format("MAINTAIN_ABLE-%d", id));
                    maintainTable.get(id).countDown();
                } else if (state != State.IDLE) {
                    return false;
                }
            }
            if (dispatcher.isFinished()) {
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
        TimableOutput.println(String.format("OPEN-%d-%d", floor, id));
        for (Iterator<PersonRequest> it = passengers.iterator();it.hasNext();) {
            PersonRequest req = it.next();
            if (req.getToFloor() == floor) {
                TimableOutput.println(String.format("OUT-%d-%d-%d",
                        req.getPersonId(), floor, id));
                it.remove();
            }
        }
        takeInPassengers();
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        state = State.CLOSING;
    }

    private void closing() {
        getAssignment();
        if (isMaintaining) {
            synchronized (requests) {
                for (Iterator<PersonRequest> it = passengers.iterator(); it.hasNext(); ) {
                    PersonRequest req = it.next();
                    TimableOutput.println(String.format("OUT-%d-%d-%d",
                            req.getPersonId(), floor, id));
                    if (req.getToFloor() != floor) {
                        PersonRequest newReq = new PersonRequest(floor,
                                req.getToFloor(), req.getPersonId());
                        requests.add(newReq);
                    }
                    it.remove();
                }
            }
        } else {
            takeInPassengers();
            updateTarget();
        }
        TimableOutput.println(String.format("CLOSE-%d-%d", floor, id));
    }

    private boolean checkMaintain() {
        if (isMaintaining) {
            state = State.IDLE;
            return true;
        }
        synchronized (maintainTable) {
            isMaintaining = maintainTable.get(id) != null;
        }
        if (isMaintaining) {
            synchronized (requests) {
                requests.addAll(assigned);
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
                for (PersonRequest req : requests) {
                    if (nearest == null ||
                            (Math.abs(req.getFromFloor() - target)
                                    < Math.abs(nearest.getFromFloor() - target))) {
                        nearest = req;
                    }
                }
                if (nearest == null) { // No new requests
                    state = State.IDLE;
                    return;
                } else {
                    state = State.MOVING;
                    target = nearest.getFromFloor();
                    assigned.add(nearest);
                    requests.remove(nearest);
                    if (target != floor) {
                        return;
                    } else {
                        direction = nearest.getToFloor() < floor ? -1 : 1;
                    }
                }
            }
        }
        // Have passengers or assignments
        state = State.MOVING;
        int remain = capacity - assigned.size() - passengers.size();
        synchronized (requests) {
            for (Iterator<PersonRequest> it = requests.iterator(); it.hasNext();) {
                PersonRequest req = it.next();
                if (remain > 0) {
                    if (direction < 0) { // downwards
                        if (req.getFromFloor() <= floor && req.getToFloor() < req.getFromFloor()) {
                            remain -= 1;
                            assigned.add(req);
                            it.remove();
                        }
                    } else { // upwards
                        if (req.getFromFloor() >= floor && req.getToFloor() > req.getFromFloor()) {
                            remain -= 1;
                            assigned.add(req);
                            it.remove();
                        }
                    }
                }
            }
        }
    }

    private int getDirection() {
        int direction = 0;
        for (PersonRequest req : passengers) {
            if (req.getToFloor() < floor) {
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
                target = Math.max(target, req.getToFloor());
            }
            for (PersonRequest req : assigned) {
                target = Math.max(target, req.getFromFloor());
            }
        } else {
            target = 128;
            for (PersonRequest req : passengers) {
                target = Math.min(target, req.getToFloor());
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
}
