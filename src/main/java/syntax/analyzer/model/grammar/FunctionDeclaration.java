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
public class FunctionDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByLexame(tokens, FUNCTION);
        TypeDeclaration.typeConsumer(tokens);
        TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        TokenUtil.consumerByLexame(tokens, OPEN_PARENTHESES);
        try {
            TokenUtil.consumerByLexame(tokens, CLOSE_PARENTHESES);
        } catch (SyntaxErrorException e) {
            FunctionSignature.paramsChecker(tokens);
            TokenUtil.consumerByLexame(tokens, CLOSE_PARENTHESES);
        }
        blockFunctionChecker(tokens);
    }

    public static void blockFunctionChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByLexame(tokens, OPEN_KEY);
        StatementDeclaration.statementListChecker(tokens);
        TokenUtil.consumerByLexame(tokens, CLOSE_KEY);
    }

    public static void returnChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByLexame(tokens, Terminals.RETURN);
        Token token = tokens.pop();
        Token nextToken = tokens.peek();
        tokens.push(token);

        boolean isEmptyReturn = TokenUtil.contains(token, Terminals.SEMICOLON);
        boolean isPrimaryReturn = TypeDeclaration.primaryChecker(token);

        if (!isEmptyReturn && !isPrimaryReturn) {
            throw new SyntaxErrorException(token.getLexame(),
                    SEMICOLON,
                    IDENTIFIER,
                    TRUE,
                    FALSE,
                    STRING,
                    REAL,
                    INT);
        }
        try {

            if (token.getType() == TokenType.IDENTIFIER
                    && nextToken.thisLexameIs(OPEN_PARENTHESES.getVALUE())) {
                callFunctionConsumer(tokens);
            } else if (token.getType() == TokenType.IDENTIFIER
                    && nextToken.thisLexameIs(SEMICOLON.getVALUE())) {
                TypeDeclaration.primaryConsumer(tokens);
            } else {
                Expressions.fullChecker(tokens);
            }
        } catch (SyntaxErrorException e) {
            throw new SyntaxErrorException(tokens.peek().getLexame(),
                    SEMICOLON,
                    IDENTIFIER,
                    TRUE,
                    FALSE,
                    STRING,
                    REAL);
        }
        TokenUtil.consumerByLexame(tokens, Terminals.SEMICOLON);
    }

    public static void callFunctionConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        TokenUtil.consumerByLexame(tokens, OPEN_PARENTHESES);
        try {
            TokenUtil.consumerByLexame(tokens, CLOSE_PARENTHESES);
        } catch (SyntaxErrorException ex) {
            argsListConsumer(tokens);
            TokenUtil.consumerByLexame(tokens, CLOSE_PARENTHESES);
        }
    }

    public static void argsListConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        argConsumer(tokens);
        if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
            TokenUtil.consumer(tokens);
            argsListConsumer(tokens);
        }
    }

    public static void argConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        Token token = tokens.pop();
        Token nextToken = tokens.pop();
        tokens.push(nextToken);
        tokens.push(token);

        if (TypeDeclaration.primaryChecker(token)) {
            if (token.getType() == TokenType.IDENTIFIER
                    && nextToken.thisLexameIs(OPEN_PARENTHESES.getVALUE())) {
                callFunctionConsumer(tokens);
            } else {
                TypeDeclaration.primaryConsumer(tokens);
            }
        } else {
            throw new SyntaxErrorException(token.getLexame(),
                    STRING,
                    BOOLEAN,
                    FALSE,
                    TRUE,
                    IDENTIFIER,
                    CALL_FUNCTION);
        }
    }
}
