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
public class Read {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {

        TerminalsUtil.consumerTokenByLexame(tokens, READ);
        TerminalsUtil.consumerTokenByLexame(tokens, OPEN_PARENTHESES);
        expressionReadConsumer(tokens);
        TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_PARENTHESES);
        TerminalsUtil.consumerTokenByLexame(tokens, SEMICOLON);

    }

    public static void expressionReadConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TerminalsUtil.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.STRING);

        Token token = tokens.peek();
        if (token.thisLexameIs(DOT.getVALUE())) {
            StructDeclaration.structUsageConsumer(tokens);
        } else if (token.thisLexameIs(OPEN_BRACKET.getVALUE())) {
            VarDeclaration.arraysDimensionConsumer(tokens);
        } else if (token.thisLexameIs(COMMA.getVALUE())) {
            moreReadings(tokens);
        } else {
            throw new SyntaxErrorException(token.getLexame(), DOT, OPEN_BRACKET, COMMA);
        }
    }

    public static void moreReadings(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TerminalsUtil.consumerTokenByLexame(tokens, COMMA);
        expressionReadConsumer(tokens);
    }
}
