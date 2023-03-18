import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuncDef {
    private final String rawExpr;
    private final Character[] paramNames;

    public FuncDef(String rawExpr, Character[] paramNames) {
        this.rawExpr = new Parser(new Lexer(rawExpr))
                .parseExpr().expand().toString();
        this.paramNames = paramNames;
    }

    public Factor invoke(List<Factor> params) {
        Expr expr = new Parser(new Lexer(rawExpr)).parseExpr();
        Map<Base.Type, Factor> map = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            Base.Type type;
            if (paramNames[i] == 'x') {
                type = Base.Type.VAR_X;
            } else if (paramNames[i] == 'y') {
                type = Base.Type.VAR_Y;
            } else {
                type = Base.Type.VAR_Z;
            }
            map.put(type, params.get(i));
        }
        expr.replace(map);
        return expr;
    }
}
