package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUI extends Application {

    /**
     * Scene preferred width.
     */
    public static final double WIDTH = 896;
    /**
     * Scene preferred height.
     */
    public static final double HEIGHT = 504;
    /**
     * Application title.
     */
    private static final String TITLE = "XPP Compiler";

    /**
     * Controller reference.
     */
    GUIController controller;

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/xpp-compiler-gui.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
