package syntax.analyzer.model.grammar;

import java.util.Deque;
import java.util.List;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TokenUtil;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class TypeDeclaration {

    private static final List<TokenType> tokenTypes = List.of(
            TokenType.IDENTIFIER,
            TokenType.STRING,
            TokenType.NUMBER);

    public static boolean primaryChecker(Token token) {
        return tokenTypes.stream()
                .anyMatch(tokenType -> token.getType().equals(tokenType))
                || token.thisLexameIs(Terminals.TRUE.getVALUE())
                || token.thisLexameIs(Terminals.FALSE.getVALUE());
    }

    public static void primaryConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        EOFNotExpectedException.throwIfEmpty(tokens, TRUE, FALSE, IDENTIFIER, REAL, STRING);
        Token token = tokens.peek();
        if (!primaryChecker(token)) {
            throw new SyntaxErrorException(token.getLexame(), TRUE, FALSE, IDENTIFIER, REAL, STRING);
        }
        TokenUtil.consumer(tokens);
    }

    public static void primaryListConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        primaryConsumer(tokens);
        if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
            TokenUtil.consumer(tokens);
            primaryListConsumer(tokens);
        }
    }

    public static void typeConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        EOFNotExpectedException.throwIfEmpty(tokens, BOOLEAN, IDENTIFIER, REAL, STRING, INT);
        Token token = tokens.peek();
        if (!typeChecker(token)) {
            throw new SyntaxErrorException(token.getLexame(), BOOLEAN, IDENTIFIER, STRING, INT, REAL);
        }
        TokenUtil.consumer(tokens);
    }

    public static boolean typeChecker(Token token) {
        return token.thisLexameIs(BOOLEAN.getVALUE())
                || token.thisLexameIs(Terminals.STRING.getVALUE())
                || scalarChecker(token);
    }

    public static boolean scalarChecker(Token token) {
        return token.thisLexameIs(REAL.getVALUE()) || token.thisLexameIs(INT.getVALUE());
    }

    public static void literalConsumer(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        EOFNotExpectedException.throwIfEmpty(tokens, TRUE, FALSE, REAL, INT, STRING);
        Token token = tokens.peek();
        if (!token.thisLexameIs(TRUE.getVALUE())
                && !token.thisLexameIs(FALSE.getVALUE())
                && token.getType() != TokenType.NUMBER
                && token.getType() != TokenType.STRING) {
            throw new SyntaxErrorException(token.getLexame(), TRUE, FALSE, STRING, INT, REAL);
        }
        TokenUtil.consumer(tokens);
    }

}
