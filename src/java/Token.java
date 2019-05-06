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
     */
    public Token(TokenType tokenType, TokenType attribute) {
        this.tokenType = tokenType;
        this.attribute = attribute;
    }

    /**
     * Initializes a token without an attribute.
     *
     * @param tokenType type of the generated token.
     */
    public Token(TokenType tokenType) {
        this(tokenType, TokenType.UNDEF);
    }

    /**
     * Override of equals method.
     * @param obj Object to evaluate.
     * @return true if they're equal.
     */
    public boolean equals(TokenType obj) {
        return this.getTokenType() == obj;
    }
    
    /**
     * Print token informations for debuggin purposes.
     */
    public void showCase() {
        System.out.print("lexeme:" + this.getLexeme());
        System.out.print(", TokenType: " + getTokenType());
        System.out.println(", Attribute: " + getAttribute() + '.');
    }
    
    /**
     * 
     * @return type of the token.
     */
	public TokenType getTokenType() {
		return tokenType;
	}
	
	/**
	 * 
	 * @return attribute of the token.
	 */
	public TokenType getAttribute() {
		return attribute;
	}
    
    public void setAttribute(TokenType attribute){
        this.attribute = attribute;
    }

	/**
	 * 
	 * @return lexeme of the token.
	 */
	public String getLexeme() {
		return lexeme;
	}
	
	/**
	 * Should only be used once.
	 * @param lexeme string to set.
	 */
	void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}
}
