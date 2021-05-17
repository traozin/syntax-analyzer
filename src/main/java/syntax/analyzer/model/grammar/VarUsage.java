package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.T;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class VarUsage {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        if (T.testLexameBeforeConsume(tokens, EQUALS)) {
            VarDeclaration.variableDeclaratorConsumer(tokens);
        } else if (T.testLexameBeforeConsume(tokens, DOT)) {
            T.consumerToken(tokens);
            T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
            if (!T.testLexameBeforeConsume(tokens, SEMICOLON)) {
                T.consumerTokenByLexame(tokens, EQUALS);
                try {
                    VarScope.typedVariableScoped(tokens);
                    if (T.testLexameBeforeConsume(tokens, DOT)) {
                        StructDeclaration.structUsageConsumer(tokens);
                    }
                } catch (SyntaxErrorException e1) {
                    EOFNotExpectedException.throwIfEmpty(tokens, IDENTIFIER);
                    Token token = tokens.pop();
                    EOFNotExpectedException.throwIfEmpty(tokens, OPEN_PARENTHESES, DOT, OPEN_BRACKET);
                    Token nextToken = tokens.peek();
                    tokens.push(token);

                    if (token.getType() == TokenType.IDENTIFIER
                            && nextToken.thisLexameIs(OPEN_PARENTHESES.getVALUE())) {
                        FunctionDeclaration.callFunctionConsumer(tokens);
                    } else if (token.getType() == TokenType.IDENTIFIER
                            && nextToken.thisLexameIs(DOT.getVALUE())) {
                        T.consumerToken(tokens);
                        StructDeclaration.structUsageConsumer(tokens);
                    } else if (token.getType() == TokenType.IDENTIFIER
                            && nextToken.thisLexameIs(OPEN_BRACKET.getVALUE())) {
                        T.consumerToken(tokens);
                        Arrays.dimensionConsumer(tokens);
                    } else {
                        Expressions.fullChecker(tokens);
                    }
                }
            }

        } else if (T.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
            Arrays.dimensionConsumer(tokens);
            try {
                T.consumerTokenByLexame(tokens, SEMICOLON);
            } catch (SyntaxErrorException e) {
                T.consumerTokenByLexame(tokens, EQUALS);
                try {
                    VarScope.typedVariableScoped(tokens);
                    if (T.testLexameBeforeConsume(tokens, DOT)) {
                        StructDeclaration.structUsageConsumer(tokens);
                    }
                } catch (SyntaxErrorException e1) {
                    EOFNotExpectedException.throwIfEmpty(tokens, IDENTIFIER);
                    Token token = tokens.pop();
                    EOFNotExpectedException.throwIfEmpty(tokens, DOT, OPEN_BRACKET);
                    Token nextToken = tokens.peek();
                    tokens.push(token);

                    if (token.getType() == TokenType.IDENTIFIER
                            && nextToken.thisLexameIs(DOT.getVALUE())) {
                        T.consumerToken(tokens);
                        StructDeclaration.structUsageConsumer(tokens);
                    } else if (token.getType() == TokenType.IDENTIFIER
                            && nextToken.thisLexameIs(OPEN_BRACKET.getVALUE())) {
                        T.consumerToken(tokens);
                        Arrays.dimensionConsumer(tokens);
                    } else if (TypeDeclaration.primaryChecker(token)) {
                        TypeDeclaration.primaryConsumer(tokens);
                    } else if (T.testLexameBeforeConsume(tokens, OPEN_KEY)) {
                        Arrays.initialize(tokens);
                    }
                }
            }
        }
    }
}
