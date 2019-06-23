package core;

import utils.ErrorLogger;

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

    /**
     * Start position of the current lexeme.
     */
    private int lexemeStartPosition;

    /**
     * Last char of the last line for error handling purposes.
     */
    private char lastLineEndChar;
    /**
     * Position of the last returned token.
     */
    private int lastTokenEndPosition;
    /**
     * Lexeme of the last token returned.
     */
    private String lastLexeme;
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
            updateCurrentChar();
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + sourceCode + "'");
        } catch (NullPointerException e) {
            currentChar = '$';
        }
    }

    /**
     * @return next core.Token in the input.
     * @throws IOException if an error occurs during advanceInput().
     */
    public Token getNextToken() throws IOException {

        Token token;

        consumeWhitespaces();
        lexemeStartPosition = currentLinePosition;

        if (isIdentifier()) {
            token = new Token(TokenType.IDENTIFIER);
        } else if (isIntegerLiteral()) {
            token = new Token(TokenType.INTEGER_LITERAL);
        } else if (isOperator()) {
            token = getOperators();
        } else if (isSeparator()) {
            token = getSeparators();
        } else if (isStringLiteral()) {
            token = getStringLiteral();
        } else if (isEndOfFile()) {
            token = new Token(TokenType.EOF);
        } else {
            token = handleError();
        }
        updateAndSetLexeme(token);

        if (token.equalsTokenType(TokenType.IDENTIFIER)) {
            verifyKeywords(token);
        }

        token.setPosition(lineNumberReader.getLineNumber(), currentLinePosition);
        return token;
    }

    /**
     * Updates the last lexeme and set it to the given token.
     *
     * @param token token to set the last lexeme.
     */
    private void updateAndSetLexeme(Token token) {
        updateLexeme();
        token.setLexeme(lastLexeme);
    }

    /**
     * @throws IOException if an error occurs during advanceInput().
     */
    private void consumeWhitespaces() throws IOException {
        while (isWhitespace(currentChar)) {
            advanceInput();
        }
    }

    /**
     * @return true if the current char is an IDENTIFIER token.
     * @throws IOException if an error occurs during advanceInput().
     */
    private boolean isIdentifier() throws IOException {
        boolean ret = false;
        if (isLetter(currentChar) || currentChar == '_') {
            advanceInput();
            while (isLetterOrDigit(currentChar) || currentChar == '_') {
                advanceInput();
            }
            ret = true;
        }
        return ret;
    }

    /**
     * @return true if the current char is an INTEGER_LITERAL token.
     * @throws IOException if an error occurs during advanceInput().
     */
    private boolean isIntegerLiteral() throws IOException {
        boolean ret = false;
        if (isDigit(currentChar)) {
            advanceInput();
            while (isDigit(currentChar)) {
                advanceInput();
            }
            ret = true;
        }
        return ret;
    }

    /**
     * @return true if the current char is a operator.
     */
    private boolean isOperator() {
        if (currentChar == '<') return true;
        if (currentChar == '>') return true;
        if (currentChar == '=') return true;
        if (currentChar == '!') return true;
        if (currentChar == '+') return true;
        if (currentChar == '-') return true;
        if (currentChar == '*') return true;
        if (currentChar == '/') return true;
        if (currentChar == '%') return true;
        return false;
    }

    /**
     * @return true if the current char is a separator.
     */
    private boolean isSeparator() {
        if (currentChar == '(') return true;
        if (currentChar == ')') return true;
        if (currentChar == '{') return true;
        if (currentChar == '}') return true;
        if (currentChar == '[') return true;
        if (currentChar == ']') return true;
        if (currentChar == ';') return true;
        if (currentChar == '.') return true;
        if (currentChar == ',') return true;
        return false;
    }

    /**
     * @return true if the currentChar marks the start of a String Literal.
     */
    private boolean isStringLiteral() {
        return currentChar == '"';
    }

    /**
     * @return true if the currentChar is a End of File.
     */
    private boolean isEndOfFile() {
        return currentChar == '$';
    }

    /**
     * Check if the current char is a operator and return it.
     *
     * @return A operator token.
     * @throws IOException if an error occurs during advanceInput().
     */
    private Token getOperators() throws IOException {
        Token token = null;
        if (currentChar == '<') {
            token = getLesserRelop();
        } else if (currentChar == '>') {
            token = getGreaterRelop();
        } else if (currentChar == '=') {
            token = getEqualOrAttribution();
        } else if (currentChar == '!') {
            token = getNotEqual();
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
        }
        return token;
    }

    /**
     * Check if the token is a LESS_OR_EQUAL or a LESS_THAN and return it.
     *
     * @return the correct token.
     * @throws IOException if an error occurs during advanceInput().
     */
    private Token getLesserRelop() throws IOException {
        Token token;
        advanceInput();
        if (currentChar == '=') {
            advanceInput();
            token = new Token(TokenType.REL_OP, TokenType.LESS_OR_EQUAL);
        } else {
            token = new Token(TokenType.REL_OP, TokenType.LESS_THAN);
        }
        return token;
    }

    /**
     * Check if the token is a GREATER_OR_EQUAL or a GREATER_THAN and return it.
     *
     * @return the correct token.
     * @throws IOException if an error occurs during advanceInput().
     */
    private Token getGreaterRelop() throws IOException {
        Token token;
        advanceInput();
        if (currentChar == '=') {
            advanceInput();
            token = new Token(TokenType.REL_OP, TokenType.GREATER_OR_EQUAL);
        } else {
            token = new Token(TokenType.REL_OP, TokenType.GREATER_THAN);
        }
        return token;
    }

    /**
     * Check if the token is an EQUAL or an ATTRIB and return it.
     *
     * @return the correct token.
     * @throws IOException if an error occurs during advanceInput().
     */
    private Token getEqualOrAttribution() throws IOException {
        Token token;
        advanceInput();
        if (currentChar == '=') {
            advanceInput();
            token = new Token(TokenType.REL_OP, TokenType.EQUAL);
        } else {
            token = new Token(TokenType.ATTRIB);
        }
        return token;
    }

    /**
     * Check if the token is a NOT_EQUAL return it.
     *
     * @return a NOT_EQUAL token if true and UNDEF otherwise.
     * @throws IOException if an error occurs during advanceInput().
     */
    private Token getNotEqual() throws IOException {
        Token token;
        advanceInput();
        if (currentChar == '=') {
            advanceInput();
            token = new Token(TokenType.REL_OP, TokenType.NOT_EQUAL);
        } else {
            token = new Token(TokenType.UNDEF);
            ErrorLogger.expectedChar('=', lineNumberReader.getLineNumber(), currentLinePosition);
        }
        return token;
    }

    /**
     * Check if the current token is a separator and return it.
     *
     * @return a separator Token.
     * @throws IOException if an error occurs during advanceInput().
     */
    private Token getSeparators() throws IOException {
        Token token = null;
        if (currentChar == '(') {
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
        }
        return token;
    }

    /**
     * Consumes the input and find the matching " string literal delimiter.
     *
     * @return a STRING_LITERAL token.
     * @throws IOException if an error occurs during advanceInput().
     */
    private Token getStringLiteral() throws IOException {
        advanceInput();
        while (currentChar != '"' && currentChar != '$') {
            advanceInput();
        }
        if (currentChar == '"'){
            advanceInput();
        }else {
            ErrorLogger.log("String missing end '\"'.", lineNumberReader.getLineNumber(), currentLinePosition);
        }
        return new Token(TokenType.STRING_LITERAL);
    }

    /**
     * Handle the error and return a special token.
     *
     * @return an undefined token.
     * @throws IOException if an error occurs during advanceInput.
     */
    private Token handleError() throws IOException {
        Token token;
        ErrorLogger.unexpectedChar(currentChar, lineNumberReader.getLineNumber(), currentLinePosition);
        advanceInput();
        token = new Token(TokenType.UNDEF);
        return token;
    }

    /**
     * Update lexeme according to the last valid token position.
     */
    private void updateLexeme() {
        if (currentLinePosition != 0) {
            lastTokenEndPosition = currentLinePosition;
            lastLexeme = currentLine.substring(lexemeStartPosition, lastTokenEndPosition);
        } else
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
        updateCurrentChar();
    }

    /**
     * Update the char being analyzed by the token generator.
     */
    private void updateCurrentChar() {
        if (currentLine != null && currentLine.isEmpty()) {
            currentChar = ' ';
        } else {
            currentChar = currentLine.charAt(currentLinePosition);
        }
    }

    /**
     * Verifies if the identifier is a reserved keyword or not.
     *
     * @param identifier Token which represents an identifier in the language.
     */
    private void verifyKeywords(Token identifier) {
        String lexeme = identifier.getLexeme();
        switch (lexeme) {
            case "class":
                identifier.setAttribute(TokenType.CLASS);
                break;
            case "extends":
                identifier.setAttribute(TokenType.EXTENDS);
                break;
            case "int":
                identifier.setAttribute(TokenType.INT);
                break;
            case "string":
                identifier.setAttribute(TokenType.STRING);
                break;
            case "break":
                identifier.setAttribute(TokenType.BREAK);
                break;
            case "print":
                identifier.setAttribute(TokenType.PRINT);
                break;
            case "read":
                identifier.setAttribute(TokenType.READ);
                break;
            case "return":
                identifier.setAttribute(TokenType.RETURN);
                break;
            case "if":
                identifier.setAttribute(TokenType.IF);
                break;
            case "else":
                identifier.setAttribute(TokenType.ELSE);
                break;
            case "for":
                identifier.setAttribute(TokenType.FOR);
                break;
            case "new":
                identifier.setAttribute(TokenType.NEW);
                break;
            case "constructor":
                identifier.setAttribute(TokenType.CONSTRUCTOR);
                break;
            case "super":
                identifier.setAttribute(TokenType.SUPER);
                break;
            default:
                identifier.setAttribute(TokenType.IDENTIFIER);
        }
    }
}