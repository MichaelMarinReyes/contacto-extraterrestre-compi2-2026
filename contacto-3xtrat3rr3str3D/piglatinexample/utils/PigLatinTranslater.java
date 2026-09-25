/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package igriega.piglatin.utils;

/**
 *
 * @author blue-dragon
 */
public class PigLatinTranslater {
    
    private static String VOWELS = "aeiouAEIOU";
    public static String convert(String text) {

        if (text == null || text.isEmpty()) {
            return text;
        }

        // Empieza con vocal
        if (VOWELS.indexOf(text.charAt(0)) >= 0) {
            return text + "way";
        }

        // Empieza con consonante(s)
        int firstVowel = 0;

        while (firstVowel < text.length()
                && VOWELS.indexOf(text.charAt(firstVowel)) < 0) {
            firstVowel++;
        }

        // No contiene vocales
        if (firstVowel == text.length()) {
            return text + "ay";
        }

        return text.substring(firstVowel)
                + text.substring(0, firstVowel)
                + "ay";
    }
}
