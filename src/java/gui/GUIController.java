package gui;

import core.CompilerMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    private Stage stage;
    private File currentFile;

    public void actionOpenFile(ActionEvent actionEvent) throws IOException {
        System.out.println("Opening file...");

        if (fileChooser == null || stage == null)
            return;

        currentFile = fileChooser.showOpenDialog(stage);
        openCurrentFile();
    }

    public void actionNewFile(ActionEvent actionEvent) throws IOException {
        System.out.println("Creating a new file...");
        if (fileChooser == null || stage == null)
            return;

        currentFile = fileChooser.showSaveDialog(stage);
        openCurrentFile();
    }

    public void actionSaveFile(ActionEvent actionEvent) throws IOException {
        System.out.println("Saving file...");
        String buffer = editorTextArea.getText();
        try {
            writeStringToFile(currentFile, buffer);
            JOptionPane.showMessageDialog(null, "File saved successfully!");
        }
        catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to save file.");
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
            String[] parameters = {currentFile.getAbsolutePath()};
            CompilerMain.main(parameters);
        }
        catch (IOException e) {
            System.out.println("Invalid file.");
        }
    }

    private void openCurrentFile() throws IOException {
        if (currentFile == null)
            throw new IllegalArgumentException("File cannot be null.");

        editorTextArea.setText("");
        LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(currentFile));
        String currentLine;
        while ((currentLine = lineNumberReader.readLine()) != null) {
            editorTextArea.appendText(currentLine + "\n");
        }
    }

    public MenuItem getOpenFileButton() {
        return openFileButton;
    }

    public MenuItem getNewFileButton() {
        return newFileButton;
    }

    public MenuItem getSaveFileButton() {
        return saveFileButton;
    }

    public Menu getCompileButton() {
        return compileButton;
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    public void setFileChooser(FileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
