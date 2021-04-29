package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TerminalsUtil;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class StructDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException {
        TerminalsUtil.consumerTokenByLexame(tokens, TYPEDEF, STRUCT);

        Token token = tokens.peek();
        if (token.thisLexameIs(OPEN_KEY.getVALUE())) {
            TerminalsUtil.consumerTokenByLexame(tokens, OPEN_KEY);
        } else if (token.thisLexameIs(EXTENDS.getVALUE())) {
            TerminalsUtil.consumerTokenByLexame(tokens, EXTENDS);
            TerminalsUtil.consumerTokenByType(tokens, TokenType.IDENTIFIER, IDENTIFIER);
            TerminalsUtil.consumerTokenByLexame(tokens, OPEN_KEY);
        } else {
            throw new SyntaxErrorException(token.getLexame(), OPEN_KEY, EXTENDS);
        }
//        structDefChecker(tokens);
        TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_KEY);
        TerminalsUtil.consumerTokenByType(tokens, TokenType.IDENTIFIER, IDENTIFIER);
        TerminalsUtil.consumerTokenByLexame(tokens, SEMICOLON);
    }

//    public static void structDefChecker(Deque<Token> tokens) {
//        try {
//            VarDeclaration.fullChecker(tokens);
//            structDefChecker(tokens);
//
//        } catch (Exception e) {
//            return;
//        }
//    }
}
