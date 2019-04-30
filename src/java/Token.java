/**
 * This class represents every recognized single unit(token) of the source code.
 * A token is a Pair <type, attribute> and the attribute is optional.
 *
 * @author Gabriel Chiquetto, Leonardo-Rocha
 */
public class Token {
    /**
     * Abstract symbol that represents a type of lexical unit.
     */
    private TokenType tokenType;
    /**
     * Optional value of a token.
     */
    private TokenType attribute;
    /**
     * Sequence of characters that represents the token in the source code.
     */
    private String lexeme;

    /**
     * Initializes a token with the main fields.
     *
     * @param tokenType type of the generated token.
     * @param attribute value of the token.
     * @param lexeme    sequence of characters that represents the token in the source code.
     */
    public Token(TokenType tokenType, TokenType attribute, String lexeme) {
        this.tokenType = tokenType;
        this.attribute = attribute;
        this.lexeme = lexeme;
    }

    /**
     * Initializes a token without an attribute.
     *
     * @param tokenType type of the generated token.
     * @param lexeme    sequence of characters that represents the token in the source code.
     */
    public Token(TokenType tokenType, String lexeme) {
        this(tokenType, TokenType.UNDEF, lexeme);
    }

    /**
     * Initializes a token only with a token type.
     *
     * @param tokenType type of the generated token.
     */
    public Token(TokenType tokenType){
        this(tokenType, TokenType.UNDEF, "");
    }
}
