/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * @author bianca
 */
public class Scanner 
{
    private static String input;
    private StringCharacterIterator inputIt;
    private SymbolTable st;
    private int lineNumber;
    
    public Scanner(SymbolTable globalST, String inputFileName)
    {
        File inputFile = new File(inputFileName);       
        st = globalST;
        
        try
        {
            FileReader fr = new FileReader(inputFile);
            
            int size = (int)inputFile.length();            
            char[] buffer = new char[size];
        
            fr.read(buffer, 0, size);
            
            input = new String(buffer);
            
            inputIt = new StringCharacterIterator(input);
            
            lineNumber = 1;
        }
        catch(FileNotFoundException e)
        {
            System.err.println("Arquivo não encontrado");
        }
        catch(IOException e)
        {
            System.err.println("Erro na leitura do arquivo");
        }
    }
    
    
    public Token nextToken()
    {
        Token tok = new Token(EnumToken.UNDEF);        
        
        int begin = 0, end = 0;
        String lexema;    
        char ch = inputIt.current();
         
        while (true)
        {
            //Código que consome espaços em branco e volta para o estado inicial
            //....

            //Código que ignora comentários
			//....

            //Fim de arquivo
            if (inputIt.getIndex() >= inputIt.getEndIndex() || ch == StringCharacterIterator.DONE)
            {
                tok.name = EnumToken.EOF;
                tok.lineNumber = lineNumber;
                
                return tok;
            }

            //Início do lexema do token
            begin = inputIt.getIndex();          

            //Números
            if (Character.isDigit(inputIt.current()))
            {
                tok.name = EnumToken.NUMBER;
                
                //Consome dígitos
                while (Character.isDigit(inputIt.current()))
                    inputIt.next();

                end = inputIt.getIndex();
                lexema = new String(input.substring(begin, end));                    
                tok.attribute = EnumToken.INTEGER_LITERAL;
                tok.lexeme = lexema;
                
            }//Fim NUMBER

			//DEMAIS TOKENS
		}

   }//nextToken
}
