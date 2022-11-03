public class HealingPotion extends Bottle {
    private final double efficiency;

    public HealingPotion(int id, String name, long price, double capacity, double efficiency) {
        super(id, name, price, capacity);
        this.efficiency = efficiency;
    }

    @Override
    public String toString() {
        return "The healingPotion's id is " + getId() + ", name is " + getName()
                + ", capacity is " + getCapacity() + ", filled is " + isFilled()
                + ", efficiency is " + efficiency + ".";
    }

    @Override
    public void usedBy(Adventurer adventurer) throws IllegalStateException {
        super.usedBy(adventurer);
        double addHP = getCapacity() * efficiency;
        adventurer.setHealth(adventurer.getHealth() + addHP);
        System.out.println(adventurer.getName() + " recovered extra " + addHP + ".");
    }
}
