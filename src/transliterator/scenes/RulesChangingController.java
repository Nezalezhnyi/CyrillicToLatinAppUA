package transliterator.scenes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import transliterator.Transliterator;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class RulesChangingController {
    private SceneController sceneController;
    private final Map<TextArea, String> transliterationToLatinMapDefault = new HashMap<>();
    private final Map<String, TextArea> textAreaMap = new HashMap<>();
    private final Map<TextArea, String> originalStyles = new HashMap<>();
    private Map<String, String> addedAdditionalRulesMap = new HashMap<>();
    private String oldAdditionalCustomRulesTextArea = "";
    private Stage primaryStage;

    @FXML
    private TextArea textAreaLatA, textAreaLatB, textAreaLatV, textAreaLatH, textAreaLatG, textAreaLatD,
            textAreaLatE, textAreaLatJE, textAreaLatZH, textAreaLatZ, textAreaLatY,
            textAreaLatI, textAreaLatJI, textAreaLatJ, textAreaLatK, textAreaLatL,
            textAreaLatM, textAreaLatN, textAreaLatO, textAreaLatP, textAreaLatR,
            textAreaLatS, textAreaLatT, textAreaLatU, textAreaLatF, textAreaLatKH,
            textAreaLatC, textAreaLatCH, textAreaLatSH, textAreaLatSHCH,
            textAreaLatSoft, textAreaLatJU, textAreaLatJA, textAreaLatJO, textAreaLatSoftO;

    @FXML
    private TextArea additionalCustomRulesTextArea;

    @FXML
    private Button applyRulesButton, applyAdditionalRulesButton;

    @FXML
    private Text textAboveAdditionalRulesTextArea, rulesWereResetText, saveTemplateText, loadTemplateText;

    @FXML
    public void initialize() {
        for (Map.Entry<String, TextArea> entry : textAreaMap.entrySet()) {
            originalStyles.put(entry.getValue(), entry.getValue().getStyle());
        }

        saveDefaultValues();
        textAreaMap.put("а", textAreaLatA);
        textAreaMap.put("б", textAreaLatB);
        textAreaMap.put("в", textAreaLatV);
        textAreaMap.put("г", textAreaLatH);
        textAreaMap.put("ґ", textAreaLatG);
        textAreaMap.put("д", textAreaLatD);
        textAreaMap.put("е", textAreaLatE);
        textAreaMap.put("є", textAreaLatJE);
        textAreaMap.put("ж", textAreaLatZH);
        textAreaMap.put("з", textAreaLatZ);
        textAreaMap.put("и", textAreaLatY);
        textAreaMap.put("і", textAreaLatI);
        textAreaMap.put("ї", textAreaLatJI);
        textAreaMap.put("й", textAreaLatJ);
        textAreaMap.put("к", textAreaLatK);
        textAreaMap.put("л", textAreaLatL);
        textAreaMap.put("м", textAreaLatM);
        textAreaMap.put("н", textAreaLatN);
        textAreaMap.put("о", textAreaLatO);
        textAreaMap.put("п", textAreaLatP);
        textAreaMap.put("р", textAreaLatR);
        textAreaMap.put("с", textAreaLatS);
        textAreaMap.put("т", textAreaLatT);
        textAreaMap.put("у", textAreaLatU);
        textAreaMap.put("ф", textAreaLatF);
        textAreaMap.put("х", textAreaLatKH);
        textAreaMap.put("ц", textAreaLatC);
        textAreaMap.put("ч", textAreaLatCH);
        textAreaMap.put("ш", textAreaLatSH);
        textAreaMap.put("щ", textAreaLatSHCH);
        textAreaMap.put("ь", textAreaLatSoft);
        textAreaMap.put("ю", textAreaLatJU);
        textAreaMap.put("я", textAreaLatJA);
        textAreaMap.put("йо", textAreaLatJO);
        textAreaMap.put("ьо", textAreaLatSoftO);

        for (Map.Entry<String, TextArea> entry : textAreaMap.entrySet()) {
            TextArea textArea = entry.getValue();
            textArea.textProperty().addListener((observable, oldValue, newValue) -> {
                highlightSameTextAreaLatValues();
                changeApplyChangesButtonColorIfTextAreaLatChangesDetected();
            });
        }
        additionalCustomRulesTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            changeAddAdditionalCustomRulesButtonColorIfTextAreaLatChangesDetected();
        });

    }


    @FXML
    public void updateCustomTransliterationMap() {
        for (Map.Entry<String, TextArea> cyrillicAndLatinPair : textAreaMap.entrySet()) {  // textAreaMap contains all textAreaLats which user can change
            String latinRule = cyrillicAndLatinPair.getValue().getText().trim();  // We get the value of a textAreaLat, convert it to text and erase all spaces
            String cyrillicLetterLowerCase = cyrillicAndLatinPair.getKey().trim(); // We get a lower case cyrillic letter which corresponds its latin one

            // If it's "ьо", we need to process this letter in a special way
            if (cyrillicLetterLowerCase.equals("ьо")) {
                String newLatinRuleLowerCase = latinRule.toLowerCase().trim(); // "io" -> lowercase letter
                String newLatinRuleUpperCase = latinRule.toUpperCase().trim(); // "IO" -> uppercase letter

                // Adds four special rules for "ьо" to the map
                Transliterator.updateTransliterationRule("ьо", newLatinRuleLowerCase); // "ьо" -> "io"
                Transliterator.updateTransliterationRule("ЬО", newLatinRuleUpperCase); // "ЬО" -> "IO"
                Transliterator.updateTransliterationRule("ьО", newLatinRuleLowerCase.charAt(0) + newLatinRuleUpperCase.substring(1)); // "ьО" -> "iO"
                Transliterator.updateTransliterationRule("Ьо", newLatinRuleUpperCase.charAt(0) + newLatinRuleLowerCase.substring(1)); // "Ьо" -> "Io"
            } else {
                // Standard rule for all letters
                String newLatinRuleLowerCase = latinRule.toLowerCase();
                Transliterator.updateTransliterationRule(cyrillicLetterLowerCase, newLatinRuleLowerCase); // A lowercase cyrillic letter always corresponds to its latin one

                String newLatinRuleUpperCase = latinRule.length() == 1 // If a new latin rule (in textArea box) is composed of only one letter,
                        ? latinRule.toUpperCase() // then its capital form corresponds the capital cyrillic analogue;
                        : latinRule.substring(0, 1).toUpperCase() + latinRule.substring(1).toLowerCase(); // if the latin rule has more
                Transliterator.updateTransliterationRule(cyrillicLetterLowerCase.toUpperCase(), newLatinRuleUpperCase);
            }

        }
        applyRulesButton.setStyle("-fx-border-color: #2AC139"); //BAD// Льо -> lio
    }

    /////////!!!!!!!!!!!!!!!!!!!!!!!!!
    @FXML
    public void addAdditionalCustomRules() {
        Map<String, String> previousRules = new HashMap<>(addedAdditionalRulesMap);

        String inputText = additionalCustomRulesTextArea.getText().trim(); // Gets the text from the textArea bar
        String[] ruleParts = inputText.isEmpty() ? new String[0] : inputText.split(","); // If we have many rules, it will create an array with [cyrillicRule0 -> latinRule0, cyrillicRule1 -> latinRule1, ...]

        try {
            addedAdditionalRulesMap.clear();
            for (String rule : ruleParts) {
                if (rule.contains("=")) {
                    String[] cyrillicAndLatinLetters = rule.split("="); // Breaks down cyrillicRule -> latinRule into [cyrillicRule, latinRule]
                    if (cyrillicAndLatinLetters.length == 2) {
                        String cyrillicRule = cyrillicAndLatinLetters[0].trim(); // Erases all spaces since we need only the cyrillicRule
                        String latinRule = cyrillicAndLatinLetters[1].trim(); // Erases all spaces since we need only the latinRule

                        String cyrillicRuleUpperCase = cyrillicRule.toUpperCase(); //ля
                        String cyrillicRuleLowerCase = cyrillicRule.toLowerCase(); //ЛЯ
                        String latinRuleUpperCase = latinRule.toUpperCase(); //LA
                        String latinRuleLowerCase = latinRule.toLowerCase(); //la

                        Transliterator.updateTransliterationRule(cyrillicRuleLowerCase, latinRuleLowerCase); // "ля" -> "la"
                        Transliterator.updateTransliterationRule(cyrillicRuleUpperCase, latinRuleUpperCase); // "ЛЯ" -> "LA"
                        String firstLowerSecondUpperCyrillic = cyrillicRuleLowerCase.charAt(0) + cyrillicRuleUpperCase.substring(1); // ьО
                        String firstLowerSecondUpperLatin = latinRuleLowerCase.charAt(0) + latinRuleUpperCase.substring(1); // iO
                        Transliterator.updateTransliterationRule(firstLowerSecondUpperCyrillic, firstLowerSecondUpperLatin); // "ьО" -> "iO"
                        String firstUpperSecondLowerCyrillic = cyrillicRuleUpperCase.charAt(0) + cyrillicRuleLowerCase.substring(1); // Ьо
                        String firstUpperSecondLowerLatin = latinRuleUpperCase.charAt(0) + latinRuleLowerCase.substring(1); // Io
                        Transliterator.updateTransliterationRule(firstUpperSecondLowerCyrillic, firstUpperSecondLowerLatin); // "Ьо" -> "Io"

                        addedAdditionalRulesMap.put(cyrillicRuleLowerCase, latinRuleLowerCase);
                        addedAdditionalRulesMap.put(cyrillicRuleUpperCase, latinRuleUpperCase);
                        addedAdditionalRulesMap.put(firstLowerSecondUpperCyrillic, firstLowerSecondUpperLatin);
                        addedAdditionalRulesMap.put(firstUpperSecondLowerCyrillic, firstUpperSecondLowerLatin);

                    }
                } else {
                    textAboveAdditionalRulesTextArea.setText("Додаткове правило було написано в неправильному форматі");
                    Timeline timeline = new Timeline(new KeyFrame(
                            Duration.seconds(5),
                            event -> textAboveAdditionalRulesTextArea.setText("")
                    ));

                    timeline.setCycleCount(1);
                    timeline.play();
                    return;
                }
            }
            for (Map.Entry<String, String> entry : previousRules.entrySet()) {
                if (!addedAdditionalRulesMap.containsKey(entry.getKey())) {
                    Transliterator.removeTransliterationRule(entry.getKey());
                    addedAdditionalRulesMap.remove(entry.getKey());

                }
            }
            applyAdditionalRulesButton.setStyle("-fx-border-color: #2AC139"); //BAD/
            oldAdditionalCustomRulesTextArea = inputText;


        }  catch (Exception e) {
            textAboveAdditionalRulesTextArea.setText("Додаткове правило було написано в неправильному форматі");
            }

        }

    @FXML
    public void highlightSameTextAreaLatValues() {
        for (Map.Entry<String, TextArea> entry : textAreaMap.entrySet()) {
            entry.getValue().setStyle(originalStyles.get(entry.getValue())); // Повертаємо початковий стиль
        }

        for (Map.Entry<String, TextArea> entryOriginal : textAreaMap.entrySet()) {
            String textAreaValueTextOriginal = entryOriginal.getValue().getText().toLowerCase();
            for (Map.Entry<String, TextArea> entryNext : textAreaMap.entrySet()) {

                if (entryOriginal != entryNext && entryOriginal.getValue() != textAreaLatJO && entryOriginal.getValue() != textAreaLatSoftO) {
                    String textAreaValueTextNext = entryNext.getValue().getText().toLowerCase();

                    if (Objects.equals(textAreaValueTextOriginal, textAreaValueTextNext)) {
                        entryOriginal.getValue().setStyle("-fx-control-inner-background: #FFFF99;");
                        entryNext.getValue().setStyle("-fx-control-inner-background: #FFFF99;");
                    }
                }
            }
        }
    }

    @FXML
    public void changeAddAdditionalCustomRulesButtonColorIfTextAreaLatChangesDetected() {
        if (!Objects.equals(additionalCustomRulesTextArea.getText(), oldAdditionalCustomRulesTextArea))
            applyAdditionalRulesButton.setStyle("-fx-border-color: #FF6464");
        else
            applyAdditionalRulesButton.setStyle("-fx-border-color: #2AC139");
    }

    @FXML
    public void changeApplyChangesButtonColorIfTextAreaLatChangesDetected() {
         for (Map.Entry<String, TextArea> changedButNotUpdatedRules : textAreaMap.entrySet()) {
            String changedButNotUpdatedLatinRule = changedButNotUpdatedRules.getValue().getText().toLowerCase();
            String currentLatinRule = Transliterator.getTransliterationToLatinRulesMap().get(changedButNotUpdatedRules.getKey());
            if (!Objects.equals(changedButNotUpdatedLatinRule, currentLatinRule)) {
                applyRulesButton.setStyle("-fx-border-color: #FF6464");
                return;
            }
            else {
                applyRulesButton.setStyle("-fx-border-color: #2AC139");
            }
        }
    }

    @FXML
    public void applyDefaultLatin() {
        for (Map.Entry<TextArea, String> entry : transliterationToLatinMapDefault.entrySet()) {
            entry.getKey().setText(entry.getValue());
        }

        for (Map.Entry<String, String> additionalRule : addedAdditionalRulesMap.entrySet()) {
            if (Transliterator.getTransliterationToLatinRulesMap().containsKey(additionalRule.getKey())) {
                Transliterator.removeTransliterationRule(additionalRule.getKey());
            }
        }
        updateCustomTransliterationMap();
        showSuccessfulMessage(rulesWereResetText, "Правила успішно скинуто!");
    }

    @FXML
    public void switchToTextTransliterationScene (){
        sceneController.switchToTextTransliterationScene(); // Uses sceneController, got from the MainApp.java using setter, to switch to the Text scene
    }

    @FXML
    public void switchToFileTransliterationScene () {
        sceneController.switchToFileTransliterationScene(); //Switches to the file transliteration scene using the sceneController
    }

    private void saveDefaultValues() {

        transliterationToLatinMapDefault.put(textAreaLatA, textAreaLatA.getText());
        transliterationToLatinMapDefault.put(textAreaLatB, textAreaLatB.getText());
        transliterationToLatinMapDefault.put(textAreaLatV, textAreaLatV.getText());
        transliterationToLatinMapDefault.put(textAreaLatH, textAreaLatH.getText());
        transliterationToLatinMapDefault.put(textAreaLatG, textAreaLatG.getText());
        transliterationToLatinMapDefault.put(textAreaLatD, textAreaLatD.getText());
        transliterationToLatinMapDefault.put(textAreaLatE, textAreaLatE.getText());
        transliterationToLatinMapDefault.put(textAreaLatJE, textAreaLatJE.getText());
        transliterationToLatinMapDefault.put(textAreaLatZH, textAreaLatZH.getText());
        transliterationToLatinMapDefault.put(textAreaLatZ, textAreaLatZ.getText());
        transliterationToLatinMapDefault.put(textAreaLatY, textAreaLatY.getText());
        transliterationToLatinMapDefault.put(textAreaLatI, textAreaLatI.getText());
        transliterationToLatinMapDefault.put(textAreaLatJI, textAreaLatJI.getText());
        transliterationToLatinMapDefault.put(textAreaLatJ, textAreaLatJ.getText());
        transliterationToLatinMapDefault.put(textAreaLatK, textAreaLatK.getText());
        transliterationToLatinMapDefault.put(textAreaLatL, textAreaLatL.getText());
        transliterationToLatinMapDefault.put(textAreaLatM, textAreaLatM.getText());
        transliterationToLatinMapDefault.put(textAreaLatN, textAreaLatN.getText());
        transliterationToLatinMapDefault.put(textAreaLatO, textAreaLatO.getText());
        transliterationToLatinMapDefault.put(textAreaLatP, textAreaLatP.getText());
        transliterationToLatinMapDefault.put(textAreaLatR, textAreaLatR.getText());
        transliterationToLatinMapDefault.put(textAreaLatS, textAreaLatS.getText());
        transliterationToLatinMapDefault.put(textAreaLatT, textAreaLatT.getText());
        transliterationToLatinMapDefault.put(textAreaLatU, textAreaLatU.getText());
        transliterationToLatinMapDefault.put(textAreaLatF, textAreaLatF.getText());
        transliterationToLatinMapDefault.put(textAreaLatKH, textAreaLatKH.getText());
        transliterationToLatinMapDefault.put(textAreaLatC, textAreaLatC.getText());
        transliterationToLatinMapDefault.put(textAreaLatCH, textAreaLatCH.getText());
        transliterationToLatinMapDefault.put(textAreaLatSH, textAreaLatSH.getText());
        transliterationToLatinMapDefault.put(textAreaLatSHCH, textAreaLatSHCH.getText());
        transliterationToLatinMapDefault.put(textAreaLatSoft, textAreaLatSoft.getText());
        transliterationToLatinMapDefault.put(textAreaLatJU, textAreaLatJU.getText());
        transliterationToLatinMapDefault.put(textAreaLatJA, textAreaLatJA.getText());
        transliterationToLatinMapDefault.put(textAreaLatJO, textAreaLatJO.getText());
        transliterationToLatinMapDefault.put(textAreaLatSoftO, textAreaLatSoftO.getText());
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }



    //////////////////////
    @FXML
    public void saveTemplate() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        if (selectedDirectory != null) {
            String filePath = selectedDirectory.getAbsolutePath() + "/template.txt"; // Зберігаємо у файл template.txt
            try (FileWriter writer = new FileWriter(filePath)) {
                for (Map.Entry<String, String> entry : Transliterator.getTransliterationToLatinRulesMap().entrySet()) {
                    writer.write(entry.getKey() + "=" + entry.getValue() + "\n"); // Записуємо кожен запис у форматі key=value
                }
                showSuccessfulMessage(saveTemplateText, "Шаблон успішно збережено");
            } catch (IOException e) {
                showErrorMessage(saveTemplateText);
            }
        }
    }

    @FXML
    public void loadTemplate() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                Map<String, String> loadedMap = new HashMap<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.contains("=")) {throw new IOException();}
                    String[] parts = line.split("=", 2);  // Розбиваємо кожен рядок на ключ і значення
                    if (parts.length == 2) {
                        loadedMap.put(parts[0], parts[1]); // Додаємо в мапу ключ і значення
                    }
                }

                for (Map.Entry<String, String> entry : loadedMap.entrySet()) {
                    Transliterator.updateTransliterationRule(entry.getKey(), entry.getValue());
                }

                for (Map.Entry<String, TextArea> textAreaCyrillicToLatinRule : textAreaMap.entrySet()) {
                    String cyrillicRule = textAreaCyrillicToLatinRule.getKey();
                    String currentText = textAreaCyrillicToLatinRule.getValue().getText(); // Отримуємо поточний текст з TextArea
                    String transliterationRule = Transliterator.getTransliterationToLatinRulesMap().get(cyrillicRule).toUpperCase(); // Отримуємо правило з гешмапи

                    if (!Objects.equals(currentText, transliterationRule)) {
                        textAreaCyrillicToLatinRule.getValue().setText(transliterationRule.toUpperCase()); // Оновлюємо текст в TextArea
                    }
                }

                showSuccessfulMessage(loadTemplateText, "Шаблон успішно завантажено!");


            } catch (IOException e) {
                showErrorMessage(loadTemplateText);
            }
        }


    }

    private void showErrorMessage(Text textUnderButton) {
        textUnderButton.setFill(Color.RED);
        textUnderButton.setText("Сталася помилка");
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(5),
                event -> textUnderButton.setText("")
        ));

        timeline.setCycleCount(1);
        timeline.play();
    }

    private void showSuccessfulMessage(Text textUnderButton, String message) {
        textUnderButton.setFill(Color.valueOf("#059422"));
        textUnderButton.setText(message);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(5),
                event -> textUnderButton.setText("")
        ));

        timeline.setCycleCount(1);
        timeline.play();
    }

    public void setStage(Stage primaryStage) {
        primaryStage = primaryStage;
    }



}



