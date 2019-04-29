import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static java.lang.Character.*;

/**
 * 
 */
public class TokenGenerator {
    /**
     * Scanner to parse the input.
     */
    private Scanner scanner;

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

    /**
     * Position of the last returned token.
     */
    private int lastTokenPosition;

    /**
     * Lexeme of the last token returned.
     */
    private String lastLexeme;

    /**
     * Constructor.
     *
     * @param sourceCode File reference.
     */
    TokenGenerator(File sourceCode) {
        try {
            scanner = new Scanner(sourceCode);
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + sourceCode + "'");
        } finally {
            currentLine = scanner.nextLine();
            currentLinePosition = 0;
            lastTokenPosition = 0;
            currentChar = currentLine.charAt(currentLinePosition);
        }
    }

    /**
     * @return next Token in the input.
     */
    public Token getNextToken() {

        if (isLetter(currentChar) || currentChar == '_') {
            advanceInput();
            while (isLetterOrDigit(currentChar) || currentChar == '_')
                advanceInput();

            updateLexeme();

            return new Token(TokenType.IDENTIFIER, lastLexeme);
        } else if (isDigit(currentChar)) {
            advanceInput();
            while (isDigit(currentChar))
                advanceInput();

            updateLexeme();

            return new Token(TokenType.INTEGER_LITERAL, lastLexeme);
        } else if (currentChar == '<') {
            advanceInput();
            if (currentChar == '=')
                return new Token(TokenType.REL_OP, TokenType.LESS_OR_EQUAL, "");

            return new Token(TokenType.REL_OP, TokenType.LESS_THAN, "");
        } else if (currentChar == '>') {
            advanceInput();
            if (currentChar == '=')
                return new Token(TokenType.REL_OP, TokenType.GREATER_OR_EQUAL, "");

            return new Token(TokenType.REL_OP, TokenType.GREATER_THAN, "");
        } else if (currentChar == '=') {
            advanceInput();
            if (currentChar == '=')
                return new Token(TokenType.REL_OP, TokenType.EQUAL, "");
            return new Token(TokenType.ATTRIB);
        } else if (currentChar == '!') {
            advanceInput();
            if (currentChar == '=') {
                advanceInput();
                return new Token(TokenType.NOT_EQUAL);
            }
        } else if (currentChar == '+') {
            advanceInput();
            return new Token(TokenType.PLUS);
        } else if (currentChar == '-') {
            advanceInput();
            ;
            return new Token(TokenType.MINUS);
        } else if (currentChar == '*') {
            advanceInput();
            return new Token(TokenType.TIMES);
        } else if (currentChar == '/') {
            advanceInput();
            return new Token(TokenType.DIV);
        } else if (currentChar == '%') {
            advanceInput();
            return new Token(TokenType.MOD);
        }

        return new Token(TokenType.UNDEF);
    }

    /**
     * Update lexeme according to the last valid token position.
     */
    private void updateLexeme() {
        lastTokenPosition = 0;
        if (lastLexeme != null && currentLine.contains(lastLexeme))
            lastTokenPosition = currentLine.indexOf(lastLexeme);

        lastLexeme = currentLine.substring(lastTokenPosition, currentLinePosition);
    }

    /**
     * Advance on input incrementing the line position and updating the current char.
     */
    private void advanceInput() {
        currentLinePosition++;
        currentChar = currentLine.charAt(currentLinePosition);
    }

    /**
     * @param scanner reference.
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }
}
