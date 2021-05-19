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
public class StructDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {

        TokenUtil.consumerByLexame(tokens, TYPEDEF);

        try {
            TokenUtil.consumerByLexame(tokens, STRUCT);
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewInternalError(
                    new SyntaxErrorException(tokens.peek().getLexame(), IDENTIFIER));
        }

        if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_KEY)) {
            TokenUtil.consumer(tokens);
        } else if (TokenUtil.testLexameBeforeConsume(tokens, EXTENDS)) {
            TokenUtil.consumer(tokens);
            TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
            TokenUtil.consumerByLexame(tokens, OPEN_KEY);
        } else {
            ErrorManager.addNewInternalError(new SyntaxErrorException(tokens.peek().getLexame(), OPEN_KEY, EXTENDS));
        }
        structDefConsumer(tokens);
        TokenUtil.consumerByLexame(tokens, CLOSE_KEY);
        try {
            TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewInternalError(
                    new SyntaxErrorException(tokens.peek().getLexame(), IDENTIFIER));
        }
        TokenUtil.consumerByLexame(tokens, SEMICOLON);
    }

    public static void structDefConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        try {
            VarDeclaration.fullChecker(tokens);
        } catch (SyntaxErrorException e) {
            ConstDeclaration.fullChecker(tokens);
        }
        EOFNotExpectedException.throwIfEmpty(tokens, CLOSE_KEY);
        if (TokenUtil.testLexameBeforeConsume(tokens, VAR) || TokenUtil.testLexameBeforeConsume(tokens, CONST)) {
            structDefConsumer(tokens);
        }
    }

    public static void structUsageConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByLexame(tokens, DOT);
        TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
    }
}
