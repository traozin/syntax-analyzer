package syntax.analyzer.util;

import java.util.Arrays;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public enum Terminals {
    VAR("var"),
    OPEN_KEY("{"),
    CLOSE_KEY("}"),
    CONST("const"),
    BOOLEAN("boolean"),
    STRING("string"),
    INT("int"),
    REAL("real"),
    IDENTIFIER("identifier"),
    SEMICOLON(";"),
    OPEN_BRACKET("["),
    CLOSE_BRACKET("]"),
    EQUALS("="),
    OPEN_PARENTHESES("("),
    CLOSE_PARENTHESES(")"),
    COMMA(",");

    private final String VALUE;

    public boolean isTerminal(String value) {
        return Arrays.asList(Terminals.values())
                .stream()
                .filter(terminal -> terminal.VALUE.equals(value))
                .findAny()
                .isPresent();
    }

    public String getVALUE() {
        return VALUE;
    }

    private Terminals(String value) {
        this.VALUE = value;
    }

}