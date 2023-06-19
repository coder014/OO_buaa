package helper;

import lib.Network;

public class StateHelper {
    public static void lend(String bookName) {
        System.out.printf("(State) [%s] %s transfers from Normal to Lent\n",
                Network.getDateString(), bookName);
    }

    public static void transport(String bookName) {
        System.out.printf("(State) [%s] %s transfers from Normal to Transporting\n",
                Network.getDateString(), bookName);
    }

    public static void receive(String bookName) {
        System.out.printf("(State) [%s] %s transfers from Transporting to Normal\n",
                Network.getDateString(), bookName);
    }

    public static void smear(String bookName) {
        System.out.printf("(State) [%s] %s transfers from Lent to Smeared\n",
                Network.getDateString(), bookName);
    }

    public static void fix(String bookName) {
        System.out.printf("(State) [%s] %s transfers from Smeared to Normal\n",
                Network.getDateString(), bookName);
    }

    public static void returnBook(String bookName) {
        System.out.printf("(State) [%s] %s transfers from Lent to Normal\n",
                Network.getDateString(), bookName);
    }

    public static void withhold(String bookName) {
        System.out.printf("(State) [%s] %s transfers from Normal to Normal\n",
                Network.getDateString(), bookName);
    }
}
