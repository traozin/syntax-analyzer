package syntax.analyzer.model.exceptions;

import java.util.Arrays;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Lexame;
import syntax.analyzer.model.SyntaticalError;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class SyntaxErrorException extends Exception {

    private SyntaticalError error;

    public SyntaxErrorException(Lexame lexame, String... errors) {
        super();
        this.error = new SyntaticalError(TokenType.ERROR_SYNTATICAL, lexame, Arrays.asList(errors));
    }

    public SyntaxErrorException(SyntaticalError error) {
        super();
        this.error = error;
    }

    public SyntaticalError getSyntaticalError() {
        return error;
    }
}
