import java.io.File;
import java.io.IOException;

import static jdk.nashorn.internal.parser.TokenType.*;


public class Parser {
    /**
     * Object which reads from the preprocessed code to generate Tokens.
     */
    private TokenGenerator tokenGenerator;
    /**
     * Token currently being analysed by the Parser.
     */
    private Token currentToken;
    /**
     * Error log used to register the error found during parser execution.
     */
    private ErrorLogger errorLog;

    /**
     * Requests a new token from the token generator.
     *
     * @throws IOException for the method getNexToken.
     */
    private void advanceToken() throws IOException {
        currentToken = this.tokenGenerator.getNextToken();
    }

    /**
     * Initializes the token generator with the file to be read and generates the first Token.
     *
     * @param sourceCode preprocessed file to read from.
     * @throws IOException for advanceToken method.
     */
    public Parser(File sourceCode, ErrorLogger errorLog) throws IOException {
        this.errorLog = errorLog;
        tokenGenerator = new TokenGenerator(sourceCode);
        advanceToken();
    }

    /**
     * Verifies if the type expected matches the currentToken tokenType, or in case of IDENTIFIER, attribute.
     *
     * @param type the type being compared to.
     * @throws IOException for advanceToken.
     */
    private void _tryMatch(TokenType type) throws IOException, SyntacticException {
        if (type == TokenType.IDENTIFIER) {
            if (currentToken.compareAttributes(type))
                advanceToken();
            else
                throw new SyntacticException();
        } else if (currentToken.equalsTokenType(type))
            advanceToken();
        else
            throw new SyntacticException();
    }

    private void tryMatch(TokenType tokenType, String message) throws IOException, SyntacticException {
        try {
            _tryMatch(tokenType);
        } catch (SyntacticException e) {
            throw new SyntacticException(message, errorLog, currentToken.getCodePosition());
        }
    }

    private void tryMatch(TokenType type) throws IOException, SyntacticException {
        String messageEnd = " expected.";
        String messageMain = "";

        switch (type) {
            case SEMICOLON:
                messageMain = "';'";
                break;
            case ATTRIB:
                messageMain = "'='";
                break;
            case LPAREN:
                messageMain = "'('";
                break;
            case RPAREN:
                messageMain = "')'";
                break;
            case LBRACKET:
                messageMain = "'['";
                break;
            case RBRACKET:
                messageMain = "']'";
                break;
            case LBRACE:
                messageMain = "'{'";
                break;
            case RBRACE:
                messageMain = "'}'";
                break;
            case DOT:
                messageMain = "'.'";
                break;
        }
        tryMatch(type, messageMain + messageEnd);
    }


    /**
     * If there is a program to be read, order the analysis.
     */
    public void launchParser() throws IOException {
        if (currentToken != null) {
            try {
                program();
            } catch (SyntacticException e) {
                errorLog.log("Build Failed.");
            }
        }
    }

    /**
     * Start the analysis verifying if the program is started properly.
     *
     * @throws IOException
     */
    private void program() throws IOException, SyntacticException {
        if (currentToken.compareAttributes(TokenType.CLASS)) {
            classList();
        } else
            throw new SyntacticException();
    }

    /**
     * Represents the list of classes from which the program is made of.
     *
     * @throws IOException for classDecl.
     */
    private void classList() throws IOException, SyntacticException {
        classDecl();
        classListLinha();
    }

    /**
     * If there are still more classes to be read, analyse them.
     *
     * @throws IOException for classList.
     */
    private void classListLinha() throws IOException, SyntacticException {
        if (currentToken.compareAttributes(TokenType.CLASS)) {
            classList();
        }
    }

    /**
     * A declaration of a class, if there is one.
     *
     * @throws IOException for advanceToken.
     */
    private void classDecl() throws IOException, SyntacticException {
        if (currentToken.compareAttributes(TokenType.CLASS)) {
            advanceToken();
            tryMatch(TokenType.IDENTIFIER);
            classDeclLinha();
        } else {
            throw new SyntacticException();
        }
    }

    /**
     * Verifies if the current class declaration extends an parent class.
     *
     * @throws IOException for advanceToken.
     */
    private void classDeclLinha() throws IOException, SyntacticException {
        if (currentToken.compareAttributes(TokenType.EXTENDS)) {
            advanceToken();
            tryMatch(TokenType.IDENTIFIER, "expected parent class name");
        }
        classBody();
    }

    /**
     * Calls the structures which compose the class.
     *
     * @throws IOException for advanceToken.
     */
    private void classBody() throws IOException, SyntacticException {
        tryMatch(TokenType.LBRACE);
        advanceToken();
        varDeclListOpt();
        constructDeclListOpt();
        methodDeclListOpt();
        tryMatch(TokenType.RBRACE);


    }

    /**
     * Verifies if there are variables being declared.
     *
     * @throws IOException for varDeclList.
     */
    private void varDeclListOpt() throws IOException, SyntacticException {
        if (currentToken.isTypeOfAnVariable())
            varDeclList();
    }

    /**
     * Process the declartions.
     *
     * @throws IOException for varDecl.
     */
    private void varDeclList() throws IOException, SyntacticException {
        varDecl();
        if (currentToken.isTypeOfAnVariable()) {
            varDeclList();
        }
    }

    /**
     * Call for type verification and proceed declaration.
     *
     * @throws IOException for type.
     */
    private void varDecl() throws IOException, SyntacticException {
        type();
        varDeclLinha();
    }

    /**
     * Verifies if it is an declaration of an scalar or an vector.
     *
     * @throws IOException for advanceToken.
     */
    private void varDeclLinha() throws IOException, SyntacticException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)) {
            advanceToken();
            tryMatch(TokenType.RBRACKET);
        }
        tryMatch(TokenType.IDENTIFIER, "valid variable name expected, had: '"
                + currentToken.getLexeme() + "'.");
        varDeclOpt();
        tryMatch(TokenType.SEMICOLON);
    }

    /**
     * Verifies for multiple variable declarations of the same type.
     *
     * @throws IOException for advanceToken.
     */
    private void varDeclOpt() throws IOException, SyntacticException {
        if (currentToken.equalsTokenType(TokenType.COMMA)) {
            advanceToken();
            tryMatch(TokenType.IDENTIFIER, "valid variable name expected, had: '"
                    + currentToken.getLexeme() + "'.");
            varDeclOpt();
        }
    }

    /**
     * Verifies if the currentToken is an type.
     *
     * @throws IOException for advanceToken.
     */
    private void type() throws IOException, SyntacticException {
        if (currentToken.isTypeOfAnVariable())
            advanceToken();
        else
            throw new SyntacticException("valid type expected, had: '" + currentToken.getLexeme() + "'.");
    }

    /**
     * If there is at least one constructor, process an constructor list.
     *
     * @throws IOException for constructDeclList.
     */
    private void constructDeclListOpt() throws IOException, SyntacticException {
        if (currentToken.compareAttributes(TokenType.CONSTRUCTOR))
            constructDeclList();
    }

    /**
     * Declares all of the constructor's.
     *
     * @throws IOException for constructDecl.
     */
    private void constructDeclList() throws IOException, SyntacticException {
        constructDecl();
        if (currentToken.compareAttributes(TokenType.CONSTRUCTOR))
            constructDeclList();
    }

    /**
     * If it is an constructor, verify his method body.
     *
     * @throws IOException for match.
     */
    private void constructDecl() throws IOException, SyntacticException {
        tryMatch(TokenType.CONSTRUCTOR);
        methodBody();
    }

    /**
     * If there are methods declared, process they.
     *
     * @throws IOException for methodDeclList.
     */
    private void methodDeclListOpt() throws IOException, SyntacticException {
        if (currentToken.isTypeOfAnVariable())
            methodDeclList();
    }

    /**
     * Declares the list of methods.
     *
     * @throws IOException for methodDecl.
     */
    private void methodDeclList() throws IOException, SyntacticException {
        methodDecl();
        if (currentToken.isTypeOfAnVariable()) {
            methodDeclList();
        }
    }

    /**
     * Verify if the method declaration starts with an type, then proceed.
     *
     * @throws IOException for type.
     */
    private void methodDecl() throws IOException, SyntacticException {
        type();
        methodDeclLinha();
    }

    /**
     * Verify if the returned value is an Vector and process his methodBody.
     *
     * @throws IOException for advanceToken.
     */
    private void methodDeclLinha() throws IOException, SyntacticException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)) {
            advanceToken();
            tryMatch(TokenType.RBRACKET);
        }
        tryMatch(TokenType.IDENTIFIER, "valid method name expected, had: '"
                + currentToken.getLexeme() + "'.");
        methodBody();
    }

    /**
     * Verify if the structure of an method body is present.
     *
     * @throws IOException for match.
     */
    private void methodBody() throws IOException, SyntacticException {
        tryMatch(TokenType.LPAREN);
        paramListOpt();
        tryMatch(TokenType.RPAREN);
        tryMatch(TokenType.LBRACE);
        statementsOpt();
        tryMatch(TokenType.RBRACE);
    }

    /**
     * If there is a list of parameters, process it.
     *
     * @throws IOException for paramList.
     */
    private void paramListOpt() throws IOException, SyntacticException {
        if (currentToken.isTypeOfAnVariable()) {
            paramList();
        }
    }

    /**
     * Process each of the param's in the list.
     *
     * @throws IOException for match.
     */
    private void paramList() throws IOException, SyntacticException {
        param();
        if (currentToken.equalsTokenType(TokenType.COMMA)) {
            advanceToken();
            paramList();
        }
    }

    /**
     * Verify if the param starts with an type.
     *
     * @throws IOException for type.
     */
    private void param() throws IOException, SyntacticException {
        type();
        paramLinha();
    }

    /**
     * Verify if the param is an vector.
     *
     * @throws IOException for advanceToken.
     */
    private void paramLinha() throws IOException, SyntacticException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)) {
            advanceToken();
            tryMatch(TokenType.RBRACKET);
        }

        tryMatch(TokenType.IDENTIFIER, "valid parameter name expected, had: '"
                + currentToken.getLexeme() + "'.");
    }

    /**
     * If there is an list of Statements process it.
     *
     * @throws IOException for statements
     */
    private void statementsOpt() throws IOException, SyntacticException {
        if (currentToken.isStatementStart()) {
            statements();
        }
    }

    /**
     * Process each statement in the list.
     *
     * @throws IOException for statement.
     */
    private void statements() throws IOException, SyntacticException {
        try {
            statement();
        } catch (SyntacticException e) {
            throw new SyntacticException("statement expected.");
        }
        if (currentToken.isStatementStart()) {
            statements();
        }
    }

    /**
     * Verify which kind of statement it is.
     *
     * @throws IOException for match.
     */
    private void statement() throws IOException, SyntacticException {
        if (currentToken.compareAttributes(TokenType.PRINT)) {
            printStat();
            tryMatch(TokenType.SEMICOLON);
        } else if (currentToken.compareAttributes(TokenType.READ)) {
            readStat();
            tryMatch(TokenType.SEMICOLON);
        } else if (currentToken.compareAttributes(TokenType.RETURN)) {
            returnStat();
            tryMatch(TokenType.SEMICOLON);
        } else if (currentToken.compareAttributes(TokenType.SUPER)) {
            superStat();
            tryMatch(TokenType.SEMICOLON);
        } else if (currentToken.compareAttributes(TokenType.IF)) {
            ifStat();
        } else if (currentToken.compareAttributes(TokenType.FOR)) {
            forStat();
        } else if (currentToken.compareAttributes(TokenType.BREAK)) {
            advanceToken();
            tryMatch(TokenType.SEMICOLON);
        } else if (currentToken.equalsTokenType(TokenType.SEMICOLON)) {
            advanceToken();
        } else if (currentToken.equalsTokenType(TokenType.IDENTIFIER)) {
            if (currentToken.compareAttributes(TokenType.IDENTIFIER)) {
                advanceToken();
                if (currentToken.equalsTokenType(TokenType.DOT)) {
                    atribStat();
                    tryMatch(TokenType.SEMICOLON);
                } else if (currentToken.compareAttributes(TokenType.IDENTIFIER)) {
                    varDeclList();
                } else if (currentToken.equalsTokenType(TokenType.LBRACKET)) {
                    advanceToken();
                    if (currentToken.equalsTokenType(TokenType.RBRACKET)) {
                        tryMatch(TokenType.IDENTIFIER, "expected valid variable name, had:" +
                                currentToken.getLexeme()); // Derivação para varDecl
                        varDeclOpt();
                        tryMatch(TokenType.SEMICOLON);
                    } else {
                        expression();
                        tryMatch(TokenType.RBRACKET);
                        lValueComp();
                        atribStatLinha();
                    }
                }
            } else {
                varDeclList();
            }
        } else {
            throw new SyntacticException("Invalid statement.", errorLog, currentToken.getCodePosition());
        }
    }

    /**
     * Process an attribution statement.
     *
     * @throws IOException for lValue.
     */
    private void atribStat() throws IOException, SyntacticException {
        lValue();
        atribStatLinha();
    }

    /**
     * Represents an value. Verify if it is in an index of a vector.
     *
     * @throws IOException for advanceToken.
     */
    private void lValue() throws IOException, SyntacticException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)) {
            advanceToken();
            expression();
            tryMatch(TokenType.RBRACKET);
        }
        lValueComp();
    }

    /**
     * Verify if the attribution is from an allocation or an static expression.
     *
     * @throws IOException for match.
     */
    private void atribStatLinha() throws IOException, SyntacticException {
        tryMatch(TokenType.ATTRIB);
        if (currentToken.compareAttributes(TokenType.NEW) || currentToken.isTypeOfAnVariable()) {
            allocExpression();
        } else if (currentToken.isSignal()) {
            expression();
        }
    }

    /**
     * Statement for an print request for the system.
     *
     * @throws IOException for match.
     */
    private void printStat() throws IOException, SyntacticException {
        tryMatch(TokenType.PRINT);
        expression();
    }

    /**
     * Statement for a read request for the system.
     *
     * @throws IOException for match.
     */
    private void readStat() throws IOException, SyntacticException {
        tryMatch(TokenType.READ);
        tryMatch(TokenType.IDENTIFIER, "expected valid variable name, had:" +
                currentToken.getLexeme());
        lValue();
    }

    /**
     * Statement to return an expression to the call stack.
     *
     * @throws IOException for match.
     */
    private void returnStat() throws IOException, SyntacticException {
        tryMatch(TokenType.RETURN);
        expression();
    }

    /**
     * Reference for the parent class arguments.
     *
     * @throws IOException for match.
     */
    private void superStat() throws IOException, SyntacticException {
        tryMatch(TokenType.SUPER);
        tryMatch(TokenType.LPAREN );
        argListOpt();
        tryMatch(TokenType.RPAREN);
    }

    /**
     * If Conditional Statement structure verification.
     *
     * @throws IOException for match.
     */
    private void ifStat() throws IOException, SyntacticException {
        tryMatch(TokenType.IF);
        tryMatch(TokenType.LPAREN);
        expression();
        tryMatch(TokenType.RPAREN);
        tryMatch(TokenType.LBRACE);
        statements();
        tryMatch(TokenType.RBRACE);
        ifStatLinha();
    }

    /**
     * Verify if there is an else statement attached to the previous if statement.
     *
     * @throws IOException for match.
     */
    private void ifStatLinha() throws IOException, SyntacticException {
        if (currentToken.compareAttributes(TokenType.ELSE)) {
            advanceToken();
            tryMatch(TokenType.LBRACE);
            statements();
            tryMatch(TokenType.RBRACE);
        }
    }

    /**
     * For statement structure verification.
     *
     * @throws IOException for match.
     */
    private void forStat() throws IOException, SyntacticException {
        tryMatch(TokenType.FOR);
        tryMatch(TokenType.LPAREN);
        atribStatOpt();
        tryMatch(TokenType.SEMICOLON);
        expressionOpt();
        tryMatch(TokenType.SEMICOLON);
        atribStatOpt();
        tryMatch(TokenType.RPAREN);
        tryMatch(TokenType.LBRACE);
        statements();
        tryMatch(TokenType.RBRACE);
    }

    /**
     * If there is an attribution statement, process it.
     *
     * @throws IOException for advanceToken.
     */
    private void atribStatOpt() throws IOException, SyntacticException {
        if (currentToken.equalsTokenType(TokenType.IDENTIFIER)) {
            advanceToken();
            atribStat();
        }
    }

    /**
     * if there is an expression, process it.
     *
     * @throws IOException for expression.
     */
    private void expressionOpt() throws IOException, SyntacticException {
        if (currentToken.isSignal()) {
            expression();
        }
    }

    /**
     * Process any reference for an internal method or attribute of an object.
     *
     * @throws IOException for advanceToken.
     */
    private void lValueComp() throws IOException, SyntacticException {
        if (currentToken.equalsTokenType(TokenType.DOT)) {
            advanceToken();
            tryMatch(TokenType.IDENTIFIER, "expected valid variable name, had:" +
                    currentToken.getLexeme());
            lValueCompLinha();
        }
    }

    /**
     * Verify if it is a reference for an vector index.
     *
     * @throws IOException for advanceToken.
     */
    private void lValueCompLinha() throws IOException, SyntacticException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)) {
            advanceToken();
            expression();
            tryMatch(TokenType.RBRACKET);
        }
        lValueComp();
    }

    /**
     * Process the first numerical part of the expression.
     *
     * @throws IOException for numExpression.
     */
    private void expression() throws IOException, SyntacticException {
        numExpression();
        expressionLinha();
    }

    /**
     * if it is a relational operation, process it as such.
     *
     * @throws IOException for advanceToken.
     */
    private void expressionLinha() throws IOException, SyntacticException {
        if (currentToken.equalsTokenType(TokenType.REL_OP)) {
            advanceToken();
            numExpression();
        }
    }

    /**
     * Process the structure of allocation static or dynamic.
     *
     * @throws IOException for match.
     */
    private void allocExpression() throws IOException, SyntacticException {
        if (currentToken.compareAttributes(TokenType.NEW)) {
            tryMatch(TokenType.IDENTIFIER, "valid object name expected, had: '"
                    + currentToken.getLexeme() + "'.");
            tryMatch(TokenType.LPAREN);
            argListOpt();
            tryMatch(TokenType.RPAREN);
        } else if (currentToken.isTypeOfAnVariable()) {
            type();
            tryMatch(TokenType.LBRACKET);
            expression();
            tryMatch(TokenType.RBRACKET);
        } else
            throw new SyntacticException("Allocation expression expected.", errorLog, currentToken.getCodePosition());
    }

    /**
     * Process the first term of the expression and then proceeds.
     *
     * @throws IOException for term.
     */
    private void numExpression() throws IOException, SyntacticException {
        term();
        numExpressionLinha();
    }

    /**
     * if there is another term, process it.
     *
     * @throws IOException for advanceToken.
     */
    private void numExpressionLinha() throws IOException, SyntacticException {
        if (currentToken.isSignal()) {
            advanceToken();
            term();
        }
    }

    /**
     * Process the UnaryExpression which composes the term and then proceeds.
     *
     * @throws IOException for unaryExpression.
     */
    private void term() throws IOException, SyntacticException {
        unaryExpression();
        termLinha();
    }

    /**
     * Verify for the existence of an preferential operation.
     *
     * @throws IOException for advanceToken.
     */
    private void termLinha() throws IOException, SyntacticException {
        if (currentToken.isPreferentialOperator()) {
            advanceToken();
            unaryExpression();
        }
    }

    /**
     * Verify if the factor is signed and then process it.
     *
     * @throws IOException for advanceToken.
     */
    private void unaryExpression() throws IOException, SyntacticException {
        if (currentToken.isSignal()) {
            advanceToken();
            factor();
        } else
            throw new SyntacticException("invalid unary expression ", errorLog, currentToken.getCodePosition());
    }

    /**
     * Verify if the current token is a valid factor.
     *
     * @throws IOException for advanceToken.
     */
    private void factor() throws IOException, SyntacticException {
        if (currentToken.isLiteral()) {
            advanceToken();
        } else if (currentToken.equalsTokenType(TokenType.IDENTIFIER)) {
            advanceToken();
            lValue();
        } else if (currentToken.equalsTokenType(TokenType.LPAREN)) {
            advanceToken();
            expression();
            tryMatch(TokenType.RPAREN);
        } else
            throw new SyntacticException("not a valid factor", errorLog, currentToken.getCodePosition());
    }

    /**
     * if there is an argList process it.
     *
     * @throws IOException for argList.
     */
    private void argListOpt() throws IOException, SyntacticException {
        if (currentToken.isSignal()) {
            argList();
        }
    }

    /**
     * Process each expression from the argList.
     *
     * @throws IOException for advanceToken.
     */
    private void argList() throws IOException, SyntacticException {
        expression();
        if (currentToken.equalsTokenType(TokenType.COMMA)) {
            advanceToken();
            argList();
        }
    }


}