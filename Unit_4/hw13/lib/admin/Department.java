package lib.admin;

public class Department {
    private final String name;

    protected Department(String name) {
        this.name = name;
    }

    protected String getName() {
        return name;
    }
}
