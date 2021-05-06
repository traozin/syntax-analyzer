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
 * @author Antonio Neto e Uellington Damasceno
 */
public class FunctionDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TerminalsUtil.consumerTokenByLexame(tokens, FUNCTION);
        TypeDeclaration.typeConsumer(tokens);
        TerminalsUtil.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        TerminalsUtil.consumerTokenByLexame(tokens, OPEN_PARENTHESES);
        try {
            TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_PARENTHESES);
        } catch (SyntaxErrorException e) {
            FunctionSignature.paramsChecker(tokens);
            TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_PARENTHESES);
        }
        blockFunctionChecker(tokens);
    }

    public static void blockFunctionChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TerminalsUtil.consumerTokenByLexame(tokens, OPEN_KEY);
        StatementDeclaration.fullChecker(tokens);
        returnChecker(tokens);
        TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_KEY);
    }

    public static void returnChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TerminalsUtil.consumerTokenByLexame(tokens, Terminals.RETURN);
        Token token = tokens.peek();
        boolean isEmptyReturn = TerminalsUtil.contains(token, Terminals.SEMICOLON);
        boolean isPrimaryReturn = TypeDeclaration.primaryChecker(token);
        if (!isEmptyReturn && !isPrimaryReturn) {
            throw new SyntaxErrorException(token.getLexame(),
                    SEMICOLON,
                    IDENTIFIER,
                    TRUE,
                    FALSE,
                    STRING,
                    REAL);
        }
        //TODO: Verificar se é uma chamada de função ou uma expressão.
        try {
            callFunctionConsumer(tokens);
        } catch (SyntaxErrorException e) {
            throw new SyntaxErrorException(token.getLexame(),
                    SEMICOLON,
                    IDENTIFIER,
                    TRUE,
                    FALSE,
                    STRING,
                    REAL);
        }
        TerminalsUtil.consumerTokenByLexame(tokens, Terminals.SEMICOLON);
    }

    public static void callFunctionConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TerminalsUtil.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        TerminalsUtil.consumerTokenByLexame(tokens, OPEN_PARENTHESES);
        try {
            TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_PARENTHESES);
        } catch (SyntaxErrorException ex) {
            argsListConsumer(tokens);
            TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_PARENTHESES);
        }
    }

    public static void argsListConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        argConsumer(tokens);
        if (tokens.peek().thisLexameIs(Terminals.COMMA.getVALUE())) {
            argsListConsumer(tokens);
        }
    }

    public static void argConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        Token token = tokens.pop();
        if (!TypeDeclaration.primaryChecker(token)) {
            tokens.push(token);
            throw new SyntaxErrorException(token.getLexame(),
                    Terminals.IDENTIFIER,
                    Terminals.TRUE,
                    Terminals.FALSE,
                    Terminals.REAL,
                    Terminals.STRING
            );
        }
        if (token.getType() == TokenType.IDENTIFIER) {
            Token nextToken = tokens.pop();
            if (nextToken.thisLexameIs(Terminals.OPEN_PARENTHESES.getVALUE())) {
                tokens.push(nextToken);
                tokens.push(token);
                callFunctionConsumer(tokens);
            } else if (nextToken.thisLexameIs(Terminals.COMMA.getVALUE())) {
                tokens.push(token);
                TerminalsUtil.consumerToken(tokens);
                tokens.push(nextToken);
                argsListConsumer(tokens);
            } else {
                tokens.push(token);
                TerminalsUtil.consumerToken(tokens);
                throw new SyntaxErrorException(token.getLexame(), Terminals.OPEN_PARENTHESES, Terminals.COMMA);
            }
        } else {
            tokens.push(token);
            TypeDeclaration.primaryConsumer(tokens);
        }
    }
}
