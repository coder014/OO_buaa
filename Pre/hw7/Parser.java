import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Lexer lexer;

    public Parser(String toParse) {
        this.lexer = new Lexer(toParse);
    }

    public JsonObject parseObject() throws IllegalStateException {
        if (lexer.getStatus() == Lexer.S_INIT) {
            lexer.next();
        }
        if (lexer.getStatus() != Lexer.S_LLB) {
            throw new IllegalStateException("Parse object error!");
        }
        JsonObject ret = new JsonObject();
        while (true) {
            String next = lexer.next();
            if (lexer.getStatus() == Lexer.S_RLB) {
                break;
            } else {
                Attribute attr = parseAttribute(next);
                ret.getAttributes().put(attr.getKey(), attr.getValue());
            }
        }
        return ret;
    }

    private Attribute parseAttribute(String name) throws IllegalStateException {
        String next = lexer.next();
        if (lexer.getStatus() == Lexer.S_LLB) {
            return new Attribute(name, parseObject());
        } else if (lexer.getStatus() == Lexer.S_LMB) {
            return new Attribute(name, parseArray());
        } else if (lexer.getStatus() == Lexer.S_CONTENT) {
            // Special check for retweet_count, favorite_count, reply_count
            // and possibly_sensitive_editable.
            return new Attribute(name, next);
        } else {
            throw new IllegalStateException("Parse attribute error!");
        }
    }

    private List<Object> parseArray() {
        if (lexer.getStatus() != Lexer.S_LMB) {
            throw new IllegalStateException("Parse array error!");
        }
        ArrayList<Object> ret = new ArrayList<>();
        while (true) {
            String next = lexer.next();
            if (lexer.getStatus() == Lexer.S_RMB) {
                break;
            } else if (lexer.getStatus() == Lexer.S_LLB) {
                ret.add(parseObject());
            } else {
                ret.add(next);
            }
        }
        return ret;
    }
}
