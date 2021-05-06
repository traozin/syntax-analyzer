package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TerminalsUtil;

/**
 *
 * @author Antônio Neto e Uellington Damasceno
 */
public class Print {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TerminalsUtil.consumerTokenByLexame(tokens, PRINT);
        TerminalsUtil.consumerTokenByLexame(tokens, OPEN_PARENTHESES);
        expressionPrintConsumer(tokens);
        TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_PARENTHESES);
        TerminalsUtil.consumerTokenByLexame(tokens, SEMICOLON);
    }

    public static void expressionPrintConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {

        try {
            TerminalsUtil.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.STRING);

            Token token = tokens.peek();
            if (token.thisLexameIs(DOT.getVALUE())) {
                StructDeclaration.structUsageConsumer(tokens);
            } else if (token.thisLexameIs(OPEN_BRACKET.getVALUE())) {
                VarDeclaration.arraysDimensionConsumer(tokens);
            } else if (token.thisLexameIs(COMMA.getVALUE())) {
                morePrints(tokens);
            } else {
                throw new SyntaxErrorException(token.getLexame(), DOT, OPEN_BRACKET, COMMA);
            }
        } catch (SyntaxErrorException e) {
            TerminalsUtil.consumerTokenByType(tokens, TokenType.STRING, Terminals.STRING);
            if (tokens.peek().thisLexameIs(COMMA.getVALUE())) {
                morePrints(tokens);
            }
        }
    }

    public static void morePrints(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TerminalsUtil.consumerTokenByLexame(tokens, COMMA);
        expressionPrintConsumer(tokens);
    }
}
