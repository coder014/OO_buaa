package lib.admin;

import helper.StateHelper;
import lib.Book;
import lib.Library;
import lib.Network;
import lib.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class LendLibrarian extends Department {
    private final Library lib;
    private final List<String> books = new ArrayList<>();

    protected LendLibrarian(String name, Library lib) {
        super(name);
        this.lib = lib;
    }

    public boolean borrowTo(String bookName, Student stu) {
        lib.takeBook(bookName);
        String fullName = lib.getName() + "-" + bookName;
        if (stu.hasBorrowed(bookName)) {
            books.add(fullName);
            System.out.printf("[%s] %s refused lending %s to %s\n",
                    lib.getDateString(),
                    getName(),
                    fullName,
                    stu.getNumber());
            StateHelper.withhold(bookName);
            return false;
        } else {
            System.out.printf("[%s] %s lent %s to %s\n",
                    lib.getDateString(),
                    getName(),
                    fullName,
                    stu.getNumber());
            StateHelper.lend(bookName);
            stu.takeBook(fullName);
            System.out.printf("[%s] %s borrowed %s from %s\n",
                    lib.getDateString(),
                    stu.getNumber(),
                    fullName,
                    getName());
            return true;
        }
    }

    public void returnBy(Book book, Student stu) {
        if (book.getState() == Book.State.Smeared) {
            lib.getRegisterLibrarian().punish(stu);
            System.out.printf("[%s] %s returned %s to %s\n",
                    lib.getDateString(),
                    stu.getNumber(),
                    book,
                    getName());
            System.out.printf("[%s] %s collected %s from %s\n",
                    lib.getDateString(),
                    getName(),
                    book,
                    stu.getNumber());
            StateHelper.smear(book.toShortString());
            lib.getLogDivision().fix(book.toString());
        } else if (book.getState() == Book.State.Normal) {
            System.out.printf("[%s] %s returned %s to %s\n",
                    lib.getDateString(),
                    stu.getNumber(),
                    book,
                    getName());
            System.out.printf("[%s] %s collected %s from %s\n",
                    lib.getDateString(),
                    getName(),
                    book,
                    stu.getNumber());
            StateHelper.returnBook(book.toShortString());
            books.add(book.toString());
        }
    }

    protected Collection<String> recycleBooks() {
        Collection<String> res = new ArrayList<>();
        for (String fullName : books) {
            String[] tmp = fullName.split("-");
            res.add(tmp[1] + "-" + tmp[2]);
        }
        books.clear();
        return res;
    }

    public void returnForeignBooks() {
        for (Iterator<String> it = books.iterator(); it.hasNext();) {
            String fullName = it.next();
            String[] tmp = fullName.split("-");
            String owner = tmp[0];
            if (!lib.getName().equals(owner)) {
                it.remove();
                Network.getLibrary(owner).getPurchaseDepartment().acceptBook(fullName);
                System.out.printf("[%s] %s got transported by purchasing department in %s\n",
                        lib.getDateString(),
                        fullName,
                        lib.getName());
                StateHelper.transport(tmp[1] + "-" + tmp[2]);
            }
        }
    }
}
