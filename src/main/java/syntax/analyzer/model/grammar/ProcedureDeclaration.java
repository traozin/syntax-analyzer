package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TerminalsUtil;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class ProcedureDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException {
        TerminalsUtil.consumerTokenByLexame(tokens, PROCEDURE);
        TerminalsUtil.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        try {
            TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_PARENTHESES);
        } catch (SyntaxErrorException e) {
            FunctionSignature.paramsChecker(tokens);
            TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_PARENTHESES);
        }
        blockProcedureConsumer(tokens);
    }

    public static void blockProcedureConsumer(Deque<Token> tokens) throws SyntaxErrorException {
        TerminalsUtil.consumerTokenByLexame(tokens, OPEN_KEY);
        //TODO: StatmentsList
        TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_KEY);
    }
}
