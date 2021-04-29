package syntax.analyzer.util;

import java.util.Arrays;
import java.util.Deque;
import java.util.function.Consumer;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.SyntaxErrorException;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class TerminalsUtil {

    public static void consumerTokenByLexameAndExecute(Deque<Token> tokens, Terminals terminal, Consumer<Deque<Token>> consumer) {
        if (tokens.peek().thisLexameIs(terminal.getVALUE())) {
            tokens.pop();
            consumer.accept(tokens);
        }
    }

    public static void consumerTokenByLexame(Deque<Token> tokens, Terminals terminal) throws SyntaxErrorException {
        if (tokens.isEmpty()) {
            //todo fazer depois
        }

        Token token = tokens.pop();
        if (!token.thisLexameIs(terminal.getVALUE())) {
            tokens.push(token);
            throw new SyntaxErrorException(token.getLexame(), terminal);
        }
    }

    public static void consumerTokenByLexame(Deque<Token> tokens, Terminals... terminals) throws SyntaxErrorException {
        consumerTokenByLexame(tokens, terminals.length, terminals);
    }

    private static void consumerTokenByLexame(Deque<Token> tokens, int index, Terminals... terminals) throws SyntaxErrorException {
        if (terminals.length < index) {
            consumerTokenByLexame(tokens, terminals[index++]);
        }
    }

    public static void consumerTokenByType(Deque<Token> tokens, TokenType tokenType, Terminals terminal) throws SyntaxErrorException {
        if (tokens.isEmpty()) {
            //todo fazer depois
        }

        Token token = tokens.pop();
        if (token.getType() != tokenType) {
            tokens.push(token);
            throw new SyntaxErrorException(token.getLexame(), terminal);
        }
    }

    public static boolean contains(Token token, Terminals... terminals) {
        return Arrays.asList(terminals)
                .stream()
                .filter((arg0) -> token.thisLexameIs(((Terminals) arg0).getVALUE()))
                .findAny()
                .isPresent();
    }

}
