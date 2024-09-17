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
        FXMLLoader textFXMLObjectsLoader = new FXMLLoader(getClass().getResource("/textResources/TextTransliterationScene.fxml")); // Creates an FXMLLoader to load the FXML file and link its objects to the controller class fields upon calling the load() method. This instance will also be used to get the controllers' instances via getController()
        FXMLLoader fileFXMLObjectsLoader = new FXMLLoader(getClass().getResource("/fileResources/FileTransliterationScene.fxml")); //
        Parent textSceneRoot = textFXMLObjectsLoader.load(); // Creates the instance of the controller class specified in the SceneBuilder. Fxml objects are linked to the fields of THIS instance, not to the class itself. To get this instance, we later use the FXMLLoader's getController() method
        Parent fileSceneRoot = fileFXMLObjectsLoader.load(); // it saves the information of fxml objects and assigns it to the respective root (root is the object where all fxml objects are located it)
        Scene textScene = new Scene(textSceneRoot); // Creates the Text scene (for text "translation") with the TextTransliterationScene fxml objects. The root node is passed to the scene to display all its elements
        Scene fileScene = new Scene(fileSceneRoot); // Creates the File scene (for files' changing) with the FileTransliterationScene fxml objects
        primaryStage.setTitle("Transliterator");
        primaryStage.setScene(textScene); // textScene is used as the scene we see after launching the software (could've been fileScene)
        primaryStage.show();

        SceneController sceneController = new SceneController(primaryStage, textScene, fileScene); //Creates a SceneController (which contains two methods for switching between the text and file scenes) and passes the primary stage, text scene, and file scene to it. The stage is required to call stage.setScene(scene) when switching between scenes
        TextSceneController textSceneController = textFXMLObjectsLoader.getController(); // Gets the TextSceneController instance (the instance of the class that is set as the text scene controller in the Scene Builder)
        textSceneController.setSceneController(sceneController); // Passes the sceneController to the instance. It needs the sceneController object to use its switch method and link it to the fxml button (since textSceneController is linked to the text scene, not the sceneController)
        FileSceneController fileSceneController = fileFXMLObjectsLoader.getController(); // Gets the FileSceneController instance
        fileSceneController.setSceneController(sceneController); // Passes the sceneController to the instance. It needs the sceneController object to use its switch method and link it to the fxml button

        fileSceneController.setStage(primaryStage); //The instance (fileSceneController) needs the stage for using DirectoryChooser class

        textScene.getStylesheets().add(getClass().getResource("/textResources/TextStyle.css").toExternalForm());
    }
}
