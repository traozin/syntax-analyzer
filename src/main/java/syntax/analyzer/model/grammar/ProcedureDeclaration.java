package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TokenUtil;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class ProcedureDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByLexame(tokens, PROCEDURE);
        
        TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        TokenUtil.consumerByLexame(tokens, OPEN_PARENTHESES);
        try {
            TokenUtil.consumerByLexame(tokens, CLOSE_PARENTHESES);
        } catch (SyntaxErrorException e) {
            FunctionSignature.paramsChecker(tokens);
            TokenUtil.consumerByLexame(tokens, CLOSE_PARENTHESES);
        }
        StatementDeclaration.fullChecker(tokens);
    }

}
