import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.MaintainRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;
import com.oocourse.elevator3.TimableOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Dispatcher dispatcher = Dispatcher.INSTANCE;
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        List<Thread> threads = new ArrayList<>();
        for (int i = 1;i <= 6;i++) {
            threads.add(new Thread(new Elevator(i)));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        while (true) {
            Request req = elevatorInput.nextRequest();

            if (req == null) {
                break;
            } else if (req instanceof PersonRequest) {
                dispatcher.addPersonRequest((PersonRequest)req);
            } else if (req instanceof ElevatorRequest) {
                ElevatorRequest ereq = (ElevatorRequest)req;
                long speed = Math.round(ereq.getSpeed() * 1000.0);
                Thread ele = new Thread(new Elevator(
                        ereq.getElevatorId(), speed,
                        ereq.getCapacity(), ereq.getFloor(), ereq.getAccess()));
                threads.add(ele);
                ele.start();
            } else { // req instanceof MaintainRequest
                dispatcher.addMaintainRequest((MaintainRequest)req);
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
