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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

public class FileSceneController {
    private SceneController sceneController;
    private Stage stage;
    private final FileChooser fileChooser;
    private List<File> selectedFiles;
    private String textFromFile;
    private String transliteratedText;
    private boolean hasFilesBeenChosen;

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
        selectedFiles = Objects.requireNonNull(fileChooser.showOpenMultipleDialog(stage));
        updateChosenFiles();
    }

    public void updateChosenFiles() {
        StringBuilder fileNames = new StringBuilder();
        for (File file : selectedFiles)
            fileNames.append(file.getName()).append(", ");
        textOfSelectedFiles.setText(fileNames.toString());
        hasFilesBeenChosen = true;
    }

    @FXML
    public void transliterateAndSaveTextIntoOldFiles() throws IOException {
        if (hasFilesBeenChosen) {
            for (File file : selectedFiles) {
                textFromFile = Files.readString(file.toPath());
                transliteratedText = Transliterator.transliterateInputText(textFromFile);
                Files.writeString(file.toPath(), transliteratedText);
            }
            hasFilesBeenChosen = false;
            textUnderTransliterateChosenFilesButton.setText("Файли було успішно транслітеровано!");
            textUnderTransliterateChosenFilesButton.setFill(Color.GREEN);
            textOfSelectedFiles.setText("Оберіть один файл або більше");
        }
        else {
            textUnderTransliterateChosenFilesButton.setFill(Color.RED);
            textUnderTransliterateChosenFilesButton.setText("Оберіть файли перш ніж почати транслітерацію");
        }

    }

    @FXML
    public void transliterateAndSaveTextIntoNewFiles() throws IOException {
        if (hasFilesBeenChosen) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(stage);
            for (File file : selectedFiles) {
                String newFileName = generateNewFileName(file);
                File newFile = new File(selectedDirectory, newFileName);
                textFromFile = Files.readString(file.toPath());
                transliteratedText = Transliterator.transliterateInputText(textFromFile);
                Files.writeString(newFile.toPath(), transliteratedText);
            }
            hasFilesBeenChosen = false;
            textUnderCreateNewFilesButton.setText("Файли було успішно транслітеровано!");
            textUnderCreateNewFilesButton.setFill(Color.GREEN);
            textOfSelectedFiles.setText("Оберіть один файл або більше");
        }
        else {
            textUnderCreateNewFilesButton.setFill(Color.RED);
            textUnderCreateNewFilesButton.setText("Оберіть файли перш ніж почати транслітерацію");
        }
    }
    private String generateNewFileName(File originalFile) {
        String originalName = originalFile.getName();
        return originalName.replaceFirst("(\\.[^.]+)$", "_latin$1");
    }


    @FXML
    public void switchToTextTransliterationScene () throws IOException {
        sceneController.switchToTextTransliterationScene();
    }




    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
