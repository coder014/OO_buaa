package lib;

import lib.admin.ArrangeLibrarian;
import lib.admin.LogDivision;
import lib.admin.Machine;
import lib.admin.OrderLibrarian;
import lib.admin.PurchaseDepartment;
import lib.admin.RegisterLibrarian;

import java.util.HashMap;
import java.util.Map;

public class Library {
    private final Map<String, Integer> books = new HashMap<>();
    private final Map<String, Boolean> allows = new HashMap<>();
    private final ArrangeLibrarian arrangeLibrarian;
    private final LogDivision logDivision;
    private final Machine machine;
    private final OrderLibrarian orderLibrarian;
    private final RegisterLibrarian registerLibrarian;
    private final PurchaseDepartment purchaseDepartment;
    private final String name;

    public Library(String name) {
        this.name = name;
        this.arrangeLibrarian = new ArrangeLibrarian(this);
        this.logDivision = new LogDivision(this);
        this.machine = new Machine(this);
        this.orderLibrarian = new OrderLibrarian(this);
        this.registerLibrarian = new RegisterLibrarian(this);
        this.purchaseDepartment = new PurchaseDepartment(this);
    }

    public void addBook(String name, int count, boolean allow) {
        if (!books.containsKey(name)) {
            allows.put(name, allow);
        }
        books.put(name, books.getOrDefault(name, 0) + count);
    }

    public boolean takeBook(String name) {
        Integer res = books.get(name);
        if (res == null || res.equals(0)) {
            return false;
        }
        books.put(name, res - 1);
        return true;
    }

    public void transportOut() {
        machine.returnForeignBooks();
        registerLibrarian.returnForeignBooks();
        logDivision.returnForeignBooks();
        orderLibrarian.dispatchOuterOrder();
    }

    public boolean queryBook(String name) {
        return books.containsKey(name) && !books.get(name).equals(0);
    }

    public boolean queryBookForeign(String name) {
        return books.containsKey(name) && !books.get(name).equals(0) && allows.get(name);
    }

    public boolean queryCollection(String name) {
        return books.containsKey(name);
    }

    public String getDateString() {
        return Network.getDateString();
    }

    public ArrangeLibrarian getArrangeLibrarian() {
        return arrangeLibrarian;
    }

    public LogDivision getLogDivision() {
        return logDivision;
    }

    public Machine getMachine() {
        return machine;
    }

    public OrderLibrarian getOrderLibrarian() {
        return orderLibrarian;
    }

    public RegisterLibrarian getRegisterLibrarian() {
        return registerLibrarian;
    }

    public PurchaseDepartment getPurchaseDepartment() {
        return purchaseDepartment;
    }

    public String getName() {
        return name;
    }
}
