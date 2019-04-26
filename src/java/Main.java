import java.io.*;

/**
 * Main class.
 * @author Leonardo-Rocha
 */
public class Main{

    public static void main(String[] args){

       File directory = new File("../../bin/test.xpp");
       System.out.println(directory.getAbsolutePath());
       Preprocessor preprocessor = new Preprocessor();

       try{
           FileReader fileReader = new FileReader(directory);

           BufferedReader bufferedReader = new BufferedReader(fileReader);

           preprocessor.preProcess(bufferedReader);

           bufferedReader.close();
       }
       catch(FileNotFoundException ex) {
           System.out.println("Unable to open file '" + directory + "'");
       }
       catch(IOException ex) {
           System.out.println("Error reading file '" + directory + "'");
       }
   }

}