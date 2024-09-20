package transliterator;

import java.util.*;

public class Transliterator {
        private static Map<String, String> transliterationToLatinMapDefault = new HashMap<>(Map.<String, String>ofEntries(
            Map.entry("йо", "jo"),
            Map.entry("ьо", "jo"), Map.entry("ЬО", "JO"), Map.entry("Ьо", "Jo"), Map.entry("ьО", "jO"),
            Map.entry("а", "a"), Map.entry("А", "A"),
            Map.entry("б", "b"), Map.entry("Б", "B"),
            Map.entry("в", "v"), Map.entry("В", "V"),
            Map.entry("г", "h"), Map.entry("Г", "H"),
            Map.entry("ґ", "g"), Map.entry("Ґ", "G"),
            Map.entry("д", "d"), Map.entry("Д", "D"),
            Map.entry("е", "e"), Map.entry("Е", "E"),
            Map.entry("є", "je"), Map.entry("Є", "Je"),
            Map.entry("ж", "ž"), Map.entry("Ж", "Ž"),
            Map.entry("з", "z"), Map.entry("З", "Z"),
            Map.entry("и", "y"), Map.entry("И", "Y"),
            Map.entry("і", "i"), Map.entry("І", "I"),
            Map.entry("ї", "ji"), Map.entry("Ї", "Ji"),
            Map.entry("й", "j"), Map.entry("Й", "J"),
            Map.entry("к", "k"), Map.entry("К", "K"),
            Map.entry("л", "l"), Map.entry("Л", "L"),
            Map.entry("м", "m"), Map.entry("М", "M"),
            Map.entry("н", "n"), Map.entry("Н", "N"),
            Map.entry("о", "o"), Map.entry("О", "O"),
            Map.entry("п", "p"), Map.entry("П", "P"),
            Map.entry("р", "r"), Map.entry("Р", "R"),
            Map.entry("с", "s"), Map.entry("С", "S"),
            Map.entry("т", "t"), Map.entry("Т", "T"),
            Map.entry("у", "u"), Map.entry("У", "U"),
            Map.entry("ф", "f"), Map.entry("Ф", "F"),
            Map.entry("х", "x"), Map.entry("Х", "X"),
            Map.entry("ц", "c"), Map.entry("Ц", "C"),
            Map.entry("ч", "č"), Map.entry("Ч", "Č"),
            Map.entry("ш", "š"), Map.entry("Ш", "Š"),
            Map.entry("щ", "šč"), Map.entry("Щ", "Šč"),
            Map.entry("ю", "ju"), Map.entry("Ю", "Ju"),
            Map.entry("я", "ja"), Map.entry("Я", "Ja"),
            Map.entry("ь", "'"), Map.entry("Ь", "'"),
            Map.entry("'", "'"), Map.entry("`", "'")
    ));
    private static Map<String, String> transliterationToCyrillicMapDefault = reverseTransliterationDirection(transliterationToLatinMapDefault); // Uses the function to reverse the toLatin Map and switch cyrillic and latin letters


    public static String transliterateInputText(String inputText, boolean isToLatin) { // Respective transliteration methods (toLatin and toCyrillic) from the FileSceneController and TextSceneController classes pass the boolean field isToLatin to specify the transliteration direction
        Map<String, String> transliterationDirection;
        if (isToLatin)
            transliterationDirection = transliterationToLatinMapDefault;
        else
            transliterationDirection = transliterationToCyrillicMapDefault;

        List<String> sortedKeys = transliterationDirection.keySet().stream() // The keySet() method retrieves the map's keys and sorts them so that longer Cyrillic combinations like "йо", "ьо", etc. come before shorter ones like "j".
                .sorted(Comparator.comparingInt(String::length).reversed()) // this prevents incorrect transliteration, such as translating "ju" as "йу" by matching "j" first. reversed() method is used to place letter in descending order (we need the combination of letter to come first)
                .toList(); // it returns a list with keys (cyrillic or latin) which starts with "йо", "ьо", etc. - for cyrillic; and "jo", "ju", "ji", etc. - for latin
        for (String key : sortedKeys) {
            if (!key.equals("Я") && !key.equals("Ю") && !key.equals("Є")) // ля = la to make LAJAJAJAJAJAJA not LAJaJaJaJa
                inputText = inputText.replace(key, transliterationDirection.get(key)); //  Replaces occurrences of each key in inputText with its corresponding value from the map using get(key).
        }                                                                       // sorting ensures that longer combinations like "ьо" replace first (e.g., "ьо" -> "jo", not 'о; "ju" -> "ю", not йу)
        inputText = checkOccurrenceOfLettersBeforeOrAfterJAJUJEJI(inputText); // ля = la to make LAJAJAJAJAJAJA not LAJaJaJaJa
        return inputText;
    }

    // LJA ->
    public static void updateTransliterationRule(String cyrillicLetter, String latinEquivalent) {
        transliterationToLatinMapDefault.put(cyrillicLetter, latinEquivalent);
        transliterationToCyrillicMapDefault = reverseTransliterationDirection(transliterationToLatinMapDefault);
    }

    public static void removeTransliterationRule(String ruleNeedsToBeDeleted) {
        transliterationToLatinMapDefault.remove(ruleNeedsToBeDeleted);
    }


    private static Map<String, String> reverseTransliterationDirection(Map<String, String> transliterationMap) {
        Map<String, String> reversedMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : transliterationMap.entrySet()) {// Takes each key-value pair (entry) from the transliterationToLatinMap using entrySet(), which provides a set of all pairs in the map
            String latinRule = entry.getValue();
            reversedMap.put(entry.getValue(), entry.getKey()); // Gets a value from the entry (key-value pair) and places it as a key; Gets a key and places in as a value; and puts it in the reverse map
        }
            return reversedMap;
    }






    private static String checkOccurrenceOfLettersBeforeOrAfterJAJUJEJI(String inputText) {
        for (int i = 0; i < inputText.length(); i++) {
            char currentChar = inputText.charAt(i);

            if (currentChar == 'Я' || currentChar == 'Ю' || currentChar == 'Є' || currentChar == 'Ї') {
                boolean isBefore = i > 0 && Character.isLetter(inputText.charAt(i - 1));
                boolean isAfter = i < inputText.length() - 1 && Character.isLetter(inputText.charAt(i + 1));

                if (isBefore || isAfter) {
                    if (currentChar == 'Я') {
                        inputText = inputText.substring(0, i) + "JA" + inputText.substring(i + 1);
                    } else if (currentChar == 'Ю') {
                        inputText = inputText.substring(0, i) + "JU" + inputText.substring(i + 1);
                    } else if (currentChar == 'Є') {
                        inputText = inputText.substring(0, i) + "JE" + inputText.substring(i + 1);
                    } else if (currentChar == 'Ї') {
                        inputText = inputText.substring(0, i) + "JI" + inputText.substring(i + 1);
                    }
                }
            }
        }
        return inputText;
    }

    public static Map<String, String> getTransliterationToLatinRulesMap() {
        return transliterationToLatinMapDefault;
    }

}
