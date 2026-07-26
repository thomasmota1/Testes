package com.tms.aula9.katas;

import java.util.Arrays;

public class StringCalculator {
    public int add(String input) {
        if (input == null || input.isBlank()) {
            return 0;
        }
        String delimiter = ",|\\n";
        String numbers = input;
        if (input.startsWith("//")) {
            int delimiterEnd = input.indexOf('\n');
            delimiter = java.util.regex.Pattern.quote(input.substring(2, delimiterEnd));
            numbers = input.substring(delimiterEnd + 1);
        }
        int[] values = Arrays.stream(numbers.split(delimiter)).mapToInt(Integer::parseInt).toArray();
        int[] negatives = Arrays.stream(values).filter(value -> value < 0).toArray();
        if (negatives.length > 0) {
            throw new IllegalArgumentException("Negatives not allowed: " + Arrays.toString(negatives));
        }
        return Arrays.stream(values).filter(value -> value <= 1000).sum();
    }
}
