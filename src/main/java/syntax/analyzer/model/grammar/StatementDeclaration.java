package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.T;

/**
 *
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class StatementDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        T.consumerTokenByLexame(tokens, OPEN_KEY);
        statementListChecker(tokens);
        T.consumerTokenByLexame(tokens, CLOSE_KEY);
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
            System.out.println(e.getSyntaticalError());
            try {
                Print.fullChecker(tokens);
            } catch (SyntaxErrorException e2) {
                System.out.println(e2.getSyntaticalError());
                try {
                    VarDeclaration.fullChecker(tokens);//TODO
                } catch (SyntaxErrorException e3) {
                    System.out.println(e3.getSyntaticalError());
                    try {
                        FunctionDeclaration.callFunctionConsumer(tokens);
                        T.consumerTokenByLexame(tokens, SEMICOLON);
                    } catch (SyntaxErrorException e4) {
                        System.out.println(e4.getSyntaticalError());
                        try {
                            IfElse.fullChecker(tokens);
                        } catch (SyntaxErrorException e5) {
                            System.out.println(e5.getSyntaticalError());
                            try {
                                WhileDeclaration.fullChecker(tokens);
                            } catch (SyntaxErrorException e6) {
                                System.out.println(e6.getSyntaticalError());
                                FunctionDeclaration.returnChecker(tokens);
                            }
                        }
                    }
                }
            }
        }
    }
}
