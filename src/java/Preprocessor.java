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
            if (isASinglelineComment(line)) {
                System.out.println("Found a single-line comment at line " + lineNumberReader.getLineNumber());
                skipLine = true;
            }
            if (isAMultilineComment(lineNumberReader)) {
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
    private static boolean isASinglelineComment(String line) {
        return line.contains("//");
    }

    /**
     * @param line
     * @return true if we found a multi-line comment.
     */
    private static boolean isAMultilineComment(LineNumberReader lineNumberReader) {
        //TODO complete method.
        return  true;
    }
}