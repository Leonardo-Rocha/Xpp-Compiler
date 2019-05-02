import java.io.*;

/**
 * Class to Preprocess files.
 *
 * @author Leonardo-Rocha.
 */
class Preprocessor {
    /**
     * Definition of End Of File Marker.
     */
    private static final char EOF = '$';
    
    /**
     * Maximum number of lines in the source code.
     */
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
     * @param filePath path of the file to be preprocessed.
     * @throws IOException if something goes wrong during removeCommentsAndAddEOF.
     */
    void preProcess(File filePath) throws IOException {
        output = new String[MAX_BUFFER_SIZE];
        FileReader fileReader = new FileReader(filePath);
        lineNumberReader = new LineNumberReader(fileReader);

        removeCommentsAndAddEOF();

        lineNumberReader.close();

        printOutputAndWriteToFile(filePath);
    }

    /**
     * Remove single-line/multi-line comments and add EOF to the end of the file.
     *
     * @throws IOException if something goes wrong during buffer line reading.
     */
    private void removeCommentsAndAddEOF() throws IOException {

        int outputLineIndex = 0;

        while ((currentLine = lineNumberReader.readLine()) != null) {
            if (isBeginMultiLineComment()) {
                outputLineIndex = handleMultiLineComments(outputLineIndex);
            } else if (isNotSingleLineComment() && !currentLine.isEmpty())
                output[outputLineIndex++] = currentLine;
        }
        output[outputLineIndex] = "" + EOF;
    }

    /**
     * @param outputLineIndex last valid output line index.
     * @return outputLineIndex.
     * @throws IOException if something goes wrong during splitMultiLineComment.
     */
    private int handleMultiLineComments(int outputLineIndex) throws IOException {
        String[] validCode = splitMultiLineComment();
        for (String line : validCode) {
            if (isValidLine(line))
                output[outputLineIndex++] = line;
        }
        return outputLineIndex;
    }

    /**
     * Print preprocessed code and write in a new file with the same name + _preprocessed.txt.
     *
     * @param filePath original file path.
     */
    private void printOutputAndWriteToFile(File filePath) throws IOException {

        BufferedWriter outputWriter;
        outputWriter = new BufferedWriter(new FileWriter(filePath + "_preprocessed.txt"));

        for (String line : getOutput()) {
            System.out.println(line);
            if (isValidLine(line)) {
                outputWriter.write(line);
                outputWriter.newLine();
            }
            if (line.equals("" + EOF))
                break;
        }
        outputWriter.close();
    }

    /**
     * @param line line to be evaluated.
     * @return true if the line is valid.
     */
    private boolean isValidLine(String line) {
        return line != null && !line.isEmpty();
    }

    /**
     * @return true if the current line is NOT a single-line comment.
     */
    private boolean isNotSingleLineComment() {
        return currentLine != null && !currentLine.contains("//");
    }

    /**
     * @return true if the current line begins a Multi-line comment.
     */
    private boolean isBeginMultiLineComment() {
        return currentLine != null && currentLine.contains("/*");
    }

    /**
     * @return true if the current line NOT ends a Multi-line comment
     */
    private boolean isNotEndMultiLineComment() {
    	String EOF = "" + Preprocessor.EOF;
    	boolean NotEndMultiLineComment = currentLine != null && !currentLine.contains("*/");
        return  NotEndMultiLineComment && !currentLine.contains(EOF);
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
        
        if (currentLine == null) {
        	output[1] = "";
        } else if (linesDistance == 0) {
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
     * @throws IOException if an error occurs during buffer line reading.
     */
    private int findEndMultiLineComment(int linesDistance) throws IOException {
        while (isNotEndMultiLineComment()) {
            currentLine = lineNumberReader.readLine();
            linesDistance++;
        }
        return linesDistance;
    }

    /**
     * Check the array length and access the given position.
     *
     * @param splitStrings array of strings to be filtered.
     * @param position     array position.
     * @return validStatement string.
     */
    private static String getValidStatementAtPosition(String[] splitStrings, int position) {
        String validStatement;

        if (splitStrings != null && splitStrings.length > position)
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
    @SuppressWarnings("WeakerAccess")
    public String[] getOutput() {
        return output;
    }
}
