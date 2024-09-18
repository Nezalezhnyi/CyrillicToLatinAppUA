package transliterator.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RulesChangingController {
    private SceneController sceneController;
    private int whichLatinIsUsed;

    @FXML
    private TextArea textAreaLatA, textAreaLatB, textAreaLatV, textAreaLatH, textAreaLatG,
            textAreaLatE, textAreaLatJE, textAreaLatZH, textAreaLatZ, textAreaLatY,
            textAreaLatI, textAreaLatJI, textAreaLatJ, textAreaLatK, textAreaLatL,
            textAreaLatM, textAreaLatN, textAreaLatO, textAreaLatP, textAreaLatR,
            textAreaLatS, textAreaLatT, textAreaLatU, textAreaLatF, textAreaLatKH,
            textAreaLatC, textAreaLatCH, textAreaLatSH, textAreaLatSHCH,
            textAreaLatSoft, textAreaLatJU, textAreaLatJA, textAreaLatJO, textAreaLatSoftO;

    private final Map<TextArea, String> defaultValuesMap = new HashMap<>();
    private Map<String, TextArea> transliterationToLatinCustom = new HashMap<>();

    @FXML
    public void initialize() {
        saveDefaultValues();
        createTextAreaLatinCustomMap();
    }


    @FXML
    private void applyDefaultLatin() {
        for (Map.Entry<TextArea, String> entry : defaultValuesMap.entrySet()) {
            entry.getKey().setText(entry.getValue());
        }
    }

    @FXML
    private void applyCustomLatin() {
        whichLatinIsUsed = 1;
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
        defaultValuesMap.put(textAreaLatA, textAreaLatA.getText());
        defaultValuesMap.put(textAreaLatB, textAreaLatB.getText());
        defaultValuesMap.put(textAreaLatV, textAreaLatV.getText());
        defaultValuesMap.put(textAreaLatH, textAreaLatH.getText());
        defaultValuesMap.put(textAreaLatG, textAreaLatG.getText());
        defaultValuesMap.put(textAreaLatE, textAreaLatE.getText());
        defaultValuesMap.put(textAreaLatJE, textAreaLatJE.getText());
        defaultValuesMap.put(textAreaLatZH, textAreaLatZH.getText());
        defaultValuesMap.put(textAreaLatZ, textAreaLatZ.getText());
        defaultValuesMap.put(textAreaLatY, textAreaLatY.getText());
        defaultValuesMap.put(textAreaLatI, textAreaLatI.getText());
        defaultValuesMap.put(textAreaLatJI, textAreaLatJI.getText());
        defaultValuesMap.put(textAreaLatJ, textAreaLatJ.getText());
        defaultValuesMap.put(textAreaLatK, textAreaLatK.getText());
        defaultValuesMap.put(textAreaLatL, textAreaLatL.getText());
        defaultValuesMap.put(textAreaLatM, textAreaLatM.getText());
        defaultValuesMap.put(textAreaLatN, textAreaLatN.getText());
        defaultValuesMap.put(textAreaLatO, textAreaLatO.getText());
        defaultValuesMap.put(textAreaLatP, textAreaLatP.getText());
        defaultValuesMap.put(textAreaLatR, textAreaLatR.getText());
        defaultValuesMap.put(textAreaLatS, textAreaLatS.getText());
        defaultValuesMap.put(textAreaLatT, textAreaLatT.getText());
        defaultValuesMap.put(textAreaLatU, textAreaLatU.getText());
        defaultValuesMap.put(textAreaLatF, textAreaLatF.getText());
        defaultValuesMap.put(textAreaLatKH, textAreaLatKH.getText());
        defaultValuesMap.put(textAreaLatC, textAreaLatC.getText());
        defaultValuesMap.put(textAreaLatCH, textAreaLatCH.getText());
        defaultValuesMap.put(textAreaLatSH, textAreaLatSH.getText());
        defaultValuesMap.put(textAreaLatSHCH, textAreaLatSHCH.getText());
        defaultValuesMap.put(textAreaLatSoft, textAreaLatSoft.getText());
        defaultValuesMap.put(textAreaLatJU, textAreaLatJU.getText());
        defaultValuesMap.put(textAreaLatJA, textAreaLatJA.getText());
        defaultValuesMap.put(textAreaLatJO, textAreaLatJO.getText());
        defaultValuesMap.put(textAreaLatSoftO, textAreaLatSoftO.getText());
    }

    private void createTextAreaLatinCustomMap() {
        transliterationToLatinCustom.put("а", textAreaLatA);
        transliterationToLatinCustom.put("б", textAreaLatB);
        transliterationToLatinCustom.put("в", textAreaLatV);
        transliterationToLatinCustom.put("г", textAreaLatH);
        transliterationToLatinCustom.put("ґ", textAreaLatG);
        transliterationToLatinCustom.put("е", textAreaLatE);
        transliterationToLatinCustom.put("є", textAreaLatJE);
        transliterationToLatinCustom.put("ж", textAreaLatZH);
        transliterationToLatinCustom.put("з", textAreaLatZ);
        transliterationToLatinCustom.put("и", textAreaLatY);
        transliterationToLatinCustom.put("і", textAreaLatI);
        transliterationToLatinCustom.put("ї", textAreaLatJI);
        transliterationToLatinCustom.put("й", textAreaLatJ);
        transliterationToLatinCustom.put("к", textAreaLatK);
        transliterationToLatinCustom.put("л", textAreaLatL);
        transliterationToLatinCustom.put("м", textAreaLatM);
        transliterationToLatinCustom.put("н", textAreaLatN);
        transliterationToLatinCustom.put("о", textAreaLatO);
        transliterationToLatinCustom.put("п", textAreaLatP);
        transliterationToLatinCustom.put("р", textAreaLatR);
        transliterationToLatinCustom.put("с", textAreaLatS);
        transliterationToLatinCustom.put("т", textAreaLatT);
        transliterationToLatinCustom.put("у", textAreaLatU);
        transliterationToLatinCustom.put("ф", textAreaLatF);
        transliterationToLatinCustom.put("х", textAreaLatKH);
        transliterationToLatinCustom.put("ц", textAreaLatC);
        transliterationToLatinCustom.put("ч", textAreaLatCH);
        transliterationToLatinCustom.put("ш", textAreaLatSH);
        transliterationToLatinCustom.put("щ", textAreaLatSHCH);
        transliterationToLatinCustom.put("ь", textAreaLatSoft);
        transliterationToLatinCustom.put("ю", textAreaLatJU);
        transliterationToLatinCustom.put("я", textAreaLatJA);
        transliterationToLatinCustom.put("йо", textAreaLatJO);
        transliterationToLatinCustom.put("ьо", textAreaLatSoftO);
    }
}
