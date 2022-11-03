public class Bottle extends Equipment {
    private final double capacity;
    private boolean filled;

    public Bottle(int id, String name, long price, double capacity) {
        super(id, name, price);
        this.capacity = capacity;
        this.filled = true;
    }

    @Override
    public String toString() {
        return "The bottle's id is " + getId() + ", name is " + getName()
            + ", capacity is " + capacity + ", filled is " + filled + ".";
    }

    public void usedBy(Adventurer adventurer) throws IllegalStateException {
        if (!filled) {
            throw new IllegalStateException("Failed to use " + getName() + " because it is empty.");
        }
        double addHP = capacity / 10;
        adventurer.setHealth(adventurer.getHealth() + addHP);
        filled = false;
        setPrice(getPriceLong() / 10);
        System.out.println(adventurer.getName() + " drank " + getName()
                + " and recovered " + addHP + ".");
    }

    public final double getCapacity() {
        return capacity;
    }

    public final boolean isFilled() {
        return filled;
    }
}
