import java.io.*;

/**
 * Main class.
 *
 * @author Leonardo-Rocha, Gabriel Chiquetto.
 */
public class Main {

    public static void main(String[] args) {

        File directory = new File("../../bin/test.xpp");
        System.out.println("Test file: " + directory.getAbsolutePath());

        try {
            Preprocessor preprocessor = new Preprocessor();
            preprocessor.preProcess(directory);

        } catch (FileNotFoundException ex) {
            System.out.println("Unable to open file '" + directory + "'");
        } catch (IOException ex) {
            System.out.println("Error reading file '" + directory + "'");
        }
    }

}