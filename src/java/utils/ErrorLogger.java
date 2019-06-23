package utils;

public class ErrorLogger {
    /**
     * Indicates the error state.
     */
    private static boolean errorState = false;

    /**
     * Error log message.
     */
    private static String errorLog = "";

    public static String getErrorLog() {
        return errorLog;
    }

    /**
     * Unexpected char error.
     *
     * @param unexpectedChar char to display why is an error.
     */
    public static void unexpectedChar(char unexpectedChar, int line, int position) {
        errorLog = errorLog + ("Unexpected char found: '" + unexpectedChar + "'in line: "+ line + ":" + position + "\n");
        setErrorState(true);
    }

    public static void expectedChar(char expectedChar, int line, int position) {
        errorLog = errorLog + ("expected char missing: '" + expectedChar + "'in line: "+ line + ":" + position + "\n");
        setErrorState(true);
    }

    /**
     * Indicates the error state.
     */
    private static void setErrorState(boolean errorState) {
        ErrorLogger.errorState = errorState;
    }

    /**
     * Log error message.
     */
    public static void log(String message, int line, int position ){
        errorLog = errorLog + ( "In line: "+ line + ":" + position +" ::\t" + message + "\n");
        setErrorState(true);
    }
    /**
     * Log error message.
     */
    public static void log(String message){
        errorLog = errorLog + (message + "\n");
        setErrorState(true);
    }

    public static void eraseLog(){
        setErrorState(false);
        errorLog = "";
    }

    public static void computeErrorLog() {
        if (errorState) {
            System.out.println(errorLog);
        }else
            System.out.println("Build Successful\n");
    }
}