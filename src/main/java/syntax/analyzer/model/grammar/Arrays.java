package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import static syntax.analyzer.model.grammar.VarDeclaration.varArgsConsumer;
import syntax.analyzer.util.TokenUtil;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Arrays {

    public static void dimensionConsumer(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        TokenUtil.consumerByLexame(tokens, OPEN_BRACKET);
        try {
            TokenUtil.consumerByType(tokens, TokenType.NUMBER, Terminals.INT);
        } catch (SyntaxErrorException ex) {
            try {
                TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
            } catch (SyntaxErrorException e) {
                throw new SyntaxErrorException(tokens.peek().getLexame(), IDENTIFIER, INT);
            }
        }
        TokenUtil.consumerByLexame(tokens, CLOSE_BRACKET);
        if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_BRACKET)){
            dimensionConsumer(tokens);
        }
    }

    public static void initialize(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByLexame(tokens, OPEN_KEY);
        varArgsConsumer(tokens);
        TokenUtil.consumerByLexame(tokens, CLOSE_KEY);
    }
}
