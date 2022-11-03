import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Map<String, Map<Integer, Adventurer>> version = new HashMap<>();
    private static Map<Integer, Adventurer> adventurers = new HashMap<>();

    public static void main(String []args) {
        version.put("1", adventurers);
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        for (int i = 0; i < m; i++) {
            int op = scanner.nextInt();
            branch(scanner, op);
        }
    }

    private static void branch(Scanner scanner, int op) {
        int advID;
        int comID;
        String name;
        switch (op) {
            case 1: //Add adventurer
                int id = scanner.nextInt();
                name = scanner.next();
                adventurers.put(id, new Adventurer(id, name));
                break;

            case 2: //Add a equipment to the adventurer
                advID = scanner.nextInt();
                int equipType = scanner.nextInt();
                adventurers.get(advID).addCom(EquipmentFactory.newEquipment(equipType, scanner));
                break;

            case 3: //Remove a commodity from the adventurer
                advID = scanner.nextInt();
                comID = scanner.nextInt();
                adventurers.get(advID).removeCom(comID);
                break;

            case 4: //Print the sum price of the adventurer
                advID = scanner.nextInt();
                System.out.println(adventurers.get(advID).getPrice());
                break;

            case 5: //Print the max price of the adventurer
                advID = scanner.nextInt();
                System.out.println(adventurers.get(advID).queryPriceMax());
                break;

            case 6: //Print the coms count of the adventurer
                advID = scanner.nextInt();
                System.out.println(adventurers.get(advID).queryComCount());
                break;

            case 7: //Print the com detail of the adventurer
                advID = scanner.nextInt();
                comID = scanner.nextInt();
                System.out.println(adventurers.get(advID).queryCom(comID));
                break;

            case 8: //Use all commodities of the adventurer
                advID = scanner.nextInt();
                adventurers.get(advID).useAll();
                break;

            case 9: //Print the adventurer's detail
                advID = scanner.nextInt();
                System.out.println(adventurers.get(advID));
                break;

            case 10: //Hire another adventurer
                int adv1ID = scanner.nextInt();
                int adv2ID = scanner.nextInt();
                adventurers.get(adv1ID).addCom(adventurers.get(adv2ID));
                break;
            default:
                branch1(scanner, op);
        }
    }

    private static void branch1(Scanner scanner, int op) {
        String name;
        switch (op) {
            case 11: //New a branch and checkout
                name = scanner.next();
                Map<Integer, Adventurer> tmp = new HashMap<>();
                version.put(name, tmp);
                for (Adventurer adventurer : adventurers.values()) {
                    Adventurer a;
                    try {
                        a = adventurer.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                        return;
                    }
                    tmp.put(a.getId(), a);
                }
                // fix: recursive clone that causes missing reference
                for (Adventurer adventurer : tmp.values()) {
                    Collection<Integer> employees = adventurer.getEmployeeIDs();
                    for (Integer id : employees) {
                        adventurer.removeCom(id);
                        adventurer.addCom(tmp.get(id));
                    }
                }
                adventurers = tmp;
                break;
            case 12: //Checkout a branch
                name = scanner.next();
                adventurers = version.get(name);
                break;
            default:
        }
    }
}