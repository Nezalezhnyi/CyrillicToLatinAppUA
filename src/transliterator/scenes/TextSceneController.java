package transliterator.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import transliterator.Transliterator;


public class TextSceneController {
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
    public void transliterateText() {
        String inputText = textAreaInput.getText();
        String transliteratedText = Transliterator.transliterateInputText(inputText);
        textAreaOutput.setText(transliteratedText);
    }

    @FXML
    public void copyOutputText() {
        ClipboardContent copiedOutputText = new ClipboardContent();
        copiedOutputText.putString(textAreaOutput.getText());
        Clipboard.getSystemClipboard().setContent(copiedOutputText);
    }
}
