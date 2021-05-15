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
public class VarDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {

        if (tokens.isEmpty()) {
            throw new EOFNotExpectedException(VAR);
        }
        Token first = tokens.peek();

        if (first.thisLexameIs(VAR.getVALUE())) {
            TerminalsUtil.consumerToken(tokens);
            TerminalsUtil.consumerTokenByLexame(tokens, OPEN_KEY);
            typedVariableConsumer(tokens);
        } else if (first.thisLexameIs(CONST.getVALUE())) {
            ConstDeclaration.fullChecker(tokens);
        } else {
            throw new SyntaxErrorException(first.getLexame(), VAR, CONST);
        }
    }

    public static void typedVariableConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        TypeDeclaration.typeConsumer(tokens);
        variableConsumer(tokens);

        if (tokens.isEmpty()) {
            throw new EOFNotExpectedException(SEMICOLON);
        }
        if (tokens.peek().thisLexameIs(SEMICOLON.getVALUE())) {
            TerminalsUtil.consumerToken(tokens);
            typedVariableConsumer(tokens);
        }
    }

    public static void variableConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        variableDeclaratorConsumer(tokens);
        if (tokens.isEmpty()) {
            throw new EOFNotExpectedException(COMMA);
        }
        if (tokens.peek().thisLexameIs(COMMA.getVALUE())) {
            TerminalsUtil.consumerToken(tokens);
            variableConsumer(tokens);
        }
    }

    public static void variableDeclaratorConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {

        TerminalsUtil.consumerTokenByType(tokens, TokenType.IDENTIFIER, IDENTIFIER);
        int check = TerminalsUtil.createCheckpoint(tokens);
        Token token = tokens.peek();

        if (token.thisLexameIs(EQUALS.getVALUE())) {
            TerminalsUtil.consumerToken(tokens);
            try {
                Expressions.fullChecker(tokens);
            } catch (SyntaxErrorException e) {
                tokens = TerminalsUtil.rollbackFor(check);
                try {
                    FunctionDeclaration.callFunctionConsumer(tokens);
                } catch (SyntaxErrorException ex) {
                    tokens = TerminalsUtil.rollbackFor(check);
                    try {
                        TerminalsUtil.consumerTokenByType(tokens, TokenType.IDENTIFIER, IDENTIFIER);
                        StructDeclaration.structUsageConsumer(tokens);
                    } catch (SyntaxErrorException exx) {
                        tokens = TerminalsUtil.rollbackFor(check);
                    }
                }
            }
        } else if (token.thisLexameIs(OPEN_BRACKET.getVALUE())) {
            arraysDimensionConsumer(tokens);
            if (tokens.peek().thisLexameIs(EQUALS.getVALUE())) {
                TerminalsUtil.consumerToken(tokens);
                TerminalsUtil.consumerTokenByLexame(tokens, OPEN_KEY);
                varArgsConsumer(tokens);
                TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_KEY);
            }
        }
    }

    public static void varArgsConsumer(Deque<Token> tokens) throws SyntaxErrorException {
        try {
            TypeDeclaration.primaryConsumer(tokens);
            if (tokens.peek().thisLexameIs(COMMA.getVALUE())) {
                TerminalsUtil.consumerToken(tokens);
                varArgsConsumer(tokens);
            }
        } catch (SyntaxErrorException e) {
            throw e;
        }
    }

    public static void arraysDimensionConsumer(Deque<Token> tokens) throws SyntaxErrorException, EOFNotExpectedException {
        Token token = tokens.peek();
        if (token.thisLexameIs(OPEN_BRACKET.getVALUE())) {
            TerminalsUtil.consumerTokenByLexame(tokens, OPEN_BRACKET);
            token = tokens.peek();
            if (token.getType() != null && token.getType() == TokenType.NUMBER) {
                TerminalsUtil.consumerTokenByType(tokens, TokenType.NUMBER, Terminals.INT);
            } else if (token.getType() == TokenType.IDENTIFIER) {
                TerminalsUtil.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
            } else {
                throw new SyntaxErrorException(token.getLexame(), IDENTIFIER, INT);
            }
            TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_BRACKET);
            arraysDimensionConsumer(tokens);
        }
    }
}
