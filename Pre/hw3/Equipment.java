import java.math.BigInteger;

public abstract class Equipment implements Commodity {
    private final int id;
    private final String name;
    private long price;

    public Equipment(int id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public Equipment clone() throws CloneNotSupportedException {
        return (Equipment)super.clone();
    }

    @Override
    public int compareTo(Commodity other) {
        int result = getPrice().compareTo(other.getPrice());
        if (result == 0) {
            return id < other.getId() ? 1 : -1;
        }
        return -result;
    }

    public final int getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final BigInteger getPrice() {
        return BigInteger.valueOf(price);
    }

    public final long getPriceLong() {
        return price;
    }

    public final void setPrice(long price) {
        this.price = price;
    }

    public abstract void usedBy(Adventurer adventurer) throws IllegalStateException;
}
