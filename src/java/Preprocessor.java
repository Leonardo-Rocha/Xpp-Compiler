import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Class to Preprocess files.
 *
 * @author Leonardo-Rocha, Gabriel Chiquetto.
 */
class Preprocessor {
    private static final int MAX_BUFFER_SIZE = 8192;

    /**
     * Preprocessed output.
     */
    private String[] output;

    /**
     * PreProcess file.
     *
     * @param lineNumberReader file reference.
     * @throws IOException if something goes wrong during removeCommentsAndAddEOF.
     */
    void preProcess(LineNumberReader lineNumberReader) throws IOException {
        output = new String[MAX_BUFFER_SIZE];

        removeCommentsAndAddEOF(lineNumberReader);

        printOutput();
    }

    /**
     * Remove single-line/multi-line comments and add EOF to the end of the file.
     *
     * @param lineNumberReader file reference.
     * @throws IOException if something goes wrong during buffer line reading.
     */
    private void removeCommentsAndAddEOF(LineNumberReader lineNumberReader) throws IOException {
        String currentLine;
        int outputLineIndex = 0;

        while ((currentLine = lineNumberReader.readLine()) != null) {
            if (isAMultiLineComment(currentLine)) {
                outputLineIndex = handleMultiLineComments(lineNumberReader, currentLine, outputLineIndex);
            } else if (isNotASingleLineComment(currentLine))
                output[outputLineIndex++] = currentLine;
        }
        output[outputLineIndex] = "EOF";
    }

    /**
     * @param lineNumberReader file reference.
     * @param currentLine      last read line
     * @param outputLineIndex  last valid output line index.
     * @return outputLineIndex.
     * @throws IOException if something goes wrong during splitMultiLineComment.
     */
    private int handleMultiLineComments(LineNumberReader lineNumberReader, String currentLine, int outputLineIndex)
            throws IOException {
        String[] splitMultiLineComment = splitMultiLineComment(lineNumberReader, currentLine);
        for (String line : splitMultiLineComment) {
            if (line != null && !line.isEmpty())
                output[outputLineIndex++] = line;
        }
        return outputLineIndex;
    }

    /**
     * Print preprocessed code.
     */
    public void printOutput() {
        for (String line : getOutput()) {
            System.out.println(line);
            if (line.equals("EOF"))
                break;
        }
    }

    /**
     * @param currentLine last read line.
     * @return true if the line is NOT a single-line comment.
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

    /**
     * @param lineNumberReader file reference.
     * @param currentLine      last read line.
     * @return processed String[] without Multi-line comment.
     * @throws IOException if something goes wrong during buffer line reading or string splitting..
     */
    private static String[] splitMultiLineComment(LineNumberReader lineNumberReader, String currentLine)
            throws IOException {
        int linesDistance = 0;
        String[] output = new String[2];

        String[] splitStrings = currentLine.split("/\\*");
        if (splitStrings.length > 0)
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
            if (splitStrings.length > 1)
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

    /**
     * Get the preprocessed output.
     * @return output.
     */
    public String[] getOutput() {
        return output;
    }
}
