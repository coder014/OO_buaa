import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Adventurer {
    private final int id;
    private final String name;
    private double health;
    private double exp;
    private double money;
    private final Map<Integer, Equipment> equips;

    public Adventurer(int id, String name) {
        this.id = id;
        this.name = name;
        this.health = 100.0;
        this.exp = 0.0;
        this.money = 0.0;
        this.equips = new HashMap<>();
    }

    private Collection<Equipment> getEquips() {
        return equips.values();
    }

    public void addEquip(Equipment equip) {
        equips.put(equip.getId(), equip);
    }

    public void removeEquip(int id) {
        equips.remove(id);
    }

    public Equipment queryEquip(int id) {
        return equips.get(id);
    }

    public BigInteger queryPriceSum() {
        BigInteger ans = BigInteger.ZERO;
        for (Equipment equip : getEquips()) {
            ans = ans.add(BigInteger.valueOf(equip.getPrice()));
        }
        return ans;
    }

    public long queryPriceMax() {
        long ans = 0;
        for (Equipment equip : getEquips()) {
            if (equip.getPrice() > ans) {
                ans = equip.getPrice();
            }
        }
        return ans;
    }

    public int queryEquipCount() {
        return equips.size();
    }

    @Override
    public String toString() {
        return "The adventurer's id is " + id + ", name is " + name
                + ", health is " + health + ", exp is " + exp
                + ", money is " + money + ".";
    }

    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
