package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
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
        variableDeclaratorConsumer(tokens);
        if (T.testLexameBeforeConsume(tokens, COMMA)) {
            T.consumerToken(tokens);
            variableConsumer(tokens);
        }
    }

    public static void variableDeclaratorConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        T.consumerTokenByType(tokens, TokenType.IDENTIFIER, IDENTIFIER);
        if (!T.testLexameBeforeConsume(tokens, SEMICOLON) && !T.testLexameBeforeConsume(tokens, COMMA)) {
            try {
                arraysDimensionConsumer(tokens);
                if (T.testLexameBeforeConsume(tokens, EQUALS)) {
                    T.consumerToken(tokens);
                    T.consumerTokenByLexame(tokens, OPEN_KEY);
                    varArgsConsumer(tokens);
                    T.consumerTokenByLexame(tokens, CLOSE_KEY);
                }
            } catch (SyntaxErrorException e) {
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
                        VariableScope.scopeModifierConsumer(tokens);
                        T.consumerTokenByLexame(tokens, DOT);
                        T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
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

    }

    public static void varArgsConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TypeDeclaration.primaryConsumer(tokens);
        if (T.testLexameBeforeConsume(tokens, COMMA)) {
            T.consumerToken(tokens);
            varArgsConsumer(tokens);
        }
    }

    public static void arraysDimensionConsumer(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        T.consumerTokenByLexame(tokens, OPEN_BRACKET);
        try {
            T.consumerTokenByType(tokens, TokenType.NUMBER, Terminals.INT);
        } catch (SyntaxErrorException ex) {
            try {
                T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
            } catch (SyntaxErrorException e) {
                throw new SyntaxErrorException(tokens.peek().getLexame(), IDENTIFIER, INT);
            }
        }
        T.consumerTokenByLexame(tokens, CLOSE_BRACKET);
        if (tokens.peek().thisLexameIs(OPEN_BRACKET.getVALUE())) {
            arraysDimensionConsumer(tokens);
        }
    }
}
