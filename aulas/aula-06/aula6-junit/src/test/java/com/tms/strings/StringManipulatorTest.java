package com.tms.strings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringManipulatorTest {
    private StringManipulator manipulator;

    @BeforeEach
    void setUp() {
        manipulator = new StringManipulator();
    }

    @Test
    void testReverse() {
        assertEquals("alO", manipulator.reverse("Ola"));
        assertEquals("", manipulator.reverse(""));
        assertEquals("!@#$%", manipulator.reverse("%$#@!"));
        String longString = "a".repeat(10_000);
        assertEquals(longString, manipulator.reverse(longString));
    }

    @Test
    void testCountOccurrences() {
        assertEquals(3, manipulator.countOccurrences("banana", 'a'));
        assertEquals(0, manipulator.countOccurrences("", 'a'));
        assertEquals(3, manipulator.countOccurrences("a@b@c@", '@'));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ama",
            "arara",
            "A man a plan a canal Panama",
            "Socorram-me, subi no ônibus em Marrocos"
    })
    void testPalindromes(String value) {
        assertTrue(manipulator.isPalindrome(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {"hello", "world", "java"})
    void testNonPalindromes(String value) {
        assertFalse(manipulator.isPalindrome(value));
    }

    @Test
    void testCaseConversion() {
        assertEquals("AÇÃO", manipulator.toUpperCase("ação"));
        assertEquals("ação", manipulator.toLowerCase("AÇÃO"));
        assertEquals("", manipulator.toUpperCase(""));
    }

    @Test
    void testNullStrings() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> manipulator.reverse(null)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> manipulator.countOccurrences(null, 'a')),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> manipulator.isPalindrome(null)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> manipulator.toUpperCase(null)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> manipulator.toLowerCase(null))
        );
    }
}
