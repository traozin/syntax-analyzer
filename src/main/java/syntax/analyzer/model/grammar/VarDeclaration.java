package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.model.grammar.Arrays;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.T;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class VarDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        T.consumerTokenByLexame(tokens, VAR);
        T.consumerTokenByLexame(tokens, OPEN_KEY);
        typedVariableConsumer(tokens);
        T.consumerTokenByLexame(tokens, CLOSE_KEY);
    }

    public static void typedVariableConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TypeDeclaration.typeConsumer(tokens);
        variableConsumer(tokens);

        T.consumerTokenByLexame(tokens, SEMICOLON);

        if (TypeDeclaration.typeChecker(tokens.peek())) {
            typedVariableConsumer(tokens);
        }
    }

    public static void variableConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        T.consumerTokenByType(tokens, TokenType.IDENTIFIER, IDENTIFIER);
        if (T.testLexameBeforeConsume(tokens, EQUALS)) {
            variableDeclaratorConsumer(tokens);
        } else if (T.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
            Arrays.dimensionConsumer(tokens);
            if (T.testLexameBeforeConsume(tokens, EQUALS)) {
                T.consumerToken(tokens);
                Arrays.initialize(tokens);
            }
        }
        if (T.testLexameBeforeConsume(tokens, COMMA)) {
            T.consumerToken(tokens);
            variableConsumer(tokens);
        } else if (!T.testLexameBeforeConsume(tokens, SEMICOLON)) {
            throw new SyntaxErrorException(tokens.peek().getLexame(), EQUALS, OPEN_BRACKET);
        }
    }

    public static void variableDeclaratorConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        if (!T.testLexameBeforeConsume(tokens, SEMICOLON) && !T.testLexameBeforeConsume(tokens, COMMA)) {
            T.consumerTokenByLexame(tokens, EQUALS);
            Token token = tokens.peek();

            if (T.testTypeBeforeConsume(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER)
                    || TypeDeclaration.primaryChecker(token)
                    || token.thisLexameIs(LOCAL.getVALUE())
                    || token.thisLexameIs(GLOBAL.getVALUE())) {

                token = tokens.pop();
                if (tokens.isEmpty()) {
                    throw new EOFNotExpectedException(IDENTIFIER,
                            EXPRESSION,
                            DOT,
                            GLOBAL,
                            LOCAL);
                }
                Token nextToken = tokens.peek();
                tokens.push(token);
                if (token.thisLexameIs(GLOBAL.getVALUE())
                        || token.thisLexameIs(LOCAL.getVALUE())) {
                    VarScope.typedVariableScoped(tokens);
                } else if (nextToken.thisLexameIs(DOT.getVALUE())) {
                    T.consumerToken(tokens);
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
        if (T.testLexameBeforeConsume(tokens, COMMA)) {
            T.consumerToken(tokens);
            varArgsConsumer(tokens);
        }
    }
}
