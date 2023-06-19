package lib.admin;

public class Department {
    private final String name;

    protected Department(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
