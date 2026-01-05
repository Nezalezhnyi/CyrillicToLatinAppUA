package com.darion.app.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.entry;

public class TransliterationEngine {

    private static Map<String, String> DEFAULT_CYRILLIC_TO_LATIN = Map.<String, String>ofEntries(
            entry("а", "a"),
            entry("б", "b"),
            entry("в", "v"),
            entry("г", "h"),
            entry("ґ", "g"),
            entry("д", "d"),
            entry("е", "e"),
            entry("є", "je"),
            entry("ж", "ž"),
            entry("з", "z"),
            entry("и", "y"),
            entry("і", "i"),
            entry("ї", "ji"),
            entry("й", "j"),
            entry("к", "k"),
            entry("л", "l"),
            entry("м", "m"),
            entry("н", "n"),
            entry("о", "o"),
            entry("п", "p"),
            entry("р", "r"),
            entry("с", "s"),
            entry("т", "t"),
            entry("у", "u"),
            entry("ф", "f"),
            entry("х", "x"),
            entry("ц", "c"),
            entry("ч", "č"),
            entry("ш", "š"),
            entry("щ", "šč"),
            entry("ь", "'"),
            entry("ю", "ju"),
            entry("я", "ja"),
            entry("йо", "jo"),
            entry("ьо", "jo"),

            // --- Uppercase ---
            entry("А", "A"),
            entry("Б", "B"),
            entry("В", "V"),
            entry("Г", "H"),
            entry("Ґ", "G"),
            entry("Д", "D"),
            entry("Е", "E"),
            entry("Є", "Je"),
            entry("Ж", "Ž"),
            entry("З", "Z"),
            entry("И", "Y"),
            entry("І", "I"),
            entry("Ї", "Ji"),
            entry("Й", "J"),
            entry("К", "K"),
            entry("Л", "L"),
            entry("М", "M"),
            entry("Н", "N"),
            entry("О", "O"),
            entry("П", "P"),
            entry("Р", "R"),
            entry("С", "S"),
            entry("Т", "T"),
            entry("У", "U"),
            entry("Ф", "F"),
            entry("Х", "X"),
            entry("Ц", "C"),
            entry("Ч", "Č"),
            entry("Ш", "Š"),
            entry("Щ", "Šč"),
            entry("Ь", "'"),
            entry("Ю", "Ju"),
            entry("Я", "Ja"),
            entry("Йо", "Jo"),
            entry("ЬО", "JO")
    );

    private static Map<String, String> DEFAULT_LATIN_TO_CYRILLIC = new HashMap<>();

    private static Map<String, String> CYRILLIC_TO_LATIN = new HashMap<>(DEFAULT_CYRILLIC_TO_LATIN);;
    private static Map<String, String> LATIN_TO_CYRILLIC;

    static {
        createLatinToCyrillic(DEFAULT_CYRILLIC_TO_LATIN, DEFAULT_LATIN_TO_CYRILLIC);
        LATIN_TO_CYRILLIC = new HashMap<>(DEFAULT_LATIN_TO_CYRILLIC);
    }


    public static String transliterate(String text, boolean isCyrillicToLatin) {
        Map<String, String> rules = (isCyrillicToLatin) ? CYRILLIC_TO_LATIN : LATIN_TO_CYRILLIC;

        List<String> sortedKeys = rules.keySet().stream()
                .sorted(Comparator.comparingInt(String::length).reversed()).toList(); //from longest (йо, ьо...) to shortest

        for (String key : sortedKeys)
            text = text.replace(key, rules.get(key));

        return text;

    }

    public static void updateEngine(Map<String, String> newEngine) {
        CYRILLIC_TO_LATIN = newEngine;
        LATIN_TO_CYRILLIC = new HashMap<>();
        createLatinToCyrillic(CYRILLIC_TO_LATIN, LATIN_TO_CYRILLIC);
    }

    public static void restoreDefaults() {
        CYRILLIC_TO_LATIN = new HashMap<>(DEFAULT_CYRILLIC_TO_LATIN);
        LATIN_TO_CYRILLIC = new HashMap<>(DEFAULT_LATIN_TO_CYRILLIC);
    }

    public static Map<String, String> getDefaultRules() {
        return DEFAULT_CYRILLIC_TO_LATIN;
    }

    public static Map<String, String> getCurrentRules() {
        return CYRILLIC_TO_LATIN;
    }

    private static void createLatinToCyrillic(Map<String, String> cyrillicMap, Map<String, String> latinMap) {
        for (Map.Entry<String, String> entry : cyrillicMap.entrySet())
            latinMap.put(entry.getValue(), entry.getKey());
    }

}
