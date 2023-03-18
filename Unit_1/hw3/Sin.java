import java.math.BigInteger;
import java.util.Map;

public class Sin {
    private BigInteger exp = BigInteger.ONE;
    private Factor factor;

    public Sin(Factor factor) {
        this.factor = factor;
    }

    public void setExp(BigInteger exp) {
        this.exp = exp;
    }

    public Integer downToConst() {
        if (exp.equals(BigInteger.ZERO)) {
            return 1;
        }
        if (factor instanceof Base) {
            Base base = (Base)factor;
            base.triSimplify();
            if (base.getCoff().equals(BigInteger.ZERO)) {
                return 0;
            }
        } else {
            Expr expr = (Expr)factor;
            Result res = expr.expand();
            if (res.isEmpty()) {
                return 0;
            }
        }
        return null;
    }

    public void replace(Map<Base.Type, Factor> map) {
        if (factor instanceof Base) {
            Expr expr = new Expr();
            Term term = new Term();
            term.addFactor(factor);
            expr.addTerm(term);
            expr.replace(map);
            factor = expr;
        } else {
            ((Expr)factor).replace(map);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("sin(");
        if (factor instanceof Base) {
            Base base = (Base)factor;
            String s = base.toString();
            if (s.charAt(0) == '+') {
                s = s.substring(1);
            }
            if (base.isSimple()) {
                if (!s.contains("sin") && !s.contains("cos")) {
                    s = s.replace("*x", "**2")
                            .replace("*y", "**2")
                            .replace("*z", "**2");
                }
                sb.append(s);
            } else {
                sb.append('(').append(s).append(')');
            }
            sb.append(")");
        } else if (factor instanceof Expr) {
            Result res = ((Expr)factor).expand();
            String s = res.toString();
            if (res.isSimple()) {
                if (!s.contains("sin") && !s.contains("cos")) {
                    s = s.replace("*x", "**2")
                            .replace("*y", "**2")
                            .replace("*z", "**2");
                }
                sb.append(s);
            } else {
                sb.append('(').append(res).append(')');
            }
            sb.append(")");
        }
        if (exp.compareTo(BigInteger.ONE) > 0) {
            sb.append("**").append(exp);
        }
        return sb.toString();
    }

    public Result getDerivative(Base.Type type) {
        final Result result = new Result(false);
        if (exp.equals(BigInteger.ZERO)) {
            return result;
        }
        Base base = new Base(new Cos(factor));
        result.addBase(base);
        result.multiResult(factor.getDerivative(type));
        if (exp.compareTo(BigInteger.ONE) > 0) {
            Sin sin = new Sin(factor);
            sin.setExp(exp.add(BigInteger.ONE.negate()));
            result.multiBase(new Base(exp));
            result.multiBase(new Base(sin));
        }
        return result;
    }
}
