package syntax.analyzer.util;

import java.util.Deque;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.model.grammar.StatementDeclaration;
import syntax.analyzer.model.grammar.VarDeclaration;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class ErrorManager {

    public static void genericBlockConsumer(Deque<Token> tokens) throws EOFNotExpectedException {
        Token token = tokens.pop();
        try {
            VarDeclaration.typedVariableConsumer(tokens);
            findNext(tokens, CLOSE_KEY);
            TokenUtil.consumerByLexame(tokens, CLOSE_KEY);
            System.out.println("typedvariable");
        } catch (SyntaxErrorException e) {
            try {
                VarDeclaration.varArgsConsumer(tokens);
                findNext(tokens, CLOSE_KEY);
                TokenUtil.consumerByLexame(tokens, CLOSE_KEY);
                System.out.println("varargs");
            } catch (SyntaxErrorException e1) {
                tokens.push(token);
                StatementDeclaration.fullChecker(tokens);
                System.out.println("statement");
            }
        }
    }

    public static void consumer(Deque<Token> tokens) {
        System.out.print("Error:" );
        TokenUtil.consumer(tokens);
    }

    public static void findNext(Deque<Token> tokens, Terminals terminal) throws EOFNotExpectedException {
        if (!TokenUtil.testLexameBeforeConsume(tokens, terminal)) {
            System.out.print("next ");
            consumer(tokens);
            findNext(tokens, terminal);
        }
    }
}
