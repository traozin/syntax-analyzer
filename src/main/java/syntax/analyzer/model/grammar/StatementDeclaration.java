package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TerminalsUtil;

/**
 *
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class StatementDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TerminalsUtil.consumerTokenByLexame(tokens, OPEN_KEY);
        statementListChecker(tokens);
        TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_KEY);
    }

    public static void statementListChecker(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        simpleStatement(tokens);
        if (!tokens.isEmpty() && !tokens.peek().thisLexameIs(CLOSE_KEY.getVALUE())) {
            statementListChecker(tokens);
        }
    }

    private static void simpleStatement(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        try {
            Read.fullChecker(tokens);
        } catch (SyntaxErrorException e) {
            try {
                Print.fullChecker(tokens);
            } catch (SyntaxErrorException e2) {
                try {
                    VarDeclaration.fullChecker(tokens);//TODO
                } catch (SyntaxErrorException e4) {
                    try {
                        FunctionDeclaration.callFunctionConsumer(tokens);
                        TerminalsUtil.consumerTokenByLexame(tokens, SEMICOLON);
                    } catch (SyntaxErrorException e5) {
                        try {
                            IfElse.fullChecker(tokens);
                        } catch (SyntaxErrorException e6) {
                            WhileDeclaration.fullChecker(tokens);
                        }
                    }
                }
            }
        }
    }
}
