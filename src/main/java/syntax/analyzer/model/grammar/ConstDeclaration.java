package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.TokenUtil;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antônio Neto e Uellington Damasceno
 */
public class ConstDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByLexame(tokens, CONST);
        TokenUtil.consumerByLexame(tokens, OPEN_KEY);
        typedConstConsumer(tokens);
        TokenUtil.consumerByLexame(tokens, CLOSE_KEY);
    }

    public static void typedConstConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TypeDeclaration.typeConsumer(tokens);
        constConsumer(tokens);

        TokenUtil.consumerByLexame(tokens, SEMICOLON);
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
        }
    }

    public static void constDeclarator(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        TokenUtil.consumerByLexame(tokens, EQUALS);
        TypeDeclaration.literalConsumer(tokens);
    }
}
