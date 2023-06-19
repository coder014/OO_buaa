package lib;

import lib.admin.ArrangeLibrarian;
import lib.admin.OrderLibrarian;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Shelf {
    public static final Shelf INSTANCE = new Shelf();
    private final Map<String, Integer> books = new HashMap<>();
    private int date = 0;

    private Shelf() {}

    public void addBook(String name, int count) {
        if (count <= 0) {
            return;
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

    public boolean queryBook(String name) {
        return books.containsKey(name) && !books.get(name).equals(0);
    }

    public String getDateString() {
        return String.format("%d-%02d-%02d",
                date / 10000,
                (date % 10000) / 100,
                date % 100);
    }

    public void setDate(int date) {
        if (this.date == date) {
            return;
        } else if (this.date == 0) {
            this.date = date;
            return;
        }
        OrderLibrarian.INSTANCE.newDay();
        int pre = new Calendar.Builder().setDate(
                this.date / 10000,
                (this.date % 10000) / 100 - 1,
                this.date % 100).build().get(Calendar.DAY_OF_YEAR);
        int now = new Calendar.Builder().setDate(
                date / 10000,
                (date % 10000) / 100 - 1,
                date % 100).build().get(Calendar.DAY_OF_YEAR);
        for (int i = pre + 1; i <= now; i++) {
            if ((i - 1) % 3 == 0) {
                Calendar tmp = new Calendar.Builder().setFields(
                        Calendar.YEAR, 2023,
                        Calendar.DAY_OF_YEAR, i).build();
                this.date = 20230000 + (tmp.get(Calendar.MONTH) + 1) * 100
                        + tmp.get(Calendar.DAY_OF_MONTH);
                ArrangeLibrarian.INSTANCE.work();
                break;
            }
        }
        this.date = date;
    }

    public void setDate(String date) {
        String[] tmp = date.split("-");
        setDate(Integer.parseInt(tmp[0]) * 10000
                + Integer.parseInt(tmp[1]) * 100
                + Integer.parseInt(tmp[2]));
    }
}
