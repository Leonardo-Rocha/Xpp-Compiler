/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package compiler;

/**
 *
 * @author Bianca de Almeida Dantas <bianca at facom.ufms.br>
 */
public class CompilerException extends RuntimeException
{
    private String msg;

    public CompilerException() 
    {       
        msg = "Unexpected";
    }
    
    public CompilerException(String str)
    {
        super(str);
        msg = str;
    }
    
    public String toString()
    {
        return msg;
    }
}
