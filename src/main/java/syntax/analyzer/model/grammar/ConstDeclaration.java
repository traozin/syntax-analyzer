package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.ErrorManager;
import syntax.analyzer.util.TokenUtil;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antônio Neto e Uellington Damasceno
 */
public class ConstDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        TokenUtil.consumeExpectedTokenByLexame(tokens, OPEN_KEY);
        try {
            typedConstConsumer(tokens);
            TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_KEY);
        } catch (SyntaxErrorException e) {
            if (TokenUtil.testLexameBeforeConsume(tokens, CLOSE_KEY)) {
                ErrorManager.addNewInternalError(e);
                TokenUtil.consumer(tokens);
            }
        }
    }

    public static void typedConstConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        try {
            TypeDeclaration.typeConsumer(tokens);
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewInternalError(tokens, INT, REAL, STRING, BOOLEAN);
            throw e;
        }
        try {
            constConsumer(tokens);
            TokenUtil.consumeExpectedTokenByLexame(tokens, SEMICOLON);
        } catch (SyntaxErrorException e) {
            EOFNotExpectedException.throwIfEmpty(tokens, CLOSE_KEY);
            if (TypeDeclaration.typeChecker(tokens.peek())) {
                ErrorManager.addNewInternalError(e);
                typedConstConsumer(tokens);
            } else {
                throw e;
            }
        }
        EOFNotExpectedException.throwIfEmpty(tokens, CLOSE_KEY);
        if (TypeDeclaration.typeChecker(tokens.peek())) {
            typedConstConsumer(tokens);
        }
    }

    public static void constConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        constDeclarator(tokens);
        if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
            TokenUtil.consumer(tokens);
            constConsumer(tokens);
        } else if (TokenUtil.testTypeBeforeConsume(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER)) {
            ErrorManager.addNewInternalError(tokens, COMMA, SEMICOLON);
            constConsumer(tokens);
        }
    }

    public static void constDeclarator(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        TokenUtil.consumeExpectedTokenByLexame(tokens, EQUALS);
        TypeDeclaration.literalConsumer(tokens);
    }
}
