package core;

import javafx.util.Pair;
import utils.ErrorLogger;

public class SyntacticException extends Exception {
    public SyntacticException() {
        super();
    }

    public SyntacticException(String message) {
        super(message);
    }

    public SyntacticException(String message, Pair<Integer, Integer> codePosition) {
        super(message);
        ErrorLogger.log(message, codePosition.getKey(), codePosition.getValue());
    }
}
