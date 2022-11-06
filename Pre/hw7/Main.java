import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final List<JsonObject> MSGS = new ArrayList<>();
    private static final Map<String, Integer> DATE2INT = new HashMap<>();

    static {
        DATE2INT.put("Dec", 12);
        DATE2INT.put("Nov", 11);
        DATE2INT.put("Oct", 10);
        DATE2INT.put("Sep", 9);
        DATE2INT.put("Aug", 8);
        DATE2INT.put("Jul", 7);
        DATE2INT.put("Jun", 6);
        DATE2INT.put("May", 5);
        DATE2INT.put("Apr", 4);
        DATE2INT.put("Mar", 3);
        DATE2INT.put("Feb", 2);
        DATE2INT.put("Jan", 1);
    }

    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("END_OF_MESSAGE")) {
                break;
            } else {
                Parser parser = new Parser(line);
                MSGS.add(parser.parseObject());
            }
        }
        while (scanner.hasNextLine()) {
            String[] args = scanner.nextLine().split(" ");
            switch (args[0]) {
                case "Qdate":
                    qdate(args[1], args[2].split("~"));
                    break;
                case "Qemoji":
                    qemoji(args[1]);
                    break;
                case "Qcount":
                    qcount(args[1].split("~"));
                    break;
                case "Qtext":
                    qtext(args[1]);
                    break;
                case "Qsensitive":
                    qsensitive(args[1]);
                    break;
                case "Qlang":
                    qlang(args[1]);
                    break;
                default:
            }
        }
    }

    private static void qdate(String userID, String[] datelr) {
        int n = 0;
        int n1 = 0;
        int n2 = 0;
        int n3 = 0;
        for (JsonObject msg : MSGS) {
            JsonObject rawValue = (JsonObject) msg.getAttributes().get("raw_value");
            String id = (String) rawValue.getAttributes().get("user_id");
            String created = (String) rawValue.getAttributes().get("created_at");
            if (id.equals(userID) && checkDate1(created.split(" "),
                    datelr[0].split("-"), datelr[1].split("-"))) {
                ++n;
                n1 += Integer.parseInt((String) rawValue.getAttributes().get("retweet_count"));
                n2 += Integer.parseInt((String) rawValue.getAttributes().get("favorite_count"));
                n3 += Integer.parseInt((String) rawValue.getAttributes().get("reply_count"));
            }
        }
        System.out.println("" + n + " " + n1 + " " + n2 + " " + n3);
    }

    private static boolean checkDate1(String[] created, String[] datel, String[] dater) {
        int n = Integer.parseInt(created[4]) * 10000
                + DATE2INT.get(created[1]) * 100
                + Integer.parseInt(created[2]);
        int l = Integer.parseInt(datel[0]) * 10000
                + Integer.parseInt(datel[1]) * 100
                + Integer.parseInt(datel[2]);
        int r = Integer.parseInt(dater[0]) * 10000
                + Integer.parseInt(dater[1]) * 100
                + Integer.parseInt(dater[2]);
        return (n >= l) && (n <= r);
    }

    private static void qemoji(String id) {
        List<Object> emojis = null;
        for (JsonObject msg : MSGS) {
            JsonObject rawValue = (JsonObject) msg.getAttributes().get("raw_value");
            String mid = (String) rawValue.getAttributes().get("id");
            if (mid.equals(id)) {
                emojis = (List<Object>) rawValue.getAttributes().get("emojis");
                break;
            }
        }
        if (emojis.size() == 0) {
            System.out.println("None");
        } else {
            emojis.sort((Object o1, Object o2) -> {
                int c1 = Integer.parseInt((String) ((JsonObject) o1).getAttributes().get("count"));
                int c2 = Integer.parseInt((String) ((JsonObject) o2).getAttributes().get("count"));
                if (c1 == c2) {
                    String s1 = (String) ((JsonObject) o1).getAttributes().get("name");
                    String s2 = (String) ((JsonObject) o2).getAttributes().get("name");
                    return s1.compareTo(s2);
                }
                return (c1 > c2) ? -1 : 1;
            });
            for (Object o : emojis) {
                String s = (String) ((JsonObject) o).getAttributes().get("name");
                System.out.print(s + " ");
            }
            System.out.print('\n');
        }
    }

    private static void qcount(String[] datelr) {
        int n = 0;
        for (JsonObject msg : MSGS) {
            JsonObject rawValue = (JsonObject) msg.getAttributes().get("raw_value");
            String time = (String) msg.getAttributes().get("download_datetime");
            if (checkDate2(time.split(" ")[0].split("-"),
                    datelr[0].split("-"), datelr[1].split("-"))) {
                ++n;
            }
        }
        System.out.println(n);
    }

    private static boolean checkDate2(String[] crawled, String[] datel, String[] dater) {
        int n = Integer.parseInt(crawled[0]) * 10000
                + Integer.parseInt(crawled[1]) * 100
                + Integer.parseInt(crawled[2]);
        int l = Integer.parseInt(datel[0]) * 10000
                + Integer.parseInt(datel[1]) * 100
                + Integer.parseInt(datel[2]);
        int r = Integer.parseInt(dater[0]) * 10000
                + Integer.parseInt(dater[1]) * 100
                + Integer.parseInt(dater[2]);
        return (n >= l) && (n <= r);
    }

    private static void qtext(String id) {
        String content = null;
        for (JsonObject msg : MSGS) {
            JsonObject rawValue = (JsonObject) msg.getAttributes().get("raw_value");
            String mid = (String) rawValue.getAttributes().get("id");
            if (mid.equals(id)) {
                content = (String) rawValue.getAttributes().get("full_text");
                break;
            }
        }
        if (content == null) {
            System.out.println("None");
        } else if (content.length() > 0) {
            System.out.println(content);
        }
    }

    private static void qsensitive(String userID) {
        int n = 0;
        for (JsonObject msg : MSGS) {
            JsonObject rawValue = (JsonObject) msg.getAttributes().get("raw_value");
            String id = (String) rawValue.getAttributes().get("user_id");
            boolean sensitive = Boolean.parseBoolean((String) rawValue.getAttributes()
                    .get("possibly_sensitive_editable"));
            if (id.equals(userID) && sensitive) {
                ++n;
            }
        }
        System.out.println(n);
    }

    private static void qlang(String id) {
        String lang = null;
        for (JsonObject msg : MSGS) {
            JsonObject rawValue = (JsonObject) msg.getAttributes().get("raw_value");
            String mid = (String) rawValue.getAttributes().get("id");
            if (mid.equals(id)) {
                lang = (String) rawValue.getAttributes().get("lang");
                break;
            }
        }
        System.out.println(lang);
    }
}
