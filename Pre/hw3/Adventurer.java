import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adventurer implements Commodity {
    private final int id;
    private final String name;
    private double health;
    private double exp;
    private double money;
    private Map<Integer, Commodity> coms;

    public Adventurer(int id, String name) {
        this.id = id;
        this.name = name;
        this.health = 100.0;
        this.exp = 0.0;
        this.money = 0.0;
        this.coms = new HashMap<>();
    }

    @Override
    public Adventurer clone() throws CloneNotSupportedException {
        Adventurer clone = (Adventurer)super.clone();
        clone.coms = new HashMap<>();
        for (Commodity com : getComs()) {
            if (com instanceof Equipment) { // fix: recursive clone that causes missing reference
                Commodity c = com.clone();
                clone.coms.put(c.getId(), c);
            } else {
                clone.coms.put(com.getId(), com);
            }
        }
        return clone;
    }

    public Collection<Integer> getEmployeeIDs() {
        List<Integer> res = new ArrayList<>();
        for (Commodity com : getComs()) {
            if (com instanceof Adventurer) {
                res.add(com.getId());
            }
        }
        return res;
    }

    private Collection<Commodity> getComs() {
        return coms.values();
    }

    public void addCom(Commodity com) {
        coms.put(com.getId(), com);
    }

    public void removeCom(int id) {
        coms.remove(id);
    }

    public Commodity queryCom(int id) {
        return coms.get(id);
    }

    public BigInteger getPrice() {
        BigInteger ans = BigInteger.ZERO;
        for (Commodity com : getComs()) {
            ans = ans.add(com.getPrice());
        }
        return ans;
    }

    public BigInteger queryPriceMax() {
        BigInteger ans = BigInteger.ZERO;
        for (Commodity com : getComs()) {
            if (com.getPrice().compareTo(ans) > 0) {
                ans = com.getPrice();
            }
        }
        return ans;
    }

    public int queryComCount() {
        return coms.size();
    }

    @Override
    public String toString() {
        return "The adventurer's id is " + id + ", name is " + name
                + ", health is " + health + ", exp is " + exp
                + ", money is " + money + ".";
    }

    public void useAll() {
        usedBy(this);
    }

    public void usedBy(Adventurer adventurer) {
        List<Commodity> list = new ArrayList<>(coms.values());
        Collections.sort(list);
        for (Commodity com : list) {
            try {
                com.usedBy(adventurer);
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
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
