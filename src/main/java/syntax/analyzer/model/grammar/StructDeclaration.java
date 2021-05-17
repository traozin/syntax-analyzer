package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.T;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class StructDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {

        T.consumerTokenByLexame(tokens, TYPEDEF, STRUCT);

        if (T.testLexameBeforeConsume(tokens, OPEN_KEY)) {
            T.consumerToken(tokens);
        } else if (T.testLexameBeforeConsume(tokens, EXTENDS)) {
            T.consumerToken(tokens);
            T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
            T.consumerTokenByLexame(tokens, OPEN_KEY);
        } else {
            throw new SyntaxErrorException(tokens.peek().getLexame(), OPEN_KEY, EXTENDS);
        }
        structDefConsumer(tokens);
        T.consumerTokenByLexame(tokens, CLOSE_KEY);
        T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        T.consumerTokenByLexame(tokens, SEMICOLON);

    }

    public static void structDefConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        try {
            VarDeclaration.fullChecker(tokens);
        } catch (SyntaxErrorException e) {
            ConstDeclaration.fullChecker(tokens);
        }
        EOFNotExpectedException.throwIfEmpty(tokens, CLOSE_KEY);
        if (T.testLexameBeforeConsume(tokens, VAR) || T.testLexameBeforeConsume(tokens, CONST)) {
            structDefConsumer(tokens);
        }
    }

    public static void structUsageConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        T.consumerTokenByLexame(tokens, DOT);
        T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
    }
}
