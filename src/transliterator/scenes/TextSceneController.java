package transliterator.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import transliterator.Transliterator;

import java.io.IOException;
import java.util.Objects;


public class TextSceneController {

    private SceneController sceneController;

    @FXML
    private TextArea textAreaInput;

    @FXML
    private TextArea textAreaOutput;

    @FXML
    public void initialize() {
        textAreaInput.setEditable(true);
        textAreaOutput.setEditable(false);
        textAreaInput.setWrapText(true);
        textAreaOutput.setWrapText(true);
    }

    @FXML
    public void transliterateText(ActionEvent event) {
        String inputText = textAreaInput.getText();
        String transliteratedText = Transliterator.transliterateInputText(inputText);
        textAreaOutput.setText(transliteratedText);
    }

    @FXML
    public void copyOutputText(ActionEvent event) {
        ClipboardContent copiedOutputText = new ClipboardContent();
        copiedOutputText.putString(textAreaOutput.getText());
        Clipboard.getSystemClipboard().setContent(copiedOutputText);
    }

    @FXML
    public void switchToFileTransliterationScene (ActionEvent event) throws IOException {
        sceneController.switchToFileTransliterationScene();
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }
}
