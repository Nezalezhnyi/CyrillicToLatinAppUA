package transliterator.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

public class SceneController {
    private final Stage stage;
    private final Scene textScene;
    private final Scene fileScene;

    public SceneController(Stage stage, Scene textScene, Scene fileScene) {
        this.stage = stage;
        this.textScene = textScene;
        this.fileScene = fileScene;
    }

    public void switchToTextTransliterationScene() throws IOException {
        stage.setScene(textScene);
    }

    public void switchToFileTransliterationScene () throws IOException {
        stage.setScene(fileScene);
    }
}
