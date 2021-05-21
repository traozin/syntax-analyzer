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
 * @author Antônio Neto e Uellington Damasceno
 */
public class Print {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        TokenUtil.consumeExpectedTokenByLexame(tokens, OPEN_PARENTHESES);
        if (TokenUtil.testLexameBeforeConsume(tokens, CLOSE_PARENTHESES)) {
            ErrorManager.addNewInternalError(tokens, IDENTIFIER, STRING);
            TokenUtil.consumer(tokens);
        } else {
            expressionPrintConsumer(tokens);
        }
        TokenUtil.consumeExpectedTokenByLexame(tokens, CLOSE_PARENTHESES);
    }

    public static void expressionPrintConsumer(Deque<Token> tokens) throws EOFNotExpectedException {
        try {
            TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
            if (!TokenUtil.testLexameBeforeConsume(tokens, CLOSE_PARENTHESES)) {
                if (TokenUtil.testLexameBeforeConsume(tokens, DOT)) {
                    StructDeclaration.structUsageConsumer(tokens);
                } else if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
                    Arrays.dimensionConsumer(tokens);
                } else if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
                    morePrints(tokens);
                } else {
                    throw new SyntaxErrorException(tokens.peek().getLexame(), DOT, OPEN_BRACKET, COMMA, CLOSE_PARENTHESES);
                }
            }
        } catch (SyntaxErrorException e) {
            try {
                TokenUtil.consumerByType(tokens, TokenType.STRING, Terminals.STRING);
            } catch (SyntaxErrorException e1) {
                if (!tokens.peek().thisLexameIs(SEMICOLON.getVALUE())) {
                    ErrorManager.addNewInternalError(tokens, STRING, IDENTIFIER, CLOSE_PARENTHESES);
                    TokenUtil.consumer(tokens);
                }
            }
            if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
                morePrints(tokens);
            }
        }
    }

    public static void morePrints(Deque<Token> tokens) throws EOFNotExpectedException {
        TokenUtil.consumer(tokens);
        expressionPrintConsumer(tokens);
    }
}
