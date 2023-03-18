import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Result {
    private List<Base> bases = new ArrayList<>();

    public Result(boolean needOne) {
        if (needOne) {
            bases.add(new Base(BigInteger.ONE));
        }
    }

    public void addResult(Result result) {
        this.bases.addAll(result.bases);
    }

    public void multiBase(Base base) {
        base.triSimplify();
        if (base.getCoff().equals(BigInteger.ZERO)) {
            this.bases.clear();
            return;
        }
        for (Base b : bases) {
            b.multi(base);
        }
    }

    public void multiResult(Result result) {
        List<Base> res = new ArrayList<>();
        for (Base b1 : this.bases) {
            for (Base b2 : result.bases) {
                res.add(Base.multiply(b1, b2));
            }
        }
        this.bases = res;
        simplify();
    }

    public void setNeg() {
        for (Base base : bases) {
            base.setNeg();
        }
    }

    // Simplify level 1
    public void simplify() {
        // Step 1 : merge same power
        Collections.sort(bases);
        for (int i = bases.size() - 1; i > 0; i--) {
            Base pre = bases.get(i - 1);
            Base cur = bases.get(i);
            if (pre.canMerge(cur)) {
                pre.merge(cur);
                bases.remove(i);
            }
        }
        // Step 2 : Remove 0 coff
        for (int i = bases.size() - 1; i >= 0; i--) {
            if (bases.get(i).getCoff().equals(BigInteger.ZERO)) {
                bases.remove(i);
            }
        }
    }

    public boolean isEmpty() {
        return bases.isEmpty();
    }

    public boolean isSimple() {
        int n = bases.size();
        if (n > 1) {
            return false;
        } else if (n == 0) {
            return true;
        }
        return bases.get(0).isSimple();
    }

    @Override // Simplify level 2
    public String toString() {
        if (bases.isEmpty()) {
            return "0";
        }
        int pos = -1; // Positive term prior
        for (int i = 0; i < bases.size(); i++) {
            if (bases.get(i).getCoff().compareTo(BigInteger.ZERO) > 0) {
                pos = i;
                break;
            }
        }
        StringBuilder sb;
        if (pos >= 0) {
            sb = new StringBuilder(bases.get(pos).toString().substring(1));
        } else {
            sb = new StringBuilder();
        }
        for (int i = 0; i < bases.size(); i++) {
            if (i == pos) {
                continue;
            }
            sb.append(bases.get(i));
        }
        return sb.toString();
    }
}
