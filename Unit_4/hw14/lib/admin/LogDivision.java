package lib.admin;

import helper.StateHelper;
import lib.Library;
import lib.Network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class LogDivision extends Department {
    private final Library lib;
    private final List<String> books = new ArrayList<>();

    public LogDivision(Library lib) {
        super("logistics division");
        this.lib = lib;
    }

    protected void fix(String fullName) {
        System.out.printf("[%s] %s got repaired by %s in %s\n",
                lib.getDateString(),
                fullName,
                getName(),
                lib.getName());
        String[] tmp = fullName.split("-");
        StateHelper.fix(tmp[1] + "-" + tmp[2]);
        books.add(fullName);
    }

    protected Collection<String> recycleBooks() {
        Collection<String> res = new ArrayList<>();
        for (String fullName : books) {
            String[] tmp = fullName.split("-");
            res.add(tmp[1] + "-" + tmp[2]);
        }
        books.clear();
        return res;
    }

    public void returnForeignBooks() {
        for (Iterator<String> it = books.iterator(); it.hasNext();) {
            String fullName = it.next();
            String[] tmp = fullName.split("-");
            String owner = tmp[0];
            if (!lib.getName().equals(owner)) {
                it.remove();
                Network.getLibrary(owner).getPurchaseDepartment().acceptBook(fullName);
                System.out.printf("[%s] %s got transported by purchasing department in %s\n",
                        lib.getDateString(),
                        fullName,
                        lib.getName());
                StateHelper.transport(tmp[1] + "-" + tmp[2]);
            }
        }
    }
}
