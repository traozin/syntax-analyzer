package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.SyntaxErrorException;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class VarDeclaration {

    private static final String VAR = "var";
    private static final String OPEN_KEY = "{";
    private static final String CLOSE_KEY = "}";
    private static final String CONST = "const";
    private static final String BOOLEAN = "boolean";
    private static final String STRING = "string";
    private static final String INT = "int";
    private static final String REAL = "real";
    private static final String IDENTIFIER = "identifier";

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException {
        if (tokens.isEmpty()) {

        }
        Token first = tokens.pop();
        Token second = tokens.pop();

        if (first.thisLexameIs(VAR)) {
            if (!second.thisLexameIs(OPEN_KEY)) {
            }
            typedVariableChecker(tokens);
        } else if (first.thisLexameIs(CONST)) {
            if (!second.thisLexameIs(OPEN_KEY)) {
            }
        } else {

        }
    }

    public static void typedVariableChecker(Deque<Token> tokens) throws SyntaxErrorException {
        Token token = tokens.pop();
        typeChecker(token);
        variableChecker(tokens);
        token = tokens.pop();
        if (!token.thisLexameIs(";")) {
            throw new SyntaxErrorException(token.getLexame(), ";");
        }
    }

    public static void variableChecker(Deque<Token> tokens) throws SyntaxErrorException {
        variableDeclaratorChecker(tokens);
    }

    public static void variableDeclaratorChecker(Deque<Token> tokens) throws SyntaxErrorException {
        if (tokens.isEmpty()) {
        }
        Token token = tokens.pop();
        Token temp = tokens.peek();

        if (token.getType() != TokenType.IDENTIFIER) {
            throw new SyntaxErrorException(token.getLexame(), IDENTIFIER);
        } else {
            if (temp.thisLexameIs("[")) {

            } else if (temp.thisLexameIs("=")) {

            }
        }
    }

    public static void typeChecker(Token token) throws SyntaxErrorException {
        if (!token.thisLexameIs(BOOLEAN) || !token.thisLexameIs(STRING) || !scalarChecker(token)) {
            throw new SyntaxErrorException(token.getLexame(), BOOLEAN, STRING, INT, REAL);
        }
    }

    public static boolean scalarChecker(Token token) {
        return token.thisLexameIs(REAL) || token.thisLexameIs(INT);
    }
}
