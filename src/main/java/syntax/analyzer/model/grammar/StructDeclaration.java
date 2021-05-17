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

        Token token = tokens.peek();
        if (token.thisLexameIs(OPEN_KEY.getVALUE())) {
            T.consumerTokenByLexame(tokens, OPEN_KEY);
        } else if (token.thisLexameIs(EXTENDS.getVALUE())) {
            T.consumerTokenByLexame(tokens, EXTENDS);
            T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
            T.consumerTokenByLexame(tokens, OPEN_KEY);
        } else {
            throw new SyntaxErrorException(token.getLexame(), OPEN_KEY, EXTENDS);
        }
        structDefConsumer(tokens);
        T.consumerTokenByLexame(tokens, CLOSE_KEY);
        T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        T.consumerTokenByLexame(tokens, SEMICOLON);

    }

    public static void structDefConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        Token token = tokens.peek();
        VarDeclaration.fullChecker(tokens);

        if (token.thisLexameIs(VAR.getVALUE()) || token.thisLexameIs(CONST.getVALUE())) {
            structDefConsumer(tokens);
        }
    }

    public static void structUsageConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        T.consumerTokenByLexame(tokens, DOT);
        T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
    }
}
