public class GroupMessage extends Message {
    private final String at;

    protected GroupMessage(String rawMsg, String date, String sender, String at, String content) {
        super(rawMsg, date, sender, at, content);
        this.at = at;
    }

    public String getAt() {
        return at;
    }
}
