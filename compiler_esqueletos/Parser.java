/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiler;

/**
 *
 * @author bianca
 */

public class Parser 
{
    private Scanner scan;
    private Token lToken;
    
    private SymbolTable globalST;
    private SymbolTable currentST;
    
   
    public Parser(String inputFile)    
    {
        //Instancia a tabela de símbolos global e a inicializa
        globalST = new SymbolTable<STEntry>();
        initSymbolTable();
     
        //Faz o ponteiro para a tabela do escopo atual apontar para a tabela global
        currentST = globalST;
        
        //Instancia o analisador léxico
        scan = new Scanner(globalST, inputFile);
    }
    
    /*
     * Método que inicia o processo de análise sintática do compilador
     */
    public void execute()
    {
        advance();
        
        try
        {
            program();
        }
        catch(CompilerException e)
        {
            System.err.println(e);
        }
    }
    
    private void advance()
    {
        lToken = scan.nextToken();
        
        //System.out.print(lToken.name + "(" + lToken.lineNumber + ")" + " " );
    }
    
    private void match(EnumToken cTokenName) throws CompilerException
    {
        if (lToken.name == cTokenName)
            advance();
        else
        {            //Erro
            throw new CompilerException("Token inesperado: " + lToken.name);
        }
    }
    
    /*
     * Método para o símbolo inicial da gramática
     */    
    private void program() throws CompilerException
    {
        if (lToken.name == EnumToken.CLASS)
            classList();               
        
    }
    
    private void classList() throws CompilerException
    {
        while (lToken.name == EnumToken.CLASS)
            classDecl();
        
    }
    
    private void classDecl() throws CompilerException
    {
        if (lToken.name == EnumToken.CLASS)
        {            
            advance();
            
            if (lToken.name == EnumToken.ID)
            {
                advance();
                
                if (lToken.name == EnumToken.EXTENDS)
                {
                    advance();
                    
                    if (lToken.name == EnumToken.ID)
                    {
                        advance();
                    }
                    else
                        throw new CompilerException("Identificador esperado");
                }
                classBody();
            }
            else
                throw new CompilerException("Identificador esperado");
        }
        else
            throw new CompilerException("Classe mal definida");
        
    }

	//DEMAIS MÉTODOS....
    
    private void initSymbolTable()
    {
        Token t;
        
        t = new Token(EnumToken.CLASS);
        globalST.add(new STEntry(t, "class", true));
        t = new Token(EnumToken.EXTENDS);
        globalST.add(new STEntry(t, "extends", true));                
        t = new Token(EnumToken.CONSTRUCTOR);

		//CONTINUA COM AS DEMAIS PALAVRAS RESERVADAS
 
    }
}
