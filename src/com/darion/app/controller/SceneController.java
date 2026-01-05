package com.darion.app.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneController {
    private final Stage stage;
    private final Scene translatorScene;
    private final Scene fileScene;
    private final Scene rulesScene;

    public SceneController(Stage stage, Scene translatorScene, Scene fileScene, Scene rulesScene) {
        this.stage = stage;
        this.translatorScene = translatorScene;
        this.fileScene = fileScene;
        this.rulesScene = rulesScene;
    }

    public void toTranslatorScene() {
        stage.setScene(translatorScene);

    }

    public void toFileScene() {
        stage.setScene(fileScene);
    }

    public void toRulesScene() {
        stage.setScene(rulesScene);
    }


}
