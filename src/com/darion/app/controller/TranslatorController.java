package com.darion.app.controller;

import com.darion.app.model.TransliterationEngine;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.regex.Pattern;


public class TranslatorController  {
    @FXML
    private TextArea leftTextArea;

    @FXML
    private TextArea rightTextArea;

    @FXML
    private Label leftLabel;

    @FXML
    private Label rightLabel;

    @FXML
    private Label copiedLabel;

    @FXML
    private Button switchModeButton;

    @FXML
    private Button nextButton;

    private boolean isCyrillicToLatin = true;
    private SceneController sceneController;

    @FXML
    public void initialize() {
        updateUI();
        setMouseOnGlow();
        setUpLeftTextAreaListener();
    }

    private void setUpLeftTextAreaListener() {
        leftTextArea.textProperty().addListener((_, _, newValue) -> {
            rightTextArea.setText(TransliterationEngine.transliterate(newValue, isCyrillicToLatin));

        });
    }

    private void setMouseOnGlow() {
        nextButton.setOnMouseEntered(e -> {
            if (!nextButton.getStyleClass().contains("button-glow")) {
                nextButton.getStyleClass().add("button-glow");
            }
        });

        nextButton.setOnMouseExited(e -> {
            nextButton.getStyleClass().remove("button-glow");
        });
    }

    @FXML
    public void onCopy() {
        String textToCopy = rightTextArea.getText();

        if (textToCopy == null || textToCopy.isEmpty())
            return;

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        content.putString(textToCopy);
        clipboard.setContent(content);

        triggerCopySuccessMessage();
    }

    private PauseTransition pause = new PauseTransition(Duration.seconds(3));

    private void triggerCopySuccessMessage() {
        copiedLabel.setVisible(true);
        pause.setOnFinished(actionEvent -> {copiedLabel.setVisible(false);});
        pause.playFromStart();
    }

    @FXML
    public void onClear() {
        leftTextArea.clear();
        rightTextArea.clear();
    }

    @FXML
    public void onSwitchMode() {
        isCyrillicToLatin = !isCyrillicToLatin;

        String tempLeftText = leftTextArea.getText();
        leftTextArea.setText(rightTextArea.getText());
        rightTextArea.setText(tempLeftText);

        updateUI();
    }

    @FXML
    public void onNextScene() {
        sceneController.toFileScene();
    }

    public void setSceneController(SceneController controller) {
        this.sceneController = controller;
    }

    private void updateUI() {
        if (isCyrillicToLatin) {
            setLabelStyle(leftLabel, "Cyrillic", "cyrillic-style");
            setLabelStyle(rightLabel, "Latin", "latin-style");
            updateFocusStyle(leftTextArea, "cyrillic-focus");
            updateFocusStyle(rightTextArea, "latin-focus");
            updateButtonStyle(switchModeButton, "switch-button-to-latin");
        } else {
            setLabelStyle(leftLabel, "Latin", "latin-style");
            setLabelStyle(rightLabel, "Cyrillic", "cyrillic-style");
            updateFocusStyle(leftTextArea, "latin-focus");
            updateFocusStyle(rightTextArea, "cyrillic-focus");
            updateButtonStyle(switchModeButton, "switch-button-to-cyrillic");
        }
    }

    private void setLabelStyle(Label label, String text, String styleClass) {
        label.setText(text);
        label.getStyleClass().removeAll("cyrillic-style", "latin-style");
        label.getStyleClass().add(styleClass);
    }

    private void updateFocusStyle(TextArea area, String styleClass) {
        area.getStyleClass().removeAll("cyrillic-focus", "latin-focus");
        area.getStyleClass().add(styleClass);
    }

    private void updateButtonStyle(Button button, String styleClass) {
        button.getStyleClass().removeAll("switch-button-to-latin", "switch-button-to-cyrillic");
        button.getStyleClass().add(styleClass);
    }

}
