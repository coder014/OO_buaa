public class Lexer {
    private final String raw;
    public static final int S_INIT = 0;
    public static final int S_LLB = 1;
    public static final int S_LMB = 2;
    public static final int S_RLB = 3;
    public static final int S_RMB = 4;
    public static final int S_CONTENT = 5;
    private int status = S_INIT;
    private boolean inStr = false;
    private boolean inNum = false;
    private int ptr = 0;

    public Lexer(String toParse) {
        this.raw = toParse;
    }

    public String next() throws IllegalStateException {
        if (ptr == raw.length()) {
            throw new IllegalStateException("The lexer is already parsed.");
        }
        StringBuilder ret = new StringBuilder();
        boolean toBreak = false;
        while (!toBreak) {
            char c = raw.charAt(ptr);
            if (inStr) {
                if (c == '\"') {
                    inStr = false;
                    status = S_CONTENT;
                    toBreak = true;
                } else { ret.append(c); }
            } else if (inNum) {
                if (c >= '0' && c <= '9') { ret.append(c); }
                else {
                    inNum = false;
                    status = S_CONTENT;
                    break;
                }
            } else if (c >= '0' && c <= '9') {
                inNum = true;
                ret.append(c);
            } else if (c == '\"') {
                inStr = true;
            } else if (c == '{') {
                ret.append(c);
                status = S_LLB;
                toBreak = true;
            } else if (c == '}') {
                ret.append(c);
                status = S_RLB;
                toBreak = true;
            } else if (c == '[') {
                ret.append(c);
                status = S_LMB;
                toBreak = true;
            } else if (c == ']') {
                ret.append(c);
                status = S_RMB;
                toBreak = true;
            } else if (c == 'n') {
                ptr += 4;
                status = S_CONTENT;
                return null;
            } else if (c == 't') {
                ptr += 4;
                status = S_CONTENT;
                return "true";
            } else if (c == 'f') {
                ptr += 5;
                status = S_CONTENT;
                return "false";
            }
            ptr++;
        }
        return ret.toString();
    }

    public boolean hasNext() {
        return ptr < raw.length();
    }

    public int getStatus() {
        return status;
    }
}
