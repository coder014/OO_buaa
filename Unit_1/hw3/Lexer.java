public class Lexer {
    private final String input;
    private int pos = 0;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        next();
    }

    private String getNumber() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
            sb.append(input.charAt(pos++));
        }
        return sb.toString();
    }

    public boolean next() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
            ++pos;
        }
        if (pos == input.length()) {
            return false;
        }

        if (Character.isDigit(input.charAt(pos))) {
            curToken = getNumber();
        } else if (input.charAt(pos) == '*' && input.charAt(pos + 1) == '*') {
            pos += 2;
            curToken = "**";
        } else {
            if (input.charAt(pos) == 's') {
                curToken = "sin";
                pos += 3;
            } else if (input.charAt(pos) == 'c') {
                curToken = "cos";
                pos += 3;
            } else if (input.charAt(pos) == 'd') {
                curToken = input.substring(pos, pos + 2);
                pos += 2;
            } else {
                curToken = input.substring(pos, pos + 1);
                pos += 1;
            }
        }
        return true;
    }

    public String peek() {
        return this.curToken;
    }

}
