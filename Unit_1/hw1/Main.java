import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String raw = scanner.nextLine();

        Parser parser = new Parser(new Lexer(raw));
        Expr expr = parser.parseExpr();
        Result result = expr.expand();
        System.out.println(result);
    }
}
