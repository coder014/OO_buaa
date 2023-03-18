import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Term {
    private List<Factor> factors = new ArrayList<>();
    private boolean isNeg = false;

    public void setNeg() {
        isNeg = !isNeg;
    }

    public void addFactor(Factor factor) {
        factors.add(factor);
    }

    public void replace(Map<Base.Type, Factor> map) {
        List<Factor> newFactors = new ArrayList<>();
        for (Factor factor : factors) {
            if (factor instanceof Base) {
                Map.Entry<Factor, BigInteger> kv = ((Base) factor).replace(map);
                if (kv != null) {
                    for (BigInteger i = BigInteger.ZERO;
                         i.compareTo(kv.getValue()) < 0;
                         i = i.add(BigInteger.ONE)) {
                        newFactors.add(kv.getKey());
                    }
                } else {
                    newFactors.add(factor);
                }
            } else {
                ((Expr) factor).replace(map);
                newFactors.add(factor);
            }
        }
        factors = newFactors;
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
