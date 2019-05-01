public class LexicalError{
    private static boolean errorState = false;

    private static String errorLog = "";

    public static void UnexpectedChar(char unexpectedChar){
        errorLog = errorLog + ("Unexpected char found:" +
                unexpectedChar + "\n");
        errorState = true;
    }

    public static void ComputeErrorLog(){
        if(errorState){
            System.out.println(errorLog);
        }
    }
}