import java.util.NoSuchElementException;
import java.util.Scanner;

public class EquipmentFactory {
    public static final int E_BOTTLE = 1;
    public static final int E_HEALINGPOTION = 2;
    public static final int E_EXPBOTTLE = 3;
    public static final int E_SWORD = 4;
    public static final int E_RARESWORD = 5;
    public static final int E_EPICSWORD = 6;

    public static Equipment newEquipment(int equipType, Scanner scanner) {
        int id = scanner.nextInt();
        String name = scanner.next();
        long price = scanner.nextLong();
        double capacity;
        double sharpness;
        switch (equipType) {
            case E_BOTTLE:
                capacity = scanner.nextDouble();
                return new Bottle(id, name, price, capacity);
            case E_HEALINGPOTION:
                capacity = scanner.nextDouble();
                double efficiency = scanner.nextDouble();
                return new HealingPotion(id, name, price, capacity, efficiency);
            case E_EXPBOTTLE:
                capacity = scanner.nextDouble();
                double expRatio = scanner.nextDouble();
                return new ExpBottle(id, name, price, capacity, expRatio);
            case E_SWORD:
                sharpness = scanner.nextDouble();
                return new Sword(id, name, price, sharpness);
            case E_RARESWORD:
                sharpness = scanner.nextDouble();
                double extraExpBonus = scanner.nextDouble();
                return new RareSword(id, name, price, sharpness, extraExpBonus);
            case E_EPICSWORD:
                sharpness = scanner.nextDouble();
                double evolveRatio = scanner.nextDouble();
                return new EpicSword(id, name, price, sharpness, evolveRatio);
            default:
                throw new NoSuchElementException("There's no such equipment type: "
                        + equipType + ".");
        }
    }
}
