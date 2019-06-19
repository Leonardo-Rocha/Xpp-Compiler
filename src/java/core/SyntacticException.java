package core;

import javafx.util.Pair;
import utils.ErrorLogger;

public class SyntacticException extends Exception {
    public SyntacticException() {
        super();
    }

    public SyntacticException(String message) {
        super(message);
        System.out.println("SyntacticException created, message:\t"+ message);
    }

    public SyntacticException(String message, ErrorLogger errorLog, Pair<Integer, Integer> codePosition) {
        super(message);
        System.out.println("SyntacticException created, message:\t"+ message);
        errorLog.log(message, codePosition.getKey(), codePosition.getValue());
    }
}
