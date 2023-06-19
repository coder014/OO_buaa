package lib;

public class Book {
    public enum State {
        Normal, Smeared, Lost
    }

    public enum Type {
        A, B, C
    }

    private final String bookName;
    private final String library;
    private final Type type;
    private State state;

    public Book(String name) {
        String[] s = name.split("-");
        this.bookName = s[1] + "-" + s[2];
        this.library = s[0];
        this.type = typeOf(bookName);
        this.state = State.Normal;
    }

    public State getState() {
        return state;
    }

    public Type getType() {
        return type;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Book)) {
            return false;
        }
        return toString().equals(((Book) obj).toShortString());
    }

    @Override
    public String toString() {
        return library + "-" + bookName;
    }

    public String toShortString() {
        return bookName;
    }

    public static Type typeOf(String bookName) {
        return Type.valueOf(bookName.substring(0, 1));
    }
}
