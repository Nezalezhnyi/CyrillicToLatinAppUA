package transliterator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import transliterator.scenes.FileSceneController;
import transliterator.scenes.SceneController;
import transliterator.scenes.TextSceneController;


public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader textFXMLObjectsLoader = new FXMLLoader(getClass().getResource("/textResources/TextTransliterationScene.fxml"));
        FXMLLoader fileFXMLObjectsLoader = new FXMLLoader(getClass().getResource("/fileResources/FileTransliterationScene.fxml"));
        Parent textSceneRoot = textFXMLObjectsLoader.load();
        Parent fileSceneRoot = fileFXMLObjectsLoader.load();
        Scene textScene = new Scene(textSceneRoot);
        Scene fileScene = new Scene(fileSceneRoot);
        primaryStage.setScene(textScene);
        primaryStage.show();

        SceneController sceneController = new SceneController(primaryStage, textScene, fileScene);
        TextSceneController textSceneController = textFXMLObjectsLoader.getController();
        textSceneController.setSceneController(sceneController);
        FileSceneController fileSceneController = fileFXMLObjectsLoader.getController();
        fileSceneController.setSceneController(sceneController);
        fileSceneController.setStage(primaryStage);
    }
}
