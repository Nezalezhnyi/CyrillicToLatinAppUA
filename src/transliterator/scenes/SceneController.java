package transliterator.scenes;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {
    private final Stage stage;
    private final Scene textScene;
    private final Scene fileScene;
    private final Scene rulesScene;

    public SceneController(Stage stage, Scene textScene, Scene fileScene, Scene rulesScene) {
        this.stage = stage;
        this.textScene = textScene;
        this.fileScene = fileScene;
        this.rulesScene = rulesScene;
    }

    public void switchToTextTransliterationScene() {
        stage.setScene(textScene);
    }

    public void switchToFileTransliterationScene () {
        stage.setScene(fileScene);
    }

    public void switchToRulesChangingScene () {
        stage.setScene(rulesScene);
    }
}
