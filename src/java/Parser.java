import java.io.File;
import java.io.IOException;

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
     * Requests a new token from the token generator.
     * @throws IOException for the method getNexToken.
     */
    private void advanceToken() throws IOException{
        currentToken = this.tokenGenerator.getNextToken();
    }

    /**
     * Initializes the token generator with the file to be read and generates the first Token.
     * @param sourceCode preprocessed file to read from.
     * @throws IOException for advanceToken method.
     */
    public Parser(File sourceCode) throws IOException {
        tokenGenerator = new TokenGenerator(sourceCode);
        advanceToken();
    }

    /**
     * Verifies if the type expected matches the currentToken tokenType, or in case of IDENTIFIER, attribute.
     * @param type the type being compared to.
     * @throws IOException for advanceToken.
     */
    private void match(TokenType type) throws IOException {
        if (type == TokenType.IDENTIFIER) {
            if (currentToken.compareAttributes(type))
                advanceToken();
            else
                syntacticError();
        }else
            if (currentToken.equalsTokenType(type))
                advanceToken();
            else
                syntacticError();
    }

    /**
     * Calls the error handler to annotate the issue.
     */
    private void syntacticError(){}

    /**
     * If there is a program to be read, order the analysis.
     */
    public void launchParser() throws IOException {
        if (currentToken != null)
            program();
    }

    /**
     * Start the analysis verifying if the program is started properly.
     * @throws IOException
     */
    private void program() throws IOException {
        if(currentToken.compareAttributes(TokenType.CLASS)){
            classList();
        }else
            syntacticError();
    }

    /**
     * Represents the list of classes from which the program is made of.
     * @throws IOException for classDecl.
     */
    private void classList() throws IOException {
        classDecl();
        classListLinha();
    }

    /**
     * If there are still more classes to be read, analyse them.
     * @throws IOException for classList.
     */
    private void classListLinha() throws IOException {
        if(currentToken.compareAttributes(TokenType.CLASS)){
            classList();
        }
    }

    /**
     * A declaration of a class, if there is one.
     * @throws IOException for advanceToken.
     */
    private void classDecl() throws IOException {
        if(currentToken.compareAttributes(TokenType.CLASS)){
            advanceToken();
            match(TokenType.IDENTIFIER);
            classDeclLinha();
        }else{
            syntacticError();
        }
    }

    /**
     * Verifies if the current class declaration extends an parent class.
     * @throws IOException for advanceToken.
     */
    private  void classDeclLinha() throws IOException {
        if (currentToken.compareAttributes(TokenType.EXTENDS)){
            advanceToken();
            match(TokenType.IDENTIFIER);
        }
        classBody();
    }

    /**
     * Calls the structures which compose the class.
     * @throws IOException for advanceToken.
     */
    private void classBody() throws IOException {
        if (currentToken.equalsTokenType(TokenType.LBRACE)){
            advanceToken();
            varDeclListOpt();
            constructDeclListOpt();
            methodDeclListOpt();
            match(TokenType.RBRACE);
        }
        else{
            syntacticError();
        }
    }

    /**
     * Verifies if there are variables being declared.
     * @throws IOException for varDeclList.
     */
    private void varDeclListOpt() throws IOException {
        if (currentToken.isTypeOfAnVariable())
            varDeclList();
    }

    /**
     * Process the declartions.
     * @throws IOException for varDecl.
     */
    private void varDeclList() throws IOException {
        varDecl();
        if (currentToken.isTypeOfAnVariable()){
            varDeclList();
        }
    }

    /**
     * Call for type verification and proceed declaration.
     * @throws IOException for type.
     */
    private void varDecl() throws IOException {
        this.type();
        varDeclLinha();
    }

    /**
     * Verifies if it is an declaration of an scalar or an vector.
     * @throws IOException for advanceToken.
     */
    private void varDeclLinha() throws IOException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)) {
            advanceToken();
            match(TokenType.RBRACKET);
        }
        match(TokenType.IDENTIFIER);
        varDeclOpt();
    }

    /**
     * Verifies for multiple variable declarations of the same type.
     * @throws IOException for advanceToken.
     */
    private void varDeclOpt() throws IOException {
        if (currentToken.equalsTokenType(TokenType.COMMA)){
            advanceToken();
            match(TokenType.IDENTIFIER);
            varDeclOpt();
        }
    }

    /**
     * Verifies if the currentToken is an type.
     * @throws IOException for advanceToken.
     */
    private void type() throws IOException {
        if (currentToken.isTypeOfAnVariable())
            advanceToken();
        else
            syntacticError();
    }

    /**
     * If there is at least one constructor, process an constructor list.
     * @throws IOException for constructDeclList.
     */
    private void constructDeclListOpt() throws IOException {
        if (currentToken.compareAttributes(TokenType.CONSTRUCTOR))
            constructDeclList();
    }

    /**
     * Declares all of the constructor's.
     * @throws IOException for constructDecl.
     */
    private void  constructDeclList() throws IOException {
        constructDecl();
        if (currentToken.compareAttributes(TokenType.CONSTRUCTOR))
            constructDeclList();
    }

    /**
     * If it is an constructor, verify his method body.
     * @throws IOException for match.
     */
    private void constructDecl() throws IOException {
        match(TokenType.CONSTRUCTOR);
        methodBody();
    }

    /**
     * If there are methods declared, process they.
     * @throws IOException for methodDeclList.
     */
    private void methodDeclListOpt() throws IOException {
        if (currentToken.isTypeOfAnVariable())
            methodDeclList();
    }

    /**
     * Declares the list of methods.
     * @throws IOException for methodDecl.
     */
    private void methodDeclList() throws IOException {
        methodDecl();
        if (currentToken.isTypeOfAnVariable()){
            methodDeclList();
        }
    }

    /**
     * Verify if the method declaration starts with an type, then proceed.
     * @throws IOException for type.
     */
    private void methodDecl() throws IOException {
        type();
        methodDeclLinha();
    }

    /**
     * Verify if the returned value is an Vector and process his methodBody.
     * @throws IOException for advanceToken.
     */
    private void methodDeclLinha() throws IOException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)) {
            advanceToken();
            match(TokenType.RBRACKET);
        }
        match(TokenType.IDENTIFIER);
        methodBody();
    }

    /**
     * Verify if the structure of an method body is present.
     * @throws IOException for match.
     */
    private void methodBody() throws IOException {
        match(TokenType.LPAREN);
        paramListOpt();
        match(TokenType.RPAREN);
        match(TokenType.LBRACE);
        statementsOpt();
        match(TokenType.RBRACE);
    }

    /**
     * If there is a list of parameters, process it.
     * @throws IOException for paramList.
     */
    private void paramListOpt() throws IOException {
        if (currentToken.isTypeOfAnVariable()){
            paramList();
        }
    }

    /**
     * Process each of the param's in the list.
     * @throws IOException for match.
     */
    private void paramList() throws IOException {
        param();
        if (currentToken.isTypeOfAnVariable()){
            match(TokenType.COMMA);
            paramList();
        }
    }

    /**
     * Verify if the param starts with an type.
     * @throws IOException for type.
     */
    private void param() throws IOException {
        type();
        paramLinha();
    }

    /**
     * Verify if the param is an vector.
     * @throws IOException for advanceToken.
     */
    private void paramLinha() throws IOException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)) {
            advanceToken();
            match(TokenType.RBRACKET);
        }
        match(TokenType.IDENTIFIER);
    }

    /**
     * If there is an list of Statements process it.
     * @throws IOException for statements
     */
    private void statementsOpt() throws IOException {
        if(currentToken.isStatementStart()){
            statements();
        }
    }

    /**
     * Process each statement in the list.
     * @throws IOException for statement.
     */
    private void statements() throws IOException {
        statement();
        if (currentToken.isStatementStart()){
            statements();
        }
    }

    /**
     * Verify which kind of statement it is.
     * @throws IOException for match.
     */
    private void statement() throws IOException {
        if (currentToken.compareAttributes(TokenType.PRINT)){
            printStat();
            match(TokenType.SEMICOLON);
        }else if(currentToken.compareAttributes(TokenType.READ)){
            readStat();
            match(TokenType.SEMICOLON);
        }else if (currentToken.compareAttributes(TokenType.RETURN)){
            returnStat();
            match(TokenType.SEMICOLON);
        }else if(currentToken.compareAttributes(TokenType.SUPER)){
            superStat();
            match(TokenType.SEMICOLON);
        }else if (currentToken.compareAttributes(TokenType.IF)){
            ifStat();
        }else if (currentToken.compareAttributes(TokenType.FOR)){
            forStat();
        }else if (currentToken.compareAttributes(TokenType.BREAK)){
            advanceToken();
            match(TokenType.SEMICOLON);
        }else if (currentToken.equalsTokenType(TokenType.SEMICOLON)){
            advanceToken();
        }else if (currentToken.equalsTokenType(TokenType.IDENTIFIER)){
            if (!currentToken.compareAttributes(TokenType.UNDEF)){
                varDeclList();
            }else{
                advanceToken();
                if(currentToken.equalsTokenType(TokenType.DOT)||currentToken.equalsTokenType(TokenType.LBRACKET)){
                    atribStat();
                    match(TokenType.SEMICOLON);
                }else{
                    varDeclList();
                }
            }
        }else{
            syntacticError();
        }
    }

    /**
     * Process an attribution statement.
     * @throws IOException for lValue.
     */
    private void atribStat() throws IOException {
        lValue();
        atribStatLinha();
    }

    /**
     * Represents an value. Verify if it is in an index of a vector.
     * @throws IOException for advanceToken.
     */
    private void lValue() throws IOException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)){
            advanceToken();
            expression();
            match(TokenType.RBRACKET);
        }
        lValueComp();
    }

    /**
     * Verify if the attribution is from an allocation or an static expression.
     * @throws IOException for match.
     */
    private void atribStatLinha() throws IOException {
        match(TokenType.ATTRIB);
        if (currentToken.compareAttributes(TokenType.NEW)||currentToken.isTypeOfAnVariable()){
            allocExpression();
        }else if(currentToken.isSignal()){
            expression();
        }
    }

    /**
     * Statement for an print request for the system.
     * @throws IOException for match.
     */
    private void printStat() throws IOException {
        match(TokenType.PRINT);
        expression();
    }

    /**
     * Statement for a read request for the system.
     * @throws IOException for match.
     */
    private void readStat() throws IOException {
        match(TokenType.READ);
        match(TokenType.IDENTIFIER);
        lValue();
    }

    /**
     * Statement to return an expression to the call stack.
     * @throws IOException for match.
     */
    private void returnStat() throws IOException {
        match(TokenType.RETURN);
        expression();
    }

    /**
     * Reference for the parent class arguments.
     * @throws IOException for match.
     */
    private void superStat() throws IOException {
        match(TokenType.SUPER);
        match(TokenType.LPAREN);
        argListOpt();
        match(TokenType.RPAREN);
    }

    /**
     * If Conditional Statement structure verification.
     * @throws IOException for match.
     */
    private void ifStat() throws IOException {
        match(TokenType.IF);
        match(TokenType.LPAREN);
        expression();
        match(TokenType.RPAREN);
        match(TokenType.LBRACE);
        statements();
        match(TokenType.RBRACE);
        ifStatLinha();
    }

    /**
     * Verify if there is an else statement attached to the previous if statement.
     * @throws IOException for match.
     */
    private void ifStatLinha() throws IOException {
        if (currentToken.compareAttributes(TokenType.ELSE)){
            advanceToken();
            match(TokenType.LBRACE);
            statements();
            match(TokenType.RBRACE);
        }
    }

    /**
     * For statement structure verification.
     * @throws IOException for match.
     */
    private void forStat() throws IOException {
        match(TokenType.FOR);
        match(TokenType.LPAREN);
        atribStatOpt();
        match(TokenType.SEMICOLON);
        expressionOpt();
        match(TokenType.SEMICOLON);
        atribStatOpt();
        match(TokenType.RPAREN);
        match(TokenType.LBRACE);
        statements();
        match(TokenType.RBRACE);
    }

    /**
     * If there is an attribution statement, process it.
     * @throws IOException for advanceToken.
     */
    private void atribStatOpt() throws IOException {
        if (currentToken.equalsTokenType(TokenType.IDENTIFIER)){
            advanceToken();
            atribStat();
        }
    }

    /**
     * if there is an expression, process it.
     * @throws IOException for expression.
     */
    private void expressionOpt() throws IOException {
        if (currentToken.isSignal()){
            expression();
        }
    }

    /**
     * Process any reference for an internal method or attribute of an object.
     * @throws IOException for advanceToken.
     */
    private void lValueComp() throws IOException {
        if (currentToken.equalsTokenType(TokenType.DOT)){
            advanceToken();
            match(TokenType.IDENTIFIER);
            lValueCompLinha();
        }
    }

    /**
     * Verify if it is a reference for an vector index.
     * @throws IOException for advanceToken.
     */
    private void lValueCompLinha() throws IOException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)){
            advanceToken();
            expression();
            match(TokenType.RBRACKET);
        }
        lValueComp();
    }

    /**
     * Process the first numerical part of the expression.
     * @throws IOException for numExpression.
     */
    private void expression() throws IOException {
        numExpression();
        expressionLinha();
    }

    /**
     * if it is a relational operation, process it as such.
     * @throws IOException for advanceToken.
     */
    private void expressionLinha() throws IOException {
        if (currentToken.equalsTokenType(TokenType.REL_OP)){
            advanceToken();
            numExpression();
        }
    }

    /**
     * Process the structure of allocation static or dynamic.
     * @throws IOException for match.
     */
    private void allocExpression() throws IOException {
        if(currentToken.compareAttributes(TokenType.NEW)) {
            match(TokenType.IDENTIFIER);
            match(TokenType.LPAREN);
            argListOpt();
            match(TokenType.RPAREN);
        }else if (currentToken.isTypeOfAnVariable()){
            type();
            match(TokenType.LBRACKET);
            expression();
            match(TokenType.RBRACKET);
        }
    }

    /**
     * Process the first term of the expression and then proceeds.
     * @throws IOException for term.
     */
    private void numExpression() throws IOException {
        term();
        numExpressionLinha();
    }

    /**
     * if there is another term, process it.
     * @throws IOException for advanceToken.
     */
    private void  numExpressionLinha() throws IOException {
        if (currentToken.isSignal()){
            advanceToken();
            term();
        }
    }

    /**
     * Process the UnaryExpression which composes the term and then proceeds.
     * @throws IOException for unaryExpression.
     */
    private void term() throws IOException {
        unaryExpression();
        termLinha();
    }

    /**
     * Verify for the existence of an preferential operation.
     * @throws IOException for advanceToken.
     */
    private void termLinha() throws IOException {
        if (currentToken.isPreferentialOperator()){
            advanceToken();
            unaryExpression();
        }
    }

    /**
     * Verify if the factor is signed and then process it.
     * @throws IOException for advanceToken.
     */
    private void unaryExpression() throws IOException {
        if (currentToken.isSignal()){
            advanceToken();
            factor();
        }
    }

    /**
     * Verify if the current token is a valid factor.
     * @throws IOException for advanceToken.
     */
    private void factor() throws IOException {
        if (currentToken.isLiteral()){
            advanceToken();
        }else if (currentToken.equalsTokenType(TokenType.IDENTIFIER)){
            advanceToken();
            lValue();
        }else if (currentToken.equalsTokenType(TokenType.LPAREN)){
            advanceToken();
            expression();
            match(TokenType.RPAREN);
        }else
            syntacticError();
    }

    /**
     * if there is an argList process it.
     * @throws IOException for argList.
     */
    private void argListOpt() throws IOException {
        if (currentToken.isSignal()){
            argList();
        }
    }

    /**
     * Process each expression from the argList.
     * @throws IOException for advanceToken.
     */
    private void argList() throws IOException {
        expression();
        if (currentToken.equalsTokenType(TokenType.COMMA)){
            advanceToken();
            argList();
        }
    }

}