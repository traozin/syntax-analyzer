package syntax.analyzer.model;

import java.util.ArrayDeque;
import static java.util.stream.Collectors.toCollection;
import lexical.analyzer.model.SourceCode;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.model.grammar.ProcedureMain;
import syntax.analyzer.model.grammar.Program;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class SyntaxAnalyzer {

    public static SourceCode analyze(SourceCode code) {
        try {
            ArrayDeque<Token> tokens = code.getTokens()
                    .stream()
                    .filter(token -> !token.isError())
                    .collect(toCollection(ArrayDeque::new));
            
            Program.fullChecker(tokens);
        } catch (EOFNotExpectedException ex) {
            System.out.println(ex.getMessage());
        }
        return code;
    }
}
