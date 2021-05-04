package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TerminalsUtil;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Expressions {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException {
        addExpression(tokens);
    }

    public static void orExpressionConsumer(Deque<Token> tokens) {
        andExpression(tokens);
        try {
            TerminalsUtil.consumerTokenByLexame(tokens, Terminals.OR);
            orExpressionConsumer(tokens);
        } catch (SyntaxErrorException ex) {
        }
    }

    public static void andExpression(Deque<Token> tokens) {

    }

    public static void equalityExpression(Deque<Token> tokens) {

    }

    public static void compareExpression(Deque<Token> tokens) {

    }

    public static void addExpression(Deque<Token> tokens) throws SyntaxErrorException {
        try {
            TerminalsUtil.consumerTokenByLexame(tokens, Terminals.ADD);
        } catch (SyntaxErrorException ex) {
            try {
                TerminalsUtil.consumerTokenByLexame(tokens, Terminals.MINUS);
            } catch (SyntaxErrorException ex1) {
                multiplicationExpressionConsumer(tokens);
            }
        }
    }

    public static void multiplicationExpressionConsumer(Deque<Token> tokens) throws SyntaxErrorException {
        try {
            TerminalsUtil.consumerTokenByLexame(tokens, Terminals.MULTIPLICATION);
        } catch (SyntaxErrorException ex) {
            try {
                TerminalsUtil.consumerTokenByLexame(tokens, Terminals.DIVISION);
            } catch (SyntaxErrorException ex1) {
                primaryExpressionConsumer(tokens);
            }
        }
    }

//    public static void unaryExpression(Deque<Token> tokens) {
//        objectExpression(tokens);
//    }
//
//    public static void objectExpression(Deque<Token> tokens) throws SyntaxErrorException {
//        methodExpressionConsumer(tokens);
//    }
//
//    public static void methodExpressionConsumer(Deque<Token> tokens) throws SyntaxErrorException {
//        primaryExpressionConsumer(tokens);
//    }
    public static void primaryExpressionConsumer(Deque<Token> tokens) throws SyntaxErrorException {
        try {
            TypeDeclaration.primaryConsumer(tokens);
        } catch (SyntaxErrorException e) {
            try {
                TerminalsUtil.consumerTokenByLexame(tokens, OPEN_PARENTHESES);
                //TODO: colocar expression ou equivalente ao fullChecker do expression;
                addExpression(tokens);
                TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_PARENTHESES);
            } catch (SyntaxErrorException ex) {
//                Token token = tokens.pop();
                try {
                    addExpression(tokens);
                } catch (SyntaxErrorException ex1) {
                    TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_PARENTHESES);
                }

//                if (!TypeDeclaration.primaryChecker(token)) {
//                    tokens.push(token);
//                    throw new SyntaxErrorException(token.getLexame(),
//                            IDENTIFIER,
//                            TRUE,
//                            FALSE,
//                            REAL,
//                            OPEN_PARENTHESES
//                    );
//                }
            }
        }

    }

}
