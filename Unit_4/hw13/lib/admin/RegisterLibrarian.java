package lib.admin;

import lib.Shelf;
import lib.Student;

public class RegisterLibrarian extends LendLibrarian {
    public static final RegisterLibrarian INSTANCE = new RegisterLibrarian();

    private RegisterLibrarian() {
        super("borrowing and returning librarian");
    }

    public void punish(Student stu) {
        System.out.printf("[%s] %s got punished by %s\n",
                Shelf.INSTANCE.getDateString(),
                stu.getNumber(),
                getName());
    }
}
