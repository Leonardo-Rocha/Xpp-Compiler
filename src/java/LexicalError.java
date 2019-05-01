class LexicalError {
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
    public static void unexpectedChar(char unexpectedChar) {
        errorLog = errorLog + ("Unexpected char found:" +
                unexpectedChar + "\n");
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