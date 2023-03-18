import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Expr implements Factor {
    private BigInteger exp = BigInteger.ONE;
    private boolean isNeg = false;
    private final List<Term> terms = new ArrayList<>();

    public void setNeg() {
        isNeg = !isNeg;
    }

    public void setExp(BigInteger exp) {
        this.exp = exp;
    }

    public void addTerm(Term term) {
        terms.add(term);
    }

    public Result expand() {
        Result res = new Result(false);
        Result res1 = new Result(true);
        if (exp.equals(BigInteger.ZERO)) {
            if (isNeg) {
                res1.setNeg();
            }
            return res1;
        }
        for (Term term : terms) {
            res.addResult(term.expand());
        }
        res.simplify();
        for (BigInteger i = BigInteger.ONE;
                i.compareTo(exp) <= 0;
                i = i.add(BigInteger.ONE)) {
            res1.multiResult(res);
        }
        res = res1;
        if (isNeg) {
            res.setNeg();
        }
        return res;
    }
}
