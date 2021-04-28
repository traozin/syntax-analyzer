package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class FuctionDeclaration {

    //to do se tiver vazio
    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException {
        typedIdentifier(tokens);

        Token token = tokens.pop();
        if (!token.thisLexameIs(OPEN_PARENTHESES.getVALUE())) {
            tokens.push(token);
            throw new SyntaxErrorException(token.getLexame(), Terminals.IDENTIFIER);
        }

        token = tokens.peek();
        if (!token.thisLexameIs(CLOSE_PARENTHESES.getVALUE())) {
            try {
                if (token.getType() == TokenType.IDENTIFIER) {
                    idListChecker(tokens);
                } else {
                    paramsChecker(tokens);
                }
            } catch (SyntaxErrorException e) {
                if (!e.getSyntaticalError().thisLexameIs(CLOSE_PARENTHESES.getVALUE())) {
                    throw e;
                }
            }
        }
    }

    public static void paramsChecker(Deque<Token> tokens) throws SyntaxErrorException {
        typedIdentifier(tokens);
        if (tokens.peek().thisLexameIs(COMMA.getVALUE())) {
            tokens.pop();
            paramsChecker(tokens);
        }
    }

    /*
    Equivalente as produções:
    Func ID,
    Param
     */
    public static void typedIdentifier(Deque<Token> tokens) throws SyntaxErrorException {
        VarDeclaration.typeChecker(tokens);

        Token token = tokens.peek();
        if (token.getType() != TokenType.IDENTIFIER) {
            throw new SyntaxErrorException(token.getLexame(), Terminals.IDENTIFIER);
        }
        tokens.pop();
    }

    public static void idListChecker(Deque<Token> tokens) throws SyntaxErrorException {
        Token token = tokens.pop();
        Token nextToken = tokens.peek();

        if (token.getType() != TokenType.IDENTIFIER) {
            tokens.push(token);
            throw new SyntaxErrorException(token.getLexame(), Terminals.IDENTIFIER);
        }
        if (nextToken.thisLexameIs(COMMA.getVALUE())) {
            idListChecker(tokens);
        }
    }
}
