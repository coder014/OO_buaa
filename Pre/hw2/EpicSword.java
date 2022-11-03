public class EpicSword extends Sword {
    private final double evolveRatio;

    public EpicSword(int id, String name, long price, double sharpness, double evolveRatio) {
        super(id, name, price, sharpness);
        this.evolveRatio = evolveRatio;
    }

    @Override
    public void usedBy(Adventurer adventurer) {
        super.usedBy(adventurer);
        double afterSharp = getSharpness() * evolveRatio;
        setSharpness(afterSharp);
        System.out.println(getName() + "'s sharpness became " + afterSharp + ".");
    }

    @Override
    public String toString() {
        return "The epicSword's id is " + getId() + ", name is " + getName()
                + ", sharpness is " + getSharpness()
                + ", evolveRatio is " + evolveRatio + ".";
    }
}
