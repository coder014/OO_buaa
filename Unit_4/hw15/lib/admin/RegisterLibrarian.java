package lib.admin;

import lib.Library;
import lib.Student;

public class RegisterLibrarian extends LendLibrarian {
    private final Library lib;

    public RegisterLibrarian(Library lib) {
        super("borrowing and returning librarian", lib);
        this.lib = lib;
    }

    public void punish(Student stu) {
        System.out.printf("[%s] %s got punished by %s\n",
                lib.getDateString(),
                stu.getNumber(),
                getName());
        System.out.printf("[%s] borrowing and returning librarian received %s's fine\n",
                lib.getDateString(),
                stu.getNumber());
    }
}
