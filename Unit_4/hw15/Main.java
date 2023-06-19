import lib.Library;
import lib.Network;
import lib.Student;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < n; i++) {
            String[] tmp = scanner.nextLine().split(" ");
            Network.addLibrary(tmp[0]);
            Library lib = Network.getLibrary(tmp[0]);
            int t = Integer.parseInt(tmp[1]);
            for (int j = 0; j < t; j++) {
                tmp = scanner.nextLine().split(" ");
                lib.addBook(tmp[0], Integer.parseInt(tmp[1]), tmp[2].equals("Y"));
            }
        }
        int m = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < m; i++) {
            String[] op = scanner.nextLine().split(" ");
            Network.setDate(op[0].substring(1, op[0].length() - 1));
            Student stu = Student.TABLE.get(op[1]);
            if (stu == null) {
                stu = new Student(op[1]);
                Student.TABLE.put(op[1], stu);
            }
            switch (op[2]) {
                case "borrowed":
                    stu.borrowBook(op[3]);
                    break;
                case "returned":
                    stu.returnBook(op[3]);
                    break;
                case "smeared":
                    stu.smearBook(op[3]);
                    break;
                case "lost":
                    stu.loseBook(op[3]);
                    break;
                default: break;
            }
        }
        Network.allClosing();
    }
}
