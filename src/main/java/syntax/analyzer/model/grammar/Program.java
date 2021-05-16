package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import static syntax.analyzer.util.Terminals.PROCEDURE;
import static syntax.analyzer.util.Terminals.START;

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
        globalStatementsConsumer(tokens);
        if (!tokens.isEmpty()) {
            globalStatementsList(tokens);
        }
    }

    public static void globalStatementsConsumer(Deque<Token> tokens) throws EOFNotExpectedException {
        try {
            FunctionDeclaration.fullChecker(tokens);
        } catch (SyntaxErrorException e1) {
            try {
                StructDeclaration.fullChecker(tokens);
            } catch (SyntaxErrorException ex) {
                try {
                    VarDeclaration.fullChecker(tokens);
                } catch (SyntaxErrorException e) {
                    try {
                        Token token = tokens.pop();
                        Token nextToken = tokens.peek();

                        tokens.push(token);

                        if (token.thisLexameIs(PROCEDURE.getVALUE())) {
                            if (nextToken.thisLexameIs(START.getVALUE())) {
                                ProcedureMain.fullChecker(tokens);
                            } else {
                                ProcedureDeclaration.fullChecker(tokens);
                            }
                        }
                    } catch (SyntaxErrorException ex1) {
                        System.out.println(e.getSyntaticalError());
                    }
                }
            }
        }
    }
}
