import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import static java.lang.Character.*;

/**
 * Also known as Scanner or Lexer. This class does the lexical analysis and returns the tokens to the parser.
 *
 * @author Leonardo-Rocha, Gabriel Chiquetto.
 */
class TokenGenerator {
    /**
     * Scanner to parse the input.
     */
    private LineNumberReader lineNumberReader;

    /**
     * Line being evaluated.
     */
    private String currentLine;

    /**
     * Char at the current position.
     */
    private char currentChar;

    /**
     * Current position.
     */
    private int currentLinePosition;

    private int lexemeStartPosition;

    private char lastLineEndChar;

    /**
     * Position of the last returned token.
     */
    private int lastTokenEndPosition;

    /**
     * Lexeme of the last token returned.
     */
    private String lastLexeme;
    
    private static final String OpString = "+-/%*";    

    /**
     * Constructor.
     *
     * @param sourceCode File reference.
     * @throws IOException if an error occurs during bufferedReader readline.
     */
    TokenGenerator(File sourceCode) throws IOException {
        try {
            FileReader fileReader = new FileReader(sourceCode);
            lineNumberReader = new LineNumberReader(fileReader);

            assert lineNumberReader != null;
            currentLine = lineNumberReader.readLine();
            currentLinePosition = 0;
            lastTokenEndPosition = 0;
            currentChar = currentLine.charAt(currentLinePosition);

            if (isWhitespace(currentChar)) {
                advanceInput();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + sourceCode + "'");
        } catch (NullPointerException e) {
            currentChar = '$';
        }
    }

    /**
     * @return next Token in the input.
     * @throws IOException if an error occurs during advanceInput().
     */
    public Token getNextToken() throws IOException {
    	
    	Token token = null;
        
        while(isWhitespace(currentChar)){
            advanceInput();
        }
        lexemeStartPosition = currentLinePosition;

        if (isLetter(currentChar) || currentChar == '_') {
            advanceInput();
            while (isLetterOrDigit(currentChar) || currentChar == '_')
                advanceInput();
            
            token = new Token(TokenType.IDENTIFIER);
        } else if (isDigit(currentChar)) {
            advanceInput();
            while (isDigit(currentChar)) {
                advanceInput();
            }
            
            token = new Token(TokenType.INTEGER_LITERAL);
        } else if (currentChar == '<') {
            advanceInput();
            if (currentChar == '=') {
                advanceInput();
                
                token = new Token(TokenType.REL_OP, TokenType.LESS_OR_EQUAL);
            }
            else{
                token = new Token(TokenType.REL_OP, TokenType.LESS_THAN);
            }
            
        } else if (currentChar == '>') {
            advanceInput();
            if (currentChar == '=') {
                advanceInput();
                
                token = new Token(TokenType.REL_OP, TokenType.GREATER_OR_EQUAL);
            }
            else {
                token = new Token(TokenType.REL_OP, TokenType.GREATER_THAN);
            }
        } else if (currentChar == '=') {
            advanceInput();
            if (currentChar == '=') {
                advanceInput();
                
                token = new Token(TokenType.REL_OP, TokenType.EQUAL);
            } else {
                token = new Token(TokenType.ATTRIB);
            }
        } else if (currentChar == '!') {
            advanceInput();
            if (currentChar == '=') {
                advanceInput();
                
                token = new Token(TokenType.NOT_EQUAL);
            } else {
                LexicalError.expectedChar('=', lineNumberReader.getLineNumber(), currentLinePosition);
            }
        } else if (currentChar == '+') {
            advanceInput();
            
            token = new Token(TokenType.PLUS);
        } else if (currentChar == '-') {
            advanceInput();
            
            token = new Token(TokenType.MINUS);
        } else if (currentChar == '*') {
            advanceInput();
            
            token = new Token(TokenType.TIMES);
        } else if (currentChar == '/') {
            advanceInput();
            
            token = new Token(TokenType.DIV);
        } else if (currentChar == '%') {
            advanceInput();
            
            token = new Token(TokenType.MOD);
        } else if (currentChar == '(') {
            advanceInput();
            
            token = new Token(TokenType.LPAREN);
        } else if (currentChar == ')') {
            advanceInput();
            
            token = new Token(TokenType.RPAREN);
        } else if (currentChar == '{') {
            advanceInput();
            
            token = new Token(TokenType.LBRACE);
        } else if (currentChar == '}') {
            advanceInput();
            
            token = new Token(TokenType.RBRACE);
        } else if (currentChar == '[') {
            advanceInput();
            
            token = new Token(TokenType.LBRACKET);
        } else if (currentChar == ']') {
            advanceInput();
            
            token = new Token(TokenType.RBRACKET);
        } else if (currentChar == ';') {
            advanceInput();
            token = new Token(TokenType.SEMICOLON);
        } else if (currentChar == '.') {
            advanceInput();
            token = new Token(TokenType.DOT);
        } else if (currentChar == ',') {
            advanceInput();
            
            token = new Token(TokenType.COMMA);
        } else if (currentChar == '"') {
            advanceInput();
            
            token = new Token(TokenType.DOUBLE_QUOTATION);
        } else if (currentChar == '$') {
            token = new Token(TokenType.EOF);
        } else{
            LexicalError.unexpectedChar(currentChar, lineNumberReader.getLineNumber(), currentLinePosition);
            advanceInput();
            token = new Token(TokenType.UNDEF);
        }
        updateLexeme();
        token.setLexeme(lastLexeme);
        return token;
    }

    /**
     * Update lexeme according to the last valid token position.
     */
    private void updateLexeme() {
        if(currentLinePosition != 0){
            lastTokenEndPosition = currentLinePosition;
            lastLexeme = currentLine.substring(lexemeStartPosition, lastTokenEndPosition);
        }
        else
            lastLexeme = "" + lastLineEndChar;

        
    }

    /**
     * Advance on input incrementing the line position and updating the current
     * char.
     * 
     * @throws IOException if an error occurs during bufferedReader readline.
     */
    private void advanceInput() throws IOException {
        if (currentLinePosition + 1 != currentLine.length()) {
            currentLinePosition++;
        } else {
            lastLineEndChar = currentChar;
            currentLine = lineNumberReader.readLine();
            currentLinePosition = 0;
        }
        currentChar = currentLine.charAt(currentLinePosition);

    }

    /**
     * @param expectedOp char to evaluate.
     * @return true if the expectedOp is an operator..
     */
    private boolean isOperator(char expectedOp) {
        return OpString.contains("" + expectedOp);
    }
}

