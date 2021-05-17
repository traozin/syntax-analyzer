package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TokenUtil;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class ProcedureMain {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByLexame(tokens, PROCEDURE);
        TokenUtil.consumerByLexame(tokens, START);
        StatementDeclaration.fullChecker(tokens);
    }
}
