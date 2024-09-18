package transliterator.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import transliterator.Transliterator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RulesChangingController {
    private SceneController sceneController;

    @FXML
    private TextArea textAreaLatA, textAreaLatB, textAreaLatV, textAreaLatH, textAreaLatG, textAreaLatD,
            textAreaLatE, textAreaLatJE, textAreaLatZH, textAreaLatZ, textAreaLatY,
            textAreaLatI, textAreaLatJI, textAreaLatJ, textAreaLatK, textAreaLatL,
            textAreaLatM, textAreaLatN, textAreaLatO, textAreaLatP, textAreaLatR,
            textAreaLatS, textAreaLatT, textAreaLatU, textAreaLatF, textAreaLatKH,
            textAreaLatC, textAreaLatCH, textAreaLatSH, textAreaLatSHCH,
            textAreaLatSoft, textAreaLatJU, textAreaLatJA, textAreaLatJO, textAreaLatSoftO;

    private final Map<TextArea, String> transliterationToLatinMapDefault = new HashMap<>();
    private final Map<String, TextArea> textAreaMap = new HashMap<>();
    private final Map<TextArea, String> originalStyles = new HashMap<>();

    //////
    @FXML
    public void updateCustomTransliterationMap() {
        for (Map.Entry<String, TextArea> entry : textAreaMap.entrySet()) {
            String valueText = entry.getValue().getText(); // All letters of any valueText are uppercase!!!
            String cyrillicLetterLowerCase = entry.getKey();
            String newLatinValueLowerCase = valueText.toLowerCase();
            Transliterator.updateTransliterationRule(cyrillicLetterLowerCase, newLatinValueLowerCase);
            String cyrillicLetterUpperCase = entry.getKey().toUpperCase();
            String newLatinValueUpperCase = valueText.length() == 1 ? valueText.toUpperCase() : valueText.substring(0, 1).toUpperCase() + valueText.substring(1).toLowerCase();
            Transliterator.updateTransliterationRule(cyrillicLetterUpperCase, newLatinValueUpperCase);
        }
    }
    //////

    public void highlightSameTextAreaLatValues() {
        for (Map.Entry<String, TextArea> entry : textAreaMap.entrySet()) {
            entry.getValue().setStyle(originalStyles.get(entry.getValue())); // Повертаємо початковий стиль
        }

        for (Map.Entry<String, TextArea> entryOriginal : textAreaMap.entrySet()) {
            String textAreaValueTextOriginal = entryOriginal.getValue().getText();
            for (Map.Entry<String, TextArea> entryNext : textAreaMap.entrySet()) {

                if (entryOriginal != entryNext && entryOriginal.getValue() != textAreaLatJO && entryOriginal.getValue() != textAreaLatSoftO) {
                    String textAreaValueTextNext = entryNext.getValue().getText();

                    if (Objects.equals(textAreaValueTextOriginal, textAreaValueTextNext)) {
                        entryOriginal.getValue().setStyle("-fx-control-inner-background: #FFFF99;");
                        entryNext.getValue().setStyle("-fx-control-inner-background: #FFFF99;");
                    }
                }
            }
        }
    }


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

        //////
        for (Map.Entry<String, TextArea> entry : textAreaMap.entrySet()) {
            TextArea textArea = entry.getValue();
                textArea.textProperty().addListener((observable, oldValue, newValue) -> {
                    highlightSameTextAreaLatValues();
                });
        }
        //////

    }


    @FXML
    private void applyDefaultLatin() {
        for (Map.Entry<TextArea, String> entry : transliterationToLatinMapDefault.entrySet()) {
            entry.getKey().setText(entry.getValue());
        }
    }




    @FXML
    public void switchToTextTransliterationScene (){
        sceneController.switchToTextTransliterationScene(); // Uses sceneController, got from the MainApp.java using setter, to switch to the Text scene
    }

    @FXML
    public void switchToFileTransliterationScene () {
        sceneController.switchToFileTransliterationScene(); //Switches to the file transliteration scene using the sceneController
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
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




}
