package lib.admin;

import lib.Library;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ArrangeLibrarian extends Department {
    private final Library lib;

    public ArrangeLibrarian(Library lib) {
        super("arranging librarian");
        this.lib = lib;
    }

    public void work() {
        Collection<String> books = new ArrayList<>();
        books.addAll(lib.getRegisterLibrarian().recycleBooks());
        books.addAll(lib.getMachine().recycleBooks());
        books.addAll(lib.getLogDivision().recycleBooks());
        books.addAll(lib.getPurchaseDepartment().recycleBooks());
        Map<String, Integer> map = new HashMap<>();
        for (String book : books) {
            map.put(book, map.getOrDefault(book, 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            lib.addBook(entry.getKey(), entry.getValue(), true);
        }
        lib.getOrderLibrarian().pickAndLend(map);
    }
}
