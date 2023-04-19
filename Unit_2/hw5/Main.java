import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;
import com.oocourse.elevator1.TimableOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            PersonRequest req = elevatorInput.nextPersonRequest();
            if (req == null) {
                break;
            } else {
                new Thread(() -> dispatcher.addRequest(req)).start();
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
