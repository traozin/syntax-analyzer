package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.model.Token;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public interface Grammar {

    abstract void fullChecker(Deque<Token> tokens);

}
