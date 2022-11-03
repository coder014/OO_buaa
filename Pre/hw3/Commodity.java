import java.math.BigInteger;

public interface Commodity extends Comparable<Commodity>,Cloneable {
    void usedBy(Adventurer adventurer);

    int getId();

    BigInteger getPrice();

    Commodity clone() throws CloneNotSupportedException;
}
