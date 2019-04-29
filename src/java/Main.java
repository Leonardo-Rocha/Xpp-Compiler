import java.io.*;

/**
 * Main class.
 *
 * @author Leonardo-Rocha, Gabriel Chiquetto.
 */
public class Main {

    public static void main(String[] args) {

        File filePath = new File("../../bin/test");
        System.out.println("Test file: " + filePath.getAbsolutePath());
        System.out.println("Pre-processing test file...");
        try {
            Preprocessor preprocessor = new Preprocessor();
            preprocessor.preProcess(filePath);
            System.out.println("Preprocess successful.");
        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filePath + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + filePath + "'");
        }
    }

}