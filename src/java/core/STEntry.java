package core;

/**
 * @author facom
 */
public class STEntry 
{
    public Token token;
    public String type;
    public SymbolTable<STEntry> intern;

    public STEntry()
    {}
    
    public STEntry(Token token)
    {
        this.token = token;

    }

    public STEntry(Token token, String type)
    {
        this.token = token;
        this.type = type;

    }
    public STEntry(Token token, String type, SymbolTable<STEntry> intern)
    {
        this.token = token;
        this.type = type;
        this.intern = intern;
    }

    public String getLexeme() {
        return token.getLexeme();
    }
}
