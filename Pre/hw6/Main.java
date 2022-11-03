import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final Pattern PATTERN_DATE = Pattern.compile(
            "(\\d*)/(\\d*)/(\\d*)");
    private static final int[] DAYS = {31,28,31,30,31,30,31,31,30,31,30,31};

    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        List<Message> msgList = new ArrayList<>();
        readMessage(scanner, msgList);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            String[] args = parseArgs(line);
            String fword;
            if (args[args.length - 2].equals("-c")) {
                fword = args[args.length - 1].substring(1, args[args.length - 1].length() - 1);
            } else {
                fword = null;
            }
            switch (args[0]) {
                case "qdate":
                    Matcher matcher = PATTERN_DATE.matcher(args[1]);
                    matcher.find();
                    if (!checkDate(matcher.group(1), matcher.group(2), matcher.group(3))) {
                        System.out.println("Command Error!: Wrong Date Format! \"" + line + "\"");
                        break;
                    }
                    for (Message msg : msgList) {
                        if (msg.equalsDate(matcher.group(1), matcher.group(2), matcher.group(3))) {
                            filteredPrint(msg, fword);
                        }
                    }
                    break;
                case "qsend":
                case "qrecv":
                    if (args[1].startsWith("-") && !args[1].equals("-v")) {
                        System.out.println("Command Error!: Not Vague Query! \"" + line + "\"");
                    } else if (args[1].startsWith("\"")) {
                        String name = args[1].substring(1, args[1].length() - 1);
                        for (Message msg : msgList) {
                            String messager = args[0].equals("qsend")
                                    ? msg.getSender() : msg.getReceiver();
                            if (messager.equals(name)) {
                                filteredPrint(msg, fword);
                            }
                        }
                    } else {
                        int index = args[2].startsWith("-") ? 3 : 2;
                        String toFind = args[index].substring(1, args[index].length() - 1);
                        for (Message msg : msgList) {
                            String messager = args[0].equals("qsend")
                                    ? msg.getSender() : msg.getReceiver();
                            if (aDumbSwitch(args[2], messager, toFind)) {
                                filteredPrint(msg, fword);
                            }
                        }
                    }
                    break;
                default:
            }
        }
    }

    private static boolean aDumbSwitch(String arg, String messager, String toFind) {
        switch (arg) {
            case "-ssq":
                return ssq(messager, toFind);
            case "-ssr":
                return messager.contains(toFind);
            case "-pre":
                return messager.startsWith(toFind);
            case "-pos":
                return messager.endsWith(toFind);
            default: return messager.contains(toFind);
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

    private static void filteredPrint(Message msg, String filter) {
        if (filter == null) {
            System.out.println(msg);
        } else {
            String stars = String.join("", Collections.nCopies(filter.length(), "*"));
            String newContent = msg.getContent().replace(filter, stars);
            System.out.println(msg.toString().split("\"")[0]
                    + "\"" + newContent + "\";");
        }
    }

    private static boolean checkDate(String sy, String sm, String sd) {
        int y = 2000;
        int m = 1;
        int d = 1;
        if (sy.length() > 0) { y = Integer.parseInt(sy); }
        if (sm.length() > 0) { m = Integer.parseInt(sm); }
        if (sd.length() > 0) { d = Integer.parseInt(sd); }
        int run = ((y % 100 != 0) && (y % 4 == 0) || (y % 400 == 0)) ? 1 : 0;
        if ((m < 1) || (m > 12)) { return false; }
        if (m == 2) {
            return (d >= 1) && (d <= DAYS[m - 1] + run);
        } else {
            return (d >= 1) && (d <= DAYS[m - 1]);
        }
    }

    private static boolean ssq(String s, String t) {
        char[] charS = s.toCharArray();
        int cnt = 0;
        for (char c : charS) {
            if (cnt >= t.length()) { break; }
            if (t.charAt(cnt) == c) { ++cnt; }
        }
        return cnt >= t.length();
    }

    private static String[] parseArgs(String line) {
        String[] tmp = line.split(" (?=[-\"].+)");
        List<String> list = new ArrayList<>();
        if (line.startsWith("qdate")) {
            list.addAll(Arrays.asList(tmp[0].split(" ")));
            list.addAll(Arrays.asList(tmp).subList(1, tmp.length));
            return list.toArray(new String[0]);
        } else {
            return tmp;
        }
    }
}
