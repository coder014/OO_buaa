import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private static final Map<String, FuncDef> GFT = new HashMap<>();
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr res = new Expr();
        if (lexer.peek().equals("-")) {
            lexer.next();
            Term term = parseTerm();
            term.setNeg();
            res.addTerm(term);
        } else if (lexer.peek().equals("+")) {
            lexer.next();
            res.addTerm(parseTerm());
        } else {
            res.addTerm(parseTerm());
        }

        while (lexer.peek().equals("+") || lexer.peek().equals("-")) {
            if (lexer.peek().equals("-")) {
                lexer.next();
                Term term = parseTerm();
                term.setNeg();
                res.addTerm(term);
            } else {
                lexer.next();
                res.addTerm(parseTerm());
            }
        }
        return res;
    }

    private Term parseTerm() {
        Term res = new Term();
        if (lexer.peek().equals("-")) {
            lexer.next();
            Factor factor = parseFactor();
            factor.setNeg();
            res.addFactor(factor);
        } else if (lexer.peek().equals("+")) {
            lexer.next();
            res.addFactor(parseFactor());
        } else {
            res.addFactor(parseFactor());
        }

        while (lexer.peek().equals("*")) {
            lexer.next();
            res.addFactor(parseFactor());
        }
        return res;
    }

    private Factor parseFactor() {
        Factor res;
        if (lexer.peek().equals("(")) {
            lexer.next();
            res = parseExpr();
            lexer.next(); // must be ")", or is ILLEGAL
            if (lexer.peek().equals("**")) {
                res.setExp(consumePower());
            }
            return res;
        }

        boolean needNeg = false;
        if (lexer.peek().equals("-")) {
            lexer.next();
            needNeg = true;
        } else if (lexer.peek().equals("+")) {
            lexer.next();
        }

        if (lexer.peek().equals("x")) {
            res = new Base(Base.Type.VAR_X);
        } else if (lexer.peek().equals("y")) {
            res = new Base(Base.Type.VAR_Y);
        } else if (lexer.peek().equals("z")) {
            res = new Base(Base.Type.VAR_Z);
        } else if (lexer.peek().equals("sin")) {
            lexer.next();
            lexer.next();
            res = new Base(Base.Type.SIN, parseFactor());
        } else if (lexer.peek().equals("cos")) {
            lexer.next();
            lexer.next();
            res = new Base(Base.Type.COS, parseFactor());
        } else if (lexer.peek().equals("f")
                    || lexer.peek().equals("g")
                    || lexer.peek().equals("h")) {
            res = parseFunc();
        } else {
            res = new Base(new BigInteger(lexer.peek()));
        }
        lexer.next();
        if (lexer.peek().equals("**")) {
            res.setExp(consumePower());
        }
        if (needNeg) {
            res.setNeg();
        }
        return res;
    }

    private BigInteger consumePower() {
        lexer.next();
        if (lexer.peek().equals("+")) {
            lexer.next();
        }
        BigInteger res = new BigInteger(lexer.peek());
        lexer.next();
        return res;
    }

    public void parseFuncDef() {
        final String name = lexer.peek();
        List<Character> paramNames = new ArrayList<>();
        lexer.next();
        lexer.next();
        paramNames.add(lexer.peek().charAt(0));
        lexer.next();
        while (lexer.peek().equals(",")) {
            lexer.next();
            paramNames.add(lexer.peek().charAt(0));
            lexer.next();
        }
        lexer.next();
        lexer.next();
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(lexer.peek());
        } while (lexer.next());
        Character[] param = new Character[paramNames.size()];
        GFT.put(name, new FuncDef(sb.toString(), paramNames.toArray(param)));
    }

    private Factor parseFunc() {
        final FuncDef func = GFT.get(lexer.peek());
        lexer.next();
        lexer.next();
        List<Factor> params = new ArrayList<>();
        params.add(parseFactor());
        while (lexer.peek().equals(",")) {
            lexer.next();
            params.add(parseFactor());
        }
        return func.invoke(params);
    }

}
