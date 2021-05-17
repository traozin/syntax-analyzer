package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import static syntax.analyzer.util.Terminals.*;

/**
 * TODO: testar struct, procedure, function, const e var
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class Program {

    public static void fullChecker(Deque<Token> tokens) throws EOFNotExpectedException {
        globalStatementsList(tokens);
    }

    public static void globalStatementsList(Deque<Token> tokens) throws EOFNotExpectedException {
        try {
            globalStatementsConsumer(tokens);
            if (!tokens.isEmpty()) {
                globalStatementsList(tokens);
            }
        } catch (SyntaxErrorException ex) {
            System.out.println(ex.getSyntaticalError());
        }
    }

    public static void globalStatementsConsumer(Deque<Token> tokens) throws EOFNotExpectedException, SyntaxErrorException {
        if (tokens.isEmpty()) {
            throw new EOFNotExpectedException(
                    VAR,
                    CONST,
                    FUNCTION,
                    PROCEDURE,
                    TYPEDEF,
                    STRUCT);
        }
        Token token = tokens.peek();
        if (token.thisLexameIs(FUNCTION.getVALUE())) {
            FunctionDeclaration.fullChecker(tokens);
        } else if (token.thisLexameIs(TYPEDEF.getVALUE())) {
            StructDeclaration.fullChecker(tokens);
        } else if (token.thisLexameIs(VAR.getVALUE())) {
            VarDeclaration.fullChecker(tokens);
        } else if (token.thisLexameIs(CONST.getVALUE())) {
            ConstDeclaration.fullChecker(tokens);
        } else if (token.thisLexameIs(PROCEDURE.getVALUE())) {
            token = tokens.pop();
            EOFNotExpectedException.throwIfEmpty(tokens, START, IDENTIFIER);
            Token nextToken = tokens.peek();
            tokens.push(token);
            if (nextToken.thisLexameIs(START.getVALUE())) {
                ProcedureMain.fullChecker(tokens);
            } else {
                ProcedureDeclaration.fullChecker(tokens);
            }
        }
    }
}
