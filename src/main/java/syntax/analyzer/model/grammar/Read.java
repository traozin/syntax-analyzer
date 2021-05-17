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
 * @author Antônio Neto e Uellington Damasceno
 */
public class Read {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        T.consumerTokenByLexame(tokens, READ);
        T.consumerTokenByLexame(tokens, OPEN_PARENTHESES);
        expressionReadConsumer(tokens);
        T.consumerTokenByLexame(tokens, CLOSE_PARENTHESES);
    }

    public static void expressionReadConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.STRING);
        try {
            Token token = tokens.peek();
            if (token.thisLexameIs(DOT.getVALUE())) {
                StructDeclaration.structUsageConsumer(tokens);
            } else if (token.thisLexameIs(OPEN_BRACKET.getVALUE())) {
                Arrays.dimensionConsumer(tokens);
            } else if (token.thisLexameIs(COMMA.getVALUE())) {
                moreReadings(tokens);
            } else {
                throw new SyntaxErrorException(token.getLexame(), DOT, OPEN_BRACKET, COMMA);
            }
        } catch (SyntaxErrorException e) {
            if (!e.getSyntaticalError().thisLexameIs(CLOSE_PARENTHESES.getVALUE())) {
                throw e;
            }
        }
    }

    public static void moreReadings(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        T.consumerTokenByLexame(tokens, COMMA);
        expressionReadConsumer(tokens);
    }
}
