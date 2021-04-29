package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.Terminals;
import syntax.analyzer.util.TerminalsUtil;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class FunctionDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException {
        TerminalsUtil.consumerTokenByLexame(tokens, Terminals.FUNCTION);
        TypeDeclaration.typeConsumer(tokens);
        TerminalsUtil.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        TerminalsUtil.consumerTokenByLexame(tokens, Terminals.OPEN_BRACKET);
        FuctionSignature.paramsChecker(tokens);
        TerminalsUtil.consumerTokenByLexame(tokens, Terminals.CLOSE_BRACKET);
        blockFunctionChecker(tokens);
    }

    public static void blockFunctionChecker(Deque<Token> tokens) throws SyntaxErrorException {
        TerminalsUtil.consumerTokenByLexame(tokens, Terminals.OPEN_KEY);

    }

    public static void returnChecker(Deque<Token> tokens) throws SyntaxErrorException {
        TerminalsUtil.consumerTokenByLexame(tokens, Terminals.RETURN);
        TerminalsUtil.contains(tokens.peek(),
                Terminals.IDENTIFIER,
                Terminals.TRUE,
                Terminals.FALSE,
                Terminals.SEMICOLON);
    }
}
