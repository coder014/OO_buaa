package lib.admin;

import lib.Shelf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LogDivision extends Department {
    protected static final LogDivision INSTANCE = new LogDivision();
    private final List<String> books = new ArrayList<>();

    private LogDivision() {
        super("logistics division");
    }

    protected void fix(String bookName) {
        System.out.printf("[%s] %s got repaired by %s\n",
                Shelf.INSTANCE.getDateString(),
                bookName,
                getName());
        books.add(bookName);
    }

    protected Collection<String> recycleBooks() {
        Collection<String> res = new ArrayList<>(books);
        books.clear();
        return res;
    }
}
