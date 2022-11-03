import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String []args) {
        Map<Integer, Adventurer> adventurers = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        init(scanner, adventurers);

        int m = scanner.nextInt();
        for (int i = 0; i < m; i++) {
            int op = scanner.nextInt();
            OperationDispacher.INSTANCE.call(op);
        }
    }

    private static void init(Scanner scanner, Map<Integer, Adventurer> adventurers) {
        OperationDispacher.INSTANCE.register(1, () -> { //Add adventurer
            int id = scanner.nextInt();
            String name = scanner.next();
            adventurers.put(id, new Adventurer(id, name));
        });

        OperationDispacher.INSTANCE.register(2, () -> { //Add a equipment to the adventurer
            int advID = scanner.nextInt();
            int equipType = scanner.nextInt();
            adventurers.get(advID).addEquip(EquipmentFactory.newEquipment(equipType, scanner));
        });

        OperationDispacher.INSTANCE.register(3, () -> { //Remove a equipment from the adventurer
            int advID = scanner.nextInt();
            int equipID = scanner.nextInt();
            adventurers.get(advID).removeEquip(equipID);
        });

        OperationDispacher.INSTANCE.register(4, () -> { //Print the sum price of the adventurer
            int advID = scanner.nextInt();
            System.out.println(adventurers.get(advID).queryPriceSum());
        });

        OperationDispacher.INSTANCE.register(5, () -> { //Print the max price of the adventurer
            int advID = scanner.nextInt();
            System.out.println(adventurers.get(advID).queryPriceMax());
        });

        OperationDispacher.INSTANCE.register(6, () -> { //Print the equips count of the adventurer
            int advID = scanner.nextInt();
            System.out.println(adventurers.get(advID).queryEquipCount());
        });

        OperationDispacher.INSTANCE.register(7, () -> { //Print the equip detail of the adventurer
            int advID = scanner.nextInt();
            int equipID = scanner.nextInt();
            System.out.println(adventurers.get(advID).queryEquip(equipID));
        });

        OperationDispacher.INSTANCE.register(8, () -> { //Use the equip of the adventurer
            int advID = scanner.nextInt();
            int equipID = scanner.nextInt();
            Adventurer adventurer = adventurers.get(advID);
            try {
                adventurer.queryEquip(equipID).usedBy(adventurer);
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        });

        OperationDispacher.INSTANCE.register(9, () -> { //Print the adventurer's detail
            int advID = scanner.nextInt();
            System.out.println(adventurers.get(advID));
        });
    }
}