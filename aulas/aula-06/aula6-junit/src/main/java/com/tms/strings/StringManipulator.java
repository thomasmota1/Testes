package com.tms.strings;

import java.text.Normalizer;
import java.util.Locale;

public class StringManipulator {
    public String reverse(String str) {
        requireNonNull(str);
        return new StringBuilder(str).reverse().toString();
    }

    public int countOccurrences(String str, char ch) {
        requireNonNull(str);
        int count = 0;
        for (char current : str.toCharArray()) {
            if (current == ch) {
                count++;
            }
        }
        return count;
    }

    public boolean isPalindrome(String str) {
        requireNonNull(str);
        String cleaned = Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^\\p{L}\\p{N}]", "")
                .toLowerCase(Locale.ROOT);
        return cleaned.contentEquals(new StringBuilder(cleaned).reverse());
    }

    public String toUpperCase(String str) {
        requireNonNull(str);
        return str.toUpperCase(Locale.ROOT);
    }

    public String toLowerCase(String str) {
        requireNonNull(str);
        return str.toLowerCase(Locale.ROOT);
    }

    private void requireNonNull(String str) {
        if (str == null) {
            throw new IllegalArgumentException("String não pode ser nula");
        }
    }
}
