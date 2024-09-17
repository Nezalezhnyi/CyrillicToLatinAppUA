package transliterator.scenes;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {
    private final Stage stage;
    private final Scene textScene;
    private final Scene fileScene;

    public SceneController(Stage stage, Scene textScene, Scene fileScene) {
        this.stage = stage;
        this.textScene = textScene;
        this.fileScene = fileScene;
    }

    public void switchToTextTransliterationScene() {
        stage.setScene(textScene);
    }

    public void switchToFileTransliterationScene () {
        stage.setScene(fileScene);
    }
}
