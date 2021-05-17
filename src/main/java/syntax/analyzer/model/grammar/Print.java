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
 * @author Antônio Neto e Uellington Damasceno
 */
public class Print {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByLexame(tokens, PRINT);
        TokenUtil.consumerByLexame(tokens, OPEN_PARENTHESES);
        expressionPrintConsumer(tokens);
        TokenUtil.consumerByLexame(tokens, CLOSE_PARENTHESES);
    }

    public static void expressionPrintConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        try {
            TokenUtil.consumerByType(tokens, TokenType.IDENTIFIER, Terminals.STRING);
            try {
                if (TokenUtil.testLexameBeforeConsume(tokens, DOT)) {
                    StructDeclaration.structUsageConsumer(tokens);
                } else if (TokenUtil.testLexameBeforeConsume(tokens, OPEN_BRACKET)) {
                    Arrays.dimensionConsumer(tokens);
                } else if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
                    morePrints(tokens);
                } else {
                    throw new SyntaxErrorException(tokens.peek().getLexame(), DOT, OPEN_BRACKET, COMMA);
                }
            } catch (SyntaxErrorException e) {
                if (!e.getSyntaticalError().thisLexameIs(CLOSE_PARENTHESES.getVALUE())) {
                    throw e;
                }
            }

        } catch (SyntaxErrorException e) {
            TokenUtil.consumerByType(tokens, TokenType.STRING, Terminals.STRING);
            if (TokenUtil.testLexameBeforeConsume(tokens, COMMA)) {
                morePrints(tokens);
            }
        }
    }

    public static void morePrints(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TokenUtil.consumerByLexame(tokens, COMMA);
        expressionPrintConsumer(tokens);
    }
}
