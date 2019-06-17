package core;

import javafx.util.Pair;

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
     * Pair containing the code Coordinates of the Token.
     */
    private Pair<Integer,Integer> codePosition;
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
    @Override
    public boolean equals(Object obj) {
        Token token = (Token) obj;
        return this.getTokenType() == token.getTokenType();
    }

    /**
     * Compare token types.
     * @param tokentype
     * @return true if the type of this token equals the given token type.
     */
    public boolean equalsTokenType(TokenType tokentype){
        return this.getTokenType() == tokentype;
    }
    
    /**
     * Print token informations for debuggin purposes.
     */
    public void showCase() {
        System.out.print("lexeme:" + this.getLexeme());
        System.out.print(", core.TokenType: " + getTokenType());
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
    
    /**
     * Set the value of the field attribute.
     * @param attribute value to set.
     */
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

    public boolean compareAttributes(TokenType type){
	    return this.attribute == type;
    }

    boolean isTypeOfAnVariable(){
        return this.equalsTokenType(TokenType.IDENTIFIER) &&
                (this.compareAttributes(TokenType.UNDEF) || this.compareAttributes(TokenType.INT)
                        || this.compareAttributes(TokenType.STRING));
    }

    boolean isSignal(){
        return this.equalsTokenType(TokenType.MINUS) || this.equalsTokenType(TokenType.PLUS);
    }

    boolean isLiteral(){
        return this.equalsTokenType(TokenType.STRING_LITERAL) || this.equalsTokenType(TokenType.INTEGER_LITERAL);
    }

    boolean isStatementStart(){
        return this.equalsTokenType(TokenType.IDENTIFIER) || this.equalsTokenType(TokenType.SEMICOLON);
    }

    boolean isPreferentialOperator() {
        return this.equalsTokenType(TokenType.DIV) || this.equalsTokenType(TokenType.TIMES)
                || this.equalsTokenType(TokenType.MOD);
    }

    public void setPosition(Integer line, Integer column){
	    codePosition = new Pair<>(line, column);
    }

    /**
     * @return  the Pair containing the code Coordinates of the Token.
     */
    public Pair<Integer, Integer> getCodePosition() {
        return codePosition;
    }
}
