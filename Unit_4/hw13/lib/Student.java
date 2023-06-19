package lib;

import lib.admin.Machine;
import lib.admin.OrderLibrarian;
import lib.admin.RegisterLibrarian;

import java.util.HashMap;
import java.util.Map;

public class Student {
    public static final Map<String, Student> TABLE = new HashMap<>();
    private final String number;
    private Book bookB;
    private final Map<String, Book> bookC = new HashMap<>();

    public Student(String id) {
        this.number = id;
        TABLE.put(id, this);
    }

    public String getNumber() {
        return number;
    }

    public boolean hasBorrowed(String bookName) {
        if (Book.typeOf(bookName) == Book.Type.B && bookB != null) {
            return true;
        } else {
            return bookC.containsKey(bookName);
        }
    }

    public void takeBook(String bookName) {
        Book tmp = new Book(bookName);
        if (tmp.getType() == Book.Type.B) {
            OrderLibrarian.INSTANCE.cancelTypeB(this);
            bookB = tmp;
        } else if (tmp.getType() == Book.Type.C) {
            bookC.put(bookName, tmp);
        }
    }

    public void borrowBook(String bookName) {
        if (Machine.INSTANCE.query(bookName, this)) {
            if (Book.typeOf(bookName) == Book.Type.B) {
                RegisterLibrarian.INSTANCE.borrowTo(bookName, this);
            } else if (Book.typeOf(bookName) == Book.Type.C) {
                Machine.INSTANCE.borrowTo(bookName, this);
            }
        } else {
            OrderLibrarian.INSTANCE.order(bookName, this);
        }
    }

    public void returnBook(String bookName) {
        if (Book.typeOf(bookName) == Book.Type.B && bookB != null
                && bookB.toString().equals(bookName)) {
            RegisterLibrarian.INSTANCE.returnBy(bookB, this);
            bookB = null;
        } else if (Book.typeOf(bookName) == Book.Type.C) {
            Machine.INSTANCE.returnBy(bookC.get(bookName), this);
            bookC.remove(bookName);
        }
    }

    public void smearBook(String bookName) {
        if (Book.typeOf(bookName) == Book.Type.B && bookB != null
                && bookB.toString().equals(bookName)) {
            bookB.setState(Book.State.Smeared);
        } else if (Book.typeOf(bookName) == Book.Type.C) {
            bookC.get(bookName).setState(Book.State.Smeared);
        }
    }

    public void loseBook(String bookName) {
        if (Book.typeOf(bookName) == Book.Type.B && bookB != null
                && bookB.toString().equals(bookName)) {
            bookB = null;
            RegisterLibrarian.INSTANCE.punish(this);
        } else if (Book.typeOf(bookName) == Book.Type.C) {
            bookC.remove(bookName);
            RegisterLibrarian.INSTANCE.punish(this);
        }
    }
}
