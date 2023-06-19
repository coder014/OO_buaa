package lib.admin;

import lib.Book;
import lib.Shelf;
import lib.Student;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class OrderLibrarian extends LendLibrarian {
    public static final OrderLibrarian INSTANCE = new OrderLibrarian();
    private final Map<String, Integer> count = new HashMap<>();
    private final Queue<Record> order = new LinkedList<>();

    private OrderLibrarian() {
        super("ordering librarian");
    }

    public void newDay() {
        count.clear();
    }

    public void cancelTypeB(Student stu) {
        order.removeIf(rec -> rec.stuNo.equals(stu.getNumber())
                && Book.typeOf(rec.bookName) == Book.Type.B);
    }

    public void order(String bookName, Student stu) {
        if (count.getOrDefault(stu.getNumber(), 0) >= 3) {
            return;
        }
        if (!stu.hasBorrowed(bookName)) {
            Record tmp = new Record(stu.getNumber(), bookName);
            for (Record rec : order) {
                if (tmp.equals(rec)) {
                    return;
                }
            }
            System.out.printf("[%s] %s ordered %s from %s\n",
                    Shelf.INSTANCE.getDateString(),
                    stu.getNumber(),
                    bookName,
                    getName());
            order.add(tmp);
            count.put(stu.getNumber(),
                    count.getOrDefault(stu.getNumber(), 0) + 1);
        }
    }

    protected void pickAndLend(Map<String, Integer> books) {
        Queue<Record> toLend = new LinkedList<>();
        Set<String> lendB = new HashSet<>();
        for (Iterator<Record> it = order.iterator(); it.hasNext();) {
            Record rec = it.next();
            if (books.getOrDefault(rec.bookName, 0) > 0) {
                if (Book.typeOf(rec.bookName) == Book.Type.B && !lendB.contains(rec.stuNo)) {
                    toLend.add(rec);
                    lendB.add(rec.stuNo);
                    books.put(rec.bookName, books.get(rec.bookName) - 1);
                } else if (Book.typeOf(rec.bookName) == Book.Type.C) {
                    toLend.add(rec);
                    books.put(rec.bookName, books.get(rec.bookName) - 1);
                    it.remove();
                }
            }
        }
        for (Record rec : toLend) {
            borrowTo(rec.bookName, Student.TABLE.get(rec.stuNo));
        }
    }

    @Override
    public boolean borrowTo(String bookName, Student stu) {
        Shelf.INSTANCE.takeBook(bookName);
        stu.takeBook(bookName);
        System.out.printf("[%s] %s borrowed %s from %s\n",
                Shelf.INSTANCE.getDateString(),
                stu.getNumber(),
                bookName,
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
