public class ExpBottle extends Bottle {
    private final double expRatio;

    public ExpBottle(int id, String name, long price, double capacity, double expRatio) {
        super(id, name, price, capacity);
        this.expRatio = expRatio;
    }

    @Override
    public String toString() {
        return "The expBottle's id is " + getId() + ", name is " + getName()
                + ", capacity is " + getCapacity() + ", filled is " + isFilled()
                + ", expRatio is " + expRatio + ".";
    }

    @Override
    public void usedBy(Adventurer adventurer) throws IllegalStateException {
        super.usedBy(adventurer);
        double afterExp = adventurer.getExp() * expRatio;
        adventurer.setExp(afterExp);
        System.out.println(adventurer.getName() + "'s exp became " + afterExp + ".");
    }
}
