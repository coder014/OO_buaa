import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Message> msgList = new ArrayList<>();
        while (true) {
            String line = scanner.nextLine();
            if (line.startsWith("END_OF_MESSAGE")) {
                break;
            }
            line = line.trim();
            String[] msgs = line.split(";");
            for (String msg : msgs) {
                msgList.add(Message.parseRaw(msg));
            }
        }
        while (scanner.hasNext()) {
            String op = scanner.next();
            String arg = scanner.next();
            switch (op) {
                case "qdate":
                    int d = Message.parseDate(arg);
                    for (Message msg : msgList) {
                        if (msg.getDateNum() == d) {
                            System.out.println(msg);
                        }
                    }
                    break;
                case "qsend":
                    arg = arg.substring(1, arg.length() - 1);
                    for (Message msg : msgList) {
                        if (msg.getSender().equals(arg)) {
                            System.out.println(msg);
                        }
                    }
                    break;
                case "qrecv":
                    arg = arg.substring(1, arg.length() - 1);
                    for (Message msg : msgList) {
                        if (msg.getReceiver().equals(arg)) {
                            System.out.println(msg);
                        }
                    }
                    break;
                default:
            }
        }
    }
}
