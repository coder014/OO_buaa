public class RareSword extends Sword {
    private final double extraExpBonus;

    public RareSword(int id, String name, long price, double sharpness, double extraExpBonus) {
        super(id, name, price, sharpness);
        this.extraExpBonus = extraExpBonus;
    }

    @Override
    public void usedBy(Adventurer adventurer) {
        super.usedBy(adventurer);
        adventurer.setExp(adventurer.getExp() + extraExpBonus);
        System.out.println(adventurer.getName() + " got extra exp " + extraExpBonus + ".");
    }

    @Override
    public String toString() {
        return "The rareSword's id is " + getId() + ", name is " + getName()
                + ", sharpness is " + getSharpness()
                + ", extraExpBonus is " + extraExpBonus + ".";
    }
}
