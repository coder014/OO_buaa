import java.util.ArrayList;
import java.util.List;

public class Term {
    private final List<Factor> factors = new ArrayList<>();
    private boolean isNeg = false;

    public void setNeg() {
        isNeg = !isNeg;
    }

    public void addFactor(Factor factor) {
        factors.add(factor);
    }

    public Result expand() {
        Result res = new Result(true);
        for (Factor factor : factors) {
            if (factor instanceof Base) {
                res.multiBase((Base)factor);
            } else { // factor instanceof Expr
                res.multiResult(((Expr)factor).expand());
            }
        }
        if (isNeg) {
            res.setNeg();
        }
        return res;
    }
}
