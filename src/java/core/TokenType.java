package core;

/**
 * Enum used to group all possible token types.
 *
 * @author Gabriel Chiquetto, Leonardo-Rocha
 */
enum TokenType {
    /**
     * Undefined - used when doesn't match any type of token.
     */
    UNDEF,
    /**
     * A sequence of letters, numbers and underscores.
     */
    IDENTIFIER,
     /**
     * Used for class declarations.
     */
    CLASS,
    /**
     * Used for class inheritance.
     */
    EXTENDS,
    /**
     * Primitive type declaration "integer".
     */
    INT,
    /**
     * String reference declaration.
     */
    STRING,
    /**
     * Terminates execution of current scope.
     */
    BREAK,
    /**
     * Show a message in the standard output.
     */
    PRINT,
    /**
     * Read from standard input.
     */
    READ,
    /**
     * It causes program control to transfer back to the caller of the method.
     */
    RETURN,
    /**
     * References the immediate parent of a class.
     */
    SUPER,
    /**
     * Conditional statement that executes a block when the expression is true.
     */
    IF,
    /**
     * Executes when the Boolean expression of the matching "if" is false.
     */
    ELSE,
    /**
     * Iterative Loop control structure.
     */
    FOR,
    /**
     * Memory allocation for a new object.
     */
    NEW,
    /**
     * Method called when the object is allocated.
     */
    CONSTRUCTOR,
    /**
     * A sequence of decimal digits.
     */
    INTEGER_LITERAL,
    /**
     * Relational operator attribute.
     */
    REL_OP,
    /**
     * Relational operator '<'.
     */
    LESS_THAN,
    /**
     * Relational operator '>'.
     */
    GREATER_THAN,
    /**
     * Relational operator '<='.
     */
    LESS_OR_EQUAL,
    /**
     * Relational operator '>='.
     */
    GREATER_OR_EQUAL,
    /**
     * Relational operator '=='.
     */
    EQUAL,
    /**
     * Relational operator '!='.
     */
    NOT_EQUAL,
    /**
     * Addition operator '+'.
     */
    PLUS,
    /**
     * Subtraction operator '-'.
     */
    MINUS,
    /**
     * Multiplication operator '*'.
     */
    TIMES,
    /**
     * Division operator '/'.
     */
    DIV,
    /**
     * Modulo operator '%'.
     */
    MOD,
    /**
     * Attribution operator '='.
     */
    ATTRIB,
    /**
     * Left parenthesis separator '('.
     */
    LPAREN,
    /**
     * Right parenthesis separator ')'.
     */
    RPAREN,
    /**
     * Left bracket separator '['.
     */
    LBRACKET,
    /**
     * Right bracket separator ']'.
     */
    RBRACKET,
    /**
     * Left brace separator '{'.
     */
    LBRACE,
    /**
     * Right brace separator '}'.
     */
    RBRACE,
    /**
     * End of instruction indicator ';'.
     */
    SEMICOLON,
    /**
     * Floating point number or object field accessor '.'.
     */
    DOT,
    /**
     * Array element separator ',' - e.g. String[2] test = {"test1", "test2"}.
     */
    COMMA,
    /**
     * String literal delimitation - e.g. String test = "test".
     */
    STRING_LITERAL,
    /**
     * End of file indicator.
     */
    EOF
}
