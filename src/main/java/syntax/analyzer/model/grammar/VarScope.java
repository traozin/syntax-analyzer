package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.T;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class VarScope {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        typedVariableScoped(tokens);
        if (T.testLexameBeforeConsume(tokens, EQUALS)) {
            try {
                VarDeclaration.variableDeclaratorConsumer(tokens);
            } catch (SyntaxErrorException e) {
                Token token = tokens.pop();
                Token nextToken = tokens.pop();
                Token nextNextToken = tokens.peek();
                tokens.push(nextToken);
                tokens.push(token);
                if (nextNextToken.thisLexameIs(SEMICOLON.getVALUE())
                        || nextNextToken.thisLexameIs(OPEN_BRACKET.getVALUE())) {
                    T.consumerTokenByLexame(tokens, EQUALS);
                    try {
                        VarScope.scopeModifierConsumer(tokens);
                        if (T.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
                            Arrays.dimensionConsumer(tokens);
                        }
                    } catch (SyntaxErrorException ex) {
                        T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
                        if (T.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
                            Arrays.dimensionConsumer(tokens);
                        }
                    }
                } else {
                    VarUsage.fullChecker(tokens);
                }
            }

        } else {
            VarUsage.fullChecker(tokens);
        }
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

    public static void typedVariableScoped(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        VarScope.scopeModifierConsumer(tokens);
        T.consumerTokenByLexame(tokens, DOT);
        T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
    }
}
