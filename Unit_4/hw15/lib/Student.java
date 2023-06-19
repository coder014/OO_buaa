package lib;

import java.util.HashMap;
import java.util.Map;

public class Student {
    public static final Map<String, Student> TABLE = new HashMap<>();
    private final String number;
    private final String school;
    private final Library lib;
    private Book bookB;
    private final Map<String, Book> bookC = new HashMap<>();

    public Student(String id) {
        this.number = id;
        this.school = id.split("-")[0];
        this.lib = Network.getLibrary(this.school);
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

    public void takeBook(String fullName) {
        Book tmp = new Book(fullName, Network.getDays());
        if (tmp.getType() == Book.Type.B) {
            lib.getOrderLibrarian().cancelTypeB(this);
            bookB = tmp;
        } else if (tmp.getType() == Book.Type.C) {
            String[] s = fullName.split("-");
            bookC.put(s[1] + "-" + s[2], tmp);
        }
    }

    public void borrowBook(String bookName) {
        if (lib.getMachine().query(bookName, this)) {
            if (Book.typeOf(bookName) == Book.Type.B) {
                lib.getRegisterLibrarian().borrowTo(bookName, this);
            } else if (Book.typeOf(bookName) == Book.Type.C) {
                lib.getMachine().borrowTo(bookName, this);
            }
        } else {
            lib.getOrderLibrarian().order(bookName, this);
        }
    }

    public void returnBook(String bookName) {
        if (Book.typeOf(bookName) == Book.Type.B && bookB != null
                && bookB.toShortString().equals(bookName)) {
            if (Network.getDays() - bookB.getBorrowDay() > 30) {
                lib.getRegisterLibrarian().punish(this);
            }
            lib.getRegisterLibrarian().returnBy(bookB, this);
            bookB = null;
        } else if (Book.typeOf(bookName) == Book.Type.C) {
            if (Network.getDays() - bookC.get(bookName).getBorrowDay() > 60) {
                lib.getRegisterLibrarian().punish(this);
            }
            lib.getMachine().returnBy(bookC.get(bookName), this);
            bookC.remove(bookName);
        }
    }

    public void smearBook(String bookName) {
        if (Book.typeOf(bookName) == Book.Type.B && bookB != null
                && bookB.toShortString().equals(bookName)) {
            bookB.setState(Book.State.Smeared);
        } else if (Book.typeOf(bookName) == Book.Type.C) {
            bookC.get(bookName).setState(Book.State.Smeared);
        }
    }

    public void loseBook(String bookName) {
        if (Book.typeOf(bookName) == Book.Type.B && bookB != null
                && bookB.toShortString().equals(bookName)) {
            bookB = null;
            lib.getRegisterLibrarian().punish(this);
        } else if (Book.typeOf(bookName) == Book.Type.C) {
            bookC.remove(bookName);
            lib.getRegisterLibrarian().punish(this);
        }
    }
}
