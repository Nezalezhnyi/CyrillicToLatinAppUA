package transliterator;

import javafx.scene.control.TextArea;

import java.util.*;

public class Transliterator {
        private static Map<String, String> transliterationToLatinMap = new HashMap<>(Map.<String, String>ofEntries(
            Map.entry("'я", "ьja"), Map.entry("'ю", "ьju"), Map.entry("'є", "ьje"), Map.entry("'ї", "ьji"),
            Map.entry("'Я", "ЬJA"), Map.entry("'Ю", "ЬJU"), Map.entry("'Є", "ЬJE"), Map.entry("'Ї", "ЬJI"),
            Map.entry("йо", "jo"),
            Map.entry("ьо", "jo"), Map.entry("ЬО", "JO"), Map.entry("Ьо", "Jo"), Map.entry("ьО", "jO"),
            Map.entry("а", "a"), Map.entry("А", "A"),
            Map.entry("б", "b"), Map.entry("Б", "B"),
            Map.entry("в", "v"), Map.entry("В", "V"),
            Map.entry("г", "g"), Map.entry("Г", "G"),
            Map.entry("ґ", "ĝ"), Map.entry("Ґ", "Ĝ"),
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
            Map.entry("х", "h"), Map.entry("Х", "H"),
            Map.entry("ц", "c"), Map.entry("Ц", "C"),
            Map.entry("ч", "č"), Map.entry("Ч", "Č"),
            Map.entry("ш", "š"), Map.entry("Ш", "Š"),
            Map.entry("щ", "šč"), Map.entry("Щ", "Šč"),
            Map.entry("ю", "ju"), Map.entry("Ю", "Ju"),
            Map.entry("я", "ja"), Map.entry("Я", "Ja"),
            Map.entry("ь", "'"), Map.entry("Ь", "'"),
            Map.entry("'", "'")

                // К -> Ллл лише на початку речення
                // Я -> ЛЛЛ лише якщо капс, тобто: (правилЯ, ЯРмарок, Я Тут). Тобто, цей випадок справедливий якщо перед великою кириличною літерою є літера (будь-яка); або після неї - велика (можна знищити усі пробіли)
                //Ja -> Я лише якщо після неї мала літера,


                // К : ліворуч літера || праворуч літера Велика -> ЛЛЛ...
                // К : праворуч літера мала -> Ллл...
                // к : -> ллл...
    ));
    private static Map<String, String> transliterationToCyrillicMap = reverseTransliterationDirection(transliterationToLatinMap); // Uses the function to reverse the toLatin Map and switch cyrillic and latin letters


    public static String transliterateInputText(String inputText, boolean isToLatin) { // Respective transliteration methods (toLatin and toCyrillic) from the FileSceneController and TextSceneController classes pass the boolean field isToLatin to specify the transliteration direction
        Map<String, String> transliterationDirection;
        if (isToLatin)
            transliterationDirection = transliterationToLatinMap;
        else {
            transliterationDirection = transliterationToCyrillicMap;
            transliterationDirection.put("JA", "Я");
            transliterationDirection.put("JU", "Ю");
            transliterationDirection.put("JE", "Є");
            transliterationDirection.put("JI", "Ї");
        }

        List<String> sortedKeys = transliterationDirection.keySet().stream() // The keySet() method retrieves the map's keys and sorts them so that longer Cyrillic combinations like "йо", "ьо", etc. come before shorter ones like "j".
                .sorted((key1, key2) -> {
                    // Пріоритет апострофа
                    if (key1.equals("'")) {
                        return -1; // Ставимо апостроф на початок
                    } else if (key2.equals("'")) {
                        return 1; // Ставимо інші комбінації після апострофа
                    }
                    // Обробка всіх інших комбінацій за довжиною
                    return Integer.compare(key2.length(), key1.length());
                }) // this prevents incorrect transliteration, such as translating "ju" as "йу" by matching "j" first. reversed() method is used to place letter in descending order (we need the combination of letter to come first)
                .toList(); // it returns a list with keys (cyrillic or latin) which starts with "йо", "ьо", etc. - for cyrillic; and "jo", "ju", "ji", etc. - for latin
        System.out.println(sortedKeys);
        for (String key : sortedKeys) {
            if (!key.equals("Я") && !key.equals("Ю") && !key.equals("Є")) // ля = la to make LAJAJAJAJAJAJA not LAJaJaJaJa
                inputText = inputText.replace(key, transliterationDirection.get(key)); //  Replaces occurrences of each key in inputText with its corresponding value from the map using get(key).
        }
        // sorting ensures that longer combinations like "ьо" replace first (e.g., "ьо" -> "jo", not 'о; "ju" -> "ю", not йу)
        if (isToLatin)
            inputText = checkOccurrenceOfLettersBeforeOrAfterJAJUJEJI(inputText); // ля = la to make LAJAJAJAJAJAJA not LAJaJaJaJa
        System.out.println(transliterationToCyrillicMap);
        return inputText;
    }

    // LJA ->
    public static void updateTransliterationRule(String cyrillicLetter, String latinEquivalent) {
        transliterationToLatinMap.put(cyrillicLetter, latinEquivalent);
        transliterationToCyrillicMap = reverseTransliterationDirection(transliterationToLatinMap);
    }

    public static void removeTransliterationRule(String ruleNeedsToBeDeleted) {
        transliterationToLatinMap.remove(ruleNeedsToBeDeleted);
    }


    private static Map<String, String> reverseTransliterationDirection(Map<String, String> transliterationMap) {
        Map<String, String> reversedMap = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : transliterationMap.entrySet()) {// Takes each key-value pair (entry) from the transliterationToLatinMap using entrySet(), which provides a set of all pairs in the map
            reversedMap.put(entry.getValue(), entry.getKey()); // Gets a value from the entry (key-value pair) and places it as a key; Gets a key and places in as a value; and puts it in the reverse map
        }

            return reversedMap;
    }






    private static String checkOccurrenceOfLettersBeforeOrAfterJAJUJEJI(String inputText) {
        for (int i = 0; i < inputText.length(); i++) {
            char currentChar = inputText.charAt(i);

            if (currentChar == 'Я' || currentChar == 'Ю' || currentChar == 'Є' || currentChar == 'Ї') {
                boolean isBefore = i > 0 && Character.isLetter(inputText.charAt(i - 1));
                boolean isAfter = i < inputText.length() - 1 && Character.isUpperCase(inputText.charAt(i + 1));

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
                } else {
                    if (currentChar == 'Я') {
                        inputText = inputText.substring(0, i) + "Ja" + inputText.substring(i + 1);
                    } else if (currentChar == 'Ю') {
                        inputText = inputText.substring(0, i) + "Ju" + inputText.substring(i + 1);
                    } else if (currentChar == 'Є') {
                        inputText = inputText.substring(0, i) + "Je" + inputText.substring(i + 1);
                    } else if (currentChar == 'Ї') {
                        inputText = inputText.substring(0, i) + "Ji" + inputText.substring(i + 1);
                        }
                }
            }
        }
        return inputText;
    }

    public static Map<String, String> getTransliterationToLatinRulesMap() {
        return transliterationToLatinMap;
    }

    public static Map<String, String> getTransliterationToCyrillicRulesMap() {
        return transliterationToCyrillicMap;
    }


}
