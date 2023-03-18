import java.math.BigInteger;

public class Parser {
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

}
