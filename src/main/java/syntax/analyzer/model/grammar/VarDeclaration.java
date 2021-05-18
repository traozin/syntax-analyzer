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
public class VarDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByLexame(tokens, VAR);
        try {
            TokenUtil.consumerByLexame(tokens, OPEN_KEY);
        } catch (SyntaxErrorException e) {
            ErrorManager.addNewInternalError(new SyntaxErrorException(tokens.peek().getLexame(), OPEN_KEY));
        }
        
        try {
            typedVariableConsumer(tokens);
            TokenUtil.consumerByLexame(tokens, CLOSE_KEY);
        } catch (SyntaxErrorException e) {
            if (TokenUtil.testLexameBeforeConsume(tokens, CLOSE_KEY)) {
                ErrorManager.addNewInternalError(e.getSyntaticalError()
                        .toString()
                        .contains(GLOBAL.getVALUE())
                        ? new SyntaxErrorException(tokens.peek().getLexame(),
                                EQUALS,
                                OPEN_BRACKET,
                                SEMICOLON)
                        : e);
                TokenUtil.consumer(tokens);
            }
        }
    }

    public static void typedVariableConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TypeDeclaration.typeConsumer(tokens);
        try {
            variableConsumer(tokens);
            TokenUtil.consumerByLexame(tokens, SEMICOLON);
        } catch (SyntaxErrorException e) {
            EOFNotExpectedException.throwIfEmpty(tokens, CLOSE_KEY);
            if (TypeDeclaration.typeChecker(tokens.peek())) {
                ErrorManager.addNewInternalError(e.getSyntaticalError()
                        .toString()
                        .contains(GLOBAL.getVALUE())
                        ? new SyntaxErrorException(tokens.peek().getLexame(),
                                EQUALS,
                                OPEN_BRACKET,
                                SEMICOLON)
                        : e);
                typedVariableConsumer(tokens);
            } else {
                throw e;
            }
        }
        EOFNotExpectedException.throwIfEmpty(tokens, CLOSE_KEY);
        if (TypeDeclaration.typeChecker(tokens.peek())) {
            typedVariableConsumer(tokens);
        }
    }

    public static void variableConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, IDENTIFIER);
        if (TokenUtil.testLexameBeforeConsume(tokens, EQUALS)) {
            variableDeclaratorConsumer(tokens);
        } else if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
            Arrays.dimensionConsumer(tokens);
            if (TokenUtil.testLexameBeforeConsume(tokens, EQUALS)) {
                TokenUtil.consumer(tokens);
                Arrays.initialize(tokens);
            }
        }
        if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
            TokenUtil.consumer(tokens);
            variableConsumer(tokens);
        } else if (TokenUtil.testTypeBeforeConsume(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER)) {
            ErrorManager.addNewInternalError(new SyntaxErrorException(tokens.peek().getLexame(), COMMA, SEMICOLON));
            variableConsumer(tokens);
        } else if (!TokenUtil.testLexameBeforeConsume(tokens, SEMICOLON)) {
            throw new SyntaxErrorException(tokens.peek().getLexame(), EQUALS, OPEN_BRACKET, SEMICOLON);
        }
    }

    public static void variableDeclaratorConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        if (!TokenUtil.testLexameBeforeConsume(tokens, SEMICOLON) && !TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
            TokenUtil.consumerByLexame(tokens, EQUALS);
            EOFNotExpectedException.throwIfEmpty(tokens, IDENTIFIER, LOCAL, GLOBAL, REAL, INT, TRUE, FALSE);
            Token token = tokens.peek();

            if (TokenUtil.testTypeBeforeConsume(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER)
                    || TypeDeclaration.primaryChecker(token)
                    || token.thisLexameIs(LOCAL.getVALUE())
                    || token.thisLexameIs(GLOBAL.getVALUE())) {

                token = tokens.pop();
                EOFNotExpectedException.throwIfEmpty(tokens, IDENTIFIER, LOCAL, GLOBAL, DOT, EXPRESSION);
                Token nextToken = tokens.peek();
                tokens.push(token);
                if (token.thisLexameIs(GLOBAL.getVALUE())
                        || token.thisLexameIs(LOCAL.getVALUE())) {
                    VarScope.typedVariableScoped(tokens);
                } else if (nextToken.thisLexameIs(DOT.getVALUE())) {
                    TokenUtil.consumer(tokens);
                    StructDeclaration.structUsageConsumer(tokens);
                } else if (nextToken.thisLexameIs(OPEN_PARENTHESES.getVALUE())) {
                    FunctionDeclaration.callFunctionConsumer(tokens);
                } else {
                    try {
                        Expressions.fullChecker(tokens);
                    } catch (SyntaxErrorException e1) {
                        throw new SyntaxErrorException(tokens.peek().getLexame(),
                                DOT, GLOBAL, LOCAL, OPEN_PARENTHESES, EXPRESSION);
                    }
                }
            }
        }
    }

    public static void varArgsConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TypeDeclaration.primaryConsumer(tokens);
        EOFNotExpectedException.throwIfEmpty(tokens, COMMA, CLOSE_PARENTHESES);
        if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
            TokenUtil.consumer(tokens);
            varArgsConsumer(tokens);
        } else if (TypeDeclaration.primaryChecker(tokens.peek())) {
            ErrorManager.addNewInternalError(new SyntaxErrorException(tokens.peek().getLexame(), COMMA, CLOSE_PARENTHESES));
            varArgsConsumer(tokens);
        }
    }
}
