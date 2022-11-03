public abstract class Equipment {
    private final int id;
    private final String name;
    private long price;

    public Equipment(int id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public final int getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public abstract void usedBy(Adventurer adventurer) throws IllegalStateException;
}
