/**
 * Enum used to group all possible token types.
 * @author  Gabriel Chiquetto, Leonardo-Rocha
*/
public enum TokenType {
    /** Undefined - used when doesn't match any type of token. */
    UNDEF,
    /** A sequence of letters, numbers and underscores. */
    IDENTIFIER,
    /** A sequence of decimal digits. */
    INTEGER_LITERAL,
    /** Relational operator '<'. */
    LESSTHAN,
    /** Relational operator '>'. */
    GREATER_THAN,
    /** Relational operator '<='. */
    LESS_OR_EQUAL,
    /** Relational operator '>='. */
    GREATER_OR_EQUAL,
    /** Relational operator '=='. */
    EQUAL,
    /** Relational operator '!='. */
    NOT_EQUAL,
    /** Addition operator '+'. */
    PLUS,
    /** Subtraction operator '-'. */
    MINUS,
    /** Multiplication operator '*'. */
    TIMES,
    /** Division operator '/'. */
    DIV,
    /** Modulo operator '%'. */
    MOD,
    /** Attribution operator '='. */
    ATTRIB,
    /** Left parenthesis separator '('. */
    LPAREN,
    /** Right parenthesis separator ')'. */
    RPAREN,
    /** Left bracket separator '['. */
    LBRACKET,
    /** Right bracket separator ']'. */
    RBRACKET,
    /** Left brace separator '{'. */
    LBRACE,
    /** Right brace separator '}'. */
    RBRACE,
    /** End of instruction indicator ';'. */
    SEMICOLON,
    /** Floating point number or object field accessor '.'. */
    DOT,
    /** Array element separator ',' - e.g. String[2] test = {"test1", "test2"}. */
    COMMA,
    /** String literal delimitation - e.g. String test = "teste". */
    DOUBLE_QUOTATION,
    /** Cpp-style single-line comment using "//". */
    LINE_COMMENT,
    /** C-style multi-line comment start using "/*".*/
    LBLOCK_COMMENT,
    /** C-style multi-line comment end using "*/".*/
    RBLOCK_COMMENT,
    /** End of file indicator. */
    EOF
}
