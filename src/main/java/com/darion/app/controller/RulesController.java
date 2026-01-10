package com.darion.app.controller;

import com.darion.app.model.TransliterationEngine;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;


public class RulesController {

    @FXML
    private Text arrowText;

    @FXML
    private TextArea textAreaLatA, textAreaLatB, textAreaLatV, textAreaLatH, textAreaLatG, textAreaLatD,
            textAreaLatE, textAreaLatJE, textAreaLatZH, textAreaLatZ, textAreaLatY,
            textAreaLatI, textAreaLatJI, textAreaLatJ, textAreaLatK, textAreaLatL,
            textAreaLatM, textAreaLatN, textAreaLatO, textAreaLatP, textAreaLatR,
            textAreaLatS, textAreaLatT, textAreaLatU, textAreaLatF, textAreaLatKH,
            textAreaLatC, textAreaLatCH, textAreaLatSH, textAreaLatSHCH,
            textAreaLatSoft, textAreaLatJU, textAreaLatJA, textAreaLatJO, textAreaLatSoftO;

    @FXML
    private TextArea textAreaCyrA, textAreaCyrB, textAreaCyrV, textAreaCyrH, textAreaCyrG, textAreaCyrD,
            textAreaCyrE, textAreaCyrJE, textAreaCyrZH, textAreaCyrZ, textAreaCyrY,
            textAreaCyrI, textAreaCyrJI, textAreaCyrJ, textAreaCyrK, textAreaCyrL,
            textAreaCyrM, textAreaCyrN, textAreaCyrO, textAreaCyrP, textAreaCyrR,
            textAreaCyrS, textAreaCyrT, textAreaCyrU, textAreaCyrF, textAreaCyrX,
            textAreaCyrC, textAreaCyrCH, textAreaCyrSH, textAreaCyrSHCH,
            textAreaCyrSoft, textAreaCyrJU, textAreaCyrJA, textAreaCyrJO, textAreaCyrSoftO;

    @FXML
    private Button previousButton;

    @FXML
    private TextArea customRulesTextArea;

    @FXML
    private Label resetToDefaultLabel, saveRulesLabel, openRuleLabel, customRulesLabel;

    private SceneController sceneController;
    private final LinkedHashMap<TextArea, TextArea> cyrToLatMap = new LinkedHashMap<>();
    private Map<String, String> currentRules = new HashMap<>();
    private final List<String> specialCyrillicCases = Arrays.asList("Я", "Є", "Ю", "Йо", "Ї", "Щ");
    private final File directoryWithSavedFiles = new File(System.getProperty("user.home"), "CyrLat");

    @FXML
    public void initialize() {
        initializeCyrToLatMap();
        setUI();
        setupListeners();

        directoryWithSavedFiles.mkdir();

    }

    private void setupListeners() {
        for (Map.Entry<TextArea, TextArea> entry : cyrToLatMap.entrySet()) {
            TextArea cyrillicBox = entry.getKey();
            TextArea latinBox = entry.getValue();

            String cyrillicKey = cyrillicBox.getText();
            String latinKey = latinBox.getText();

            formatAndAdd(currentRules, cyrillicKey, latinKey);

            latinBox.textProperty().addListener((_, oldValue, newValue) -> {

                if (newValue.length() > 4) {
                    latinBox.setText(oldValue);
                    return;
                }

                formatAndAdd(currentRules, cyrillicKey, newValue);

                TransliterationEngine.updateEngine(currentRules);

            });
        }
    }


    @FXML
    public void onResetToDefault() {
        TransliterationEngine.restoreDefaults();
        Map<String, String> defaults = TransliterationEngine.getDefaultRules();
        updateTextBoxes(defaults);


        currentRules.clear();
        currentRules.putAll(defaults);

        clearCustomRulesTextArea();
        showMessage(resetToDefaultLabel, "Success!", "#00ff00");
    }

    private void updateTextBoxes(Map<String, String> rules) {
        for (Map.Entry<TextArea, TextArea> entry : cyrToLatMap.entrySet()) {
            TextArea cyrillicBox = entry.getKey();
            TextArea latinBox = entry.getValue();

            String cyrillicText = cyrillicBox.getText();
            String defaultLatinText = rules.get(cyrillicText);

            if (defaultLatinText != null)
                latinBox.setText(defaultLatinText);
        }
    }

    private void clearCustomRulesTextArea() {
        customRulesTextArea.clear();
    }

    @FXML
    public void onSaveRules() {
        File selectedFile = selectFileToSaveRulesIn();

        if (selectedFile == null)
            return;

        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(TransliterationEngine.getCurrentRules());
            Files.writeString(selectedFile.toPath(), json);
            showMessage(saveRulesLabel, "Success!", "#00ff00");
        } catch (IOException e) {
            showMessage(saveRulesLabel, "Error", "#ff4444");
            e.printStackTrace();
        }
    }

    private File selectFileToSaveRulesIn() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a directory to save your rules in");
        fileChooser.setInitialFileName("rules.json");
        fileChooser.setInitialDirectory(directoryWithSavedFiles);

        Stage stage = (Stage) customRulesTextArea.getScene().getWindow();

        return fileChooser.showSaveDialog(stage);


    }

    @FXML
    public void onOpenRules() {
        File selectededFile = selectFileWithRules();

        if (selectededFile != null) {
            Type mapType = new TypeToken<Map<String, String>>() {
            }.getType();

            try (FileReader reader = new FileReader(selectededFile)) {
                Map<String, String> openedRules = new Gson().fromJson(reader, mapType);
                TransliterationEngine.updateEngine(openedRules);
                updateTextBoxes(TransliterationEngine.getCurrentRules());
                showMessage(openRuleLabel, "Success!", "#00ff00");
            } catch (IOException | JsonSyntaxException e) {
                showMessage(openRuleLabel, "Error", "#ff4444");
                e.printStackTrace();
            }

        }
    }

    @FXML
    private File selectFileWithRules() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a json file with transliteration rules");
        fileChooser.setInitialDirectory(directoryWithSavedFiles);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );

        Stage stage = (Stage) customRulesTextArea.getScene().getWindow();
        return fileChooser.showOpenDialog(stage);
    }



    @FXML
    public void onSetCustomRules() {
        String rawText = customRulesTextArea.getText();

        if (!isFormatValid(rawText)) {
            showMessage(customRulesLabel, "Invalid Format!", "#ff4444");
            return;
        }

        showMessage(customRulesLabel, "Rules Applied Successfully", "#00ff00");
        parseAndApplyCustomRules(rawText);
    }

    private boolean isFormatValid(String text) {
        if (text.isEmpty()) return false;

        String rulePattern = "\"[^\"]+\"\\s*=\\s*\"[^\"]+\"";
        String temp = text.replaceAll(rulePattern, "");
        temp = temp.replaceAll("[\\s,]+", "");
        System.out.println(temp);

        return temp.isEmpty();
    }

    private void showMessage(Label label, String textToShow, String colourHex) {
        label.setVisible(true);
        label.setText(textToShow);
        label.setStyle("-fx-text-fill: " + colourHex);

        PauseTransition messageTimer = new PauseTransition(Duration.seconds(3));
        messageTimer.setOnFinished(actionEvent -> {label.setVisible(false);});
        messageTimer.playFromStart();
    }

    private final Pattern textAreaRegexInputRule = Pattern.compile("\"([^\"])+\"\\s*=\\s*\"([^\"])+\"");

    private void parseAndApplyCustomRules(String rawText) {
        Map<String, String> customRules = new HashMap<>(TransliterationEngine.getDefaultRules());

        Matcher matcher = textAreaRegexInputRule.matcher(rawText);

        while(matcher.find()) {
            String cyrillic = matcher.group(1);
            String latin = matcher.group(2);
            formatAndAdd(customRules, cyrillic, latin);
        }

        TransliterationEngine.updateEngine(customRules);
    }



    @FXML
    public void onPreviousScene() {
        sceneController.toFileScene();
    }


    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }



    private void setUI() {
        for (Map.Entry<TextArea, TextArea> entry : cyrToLatMap.entrySet()) {
            setTextAreaStyle(entry.getKey(), "cyrillic-focus");
            setTextAreaStyle(entry.getValue(), "latin-focus");
        }
        setArrowStyle();
        addGlowOnHover();

    }

    private void setTextAreaStyle(TextArea textArea, String styleClass) {
        textArea.getStyleClass().removeAll("cyrillic-focus", "latin-focus");
        textArea.getStyleClass().add(styleClass);
    }

    private void setArrowStyle() {
        arrowText.getStyleClass().removeAll("arrow-style");
        arrowText.getStyleClass().add("arrow-style");
    }

    private void addGlowOnHover() {
        previousButton.setOnMouseEntered(e -> {
            if (!previousButton.getStyleClass().contains("button-glow")) {
                previousButton.getStyleClass().add("button-glow");
            }
        });

        previousButton.setOnMouseExited(e -> {
            previousButton.getStyleClass().remove("button-glow");
        });
    }

    private void formatAndAdd(Map<String, String> map, String cyrillicKey, String latinValue) {
        map.put(cyrillicKey.toLowerCase(), latinValue.toLowerCase());

        if (specialCyrillicCases.contains(cyrillicKey)) {
            if (latinValue.length() > 1)
                map.put(cyrillicKey, latinValue.substring(0, 1).toUpperCase() + latinValue.substring(1).toLowerCase());
            else
                map.put(cyrillicKey.toUpperCase(), latinValue.toUpperCase());
        } else
            map.put(cyrillicKey.toUpperCase(), latinValue.toUpperCase());
    }

    private void initializeCyrToLatMap() {
        cyrToLatMap.put(textAreaCyrA, textAreaLatA);
        cyrToLatMap.put(textAreaCyrB, textAreaLatB);
        cyrToLatMap.put(textAreaCyrV, textAreaLatV);
        cyrToLatMap.put(textAreaCyrH, textAreaLatH);
        cyrToLatMap.put(textAreaCyrG, textAreaLatG);
        cyrToLatMap.put(textAreaCyrD, textAreaLatD);

        cyrToLatMap.put(textAreaCyrE, textAreaLatE);
        cyrToLatMap.put(textAreaCyrJE, textAreaLatJE);
        cyrToLatMap.put(textAreaCyrZH, textAreaLatZH);
        cyrToLatMap.put(textAreaCyrZ, textAreaLatZ);
        cyrToLatMap.put(textAreaCyrY, textAreaLatY);

        cyrToLatMap.put(textAreaCyrI, textAreaLatI);
        cyrToLatMap.put(textAreaCyrJI, textAreaLatJI);
        cyrToLatMap.put(textAreaCyrJ, textAreaLatJ);
        cyrToLatMap.put(textAreaCyrK, textAreaLatK);
        cyrToLatMap.put(textAreaCyrL, textAreaLatL);

        cyrToLatMap.put(textAreaCyrM, textAreaLatM);
        cyrToLatMap.put(textAreaCyrN, textAreaLatN);
        cyrToLatMap.put(textAreaCyrO, textAreaLatO);
        cyrToLatMap.put(textAreaCyrP, textAreaLatP);
        cyrToLatMap.put(textAreaCyrR, textAreaLatR);

        cyrToLatMap.put(textAreaCyrS, textAreaLatS);
        cyrToLatMap.put(textAreaCyrT, textAreaLatT);
        cyrToLatMap.put(textAreaCyrU, textAreaLatU);
        cyrToLatMap.put(textAreaCyrF, textAreaLatF);
        cyrToLatMap.put(textAreaCyrX, textAreaLatKH);

        cyrToLatMap.put(textAreaCyrC, textAreaLatC);
        cyrToLatMap.put(textAreaCyrCH, textAreaLatCH);
        cyrToLatMap.put(textAreaCyrSH, textAreaLatSH);
        cyrToLatMap.put(textAreaCyrSHCH, textAreaLatSHCH);

        cyrToLatMap.put(textAreaCyrSoft, textAreaLatSoft);
        cyrToLatMap.put(textAreaCyrJU, textAreaLatJU);
        cyrToLatMap.put(textAreaCyrJA, textAreaLatJA);
        cyrToLatMap.put(textAreaCyrJO, textAreaLatJO);
        cyrToLatMap.put(textAreaCyrSoftO, textAreaLatSoftO);
    }
}
