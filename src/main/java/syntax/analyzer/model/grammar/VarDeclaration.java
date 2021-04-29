package syntax.analyzer.model.grammar;

import java.util.Deque;
import lexical.analyzer.enums.TokenType;
import lexical.analyzer.model.Token;
import syntax.analyzer.model.exceptions.SyntaxErrorException;
import syntax.analyzer.util.Terminals;
import static syntax.analyzer.util.Terminals.*;

/**
 *
 * @author Antonio Neto e Uellington Damasceno
 */
public class VarDeclaration {
    
    public static void fullChecker(Deque<Token> tokens) throws SyntaxErrorException {
        if (tokens.isEmpty()) {
            //se tiver vazio
        }
        Token first = tokens.pop();
        Token second = tokens.pop();
        
        if (first.thisLexameIs(VAR.getVALUE())) {
            if (!second.thisLexameIs(OPEN_KEY.getVALUE())) {
                throw new SyntaxErrorException(second.getLexame(), OPEN_KEY);
            }
            typedVariableChecker(tokens);
        } else if (first.thisLexameIs(CONST.getVALUE())) {
            if (!second.thisLexameIs(OPEN_KEY.getVALUE())) {
                throw new SyntaxErrorException(second.getLexame(), OPEN_KEY);
            }
        } else {
            throw new SyntaxErrorException(first.getLexame(), VAR, CONST);
        }
    }
    
    public static void typedVariableChecker(Deque<Token> tokens) throws SyntaxErrorException {
        Token token = tokens.pop();
        if (!token.thisLexameIs(SEMICOLON.getVALUE())) {
            
            TypeDeclaration.typeConsumer(tokens);
            variableChecker(tokens);
            
            token = tokens.peek();
            if (!token.thisLexameIs(SEMICOLON.getVALUE())) {
                throw new SyntaxErrorException(token.getLexame(), SEMICOLON);
            }
            
            typedVariableChecker(tokens);
        }
    }
    
    public static void variableChecker(Deque<Token> tokens) throws SyntaxErrorException {
        variableDeclaratorChecker(tokens);
    }

    //consumir token para dar certo
    public static void variableDeclaratorChecker(Deque<Token> tokens) throws SyntaxErrorException {
        if (tokens.isEmpty()) {
            //se tiver vazio
        }
        Token token = tokens.pop();
        Token nextToken = tokens.peek();
        
        if (token.getType() != TokenType.IDENTIFIER) {
            tokens.push(token);
            throw new SyntaxErrorException(token.getLexame(), Terminals.IDENTIFIER);
        } else {
            if (nextToken.thisLexameIs(OPEN_BRACKET.getVALUE())) {
                
            } else if (nextToken.thisLexameIs(EQUALS.getVALUE())) {
                
            }
        }
    }
   
   
}
