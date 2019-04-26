import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Class to Preprocess files.
 *
 * @author Leonardo-Rocha
 */
class Preprocessor {
    private static final int MAX_BUFFER_SIZE = 8192;

    /**
     * Preprocessed output.
     */
    private String[] output;

    /**
     * PreProcess file removing comments.
     *
     * @param lineNumberReader file reference.
     */
    void preProcess(LineNumberReader lineNumberReader) throws IOException {
        String line;
        output = new String[MAX_BUFFER_SIZE];
        int outputLineIndex = 0;
        boolean skipLine = false;

        while ((line = lineNumberReader.readLine()) != null) {
            if (isASingleLineComment(line)) {
                System.out.println("Found a single-line comment at line " + lineNumberReader.getLineNumber());
                skipLine = true;
            }
            if (isAMultiLineComment(lineNumberReader, line)) {
                //TODO complete method.
            }

            if (!skipLine)
                output[outputLineIndex++] = line;

            skipLine = false;
        }
        output[outputLineIndex] = "EOF";
        printOutput();
    }

    /**
     * Print preprocessed code.
     */
    public void printOutput() {
        for (String line : output) {
            System.out.println(line);
            if (line.equals("EOF"))
                break;
        }
    }

    /**
     * @param line current line being preprocessed.
     * @return true if the line is a single-line comment.
     */
    private static boolean isASingleLineComment(String line) {
        return line.contains("//");
    }

    /**
     * @param line
     * @param currentLine
     * @return true if we found a multi-line comment.
     */
    private static boolean isAMultiLineComment(LineNumberReader lineNumberReader, String currentLine) {
        if(currentLine.contains("/*")){
            //try to match with another */
            String separator = "/" + "*";
            if(currentLine.contains("*/")) {
                String[] splitStrings = currentLine.split(separator);
                System.out.println(splitStrings[0]);
                System.out.println(splitStrings[1]);
            }
            else{
                //search for the matching */ in another line
                while(!(currentLine = lineNumberReader.readLine()).contains("*/")){
                    //found the line that contains the matching */
                }
            }
        }
        return  true;
    }
}