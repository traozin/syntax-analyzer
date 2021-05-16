package syntax.analyzer.util;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import static java.util.stream.Collectors.toCollection;
import java.util.stream.Stream;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.EOFNotExpectedException;
import syntax.analyzer.model.exceptions.SyntaxErrorException;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class T {

    private static final List<Deque<Token>> COMPLETED_PRODUCTIONS = new LinkedList();
    private static final List<Deque<Token>> CHECKPOINTS = new LinkedList();

    public static boolean testBeforeConsume(Deque<Token> tokens, Terminals terminal) throws EOFNotExpectedException {
        if (tokens.isEmpty()) {
            throw new EOFNotExpectedException(terminal);
        }
        return tokens.peek().thisLexameIs(terminal.getVALUE());
    }

    public static void consumerTokenByLexame(Deque<Token> tokens, Terminals... terminals) throws SyntaxErrorException, EOFNotExpectedException {
        consumerTokenByLexame(tokens, terminals.length, terminals);
    }

    private static void consumerTokenByLexame(Deque<Token> tokens, int index, Terminals... terminals) throws SyntaxErrorException, EOFNotExpectedException {
        if (terminals.length < index) {
            consumerTokenByLexame(tokens, terminals[index++]);
        }
    }

    public static void consumerTokenByLexame(Deque<Token> tokens, Terminals terminal) throws SyntaxErrorException, EOFNotExpectedException {
        if (tokens.isEmpty()) {
            throw new EOFNotExpectedException(terminal);
        }

        Token token = tokens.pop();
        if (!token.thisLexameIs(terminal.getVALUE())) {
            tokens.push(token);
            throw new SyntaxErrorException(token.getLexame(), terminal);
        }
        System.out.println(token);
    }

    public static void consumerTokenByType(Deque<Token> tokens, TokenType tokenType, Terminals terminal) throws SyntaxErrorException, EOFNotExpectedException {
        if (tokens.isEmpty()) {
            throw new EOFNotExpectedException(terminal);
        }

        Token token = tokens.pop();
        if (token.getType() != tokenType) {
            tokens.push(token);
            throw new SyntaxErrorException(token.getLexame(), terminal);
        }
        System.out.println(token);
    }

    public static boolean contains(Token token, Terminals... terminals) {
        return Arrays.asList(terminals)
                .stream()
                .anyMatch((terminal) -> token.thisLexameIs(terminal.getVALUE()));
    }

    public static void consumerToken(Deque<Token> tokens) {
        System.out.println(tokens.pop());
    }

    public static void complete(Deque<Token> tokens, int pos) {
        Deque<Token> anotherDeque = CHECKPOINTS.get(pos);
        COMPLETED_PRODUCTIONS.add(Stream
                .generate(anotherDeque::pop)
                .limit(anotherDeque.size() - tokens.size())
                .collect(toCollection(ArrayDeque::new)));
    }

    public static Deque<Token> rollbackFor(int pos) {
        return CHECKPOINTS.get(pos);
    }
    public static int createCheckpoint(Deque<Token> tokens) {
        CHECKPOINTS.add(new ArrayDeque(tokens));
        return CHECKPOINTS.size()-1;
    }
}
