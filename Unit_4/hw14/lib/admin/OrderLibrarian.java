package lib.admin;

import helper.StateHelper;
import lib.Book;
import lib.Library;
import lib.Network;
import lib.Student;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class OrderLibrarian extends LendLibrarian {
    private final Library lib;
    private final Map<String, Integer> innerCount = new HashMap<>();
    private final Queue<Record> order = new LinkedList<>();
    private final Queue<Record> innerOrder = new LinkedList<>();

    public OrderLibrarian(Library lib) {
        super("ordering librarian", lib);
        this.lib = lib;
    }

    public void cancelTypeB(Student stu) {
        order.removeIf(rec -> rec.stuNo.equals(stu.getNumber())
                && Book.typeOf(rec.bookName) == Book.Type.B);
        for (Iterator<Record> it = innerOrder.iterator(); it.hasNext();) {
            Record rec = it.next();
            if (rec.stuNo.equals(stu.getNumber()) && Book.typeOf(rec.bookName) == Book.Type.B) {
                it.remove();
                lib.getPurchaseDepartment().revokePreBuy(rec.bookName);
            }
        }
    }

    public void order(String bookName, Student stu) {
        Record record = new Record(stu.getNumber(), bookName);
        for (Record rec : order) {
            if (record.equals(rec)) {
                return;
            }
        }
        order.add(record);
    }

    public void dispatchInnerOrder() {
        for (Iterator<Record> it = order.iterator(); it.hasNext();) {
            Record rec = it.next();
            String outerLib = Network.queryBook(rec.bookName, getName());
            if (outerLib == null) {
                innerOrder(rec);
                it.remove();
            }
        }
        innerCount.clear();
    }

    private void innerOrder(Record record) {
        if (innerCount.getOrDefault(record.stuNo, 0) >= 3) {
            return;
        }
        Student stu = Student.TABLE.get(record.stuNo);
        if (!stu.hasBorrowed(record.bookName)) {
            if (!lib.queryCollection(record.bookName)) {
                lib.getPurchaseDepartment().preBuyBook(record.bookName);
            }
            innerOrder.add(record);
            innerCount.put(stu.getNumber(),
                    innerCount.getOrDefault(stu.getNumber(), 0) + 1);
            System.out.printf("[%s] %s ordered %s-%s from %s\n",
                    lib.getDateString(),
                    stu.getNumber(),
                    lib.getName(),
                    record.bookName,
                    getName());
            System.out.printf("[%s] ordering librarian recorded %s's order of %s-%s\n",
                    lib.getDateString(),
                    stu.getNumber(),
                    lib.getName(),
                    record.bookName);
        }
    }

    public void dispatchOuterOrder() {
        Set<String> lendB = new HashSet<>();
        for (Iterator<Record> it = order.iterator(); it.hasNext();) {
            Record rec = it.next();
            Student stu = Student.TABLE.get(rec.stuNo);
            if (!stu.hasBorrowed(rec.bookName)) {
                // Check duplicated order include previous order (B-only)
                Library l = Network.getLibrary(Network.queryBook(rec.bookName, getName()));
                if (Book.typeOf(rec.bookName) == Book.Type.B && !lendB.contains(rec.stuNo)) {
                    lendB.add(rec.stuNo);
                    l.getPurchaseDepartment().passiveTransport(rec.bookName, lib, rec.stuNo);
                } else if (Book.typeOf(rec.bookName) == Book.Type.C) {
                    l.getPurchaseDepartment().passiveTransport(rec.bookName, lib, rec.stuNo);
                }
            }
            it.remove();
        }
    }

    protected void pickAndLend(Map<String, Integer> books) {
        Queue<Record> toLend = new LinkedList<>();
        for (Iterator<Record> it = innerOrder.iterator(); it.hasNext();) {
            Record rec = it.next();
            if (books.getOrDefault(rec.bookName, 0) > 0) {
                toLend.add(rec);
                it.remove();
            }
        }
        for (Record rec : toLend) {
            borrowTo(rec.bookName, Student.TABLE.get(rec.stuNo));
        }
    }

    @Override
    public boolean borrowTo(String bookName, Student stu) {
        if (stu.hasBorrowed(bookName)) {
            return false;
        }
        lib.takeBook(bookName);
        String fullName = lib.getName() + "-" + bookName;
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

    private static class Record {
        private final String stuNo;
        private final String bookName;

        public Record(String stuNo, String bookName) {
            this.stuNo = stuNo;
            this.bookName = bookName;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Record)) {
                return false;
            }
            return stuNo.equals(((Record) obj).stuNo) && bookName.equals(((Record) obj).bookName);
        }
    }
}
