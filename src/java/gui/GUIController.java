package gui;

import core.CompilerMain;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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

    public void initialize() {
        fileChooser = new FileChooser();
    }

    public void actionOpenFile(ActionEvent actionEvent) throws IOException {
        System.out.println("Opening file...");

        currentFile = fileChooser.showOpenDialog(primaryStage);
        if (currentFile != null) {
            openCurrentFile();
        }
    }

    public void actionNewFile(ActionEvent actionEvent) throws IOException {
        System.out.println("Creating a new file...");
        if (currentFile != null) {
            int answer = JOptionPane.showConfirmDialog(null,
                    "Do you want to save changes to the open file? ",
                    "SIMP", YES_NO_CANCEL_OPTION);
            if (answer == YES_OPTION) {
                onSaveAs();
            } else if (answer == NO_OPTION) {
            }
        }
    }

    /**
     * Save button action. If the image was open or already saved, save the changes on it.
     * Otherwise, call onSaveAs().
     */
    public void onSave() {
        if (currentFile != null) {
            try {
                writeStringToFile(currentFile, editorTextArea.getText());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to save file :" + e.getMessage());
            }
        } else
            onSaveAs();
    }

    /**
     * SaveAs button action. Opens the file chooser and save the image in the chosen directory.
     */
    public void onSaveAs() {
        try {
            //Image snapshot = canvas.snapshot(null, null);
            fileChooser.setTitle("Save As");
            currentFile = fileChooser.showSaveDialog(primaryStage);
            if (currentFile != null) {
                //ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), getFileFormat(), currentFile);
                writeStringToFile(currentFile, editorTextArea.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save image.");
        }
    }

    public void actionSaveFile(ActionEvent actionEvent) throws IOException {
        System.out.println("Saving file...");
        String buffer = editorTextArea.getText();
        try {
            writeStringToFile(currentFile, buffer);
            JOptionPane.showMessageDialog(null, "File saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to save file :" + e.getMessage());
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
        System.out.println("Compiling...");
        try {
            actionSaveFile(actionEvent);
            String[] parameters = {currentFile.getAbsolutePath()};
            CompilerMain.main(parameters);
        } catch (IOException e) {
            System.out.println("Invalid file.");
        }
    }

    private void openCurrentFile() throws IOException {
        try {
            currentFile = fileChooser.showOpenDialog(primaryStage);
            editorTextArea.setText("");
            LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(currentFile));
            String currentLine;
            while ((currentLine = lineNumberReader.readLine()) != null) {
                editorTextArea.appendText(currentLine + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to open file.");
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
