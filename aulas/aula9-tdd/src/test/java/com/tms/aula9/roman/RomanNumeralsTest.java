package com.tms.aula9.roman;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RomanNumeralsTest {
    private RomanNumerals converter;

    @BeforeEach
    void setUp() {
        converter = new RomanNumerals();
    }

    @ParameterizedTest
    @CsvSource({"1, I", "4, IV", "9, IX", "40, XL", "90, XC", "159, CLIX", "2024, MMXXIV"})
    void shouldConvertArabicToRoman(int arabic, String roman) {
        assertEquals(roman, converter.toRoman(arabic));
    }

    @ParameterizedTest
    @CsvSource({"I, 1", "IV, 4", "CLIX, 159", "MMXXIV, 2024"})
    void shouldConvertRomanToArabic(String roman, int arabic) {
        assertEquals(arabic, converter.toArabic(roman));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -5, 4000})
    void shouldRejectInvalidArabicNumbers(int number) {
        assertThrows(IllegalArgumentException.class, () -> converter.toRoman(number));
    }

    @ParameterizedTest
    @ValueSource(strings = {"XLAC", "IIII", "VV"})
    void shouldRejectInvalidRomanNumbers(String roman) {
        assertThrows(IllegalArgumentException.class, () -> converter.toArabic(roman));
    }
}
