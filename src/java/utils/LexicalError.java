package utils;

public class LexicalError {
    /**
     * Indicates the error state.
     */
    private static boolean errorState = false;

    /**
     * Error log message.
     */
    private static String errorLog = "";

    /**
     * Unexpected char error.
     *
     * @param unexpectedChar char to display why is an error.
     */
    public static void unexpectedChar(char unexpectedChar, int line, int position) {
        errorLog = errorLog + ("Unexpected char found: '" + unexpectedChar + "'in line: "+ line + ":" + position + "\n");
        errorState = true;
    }

    public static void expectedChar(char expectedChar, int line, int position) {
        errorLog = errorLog + ("expected char missing: '" + expectedChar + "'in line: "+ line + ":" + position + "\n");
        errorState = true;
    }

    /**
     * Log error message.
     */
    public static void computeErrorLog() {
        if (errorState) {
            System.out.println(errorLog);
        }
    }
}