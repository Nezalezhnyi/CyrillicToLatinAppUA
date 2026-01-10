package com.darion.app;

import com.darion.app.controller.FileController;
import com.darion.app.controller.RulesController;
import com.darion.app.controller.SceneController;
import com.darion.app.controller.TranslatorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader translatorFXMLObjectsLoader = new FXMLLoader(Main.class.getResource("/com/darion/app/view/fxml/translator-view.fxml"));
        FXMLLoader fileFXMLObjectsLoader = new FXMLLoader(Main.class.getResource("/com/darion/app/view/fxml/file-view.fxml"));
        FXMLLoader rulesFXMLObjectsLoader = new FXMLLoader(Main.class.getResource("/com/darion/app/view/fxml/rules-view.fxml"));

        Scene translatorScene = new Scene(translatorFXMLObjectsLoader.load(), 818, 714);
        Scene fileScene = new Scene(fileFXMLObjectsLoader.load(), 818, 714);
        Scene rulesScene = new Scene(rulesFXMLObjectsLoader.load(), 818, 714);

        stage.setTitle("Transliteration App");
        stage.setScene(translatorScene);
        stage.setResizable(false);
        stage.show();

        SceneController sceneController = new SceneController(stage, translatorScene, fileScene, rulesScene);

        TranslatorController translatorController = translatorFXMLObjectsLoader.getController();
        translatorController.setSceneController(sceneController);

        FileController fileController = fileFXMLObjectsLoader.getController();
        fileController.setSceneController(sceneController);

        RulesController rulesController = rulesFXMLObjectsLoader.getController();
        rulesController.setSceneController(sceneController);

        translatorScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("view/css/translatorStyles.css")).toExternalForm());
        fileScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("view/css/fileStyles.css")).toExternalForm());
        rulesScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("view/css/rulesStyles.css")).toExternalForm());
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}