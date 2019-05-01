/**
 * This class represents every recognized single unit(token) of the source code.
 * A token is a Pair <type, attribute> and the attribute is optional.
 *
 * @author Gabriel Chiquetto, Leonardo-Rocha
 */
class Token {
    /**
     * Abstract symbol that represents a type of lexical unit.
     */
    private final TokenType tokenType;
    /**
     * Optional value of a token.
     */
    private final TokenType attribute;
    /**
     * Sequence of characters that represents the token in the source code.
     */
    private final String lexeme;

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
    public Token(TokenType tokenType) {
        this(tokenType, TokenType.UNDEF, "");
    }

    public boolean equals(TokenType obj) {
        return this.tokenType == obj;
    }

    public void showCase() {
        System.out.println("lexeme :" + this.lexeme);
        System.out.println("TokenType : " + tokenType);
        System.out.println("attribute : " + attribute);
    }
}
