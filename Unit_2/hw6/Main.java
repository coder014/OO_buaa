import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.MaintainRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;
import com.oocourse.elevator2.TimableOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Dispatcher dispatcher = new Dispatcher();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        List<Thread> threads = new ArrayList<>();
        for (int i = 1;i <= 6;i++) {
            threads.add(new Thread(new Elevator(i, dispatcher)));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        while (true) {
            Request req = elevatorInput.nextRequest();

            if (req == null) {
                break;
            } else if (req instanceof PersonRequest) {
                new Thread(() -> dispatcher.addPersonRequest((PersonRequest)req)).start();
            } else if (req instanceof ElevatorRequest) {
                ElevatorRequest ereq = (ElevatorRequest)req;
                long speed = Math.round(ereq.getSpeed() * 1000.0);
                Thread ele = new Thread(new Elevator(
                        ereq.getElevatorId(),
                        dispatcher,
                        speed, ereq.getCapacity(), ereq.getFloor()));
                threads.add(ele);
                ele.start();
            } else { // req instanceof MaintainRequest
                dispatcher.addMaintainRequest((MaintainRequest)req);
            }
        }
        for (CountDownLatch latch : dispatcher.getMaintainTable().values()) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        dispatcher.setFinish();
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
