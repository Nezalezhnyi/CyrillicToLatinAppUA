package transliterator.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.text.Text;
import transliterator.Transliterator;
import javafx.scene.paint.Color;
import java.io.IOException;

public class TextSceneController {

    private SceneController sceneController;

    private boolean isToLatin = true;

    @FXML
    private TextArea textAreaInput;

    @FXML
    private TextArea textAreaOutput;

    @FXML
    private Text cyrillicText;

    @FXML
    private Text latinText;

    @FXML
    public void initialize() {
        cyrillicText.setFill(Color.rgb(255,128,0));
        latinText.setFill(Color.rgb(0, 102, 204));
        textAreaInput.setStyle("-fx-background-color: linear-gradient(to right, #ff7e5f, #feb47b);");
        textAreaOutput.setStyle("-fx-background-color: linear-gradient(to right, #66B2FF, #0066CC);");
    }


    @FXML
    public void transliterateText() {
        String inputText = textAreaInput.getText(); // Gets the written text from the first text area and stores it as a string in inputText
        String transliteratedText = Transliterator.transliterateInputText(inputText, isToLatin); //transliterates the input text
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

    @FXML
    public void switchTransliterationDirection() {
        isToLatin = !isToLatin;
        switchCyrillicAndLatinTextsAboveTextAreas();
        switchInputAndOutputTexts();
        switchInputAndOutputTextAreasStyles();
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController; // Sets the sceneController, which contains the switchToFileTransliterationScene() method.
                                                // This method is linked to the respective button in Scene Builder and is used to switch scenes.
                                                // It calls stage.setScene(fileScene), where fileScene contains the FXML objects loaded by the MainApp class.
    }

    private void switchCyrillicAndLatinTextsAboveTextAreas() {
        if (isToLatin) {
            cyrillicText.setText("Кирилиця");
            latinText.setText("Латиниця");

            cyrillicText.setFill(Color.rgb(255,128,0));
            latinText.setFill(Color.rgb(0, 102, 204));
        } else {
            cyrillicText.setText("Латиниця");
            latinText.setText("Кирилиця");

            latinText.setFill(Color.rgb(255,128,0));
            cyrillicText.setFill(Color.rgb(0, 102, 204));
        }
    }

    private void switchInputAndOutputTexts() {
        String copyOfInputText = textAreaInput.getText();
        textAreaInput.setText(textAreaOutput.getText());
        textAreaOutput.setText(copyOfInputText);
    }

    private void switchInputAndOutputTextAreasStyles() {
        String styleInput = textAreaInput.getStyle();
        String styleOutput = textAreaOutput.getStyle();

        textAreaInput.setStyle(styleOutput);
        textAreaOutput.setStyle(styleInput);
    }
}
