import java.io.FileInputStream;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.stream.Stream;

/**
 * Class to Preprocess files.
 *
 * @author Leonardo-Rocha
 */
class Preprocessor {

    /**
     * PreProcess file removing comments.
     *
     * @param lineNumberReader file reference.
     */
    static void preProcess(LineNumberReader lineNumberReader) throws IOException {
        //TODO complete method.
        String line;
        Stream<String> stringStream = lineNumberReader.lines();
        while ((line = lineNumberReader.readLine()) != null) {
            char[] charArray = line.toCharArray();

            for (int position = 0; position < charArray.length; position++) {
                if (isASingleLineComment(charArray, position)) {
                    System.out.println("Found a single-line comment at line " + lineNumberReader.getLineNumber());
                }

            }
            System.out.println(line);
        }
    }

    private static boolean isASingleLineComment(char[] charArray, int position) {
        if (position + 1 == charArray.length)
            return false;
        else
            return (charArray[position] == '/' && charArray[position + 1] == '/');
    }
}