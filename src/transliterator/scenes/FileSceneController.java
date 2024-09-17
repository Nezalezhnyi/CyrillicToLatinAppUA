package transliterator.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import transliterator.Transliterator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.util.List;

public class FileSceneController {
    private SceneController sceneController; // This object contains the method switchToTextTransliterationScene() from SceneController for switching to the Text scene
    private Stage stage; // The primary stage used to block user interaction while choosing a directory
    private List<File> selectedFiles; // Stores the list of files selected in a dialog window for later transliteration
    private boolean areFilesChosen; // Tracks whether files have been chosen to prevent starting the transliteration without selection
    private boolean isToLatin = true;

    @FXML
    private TextArea textOfSelectedFiles; // The text in the text area
    @FXML
    private Text textUnderTransliterateChosenFilesButton;
    @FXML
    private Text textUnderCreateNewFilesButton;
    @FXML
    private Text cyrillicTextAboveChangeDirectionButton;
    @FXML
    private Text latinTextAboveChangeDirectionButton;
    @FXML
    private Text warningMessageUnderChangeDirection;

    @FXML
    public void initialize() {
        cyrillicTextAboveChangeDirectionButton.setFill(Color.rgb(255,128,0));
        latinTextAboveChangeDirectionButton.setFill(Color.rgb(0, 102, 204));
        warningMessageUnderChangeDirection.setFill(Color.RED);
    }

    @FXML
    public void chooseFiles() {
        selectedFiles = new FileChooser().showOpenMultipleDialog(stage); // Opens a dialog to select multiple files and saves them into the selectedFiles variable
        if (selectedFiles != null) {
            updateTextAreaListOfChosenFiles(); // Writes a list of chosen files into TextArea box
            areFilesChosen = true; // We can now use buttons since files have been chosen
        }
    }

    @FXML
    public void transliterateAndSaveTextIntoOldFiles() throws IOException {
        if (!checkIfFilesChosen(textUnderTransliterateChosenFilesButton)) {return;} // Passes textUnderTransliterateChosenFilesButton to show an error message if no files are chosen (red text 'Оберіть файли...')
            for (File file : selectedFiles) {
                try {
                    transliterateAndSave(file, file); // Reads the input file, transliterates the text, and saves it back into the same file (overwriting the original)
                } catch (MalformedInputException e) { // If we try to transliterate a non-supported file,
                    showInvalidFileFormatMessage(textUnderTransliterateChosenFilesButton); // shows a red message ('Обрано файл із непідтримуваним...') and
                    return; // stops the execution of the method to avoid showing successfulTransliterationMessage
                }
            }
            showSuccessfulTransliterationMessage(textUnderTransliterateChosenFilesButton); // After the ending of the looping (after transliteration), we place the green text under the button which indicates a successful transliteration
            areFilesChosen = false; //checkIfFilesChosen will return false after reapplying the button which will stop method's executing and pop the red text out
    }

    @FXML
    public void transliterateAndSaveTextIntoNewFiles() throws IOException {
        if (!checkIfFilesChosen(textUnderCreateNewFilesButton)) {return;} // Passes textUnderCreateNewFilesButton to show an error message if no files are chosen (red text 'Оберіть файли...')
            File selectedDirectory = new DirectoryChooser().showDialog(stage); // Opens the directory chooser dialog. The stage is used to block interaction with the transliteration software while choosing a directory
            if (selectedDirectory != null) {
                for (File file : selectedFiles) {
                    String newFileName = generateNewFileName(file); // Generates a new file name by adding a suffix to distinguish it from the original
                    File newFile = new File(selectedDirectory, newFileName); // Creates a new file in the specified directory with the specified name
                    try {
                        transliterateAndSave(file, newFile); // Reads the input file, transliterates the text, and saves it back into the same file (overwriting the original)
                        } catch (MalformedInputException e) {
                        showInvalidFileFormatMessage(textUnderCreateNewFilesButton);
                        return;
                    }
                }
            }
            else {return;} // Doesn't show the successfulTransliterationMessage if a directory was not selected (stops the execution of the method before the showSuccessfulTransliterationMessage() )
            showSuccessfulTransliterationMessage(textUnderCreateNewFilesButton);
            areFilesChosen = false;
    }

    @FXML
    public void switchToTextTransliterationScene (){
        sceneController.switchToTextTransliterationScene(); // Uses sceneController, got from the MainApp.java using setter, to switch to the Text scene
    }

    @FXML
    public void switchTransliterationDirection() {
        isToLatin = !isToLatin;
        switchCyrillicAndLatinTextsAboveTextAreas();
        showWarningChangingDirectionsMessage();
    }


    private String generateNewFileName(File originalFile) {
        String originalName = originalFile.getName();
        return originalName.replaceFirst("(\\.[^.]+)$", "_latin$1");
    }

    private void updateTextAreaListOfChosenFiles() {
        StringBuilder fileNames = new StringBuilder();
        for (File file : selectedFiles)
            fileNames.append(file.getName()).append(", "); // Adds each file name to StringBuilder, separating them with a comma and a space
        textOfSelectedFiles.setText(fileNames.toString()); // Converts the sequence of files, located in StringBuilder, into the normal String and set in to the TextArea
    }

    private void transliterateAndSave(File inputFile, File outputFile) throws IOException {
        String textFromFile = Files.readString(inputFile.toPath()); // Reads text from the file (inputFile.toPath() returns URL which is used by readString() to get the string text from the URL and store it in the textFromFile
        String transliteratedText = Transliterator.transliterateInputText(textFromFile, isToLatin);
        Files.writeString(outputFile.toPath(), transliteratedText); // Saves the transliterated text into the outputFile we chose (or didn't)
    }

    private boolean checkIfFilesChosen(Text textUnderButton) {
        if (!areFilesChosen) {
            showFilesAreNotChosenMessage(textUnderButton); // Shows a red message ('Оберіть файли...') if no files are chosen and the button was later clicked, and stops further execution
            return false; // Stops executing of transliterateAndSaveTextIntoOldFiles() or transliterateAndSaveTextIntoNewFiles() method
        }
        return true; // Allows transliterateAndSaveTextIntoOldFiles() or transliterateAndSaveTextIntoNewFiles() method to keep executing
    }
    
    private void showSuccessfulTransliterationMessage(Text textUnderButton) {
        textUnderButton.setText("Файли було успішно транслітеровано!"); // Writes the text under a button after transliteration (when the files were chosen)
        textUnderButton.setFill(Color.GREEN);
        textOfSelectedFiles.setText("Оберіть один файл або більше"); // Resets the text area after successful transliteration, as the file list is no longer needed
    }

    private void showFilesAreNotChosenMessage(Text textUnderButton) {
        textUnderButton.setFill(Color.RED);
        textUnderButton.setText("Оберіть файли перш ніж почати транслітерацію"); // Is used it checkIfFilesChosen() method if we didn't choose files
    }

    private void showInvalidFileFormatMessage(Text textUnderButton) {
        textUnderButton.setFill(Color.RED);
        textUnderButton.setText("Обрано файл із непідтримуваним форматом");
    }

    private void switchCyrillicAndLatinTextsAboveTextAreas() {
        if (isToLatin) {
            cyrillicTextAboveChangeDirectionButton.setText("Кирилиця");
            latinTextAboveChangeDirectionButton.setText("Латиниця");

            cyrillicTextAboveChangeDirectionButton.setFill(Color.rgb(255,128,0));
            latinTextAboveChangeDirectionButton.setFill(Color.rgb(0, 102, 204));
        } else {
            cyrillicTextAboveChangeDirectionButton.setText("Латиниця");
            latinTextAboveChangeDirectionButton.setText("Кирилиця");

            latinTextAboveChangeDirectionButton.setFill(Color.rgb(255,128,0));
            cyrillicTextAboveChangeDirectionButton.setFill(Color.rgb(0, 102, 204));
        }
    }

    private void showWarningChangingDirectionsMessage() {
        if (!isToLatin)
            warningMessageUnderChangeDirection.setText(
                            "Увага! Кирилізація системних або технічних файлів " +
                            "змінить системні команди й важливі символи також, " +
                            "що може порушити нормальну роботу файлів. " +
                            "Уникайте кирилізації технічних та системних файлів!"
            );
        else
            warningMessageUnderChangeDirection.setText("");
    }


    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController; // Sets the sceneController, which contains the switchToTextTransliterationScene() method.
                                                // This method is linked to the respective button in Scene Builder and is used to switch scenes.
                                                // It calls stage.setScene(textScene), where textScene contains the FXML objects loaded by the MainApp class.
    }

    public void setStage(Stage stage) {
        this.stage = stage; // Is used for DirectoryChooser (to prevent interaction with the transliteration software while choosing a directory)
    }
}
