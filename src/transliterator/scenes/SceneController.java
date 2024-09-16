package transliterator.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneController {
    private final Stage stage;

    public SceneController(Stage stage) { this.stage = stage; }

    public void switchToTextTransliterationScene() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/textResources/TextTransliterationScene.fxml")));
        Scene scene = new Scene(root);
        //stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/textResources/TextStyle.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void switchToFileTransliterationScene () throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resources/FileTransliterationScene.fxml")));
        Scene scene = new Scene(root);
        //stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
