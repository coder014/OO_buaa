import java.math.BigInteger;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class Base implements Factor, Comparable<Base> {
    public enum Type {
        VAR_X, VAR_Y, VAR_Z, CONST, SIN, COS
    }

    private static final BigInteger BG_TWO = BigInteger.valueOf(2);

    private BigInteger coff;
    private final BigInteger[] exp = {BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO};
    private final List<Sin> sins = new ArrayList<>();
    private final List<Cos> coss = new ArrayList<>();
    private final Type type;
    private boolean triSimplified = false;

    public Base(BigInteger constNum) {
        this.coff = constNum;
        this.type = Type.CONST;
    }

    public Base(Type type) {
        this.coff = BigInteger.ONE;
        this.type = type;
        if (type == Type.VAR_X) {
            exp[0] = BigInteger.ONE;
        } else if (type == Type.VAR_Y) {
            exp[1] = BigInteger.ONE;
        } else if (type == Type.VAR_Z) {
            exp[2] = BigInteger.ONE;
        }
    }

    public Base(Type type, Factor factor) {
        this.coff = BigInteger.ONE;
        this.type = type;
        if (type == Type.SIN) {
            sins.add(new Sin(factor));
        } else if (type == Type.COS) {
            coss.add(new Cos(factor));
        }
    }

    public void setExp(BigInteger exp) {
        if (type == Type.VAR_X) {
            this.exp[0] = exp;
        } else if (type == Type.VAR_Y) {
            this.exp[1] = exp;
        } else if (type == Type.VAR_Z) {
            this.exp[2] = exp;
        } else if (type == Type.SIN) {
            sins.get(0).setExp(exp);
        } else if (type == Type.COS) {
            coss.get(0).setExp(exp);
        }
    }

    public Map.Entry<Factor, BigInteger> replace(Map<Type, Factor> map) {
        if (type == Type.SIN) {
            for (Sin sin : sins) {
                sin.replace(map);
            }
        } else if (type == Type.COS) {
            for (Cos cos : coss) {
                cos.replace(map);
            }
        } else if (type == Type.CONST) {
            return null;
        }
        BigInteger sum = exp[0].add(exp[1]).add(exp[2]);
        if (sum.equals(BigInteger.ZERO)) {
            return null;
        }
        return new AbstractMap.SimpleImmutableEntry<>(map.get(type), sum);
    }

    public void multi(Base base) {
        this.coff = this.coff.multiply(base.coff);
        this.exp[0] = this.exp[0].add(base.exp[0]);
        this.exp[1] = this.exp[1].add(base.exp[1]);
        this.exp[2] = this.exp[2].add(base.exp[2]);
        this.sins.addAll(base.sins);
        this.coss.addAll(base.coss);
    }

    public static Base multiply(Base a, Base b) {
        Base res = new Base(Type.CONST);
        res.coff = a.coff.multiply(b.coff);
        res.exp[0] = a.exp[0].add(b.exp[0]);
        res.exp[1] = a.exp[1].add(b.exp[1]);
        res.exp[2] = a.exp[2].add(b.exp[2]);
        res.sins.addAll(a.sins);
        res.sins.addAll(b.sins);
        res.coss.addAll(a.coss);
        res.coss.addAll(b.coss);
        return res;
    }

    public BigInteger getCoff() {
        return coff;
    }

    @Override
    public void setNeg() {
        coff = coff.negate();
    }

    public boolean canMerge(Base o) {
        return this.exp[0].equals(o.exp[0])
                && this.exp[1].equals(o.exp[1])
                && this.exp[2].equals(o.exp[2])
                && this.sins.size() == 0 && o.sins.size() == 0
                && this.coss.size() == 0 && o.coss.size() == 0;
    }

    public void merge(Base o) {
        this.coff = this.coff.add(o.coff);
    }

    @Override
    public int compareTo(Base o) {
        int res;
        res = this.exp[0].compareTo(o.exp[0]);
        if (res != 0) {
            return res;
        }
        res = this.exp[1].compareTo(o.exp[1]);
        if (res != 0) {
            return res;
        }
        res = this.exp[2].compareTo(o.exp[2]);
        if (res != 0) {
            return res;
        }
        return Integer.compare(this.sins.size() + this.coss.size(),
                this.sins.size() + this.coss.size());
    }

    public void triSimplify() {
        if (triSimplified) {
            return;
        }
        triSimplified = true;
        Iterator<Sin> its = sins.iterator();
        while (its.hasNext()) {
            Sin sin = its.next();
            Integer res = sin.downToConst();
            if (res == null) {
                continue;
            }
            if (res == 1) {
                its.remove();
            } else if (res == 0) {
                this.coff = BigInteger.ZERO;
                return;
            }
        }
        Iterator<Cos> itc = coss.iterator();
        while (itc.hasNext()) {
            Cos cos = itc.next();
            Integer res = cos.downToConst();
            if (res == null) {
                continue;
            }
            if (res == 1) {
                itc.remove();
            }
        }
    }

    public boolean isSimple() {
        int triSum = sins.size() + coss.size();
        if (exp[0].add(exp[1]).add(exp[2]).equals(BigInteger.ZERO)
                && triSum == 0) {
            return true;
        }
        if (!coff.equals(BigInteger.ONE)) {
            return false;
        }
        if (exp[0].add(exp[1]).add(exp[2]).equals(BigInteger.ZERO)
                && triSum == 1) {
            return true;
        }
        if (triSum == 0 && exp[0].equals(BigInteger.ZERO)
            && exp[1].equals(BigInteger.ZERO)) {
            return true;
        }
        if (triSum == 0 && exp[0].equals(BigInteger.ZERO)
                && exp[2].equals(BigInteger.ZERO)) {
            return true;
        }
        if (triSum == 0 && exp[1].equals(BigInteger.ZERO)
                && exp[2].equals(BigInteger.ZERO)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringJoiner sj;
        BigInteger n;
        if (coff.compareTo(BigInteger.ZERO) >= 0) {
            sj = new StringJoiner("*", "+", "");
            n = coff;
        } else {
            sj = new StringJoiner("*", "-", "");
            n = coff.negate();
        }
        if (!n.equals(BigInteger.ONE)
                || (exp[0].add(exp[1]).add(exp[2]).equals(BigInteger.ZERO)
                    && sins.size() == 0 && coss.size() == 0)) {
            sj.add(n.toString());
        }
        for (int i = 0; i < 3; i++) {
            char c = (char) ('x' + i);
            if (exp[i].equals(BigInteger.ONE)) {
                sj.add(String.valueOf(c));
            } else if (exp[i].equals(BG_TWO)) {
                sj.add(c + "*" + c);
            } else if (!exp[i].equals(BigInteger.ZERO)) {
                sj.add(c + "**" + exp[i]);
            }
        }
        for (Sin sin : sins) {
            sj.add(sin.toString());
        }
        for (Cos cos : coss) {
            sj.add(cos.toString());
        }
        return sj.toString();
    }
}

