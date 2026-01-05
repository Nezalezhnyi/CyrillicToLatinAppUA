package com.darion.app.controller;

import com.darion.app.model.TransliterationEngine;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileController {

    @FXML
    private Button chooseFilesButton;

    @FXML
    private Button switchDirectionButton;

    @FXML
    private Button nextButton;

    @FXML
    private TextArea filesTextArea;

    @FXML
    private Label leftLabel;

    @FXML
    private Label arrowLabel;

    @FXML
    private Label rightLabel;

    @FXML
    private Label chosenFilesSuccessLabel;

    @FXML
    private Label newFilesSuccessLabel;

    private SceneController sceneController;
    private List<File> selectedFiles;
    private TransliterationEngine engine = new TransliterationEngine();
    private boolean isCyrillicToLatin = true;

    @FXML
    public void initialize() {
        nextButton.setOnMouseEntered(e -> {
            if (!nextButton.getStyleClass().contains("button-glow")) {
                nextButton.getStyleClass().add("button-glow");
            }
        });

        nextButton.setOnMouseExited(e -> {
            nextButton.getStyleClass().remove("button-glow");
        });

        updateUI();
    }

    @FXML
    public void onChooseFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Files to Transliterate");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = (Stage) chooseFilesButton.getScene().getWindow();

        selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        if (selectedFiles == null) return;

        addFilesNamesToTextArea();
    }

    @FXML
    public void onTransliterateChosenFiles() {
        if (selectedFiles == null || selectedFiles.isEmpty()) return;

        for (File file : selectedFiles) {
            try {
                String text = Files.readString(file.toPath());
                String transliteratedText = engine.transliterate(text, isCyrillicToLatin);
                Files.writeString(file.toPath(), transliteratedText);
            } catch (IOException e) {
                System.err.println("Failed to process file: " + file.getName());
                e.printStackTrace();
            }
        }


            addSuccessMessage(newFilesSuccessLabel);

        filesTextArea.clear();

        selectedFiles = new ArrayList<>();
    }

    @FXML
    public void onCreateNewFiles() {
        if (selectedFiles == null || selectedFiles.isEmpty()) return;

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Target Folder");

        Stage stage = (Stage) filesTextArea.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory == null) return;

        for (File file : selectedFiles) {
            try {
                String text = Files.readString(file.toPath());
                String transliteratedText = engine.transliterate(text, isCyrillicToLatin);

                String originalFileName = file.getName();
                String newName;

                if (originalFileName.contains(".")) {
                    int dotIndex = originalFileName.lastIndexOf(".");
                    String namePart = originalFileName.substring(0, dotIndex);
                    String extensionPart = originalFileName.substring(dotIndex);
                    newName = namePart + "-transliterated" + extensionPart;
                } else
                    newName = originalFileName + "-transliterated";

                File newFile = new File(selectedDirectory, newName);
                Files.writeString(newFile.toPath(), transliteratedText);

            } catch (IOException e) {
                System.err.println("Failed to process file: " + file.getName());
                e.printStackTrace();
            }
        }

        addSuccessMessage(chosenFilesSuccessLabel);
        filesTextArea.clear();
        selectedFiles = new ArrayList<>();
    }

    @FXML
    public void onChangeDirection() {
        isCyrillicToLatin = !isCyrillicToLatin;

        updateUI();
    }


    @FXML
    public void onNextScene() {
        sceneController.toRulesScene();
    }

    @FXML
    public void onPreviousScene() {
        sceneController.toTranslatorScene();
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    private void addSuccessMessage(Label label) {
        label.setVisible(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(actionEvent -> {label.setVisible(false);});
        pause.playFromStart();
    }

    private void updateUI() {
        if (isCyrillicToLatin) {
            setLabelStyle(leftLabel, "Cyrillic", "cyrillic-style");
            setLabelStyle(arrowLabel, "→", "arrow-to-latin-style");
            setLabelStyle(rightLabel, "Latin", "latin-style");
            updateButtonStyle(switchDirectionButton, "switch-button-to-latin");
        } else {
            setLabelStyle(leftLabel, "Latin", "latin-style");
            setLabelStyle(arrowLabel, "→", "arrow-to-cyrillic-style");
            setLabelStyle(rightLabel, "Cyrillic", "cyrillic-style");
            updateButtonStyle(switchDirectionButton, "switch-button-to-cyrillic");
        }
    }

    private void setLabelStyle(Label label, String text, String styleClass) {
        label.setText(text);
        label.getStyleClass().removeAll("latin-style", "cyrillic-style", "arrow-to-cyrillic-style", "arrow-to-latin-style");
        label.getStyleClass().add(styleClass);
    }

    private void updateButtonStyle(Button button, String styleClass) {
        button.getStyleClass().removeAll("switch-button-to-latin", "switch-button-to-cyrillic");
        button.getStyleClass().add(styleClass);
    }

    private void addFilesNamesToTextArea() {
        if (selectedFiles.isEmpty()) return;

        String text = String.join(", ", selectedFiles.stream().map(File::getName).toList());

        filesTextArea.setText(text);
    }
}
