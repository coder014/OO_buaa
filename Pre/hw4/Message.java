import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    private final String rawMsg;
    private final String date;
    private final int dateNum;
    private final String sender;
    private final String receiver;
    private final String content;

    private static final Pattern PATTERN_MESSAGE = Pattern.compile(
            "(\\d+/\\d+/\\d+)-(\\w+)(@\\w+ )?:\"([^@]*?(@\\w+ )?[^@]*?)\"");

    protected Message(String rawMsg, String date, String sender, String receiver, String content) {
        this.rawMsg = rawMsg;
        this.date = date;
        this.dateNum = parseDate(date);
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public String getRawMsg() {
        return rawMsg;
    }

    public String getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public int getDateNum() {
        return dateNum;
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
