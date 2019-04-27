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
        String currentLine;
        output = new String[MAX_BUFFER_SIZE];
        int outputLineIndex = 0;

        while ((currentLine = lineNumberReader.readLine()) != null) {
            if (isAMultiLineComment(currentLine)) {
                String[] splitMultiLineComment = splitMultiLineComment(lineNumberReader, currentLine);
                for (String line : splitMultiLineComment) {
                    if (line != null && !line.isEmpty())
                        output[outputLineIndex++] = line;
                }
            } else if (isNotASingleLineComment(currentLine))
                output[outputLineIndex++] = currentLine;
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
     * @param currentLine last read line..
     * @return true if the line is a single-line comment.
     */
    private static boolean isNotASingleLineComment(String currentLine) {
        return !currentLine.contains("//");
    }

    /**
     * @param currentLine last read line.
     * @return true if the line marks a Multi-line comment.
     */
    private static boolean isAMultiLineComment(String currentLine) {
        return currentLine.contains("/*");
    }

    private static String[] splitMultiLineComment(LineNumberReader lineNumberReader, String currentLine) throws IOException {
        int linesDistance = 0;
        String[] output = new String[2];

        String[] splitStrings = currentLine.split("/\\*");
        if(splitStrings.length > 0)
            output[0] = splitStrings[0];
        else
            output[0] = "";
        //search for the matching */
        while (!currentLine.contains("*/")) {
            currentLine = lineNumberReader.readLine();
            linesDistance++;
        }
        //found the line that contains the matching */
        if (linesDistance == 0) {
            splitStrings = splitStrings[1].split("\\*/");
            if(splitStrings.length > 1)
                output[0] += "" + splitStrings[1];
            else
                output[0] += "";
        } else {
            splitStrings = currentLine.split("\\*/");
            if (splitStrings.length > 1)
                output[1] = splitStrings[1];
            else
                output[1] = "";
        }

        return output;
    }


}
