package lib.admin;

import lib.Book;
import lib.Shelf;
import lib.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LendLibrarian extends Department {
    private final List<String> books = new ArrayList<>();

    protected LendLibrarian(String name) {
        super(name);
    }

    public boolean borrowTo(String bookName, Student stu) {
        Shelf.INSTANCE.takeBook(bookName);
        if (stu.hasBorrowed(bookName)) {
            books.add(bookName);
            return false;
        } else {
            stu.takeBook(bookName);
            System.out.printf("[%s] %s borrowed %s from %s\n",
                    Shelf.INSTANCE.getDateString(),
                    stu.getNumber(),
                    bookName,
                    getName());
            return true;
        }
    }

    public void returnBy(Book book, Student stu) {
        if (book.getState() == Book.State.Smeared) {
            RegisterLibrarian.INSTANCE.punish(stu);
            System.out.printf("[%s] %s returned %s to %s\n",
                    Shelf.INSTANCE.getDateString(),
                    stu.getNumber(),
                    book,
                    getName());
            LogDivision.INSTANCE.fix(book.toString());
        } else if (book.getState() == Book.State.Normal) {
            System.out.printf("[%s] %s returned %s to %s\n",
                    Shelf.INSTANCE.getDateString(),
                    stu.getNumber(),
                    book,
                    getName());
            books.add(book.toString());
        }
    }

    protected Collection<String> recycleBooks() {
        Collection<String> res = new ArrayList<>(books);
        books.clear();
        return res;
    }
}
