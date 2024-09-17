package transliterator.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class FileSceneController {
    private SceneController sceneController;
    private Stage stage;
    private final FileChooser fileChooser;
    private List<File> selectedFiles;

    @FXML
    private TextArea textOfSelectedFiles;

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

    private void updateChosenFiles() {
        StringBuilder fileNames = new StringBuilder();
        for (File file : selectedFiles)
            fileNames.append(file.getName()).append(", ");
        textOfSelectedFiles.setText(fileNames.toString());
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
