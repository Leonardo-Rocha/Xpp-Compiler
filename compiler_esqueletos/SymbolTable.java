/*  
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;

import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author bianca
 */
public class SymbolTable<T extends STEntry> implements Iterable<T>
{
    SymbolTable<T> parent;
    TreeMap<String, T> symbols;

    SymbolTable()
    {
        symbols = new TreeMap<String, T>();
    }
    
    SymbolTable(SymbolTable<T> p)
    {
        symbols = new TreeMap<String, T>();
        
        parent = p;
    }

    public boolean add(T t)
    {
        //System.out.print(t.lexeme);
        
        if (symbols.containsKey(t.lexeme))
                return false;
        symbols.put(t.lexeme, t);
        return true;
    }

    public boolean remove(String name)
    {
        return symbols.remove(name) != null;
    }

    public void clear()
    {
        symbols.clear();
    }

    public boolean isEmpty()
    {
        return symbols.isEmpty();
    }

    public T get(String name)
    {
        T s;
        SymbolTable<T> table = this;

        do
        {
            s = table.symbols.get(name);
        } while (s == null && (table = table.parent) != null);
        
        return s;
    }

    public Iterator<T> iterator()
    {
        return symbols.values().iterator();
    } 
}
    