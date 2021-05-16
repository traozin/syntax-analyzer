package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.T;
import static syntax.analyzer.util.Terminals.DOT;
import static syntax.analyzer.util.Terminals.GLOBAL;
import static syntax.analyzer.util.Terminals.LOCAL;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class VariableScope {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        scopeModifierConsumer(tokens);
        T.consumerTokenByLexame(tokens, DOT);

    }
    
    public static void scopeModifierConsumer(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        try {
            T.consumerTokenByLexame(tokens, GLOBAL);
        } catch (SyntaxErrorException e) {
            try {
                T.consumerTokenByLexame(tokens, LOCAL);
            } catch (SyntaxErrorException e1) {
                throw new SyntaxErrorException(tokens.peek().getLexame(), GLOBAL, LOCAL);
            }
        }
    }
}
