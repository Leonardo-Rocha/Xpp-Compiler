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

    public void match(TokenType type){
        //if(type != currentToken.getAttribute())
    }

    public void program(){
        if(currentToken.equalsTokenType(TokenType.CLASS)){
            classList();
        }
    }

    private void classList(){
        classDecl();
        classListLinha();
    }
    
    private void classListLinha(){
        if(currentToken.equalsTokenType(TokenType.CLASS)){
            classList();
        }
    }

    private void classDecl(){
        //match(CLASS);
        //match(IDENTIFIER);
        //classDeclLinha();
    }
}