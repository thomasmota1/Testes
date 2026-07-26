package com.tms.aula9.roman;

import java.util.LinkedHashMap;
import java.util.Map;

public class RomanNumerals {
    private static final LinkedHashMap<Integer, String> SYMBOLS = new LinkedHashMap<>();

    static {
        SYMBOLS.put(1000, "M");
        SYMBOLS.put(900, "CM");
        SYMBOLS.put(500, "D");
        SYMBOLS.put(400, "CD");
        SYMBOLS.put(100, "C");
        SYMBOLS.put(90, "XC");
        SYMBOLS.put(50, "L");
        SYMBOLS.put(40, "XL");
        SYMBOLS.put(10, "X");
        SYMBOLS.put(9, "IX");
        SYMBOLS.put(5, "V");
        SYMBOLS.put(4, "IV");
        SYMBOLS.put(1, "I");
    }

    public String toRoman(int number) {
        if (number < 1 || number > 3999) {
            throw new IllegalArgumentException("Number must be between 1 and 3999");
        }
        StringBuilder roman = new StringBuilder();
        int remaining = number;
        for (Map.Entry<Integer, String> entry : SYMBOLS.entrySet()) {
            while (remaining >= entry.getKey()) {
                roman.append(entry.getValue());
                remaining -= entry.getKey();
            }
        }
        return roman.toString();
    }

    public int toArabic(String roman) {
        if (roman == null || roman.isBlank()) {
            throw new IllegalArgumentException("Roman must not be blank");
        }
        String normalized = roman.trim().toUpperCase();
        int result = 0;
        int index = 0;
        for (Map.Entry<Integer, String> entry : SYMBOLS.entrySet()) {
            while (normalized.startsWith(entry.getValue(), index)) {
                result += entry.getKey();
                index += entry.getValue().length();
            }
        }
        if (index != normalized.length() || !toRoman(result).equals(normalized)) {
            throw new IllegalArgumentException("Invalid Roman numeral");
        }
        return result;
    }
}
