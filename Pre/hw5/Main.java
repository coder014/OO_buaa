import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final Pattern PATTERN_DATE = Pattern.compile(
            "(\\d*)/(\\d*)/(\\d*)");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Message> msgList = new ArrayList<>();
        readMessage(scanner, msgList);
        while (scanner.hasNext()) {
            String op = scanner.next();
            String arg = scanner.next();
            switch (op) {
                case "qdate":
                    Matcher matcher = PATTERN_DATE.matcher(arg);
                    matcher.find();
                    for (Message msg : msgList) {
                        if (msg.equalsDate(matcher.group(1), matcher.group(2), matcher.group(3))) {
                            System.out.println(msg);
                        }
                    }
                    break;
                case "qsend":
                    if (arg.startsWith("\"")) {
                        arg = arg.substring(1, arg.length() - 1);
                        for (Message msg : msgList) {
                            if (msg.getSender().equals(arg)) {
                                System.out.println(msg);
                            }
                        }
                    } else {
                        arg = scanner.next();
                        arg = arg.substring(1, arg.length() - 1);
                        for (Message msg : msgList) {
                            if (msg.getSender().contains(arg)) {
                                System.out.println(msg);
                            }
                        }
                    }
                    break;
                case "qrecv":
                    if (arg.startsWith("\"")) {
                        arg = arg.substring(1, arg.length() - 1);
                        for (Message msg : msgList) {
                            if (msg.getReceiver().equals(arg)) {
                                System.out.println(msg);
                            }
                        }
                    } else {
                        arg = scanner.next();
                        arg = arg.substring(1, arg.length() - 1);
                        for (Message msg : msgList) {
                            if (msg.getReceiver().contains(arg)) {
                                System.out.println(msg);
                            }
                        }
                    }
                    break;
                default:
            }
        }
    }

    private static void readMessage(Scanner scanner, List<Message> msgList) {
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
    }
}
