package lib;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Network {
    public static final List<Library> LIBS = new ArrayList<>();
    private static int date = 0;

    public static void addLibrary(String name) {
        LIBS.add(new Library(name));
    }

    public static Library getLibrary(String name) {
        for (Library lib : LIBS) {
            if (lib.getName().equals(name)) {
                return lib;
            }
        }
        return null;
    }

    public static String queryBook(String bookName, String libName) {
        for (Library lib : LIBS) {
            if (!lib.getName().equals(libName)) {
                if (lib.queryBookForeign(bookName)) {
                    return lib.getName();
                }
            }
        }
        return null;
    }

    public static String getDateString() {
        return String.format("%d-%02d-%02d",
                date / 10000,
                (date % 10000) / 100,
                date % 100);
    }

    public static void setDate(String date) {
        String[] tmp = date.split("-");
        setDate(Integer.parseInt(tmp[0]) * 10000
                + Integer.parseInt(tmp[1]) * 100
                + Integer.parseInt(tmp[2]));
    }

    public static void setDate(int date) {
        if (Network.date >= date) {
            return;
        }
        int pre = Network.date == 0 ? 0 : new Calendar.Builder().setDate(
                Network.date / 10000,
                (Network.date % 10000) / 100 - 1,
                Network.date % 100).build().get(Calendar.DAY_OF_YEAR);
        int now = new Calendar.Builder().setDate(
                date / 10000,
                (date % 10000) / 100 - 1,
                date % 100).build().get(Calendar.DAY_OF_YEAR);
        allClosing();
        for (int i = pre + 1; i <= now; i++) {
            Calendar tmp = new Calendar.Builder().setFields(
                    Calendar.YEAR, 2023,
                    Calendar.DAY_OF_YEAR, i).build();
            Network.date = 20230000 + (tmp.get(Calendar.MONTH) + 1) * 100
                    + tmp.get(Calendar.DAY_OF_MONTH);
            allOpening();
            if ((i - 1) % 3 == 0) {
                allArranging();
            }
        }
    }

    public static void allClosing() {
        for (Library lib : LIBS) {
            lib.getOrderLibrarian().dispatchInnerOrder();
        }
        for (Library lib : LIBS) {
            lib.transportOut();
        }
    }

    private static void allArranging() {
        for (Library lib : LIBS) {
            lib.getPurchaseDepartment().buyBooks();
        }
        System.out.printf("[%s] arranging librarian arranged all the books\n", getDateString());
        for (Library lib : LIBS) {
            lib.getArrangeLibrarian().work();
        }
    }

    private static void allOpening() {
        for (Library lib : LIBS) {
            lib.getPurchaseDepartment().checkBooks();
        }
        for (Library lib : LIBS) {
            lib.getPurchaseDepartment().dispatchBooks();
        }
    }
}
