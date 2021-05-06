package syntax.analyzer.model;

import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.toCollection;
import lexical.analyzer.model.SourceCode;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.model.grammar.Expressions;
import syntax.analyzer.model.grammar.VarDeclaration;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class SyntaxAnalyzer {

    public static SourceCode analyze(SourceCode code){
        try {
            ArrayDeque<Token> tokens = code.getTokens()
                    .stream()
                    .filter(token -> !token.isError())
                    .collect(toCollection(ArrayDeque::new));            
            
//            VarDeclaration.fullChecker(tokens);
            Expressions.fullChecker(tokens);
            
        } catch (SyntaxErrorException ex) {
            System.out.println(ex.getSyntaticalError());
        } catch (EOFNotExpectedException ex) {
            System.out.println(ex.getMessage());
        }
        return code;
    }
}
