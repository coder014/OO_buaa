public class Bottle {
    private final int id;
    private final String name;
    private long price;
    private final double capacity;
    private boolean filled;

    public Bottle(int id, String name, long price, double capacity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.capacity = capacity;
        this.filled = true;
    }

    @Override
    public String toString() {
        return "The bottle's id is " + id + ", name is " + name
            + ", capacity is " + capacity + ", filled is " + filled + ".";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public double getCapacity() {
        return capacity;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }
}
