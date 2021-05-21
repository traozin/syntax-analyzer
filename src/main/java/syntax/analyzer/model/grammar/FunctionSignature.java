package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.ErrorManager;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TokenUtil;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class FunctionSignature {

    public static void paramsChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        try {
            typedIdentifier(tokens);
            EOFNotExpectedException.throwIfEmpty(tokens, COMMA, CLOSE_PARENTHESES);
            if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
                TokenUtil.consumer(tokens);
                paramsChecker(tokens);
            } else if (TypeDeclaration.primaryChecker(tokens.peek())) {
                ErrorManager.addNewInternalError(tokens, COMMA, CLOSE_PARENTHESES);
                paramsChecker(tokens);
            }
        } catch (SyntaxErrorException e) {
            if (!TokenUtil.testLexameBeforeConsume(tokens, CLOSE_PARENTHESES)) {
                throw e;
            } else {
                ErrorManager.addNewInternalError(tokens, IDENTIFIER);
            }
        }
    }

    /*
    Equivalente as produções:
    Func ID,
    Param
     */
    public static void typedIdentifier(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {

        try {
            TypeDeclaration.typeConsumer(tokens);
            TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        } catch (SyntaxErrorException e) {
            if (TokenUtil.testTypeBeforeConsume(tokens, TokenType.IDENTIFIER, IDENTIFIER)) {
                ErrorManager.addNewInternalError(tokens, INT, REAL, STRING, BOOLEAN);
                TokenUtil.consumer(tokens);
            } else if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
                ErrorManager.addNewInternalError(tokens, IDENTIFIER);
                TokenUtil.consumer(tokens);
                typedIdentifier(tokens);
            } else {
                throw e;
            }
        }
    }

}
