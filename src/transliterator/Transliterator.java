package transliterator;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class Transliterator {
    private static final Map<String, String> transliteration = Map.<String, String>ofEntries(
            Map.entry("йо", "jo"), Map.entry("ЙО", "JO"), Map.entry("Йо", "Jo"), Map.entry("йО", "jO"),
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
            Map.entry("ь", "'"), Map.entry("Ь", "'")
    );

    public static String transliterateInputText(String inputText) {
        return Arrays.stream(inputText.split("")) // Splits an inputText into separate letters and symbols
                .map(letter -> transliteration.getOrDefault(letter, letter)) // Maps (changes) an input letter using the corresponding latin letter in the transliteration Map; passes a letter (including symbols and spaces) if it doesn't appear in the Map
                .collect(Collectors.joining()); // Joins transliterated letters (and unchangeable symbols with spaces) into one string and returns it
    }

}
