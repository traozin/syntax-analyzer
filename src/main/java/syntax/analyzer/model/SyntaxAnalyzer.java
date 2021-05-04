package syntax.analyzer.model;

import java.util.ArrayDeque;
import static java.util.stream.Collectors.toCollection;
import lexical.analyzer.model.SourceCode;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.model.grammar.VarDeclaration;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class SyntaxAnalyzer {

    public static SourceCode analyze(SourceCode code) {
        try {
            VarDeclaration.fullChecker(code.getTokens()
                    .stream()
                    .filter(token -> !token.isError())
                    .collect(toCollection(ArrayDeque::new)));
        } catch (SyntaxErrorException ex) {
            System.out.println(ex.getSyntaticalError());
        }
        return code;
    }
}
