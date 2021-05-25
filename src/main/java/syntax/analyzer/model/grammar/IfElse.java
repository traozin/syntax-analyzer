package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TokenUtil;

/**
 *
 * @author Antônio Neto e Uellington Damasceno
 */
public class IfElse {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        ifConsumer(tokens);
        elseConsumer(tokens);
    }

    public static void ifConsumer(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        TokenUtil.consumer(tokens);
        Expressions.fullChecker(tokens);
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
