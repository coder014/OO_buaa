package lib.admin;

import lib.Shelf;
import lib.Student;

public class Machine extends LendLibrarian {
    public static final Machine INSTANCE = new Machine();

    private Machine() {
        super("self-service machine");
    }

    public boolean query(String bookName, Student stu) {
        System.out.printf("[%s] %s queried %s from %s\n",
                Shelf.INSTANCE.getDateString(),
                stu.getNumber(),
                bookName,
                getName());
        return Shelf.INSTANCE.queryBook(bookName);
    }
}
