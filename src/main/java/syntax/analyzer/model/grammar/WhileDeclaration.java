package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TerminalsUtil;

/**
 *
 * @author Antônio Neto e Uellington Damasceno
 */
public class WhileDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {

        TerminalsUtil.consumerTokenByLexame(tokens, WHILE);
        TerminalsUtil.consumerTokenByLexame(tokens, OPEN_PARENTHESES);
        Expressions.fullChecker(tokens);
        TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_PARENTHESES);
        StatementDeclaration.fullChecker(tokens);

    }

}
