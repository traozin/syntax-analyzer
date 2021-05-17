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
public class FunctionSignature {

    //to do se tiver vazio
    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        typedIdentifier(tokens);
        T.consumerTokenByLexame(tokens, OPEN_PARENTHESES);
        
        if (!T.testLexameBeforeConsume(tokens, CLOSE_PARENTHESES)) {
            try {
                if (tokens.peek().getType() == TokenType.IDENTIFIER) {
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

    public static void paramsChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        typedIdentifier(tokens);
        if (T.testLexameBeforeConsume(tokens, COMMA)) {
            T.consumerToken(tokens);
            paramsChecker(tokens);
        }
    }

    /*
    Equivalente as produções:
    Func ID,
    Param
     */
    public static void typedIdentifier(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TypeDeclaration.typeConsumer(tokens);
        T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
    }

    //todo melhorar
    public static void idListChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        T.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        if (T.testLexameBeforeConsume(tokens, COMMA)) {
            T.consumerToken(tokens);
            idListChecker(tokens);
        }
    }
}
