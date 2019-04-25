/**
 * This class represents every recognized single unit(token) of the source code.
 * A token is a Pair <type, attribute> and the attribute is optional.
 */
public class Token {
    /** Abstract symbol that represents a type of lexical unit. */
    private TokenType tokenType;
    /** Optional value of a token. */
    private TokenType attribute;
    /** Sequence of characters that represents the token in the source code. */
    private String lexeme;

    /**
     * Initializes a token with the main fields.
     * @param tokenType type of the generated token.
     * @param attribute value of the token.
     * @param lexeme sequence of characacters that represents the token in the source code.
     */
    public Token(TokenType tokenType, TokenType attribute, String lexeme){
        this.tokenType = tokenType;
        this.attribute = attribute;
        this.lexeme = lexeme;
    }

    /**
     * Initializes a token without an attribute.
     * @param tokenType type of the generated token.
     * @param lexeme sequence of characacters that represents the token in the source code.
     */
    public Token(TokenType tokenType, String lexeme){
        Token(tokenType, TokenType.UNDEF, lexeme);
    }
}
