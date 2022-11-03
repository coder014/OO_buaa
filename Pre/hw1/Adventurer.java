import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Adventurer {
    private final int id;
    private final String name;
    private final Map<Integer, Bottle> bottles;

    public Adventurer(int id, String name) {
        this.id = id;
        this.name = name;
        this.bottles = new HashMap<>();
    }

    private Collection<Bottle> getBottles() {
        return bottles.values();
    }

    public void addBottle(int id, String name, long price, double capacity) {
        bottles.put(id, new Bottle(id, name, price, capacity));
    }

    public void removeBottle(int id) {
        bottles.remove(id);
    }

    public Bottle queryBottle(int id) {
        return bottles.get(id);
    }

    public String queryPriceSum() {
        BigInteger ans = BigInteger.ZERO;
        for (Bottle bottle : getBottles()) {
            ans = ans.add(BigInteger.valueOf(bottle.getPrice()));
        }
        return ans.toString();
    }

    public long queryPriceMax() {
        long ans = Long.MIN_VALUE;
        for (Bottle bottle : getBottles()) {
            if (bottle.getPrice() > ans) {
                ans = bottle.getPrice();
            }
        }
        return ans;
    }
}
