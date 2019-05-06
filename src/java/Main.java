import java.io.*;

/**
 * Main class.
 *
 * @author Leonardo-Rocha, Gabriel Chiquetto.
 */
class Main {

    public static void main(String[] args) throws IOException {

        File filePath = openFile("Xpp-compiler/bin/test");

        filePath = openFile(preprocessFile(filePath));

        runTest(filePath);

        LexicalError.computeErrorLog();

        System.out.println("Process terminated.");
    }

    /**
     * @param path path of the file to be open.
     * @return open file reference.
     */
    private static File openFile(String path) {
        return new File(path);
    }

    /**
     * Runs a test with the TokenGenerator to visually check if it's working.
     *
     * @param source file to run the token generator.
     * @throws IOException if an error occurs during getNextToken().
     */
    private static void runTest(File source) throws IOException {
        TokenGenerator Tokenizer = new TokenGenerator(source);
        Token currentToken = Tokenizer.getNextToken();
        while (!currentToken.equals(TokenType.EOF)) {
            currentToken.showCase();
            currentToken = Tokenizer.getNextToken();
        }

    }

    /**
     * @param rawSource source to preprocess.
     * @return preprocessed file path.
     */
    private static String preprocessFile(File rawSource) {
        System.out.println("Test file: " + rawSource.getAbsolutePath());
        System.out.println("Pre-processing test file...");
        try {
            Preprocessor preprocessor = new Preprocessor();
            preprocessor.preProcess(rawSource);
            System.out.println("Preprocess successful.");
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + rawSource + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + rawSource + "'");
        }
        return rawSource + "_preprocessed.txt";
    }
}