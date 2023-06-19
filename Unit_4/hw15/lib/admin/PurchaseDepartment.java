package lib.admin;

import helper.StateHelper;
import lib.Library;
import lib.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PurchaseDepartment extends Department {
    private final Library lib;
    private final List<String> buyBooks = new LinkedList<>();
    private final List<String> books = new LinkedList<>();
    private final List<String> newBooks = new LinkedList<>();

    public PurchaseDepartment(Library lib) {
        super("purchasing department");
        this.lib = lib;
    }

    public void preBuyBook(String bookName) {
        buyBooks.add(bookName);
    }

    public void revokePreBuy(String bookName) {
        buyBooks.remove(bookName);
    }

    public void acceptBook(String fullName) {
        newBooks.add(fullName);
    }

    public void passiveTransport(String bookName, Library toLib, String stuNo) {
        lib.takeBook(bookName);
        String fullName = lib.getName() + "-" + bookName;
        toLib.getPurchaseDepartment().acceptBook(fullName + "-" + stuNo);
        System.out.printf("[%s] %s got transported by purchasing department in %s\n",
                lib.getDateString(),
                fullName,
                lib.getName());
        StateHelper.transport(bookName);
    }

    public void checkBooks() {
        for (Iterator<String> it = newBooks.iterator(); it.hasNext();) {
            String fullName = it.next();
            String[] tmp = fullName.split("-");
            System.out.printf("[%s] %s-%s-%s got received by purchasing department in %s\n",
                    lib.getDateString(),
                    tmp[0], tmp[1], tmp[2],
                    lib.getName());
            StateHelper.receive(tmp[1] + "-" + tmp[2]);
            if (tmp[0].equals(lib.getName())) { // returned
                books.add(tmp[1] + "-" + tmp[2]);
                it.remove();
            }
        }
    }

    public void dispatchBooks() {
        for (String speName : newBooks) {
            String[] tmp = speName.split("-");
            Student stu = Student.TABLE.get(tmp[3] + "-" + tmp[4]);
            String fullName = tmp[0] + "-" + tmp[1] + "-" + tmp[2];
            System.out.printf("[%s] %s lent %s to %s\n",
                    lib.getDateString(),
                    getName(),
                    fullName,
                    stu.getNumber());
            StateHelper.lend(tmp[1] + "-" + tmp[2]);
            stu.takeBook(fullName);
            System.out.printf("[%s] %s borrowed %s from %s\n",
                    lib.getDateString(),
                    stu.getNumber(),
                    fullName,
                    getName());
        }
        newBooks.clear();
    }

    public void buyBooks() {
        List<String> order = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        for (String bookName : buyBooks) {
            if (!order.contains(bookName)) {
                order.add(bookName);
                System.out.printf("[%s] %s-%s got purchased by %s in %s\n",
                        lib.getDateString(),
                        lib.getName(), bookName,
                        getName(),
                        lib.getName());
                lib.addBook(bookName, 0, true);
            }
            map.put(bookName, map.getOrDefault(bookName, 0) + 1);
        }
        buyBooks.clear();
        map.replaceAll((k, v) -> v > 3 ? v : 3);
        for (String bookName : map.keySet()) {
            int n = map.get(bookName);
            for (int i = 0; i < n; i++) {
                books.add(bookName);
            }
        }
    }

    protected Collection<String> recycleBooks() {
        Collection<String> res = new ArrayList<>(books);
        books.clear();
        return res;
    }
}
