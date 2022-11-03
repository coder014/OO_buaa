import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String []args) {
        Map<Integer, Adventurer> adventurers = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
        init(scanner, adventurers);

        OperationDispacher.INSTANCE.register(11, () -> { //Print the sum price of the adventurer
            int advID = scanner.nextInt();
            System.out.println(adventurers.get(advID).queryPriceSum());
        });
        OperationDispacher.INSTANCE.register(12, () -> { //Print the max price of the adventurer
            int advID = scanner.nextInt();
            System.out.println(adventurers.get(advID).queryPriceMax());
        });

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

        OperationDispacher.INSTANCE.register(2, () -> { //Add a bottle to the adventurer
            int advID = scanner.nextInt();
            int botID = scanner.nextInt();
            String name = scanner.next();
            long price = scanner.nextLong();
            double capacity = scanner.nextDouble();
            adventurers.get(advID).addBottle(botID, name, price, capacity);
        });

        OperationDispacher.INSTANCE.register(3, () -> { //Remove a bottle from the adventurer
            int advID = scanner.nextInt();
            int botID = scanner.nextInt();
            adventurers.get(advID).removeBottle(botID);
        });

        OperationDispacher.INSTANCE.register(4, () -> { //Modify the price of the bottle
            int advID = scanner.nextInt();
            int botID = scanner.nextInt();
            long price = scanner.nextLong();
            adventurers.get(advID).queryBottle(botID).setPrice(price);
        });

        OperationDispacher.INSTANCE.register(5, () -> { //Modify the fill status of the bottle
            int advID = scanner.nextInt();
            int botID = scanner.nextInt();
            boolean filled = scanner.nextBoolean();
            adventurers.get(advID).queryBottle(botID).setFilled(filled);
        });

        OperationDispacher.INSTANCE.register(6, () -> { //Print the name of the bottle
            int advID = scanner.nextInt();
            int botID = scanner.nextInt();
            System.out.println(adventurers.get(advID).queryBottle(botID).getName());
        });

        OperationDispacher.INSTANCE.register(7, () -> { //Print the price of the bottle
            int advID = scanner.nextInt();
            int botID = scanner.nextInt();
            System.out.println(adventurers.get(advID).queryBottle(botID).getPrice());
        });

        OperationDispacher.INSTANCE.register(8, () -> { //Print the capacity of the bottle
            int advID = scanner.nextInt();
            int botID = scanner.nextInt();
            System.out.println(adventurers.get(advID).queryBottle(botID).getCapacity());
        });

        OperationDispacher.INSTANCE.register(9, () -> { //Print the filled status of the bottle
            int advID = scanner.nextInt();
            int botID = scanner.nextInt();
            System.out.println(adventurers.get(advID).queryBottle(botID).isFilled());
        });

        OperationDispacher.INSTANCE.register(10, () -> { //Print the description of the bottle
            int advID = scanner.nextInt();
            int botID = scanner.nextInt();
            System.out.println(adventurers.get(advID).queryBottle(botID));
        });
    }
}