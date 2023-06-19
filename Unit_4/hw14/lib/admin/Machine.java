package lib.admin;

import lib.Library;
import lib.Student;

public class Machine extends LendLibrarian {
    private final Library lib;

    public Machine(Library lib) {
        super("self-service machine", lib);
        this.lib = lib;
    }

    public boolean query(String bookName, Student stu) {
        System.out.printf("[%s] %s queried %s from %s\n",
                lib.getDateString(),
                stu.getNumber(),
                bookName,
                getName());
        System.out.printf("[%s] self-service machine provided information of %s\n",
                lib.getDateString(),
                bookName);
        return lib.queryBook(bookName);
    }
}
