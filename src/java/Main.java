import java.io.*;

/**
 * Main class.
 *
 * @author Leonardo-Rocha, Gabriel Chiquetto.
 */
public class Main {

    public static void main(String[] args) {

        File filePath = new File("bin\\test");

        filePath = FindFile(useProcessedFile(filePath));

        RunTest(filePath);
    }

    private static File FindFile(String path){
        File filePath = new File(path);
        return filePath;
    }

    private static void RunTest(File source){
        TokenGenerator Tokenizer = new TokenGenerator(source);
        Token currentToken = Tokenizer.getNextToken();
        while (!currentToken.equals(TokenType.EOF)){
            currentToken.showCase();
            currentToken = Tokenizer.getNextToken();
        }
        
    }

    private static String useProcessedFile(File rawSource){
        System.out.println("Test file: " + rawSource.getAbsolutePath());
        System.out.println("Pre-processing test file...");
        try {
            Preprocessor preprocessor = new Preprocessor();
            preprocessor.preProcess(rawSource);
            System.out.println("Preprocess successful.");
        }catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + rawSource + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + rawSource + "'");
    }
        return rawSource + "_preprocessed.txt";
    }
}