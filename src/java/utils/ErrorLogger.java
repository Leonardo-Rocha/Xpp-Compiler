package utils;

public class ErrorLogger {
    /**
     * Indicates the error state.
     */
    private boolean errorState = false;

    /**
     * Error log message.
     */
    private String errorLog = "";

    /**
     * Unexpected char error.
     *
     * @param unexpectedChar char to display why is an error.
     */
    public void unexpectedChar(char unexpectedChar, int line, int position) {
        errorLog = errorLog + ("Unexpected char found: '" + unexpectedChar + "'in line: "+ line + ":" + position + "\n");
        setErrorState(true);
    }

    public void expectedChar(char expectedChar, int line, int position) {
        errorLog = errorLog + ("expected char missing: '" + expectedChar + "'in line: "+ line + ":" + position + "\n");
        setErrorState(true);
    }

    /**
     * Indicates the error state.
     */
    private void setErrorState(boolean errorState) {
        this.errorState = errorState;
    }

    /**
     * Log error message.
     */
    public void log(String message, int line, int position ){
        errorLog = errorLog + ( "In line: "+ line + ":" + position +" ::\t" + message + "\n");
        setErrorState(true);
    }
    /**
     * Log error message.
     */
    public void log(String message){
        errorLog = errorLog + (message + "\n");
        setErrorState(true);
    }

    public void computeErrorLog() {
        if (errorState) {
            System.out.println(errorLog);
        }else
            System.out.println("Build Successful\n");
    }
}