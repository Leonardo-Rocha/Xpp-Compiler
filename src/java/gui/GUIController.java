package gui;

import core.CompilerMain;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.ErrorLogger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static javax.swing.JOptionPane.CANCEL_OPTION;
import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.YES_NO_CANCEL_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

public class GUIController {

    @FXML
    private MenuItem openFileButton;
    @FXML
    private MenuItem newFileButton;
    @FXML
    private MenuItem saveFileButton;
    @FXML
    private Menu compileButton;
    @FXML
    private TextArea editorTextArea;
    @FXML
    private TextArea consoleTextArea;

    private FileChooser fileChooser;
    private File currentFile;
    private Stage primaryStage;

    private boolean bFileSaved = true;

    public void initialize() {
        fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extensionFilter;
        extensionFilter = new FileChooser.ExtensionFilter("X++" + " Files (" + "*.xpp" + ")",
                "*.xpp", "*.XPP");
        fileChooser.getExtensionFilters().add(extensionFilter);
    }

    public void actionOpenFile(ActionEvent actionEvent) {
        currentFile = fileChooser.showOpenDialog(primaryStage);
        openCurrentFile();
    }

    public void actionNewFile(ActionEvent actionEvent) {
        if (currentFile != null || !editorTextArea.getText().equals("")) {
            int answer = JOptionPane.showConfirmDialog(null,
                    "Do you want to save changes to the open file? ",
                    "SIMP", YES_NO_CANCEL_OPTION);
            if (answer == YES_OPTION) {
                onSaveAs();
            }
            if (answer != CANCEL_OPTION && bFileSaved) {
                editorTextArea.setText("");
            }
        }
    }

    /**
     * SaveAs button action. Opens the file chooser and save the image in the chosen directory.
     */
    public void onSaveAs() {
        fileChooser.setTitle("Save As");
        currentFile = fileChooser.showSaveDialog(primaryStage);
        try {
            if (currentFile != null) {
                writeStringToFile(currentFile, editorTextArea.getText());
                bFileSaved = true;
            } else {
                bFileSaved = false;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to save file.");
        }
    }

    public void actionSaveFile(ActionEvent actionEvent) throws IOException {
        if (currentFile != null) {
            try {
                writeStringToFile(currentFile, editorTextArea.getText());
                JOptionPane.showMessageDialog(null, "File saved successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to save file :" + e.getMessage());
            }
        } else {
            onSaveAs();
        }
    }

    public void writeStringToFile(File file, String text) throws IOException {
        if (file == null)
            throw new IllegalArgumentException("File cannot be null.");

        if (text == null)
            throw new IllegalArgumentException("Text cannot be null.");

        String filePath = file.getAbsolutePath();

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write(text);
        }
    }

    public void actionCompileProgram(ActionEvent actionEvent) {
        try {
            actionSaveFile(actionEvent);
            if (currentFile != null) {
                String[] parameters = {currentFile.getAbsolutePath()};
                CompilerMain.main(parameters);
                if (ErrorLogger.getErrorLog().equals("")) {
                    consoleTextArea.setText("Build successful!");
                }
                else {
                    consoleTextArea.setText(ErrorLogger.getErrorLog());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to open file.");
        }
    }

    private void openCurrentFile() {
        try {
            if (currentFile != null) {
                editorTextArea.setText("");
                LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(currentFile));
                String currentLine;
                while ((currentLine = lineNumberReader.readLine()) != null) {
                    editorTextArea.appendText(currentLine + "\n");
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to open file.");
        }
    }

    @FXML
    private void actionExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
