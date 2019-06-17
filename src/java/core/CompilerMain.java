package core;

import utils.LexicalError;
import utils.Preprocessor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * core.CompilerMain class.
 *
 * @author Leonardo-Rocha, Gabriel Chiquetto.
 */
public class CompilerMain {

    public static void main(String[] args) throws IOException {

<<<<<<< HEAD:src/java/Main.java
        File filePath = openFile("/../../bin/arqTest1.xpp");
=======
        if (args.length > 0) {
            File filePath = openFile(args[0]);
>>>>>>> development:src/java/core/CompilerMain.java

            filePath = openFile(preprocessFile(filePath));

            runTest(filePath);

<<<<<<< HEAD:src/java/Main.java
        ErrorLogger.computeErrorLog();
=======
            LexicalError.computeErrorLog();
>>>>>>> development:src/java/core/CompilerMain.java

            System.out.println("Process terminated.");
        } else {
            System.out.println("Please, insert a valid file path.");
        }
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
        TokenGenerator tokenizer = new TokenGenerator(source);
        Token currentToken = tokenizer.getNextToken();
        while (!currentToken.equalsTokenType(TokenType.EOF)) {
            currentToken.showCase();
            currentToken = tokenizer.getNextToken();
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