package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;
import syntax.analyzer.util.TerminalsUtil;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class VarDeclaration {

    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException {
        if (tokens.isEmpty()) {
            //se tiver vazio
        }
        Token first = tokens.pop();

        if (first.thisLexameIs(VAR.getVALUE())) {
            TerminalsUtil.consumerTokenByLexame(tokens, OPEN_KEY);
            tokens.push(first);
            typedVariableConsumer(tokens);
        } else if (first.thisLexameIs(CONST.getVALUE())) {
            TerminalsUtil.consumerTokenByLexame(tokens, OPEN_KEY);
        } else {
            throw new SyntaxErrorException(first.getLexame(), VAR, CONST);
        }
    }

    public static void typedVariableConsumer(Deque<Token> tokens) throws SyntaxErrorException {
        Token token = tokens.peek();
        if (!token.thisLexameIs(SEMICOLON.getVALUE())) {

            TypeDeclaration.typeConsumer(tokens);
            variableConsumer(tokens);

            token = tokens.pop();
            if (!token.thisLexameIs(SEMICOLON.getVALUE())) {
                throw new SyntaxErrorException(token.getLexame(), SEMICOLON);
            }

            typedVariableConsumer(tokens);
        }
    }

    public static void variableConsumer(Deque<Token> tokens) throws SyntaxErrorException {
        variableDeclaratorConsumer(tokens);
    }

    //consumir token para dar certo
    public static void variableDeclaratorConsumer(Deque<Token> tokens) throws SyntaxErrorException {
        TerminalsUtil.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
        Token token = tokens.peek();
        if (token.thisLexameIs(EQUALS.getVALUE())) {
            //callFunction
            //expression = variableInit
            //structUsage
            //scoped
        } else if (token.thisLexameIs(OPEN_BRACKET.getVALUE())) {
            arraysDimensionConsumer(tokens);
            TerminalsUtil.consumerTokenByLexame(tokens, EQUALS);
            try {
                TerminalsUtil.consumerTokenByLexame(tokens, OPEN_KEY);
            } catch (SyntaxErrorException e) {
                TypeDeclaration.primaryListConsumer(tokens);
                TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_KEY);
            }
        }
    }

    public static void arraysDimensionConsumer(Deque<Token> tokens) throws SyntaxErrorException {
        Token token = tokens.peek();

        if (token.thisLexameIs(OPEN_BRACKET.getVALUE())) {
            TerminalsUtil.consumerTokenByLexame(tokens, OPEN_BRACKET);
            token = tokens.peek();
            if (token.thisLexameIs(INT.getVALUE())) {
                TerminalsUtil.consumerTokenByLexame(tokens, INT);
            } else if (token.getType() == TokenType.IDENTIFIER) {
                TerminalsUtil.consumerTokenByType(tokens, TokenType.IDENTIFIER, Terminals.IDENTIFIER);
            } else {
                try {
                    Expressions.addExpression(tokens);
                } catch (SyntaxErrorException e) {
                    throw new SyntaxErrorException(token.getLexame(), IDENTIFIER, INT, EXPRESSION);
                }
            }
            TerminalsUtil.consumerTokenByLexame(tokens, CLOSE_BRACKET);
            arraysDimensionConsumer(tokens);
        }
    }
}
