import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    private final String rawMsg;
    private final int year;
    private final int month;
    private final int day;
    private final String sender;
    private final String receiver;
    private final String content;

    private static final Pattern PATTERN_MESSAGE = Pattern.compile(
            "(\\d+/\\d+/\\d+)-(\\w+)(@\\w+ )?:\"([^@]*?(@\\w+ )?[^@]*?)\"");

    protected Message(String rawMsg, String date, String sender, String receiver, String content) {
        this.rawMsg = rawMsg;
        int dateNum = parseDate(date);
        this.year = dateNum / 10000;
        this.month = (dateNum / 100) % 100;
        this.day = dateNum % 100;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getContent() {
        return content;
    }

    public boolean equalsDate(String y, String m, String d) {
        return (y.length() == 0 || Integer.parseInt(y) == year)
                && (m.length() == 0 || Integer.parseInt(m) == month)
                && (d.length() == 0 || Integer.parseInt(d) == day);
    }

    public static Message parseRaw(String raw) {
        Matcher matcher = PATTERN_MESSAGE.matcher(raw);
        if (!matcher.find()) {
            throw new RuntimeException("Failed to parse Message: " + raw);
        }
        String at = matcher.group(3);
        if (at == null) { //Group Message
            at = matcher.group(5);
            if (at == null) {
                at = "";
            } else {
                at = at.substring(1).trim();
            }
            return new GroupMessage(matcher.group() + ";",
                    matcher.group(1), matcher.group(2), at, matcher.group(4));
        } else { //Personal Message
            at = at.substring(1).trim();
            return new Message(matcher.group() + ";",
                    matcher.group(1), matcher.group(2), at, matcher.group(4));
        }
    }

    public static int parseDate(String d) {
        String[] s1 = d.split("/");
        int n = 0;
        for (String s : s1) {
            n = n * 100 + Integer.parseInt(s);
        }
        return n;
    }

    @Override
    public String toString() {
        return rawMsg;
    }
}
