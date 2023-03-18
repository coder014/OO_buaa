import java.math.BigInteger;
import java.util.StringJoiner;

public class Base implements Factor, Comparable<Base> {
    public enum Type {
        VAR_X, VAR_Y, VAR_Z, CONST
    }

    private static final BigInteger BG_TWO = BigInteger.valueOf(2);

    private BigInteger coff;
    private final BigInteger[] exp = {BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO};
    private final Type type;

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

    public void setExp(BigInteger exp) {
        if (type == Type.VAR_X) {
            this.exp[0] = exp;
        } else if (type == Type.VAR_Y) {
            this.exp[1] = exp;
        } else if (type == Type.VAR_Z) {
            this.exp[2] = exp;
        } // Constant calculation not implemented
    }

    public void multi(Base base) {
        this.coff = this.coff.multiply(base.coff);
        this.exp[0] = this.exp[0].add(base.exp[0]);
        this.exp[1] = this.exp[1].add(base.exp[1]);
        this.exp[2] = this.exp[2].add(base.exp[2]);
    }

    public static Base multiply(Base a, Base b) {
        Base res = new Base(Type.CONST);
        res.coff = a.coff.multiply(b.coff);
        res.exp[0] = a.exp[0].add(b.exp[0]);
        res.exp[1] = a.exp[1].add(b.exp[1]);
        res.exp[2] = a.exp[2].add(b.exp[2]);
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
                && this.exp[2].equals(o.exp[2]);
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
        return this.exp[2].compareTo(o.exp[2]);
    }

    @Override
    public String toString() {
        StringJoiner sj;
        BigInteger n;
        if (coff.compareTo(BigInteger.ZERO) > 0) {
            sj = new StringJoiner("*", "+", "");
            n = coff;
        } else {
            sj = new StringJoiner("*", "-", "");
            n = coff.negate();
        }
        if (!n.equals(BigInteger.ONE)
                || exp[0].add(exp[1]).add(exp[2]).equals(BigInteger.ZERO)) {
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
        return sj.toString();
    }
}

