import java.io.BufferedReader;
import java.io.IOException;

/**
 * Class to Preprocess files.
 * @author Leonardo-Rocha
 */
class Preprocessor{

    /**
     * PreProcess file removing comments.
     * @param bufferedReader    file reference.
     */
     static void preProcess(BufferedReader bufferedReader) throws IOException {
        String line;

        while((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        //TODO complete method.
    }
}