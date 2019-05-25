package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainGUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/xpp-compiler-gui.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("XPP Compiler");
        primaryStage.setScene(new Scene(root, 300, 275));

        FileChooser fileChooser = new FileChooser();

        GUIController controller = loader.getController();
        controller.setFileChooser(fileChooser);
        controller.setStage(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
