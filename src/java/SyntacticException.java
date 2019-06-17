import javafx.util.Pair;

public class SyntacticException extends Exception {
    public SyntacticException() {
        super();
    }

    public SyntacticException(String message) {
        super(message);
    }

    public SyntacticException(String message, ErrorLogger errorLog, Pair<Integer, Integer> codePosition) {
        super(message);
        errorLog.log(message, codePosition.getKey(), codePosition.getValue());
    }
}
