import java.io.File;
import java.io.FileReader;
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
     * Buffered reader containing the source code.
     */
    private LineNumberReader lineNumberReader;

    /**
     * Current line being read.
     */
    private String currentLine;

    /**
     * PreProcess file.
     *
     * @param lineNumberReader file reference.
     * @throws IOException if something goes wrong during removeCommentsAndAddEOF.
     */
    void preProcess(File directory) throws IOException {
        output = new String[MAX_BUFFER_SIZE];
        FileReader fileReader = new FileReader(directory);
        lineNumberReader = new LineNumberReader(fileReader);

        removeCommentsAndAddEOF();
        lineNumberReader.close();

        printOutput();
    }

    /**
     * Remove single-line/multi-line comments and add EOF to the end of the file.
     *
     * @throws IOException if something goes wrong during buffer line reading.
     */
    private void removeCommentsAndAddEOF() throws IOException {

        int outputLineIndex = 0;

        while ((currentLine = lineNumberReader.readLine()) != null) {
            if (isABeginMultiLineComment()) {
                outputLineIndex = handleMultiLineComments(outputLineIndex);
            } else if (isNotASingleLineComment())
                output[outputLineIndex++] = currentLine;
        }
        output[outputLineIndex] = "EOF";
    }

    /**
     * @param outputLineIndex last valid output line index.
     * @return outputLineIndex.
     * @throws IOException if something goes wrong during splitMultiLineComment.
     */
    private int handleMultiLineComments(int outputLineIndex) throws IOException {
        String[] validCode = splitMultiLineComment();
        for (String line : validCode) {
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
     * @return true if the current line is NOT a single-line comment.
     */
    private boolean isNotASingleLineComment() {
        return currentLine != null && !currentLine.contains("//");
    }

    /**
     * @return true if the current line begins a Multi-line comment.
     */
    private boolean isABeginMultiLineComment() {
        return currentLine != null && currentLine.contains("/*");
    }

    /**
     * @return true if the current line NOT ends a Multi-line comment
     */
    private boolean isNotAEndMultiLineComment() {
        return currentLine != null && !currentLine.contains("*/");
    }

    /**
     * @return processed String[] without Multi-line comment.
     * @throws IOException if something goes wrong during buffer line reading or string splitting..
     */
    private String[] splitMultiLineComment() throws IOException {
        int linesDistance = 0;
        String[] output = new String[2];

        String[] splitStrings = currentLine.split("/\\*");

        output[0] = getValidStatementAtPosition(splitStrings, 0);
        linesDistance = findEndMultiLineComment(linesDistance);

        if (linesDistance == 0) {
            splitStrings = currentLine.split("\\*/");
            output[0] += getValidStatementAtPosition(splitStrings, 1);
        } else {
            splitStrings = currentLine.split("\\*/");
            output[1] = getValidStatementAtPosition(splitStrings, 1);
        }

        return output;
    }

    /**
     * @param linesDistance Distance between the initial read line and the line where the end Multi-line is found.
     * @return linesDistance after the search.
     * @throws IOException
     */
    private int findEndMultiLineComment(int linesDistance) throws IOException {
        while (isNotAEndMultiLineComment()) {
            currentLine = lineNumberReader.readLine();
            linesDistance++;
        }
        return linesDistance;
    }

    private static String getValidStatementAtPosition(String[] splitStrings, int position) {
        String validStatement;

        if (splitStrings.length > position)
            validStatement = splitStrings[position];
        else
            validStatement = "";

        return validStatement;
    }

    /**
     * Get the preprocessed output.
     *
     * @return output.
     */
    public String[] getOutput() {
        return output;
    }
}
