package lib.admin;

import lib.Shelf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ArrangeLibrarian extends Department {
    public static final ArrangeLibrarian INSTANCE = new ArrangeLibrarian();

    private ArrangeLibrarian() {
        super("arranging librarian");
    }

    public void work() {
        Collection<String> books = new ArrayList<>();
        books.addAll(RegisterLibrarian.INSTANCE.recycleBooks());
        books.addAll(Machine.INSTANCE.recycleBooks());
        books.addAll(LogDivision.INSTANCE.recycleBooks());
        Map<String, Integer> map = new HashMap<>();
        for (String book : books) {
            map.put(book, map.getOrDefault(book, 0) + 1);
        }
        OrderLibrarian.INSTANCE.pickAndLend(map);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Shelf.INSTANCE.addBook(entry.getKey(), entry.getValue());
        }
    }
}
