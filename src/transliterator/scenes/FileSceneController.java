package transliterator.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import transliterator.Transliterator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public class FileSceneController {
    private SceneController sceneController;
    private Stage stage;
    private final FileChooser fileChooser;
    private List<File> selectedFiles;
    private boolean areFilesChosen;

    @FXML
    private TextArea textOfSelectedFiles;
    @FXML
    private Text textUnderTransliterateChosenFilesButton;
    @FXML
    private Text textUnderCreateNewFilesButton;

    public FileSceneController() {
        fileChooser = new FileChooser();
    }


    @FXML
    public void initialize() {
        textOfSelectedFiles.setWrapText(true);
    }

    @FXML
    public void chooseFiles() {
        selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        if (selectedFiles != null) {updateTextAreaListOfChosenFiles();}
    }

    @FXML
    public void transliterateAndSaveTextIntoOldFiles() throws IOException {
        if (!checkIfFilesChosen(textUnderTransliterateChosenFilesButton)) {return;}
            for (File file : selectedFiles) {
                transliterateAndSave(file, file);
            }
            successfulTransliterationMessage(textUnderTransliterateChosenFilesButton);
            areFilesChosen = false;
    }

    @FXML
    public void transliterateAndSaveTextIntoNewFiles() throws IOException {
        if (!checkIfFilesChosen(textUnderCreateNewFilesButton)) {return;}
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(stage);
            if (selectedDirectory != null) {
                for (File file : selectedFiles) {
                    String newFileName = generateNewFileName(file);
                    File newFile = new File(selectedDirectory, newFileName);
                    transliterateAndSave(file, newFile);
                }
            }
            successfulTransliterationMessage(textUnderCreateNewFilesButton);
            areFilesChosen = false;
    }

    @FXML
    public void switchToTextTransliterationScene () throws IOException {
        sceneController.switchToTextTransliterationScene();
    }


    private String generateNewFileName(File originalFile) {
        String originalName = originalFile.getName();
        return originalName.replaceFirst("(\\.[^.]+)$", "_latin$1");
    }

    private void updateTextAreaListOfChosenFiles() {
        StringBuilder fileNames = new StringBuilder();
        for (File file : selectedFiles)
            fileNames.append(file.getName()).append(", ");
        textOfSelectedFiles.setText(fileNames.toString());
        areFilesChosen = true;
    }

    private void transliterateAndSave(File inputFile, File outputFile) throws IOException {
        String textFromFile = Files.readString(inputFile.toPath());
        String transliteratedText = Transliterator.transliterateInputText(textFromFile);
        Files.writeString(outputFile.toPath(), transliteratedText);
    }

    private boolean checkIfFilesChosen(Text textUnderButton) {
        if (!areFilesChosen) {
            filesAreNotChosenMessage(textUnderButton);
            return false;
        }
        return true;
    }
    
    private void successfulTransliterationMessage(Text textUnderButton) {
        textUnderButton.setText("Файли було успішно транслітеровано!");
        textUnderButton.setFill(Color.GREEN);
        textOfSelectedFiles.setText("Оберіть один файл або більше");
    }

    private void filesAreNotChosenMessage(Text textUnderButton) {
        textUnderButton.setFill(Color.RED);
        textUnderButton.setText("Оберіть файли перш ніж почати транслітерацію");
    }



    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
