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

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        ifConsumer(tokens);
        elseConsumer(tokens);
    }

    public static void ifConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByLexame(tokens, IF);
        Expressions.fullChecker(tokens);
        try {
            TokenUtil.consumerByLexame(tokens, THEN);
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewInternalError(
                    new SyntaxErrorException(tokens.peek().getLexame(),
                            THEN));
        }
        StatementDeclaration.fullChecker(tokens);
    }

    public static void elseConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        if (TokenUtil.testLexameBeforeConsume(tokens, ELSE)) {
            TokenUtil.consumerByLexame(tokens, ELSE);
            StatementDeclaration.fullChecker(tokens);
        }
    }
}
