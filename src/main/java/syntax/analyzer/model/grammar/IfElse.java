package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.ErrorManager;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TokenUtil;

/**
 *
 * @author Antônio Neto e Uellington Damasceno
 */
public class IfElse {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException {
        ifConsumer(tokens);
        elseConsumer(tokens);
    }

    public static void ifConsumer(Deque<Token> tokens) throws EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        try {
            Expressions.fullChecker(tokens);
        } catch (SyntaxErrorException e) {
            ErrorManager.findNext(tokens, THEN);
        }
        TokenUtil.consumeExpectedTokenByLexame(tokens, THEN);
        StatementDeclaration.fullChecker(tokens);
    }

    public static void elseConsumer(Deque<Token> tokens) throws EOFNotExpectedException {
        if (TokenUtil.testLexameBeforeConsume(tokens, ELSE)) {
            TokenUtil.consumer(tokens);
            StatementDeclaration.fullChecker(tokens);
        }
    }
}
