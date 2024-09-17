package transliterator.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import transliterator.Transliterator;

import java.io.IOException;

public class TextSceneController {

    private SceneController sceneController;

    @FXML
    private TextArea textAreaInput;

    @FXML
    private TextArea textAreaOutput;

    @FXML
    public void transliterateText() {
        String inputText = textAreaInput.getText(); // Gets the written text from the first text area and stores it as a string in inputText
        String transliteratedText = Transliterator.transliterateInputText(inputText); //transliterates the input text
        textAreaOutput.setText(transliteratedText); // Sets the transliterated text into the second text area (output)
    }

    @FXML
    public void copyOutputText() {
        ClipboardContent copiedOutputText = new ClipboardContent(); // Creates a ClipBoardContent object to store copied text
        copiedOutputText.putString(textAreaOutput.getText()); // Retrieves the transliterated text from the output text area and stores it in the ClipboardContent object
        Clipboard.getSystemClipboard().setContent(copiedOutputText); // Clipboard.getSystemClipboard() gets access to the OS clipboard where we store our copiedOutputText using the setContent() method
    }

    @FXML
    public void clearInputAndOutputTexts() {
        textAreaInput.setText(""); // Simply erases written texts
        textAreaOutput.setText("");
    }

    @FXML
    public void switchToFileTransliterationScene () throws IOException {
        sceneController.switchToFileTransliterationScene(); //Switches to the file transliteration scene using the sceneController
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController; // Sets the sceneController, which contains the switchToFileTransliterationScene() method.
                                                // This method is linked to the respective button in Scene Builder and is used to switch scenes.
                                                // It calls stage.setScene(fileScene), where fileScene contains the FXML objects loaded by the MainApp class.
    }
}
