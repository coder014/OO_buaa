import java.math.BigInteger;

public interface Factor {
    void setNeg();

    void setExp(BigInteger exp);

    Result getDerivative(Base.Type type);
}
