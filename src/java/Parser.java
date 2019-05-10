import java.io.File;
import java.io.IOException;

public class Parser {
    
    private TokenGenerator tokenGenerator;
    
    private Token currentToken;

    private void advanceToken() throws IOException{
        currentToken = this.tokenGenerator.getNextToken();
    }

    public Parser(File sourceCode) throws IOException {
        tokenGenerator = new TokenGenerator(sourceCode);
        advanceToken();
    }

    private boolean isTypeOfAnVariable(Token token){
        return token.equalsTokenType(TokenType.IDENTIFIER) &&
                (token.compareAttributes(TokenType.UNDEF) || token.compareAttributes(TokenType.INTEGER_LITERAL));
    }

    private boolean isSignal(Token token){
        return token.equalsTokenType(TokenType.MINUS) || token.equalsTokenType(TokenType.PLUS);
    }

    private boolean isLiteral(){
        //TODO implement changes on String token detection to be able to detect Literals.
        return true;
    }

    private boolean isStatementStart(Token token){
        return token.equalsTokenType(TokenType.IDENTIFIER) || token.equalsTokenType(TokenType.SEMICOLON);
    }

    private boolean isPreferentialOperator() {
        return currentToken.equalsTokenType(TokenType.DIV) || currentToken.equalsTokenType(TokenType.TIMES)
                || currentToken.equalsTokenType(TokenType.MOD);
    }

    private void match(TokenType type) throws IOException {
        if(currentToken.compareAttributes(type))
            advanceToken();
        else
            syntacticError();
    }

    private void syntacticError(){}

    public void program() throws IOException {
        if(currentToken.compareAttributes(TokenType.CLASS)){
            classList();
        }
    }

    private void classList() throws IOException {
        classDecl();
        classListLinha();
    }
    
    private void classListLinha() throws IOException {
        if(currentToken.compareAttributes(TokenType.CLASS)){
            classList();
        }
    }

    private void classDecl() throws IOException {
        if(currentToken.compareAttributes(TokenType.CLASS)){
            advanceToken();
            match(TokenType.IDENTIFIER);
            classDeclLinha();
        }else{
            syntacticError();
        }
    }

    private  void classDeclLinha() throws IOException {
        if (currentToken.compareAttributes(TokenType.EXTENDS)){
            advanceToken();
            match(TokenType.IDENTIFIER);
        }
        classBody();
    }

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

    private void varDeclListOpt() throws IOException {
        if (isTypeOfAnVariable(currentToken))
            varDeclList();
    }

    private void varDeclList() throws IOException {
        varDecl();
        if (isTypeOfAnVariable(currentToken)){
            varDeclList();
        }
    }

    private void varDecl() throws IOException {
        this.type();
        varDeclLinha();
    }

    private void varDeclLinha() throws IOException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)) {
            advanceToken();
            match(TokenType.RBRACKET);
        }
        match(TokenType.IDENTIFIER);
        varDeclOpt();
    }

    private void varDeclOpt() throws IOException {
        if (currentToken.equalsTokenType(TokenType.COMMA)){
            advanceToken();
            match(TokenType.IDENTIFIER);
            varDeclOpt();
        }
    }

    private void type() throws IOException {
        if (isTypeOfAnVariable(currentToken))
            advanceToken();
        else
            syntacticError();
    }

    private void constructDeclListOpt() throws IOException {
        if (currentToken.compareAttributes(TokenType.CONSTRUCTOR))
            constructDeclList();
    }

    private void  constructDeclList() throws IOException {
        constructDecl();
        if (currentToken.compareAttributes(TokenType.CONSTRUCTOR))
            constructDeclList();
    }

    private void constructDecl() throws IOException {
        match(TokenType.CONSTRUCTOR);
        methodBody();
    }

    private void methodDeclListOpt() throws IOException {
        if (isTypeOfAnVariable(currentToken))
            methodDeclList();
    }

    private void methodDeclList() throws IOException {
        methodDecl();
        if (isTypeOfAnVariable(currentToken)){
            methodDeclList();
        }
    }

    private void methodDecl() throws IOException {
        type();
        methodDeclLinha();
    }

    private void methodDeclLinha() throws IOException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)) {
            advanceToken();
            match(TokenType.RBRACKET);
        }
        match(TokenType.IDENTIFIER);
        methodBody();
    }

    private void methodBody() throws IOException {
        match(TokenType.LPAREN);
        paramListOpt();
        match(TokenType.RPAREN);
        match(TokenType.LBRACE);
        statementsOpt();
        match(TokenType.RBRACE);
    }

    private void paramListOpt() throws IOException {
        if (isTypeOfAnVariable(currentToken)){
            paramList();
        }
    }
    private void paramList() throws IOException {
        param();
        if (isTypeOfAnVariable(currentToken)){
            match(TokenType.COMMA);
            paramList();
        }
    }

    private void param() throws IOException {
        type();
        paramLinha();
    }

    private void paramLinha() throws IOException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)) {
            advanceToken();
            match(TokenType.RBRACKET);
        }
        match(TokenType.IDENTIFIER);
    }

    private void statementsOpt() throws IOException {
        if(isStatementStart(currentToken)){
            statements();
        }
    }

    private void statements() throws IOException {
        statement();
        if (isStatementStart(currentToken)){
            statements();
        }
    }

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

    private void atribStat() throws IOException {
        lValue();
        atribStatLinha();
    }

    private void lValue() throws IOException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)){
            advanceToken();
            expression();
            match(TokenType.RBRACKET);
        }
        lValueComp();
    }

    private void atribStatLinha() throws IOException {
        match(TokenType.ATTRIB);
        if (currentToken.compareAttributes(TokenType.NEW)||isTypeOfAnVariable(currentToken)){
            allocExpression();
        }else if(isSignal(currentToken)){
            expression();
        }
    }

    private void printStat() throws IOException {
        match(TokenType.PRINT);
        expression();
    }

    private void readStat() throws IOException {
        match(TokenType.READ);
        match(TokenType.IDENTIFIER);
        lValue();
    }

    private void returnStat() throws IOException {
        match(TokenType.RETURN);
        expression();
    }

    private void superStat() throws IOException {
        match(TokenType.SUPER);
        match(TokenType.LPAREN);
        argListOpt();
        match(TokenType.RPAREN);
    }

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

    private void ifStatLinha() throws IOException {
        if (currentToken.compareAttributes(TokenType.ELSE)){
            advanceToken();
            match(TokenType.LBRACE);
            statements();
            match(TokenType.RBRACE);
        }
    }

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

    private void atribStatOpt() throws IOException {
        if (currentToken.equalsTokenType(TokenType.IDENTIFIER)){
            advanceToken();
            atribStat();
        }
    }

    private void expressionOpt() throws IOException {
        if (isSignal(currentToken)){
            expression();
        }
    }

    private void lValueComp() throws IOException {
        if (currentToken.equalsTokenType(TokenType.DOT)){
            advanceToken();
            match(TokenType.IDENTIFIER);
            lValueCompLinha();
        }
    }

    private void lValueCompLinha() throws IOException {
        if (currentToken.equalsTokenType(TokenType.LBRACKET)){
            advanceToken();
            expression();
            match(TokenType.RBRACKET);
        }
        lValueComp();
    }

    private void expression() throws IOException {
        numExpression();
        expressionLinha();
    }

    private void expressionLinha() throws IOException {
        if (currentToken.equalsTokenType(TokenType.REL_OP)){
            advanceToken();
            numExpression();
        }
    }

    private void allocExpression() throws IOException {
        if(currentToken.compareAttributes(TokenType.NEW)) {
            match(TokenType.IDENTIFIER);
            match(TokenType.LPAREN);
            argListOpt();
            match(TokenType.RPAREN);
        }else if (isTypeOfAnVariable(currentToken)){
            type();
            match(TokenType.LBRACKET);
            expression();
            match(TokenType.RBRACKET);
        }
    }

    private void numExpression() throws IOException {
        term();
        numExpressionLinha();
    }

    private void  numExpressionLinha() throws IOException {
        if (isSignal(currentToken)){
            advanceToken();
            term();
        }
    }

    private void term() throws IOException {
        unaryExpression();
        termLinha();
    }

    private void termLinha() throws IOException {
        if (isPreferentialOperator()){
            advanceToken();
            unaryExpression();
        }
    }

    private void unaryExpression() throws IOException {
        if (isSignal(currentToken)){
            advanceToken();
            factor();
        }
    }

    private void factor() throws IOException {
        if (isLiteral()){
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

    private void argListOpt() throws IOException {
        if (isSignal(currentToken)){
            argList();
        }
    }

    private void argList() throws IOException {
        expression();
        if (currentToken.equalsTokenType(TokenType.COMMA)){
            advanceToken();
            argList();
        }
    }

}