package syntax.analyzer.model.exceptions;

import syntax.analyzer.util.Terminals;

/**
 *
 * @author Antônio neto e Uellington Damasceno
 */
public class EOFNotExpectedException extends Exception {

    public EOFNotExpectedException(Terminals terminal) {
        super("O arquivou terminou inesperadamente! O analisador esperava um: " + terminal.getVALUE());
    }

}
