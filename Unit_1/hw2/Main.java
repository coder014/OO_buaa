import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String raw = scanner.nextLine();
        int cnt = Integer.parseInt(raw.trim());

        for (int i = 0; i < cnt; i++) {
            raw = scanner.nextLine();
            new Parser(new Lexer(raw)).parseFuncDef();
        }

        raw = scanner.nextLine();
        Expr expr = new Parser(new Lexer(raw)).parseExpr();
        Result result = expr.expand();
        System.out.println(result);
    }
}
