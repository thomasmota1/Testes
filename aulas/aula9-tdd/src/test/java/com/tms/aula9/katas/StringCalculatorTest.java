package com.tms.aula9.katas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringCalculatorTest {
    @Test
    void shouldAddNumbersFromString() {
        StringCalculator calculator = new StringCalculator();

        assertEquals(0, calculator.add(""));
        assertEquals(3, calculator.add("1,2"));
        assertEquals(6, calculator.add("1\n2,3"));
        assertEquals(3, calculator.add("//;\n1;2"));
        assertEquals(2, calculator.add("2,1001"));
    }

    @Test
    void shouldRejectNegativeNumbers() {
        assertThrows(IllegalArgumentException.class, () -> new StringCalculator().add("1,-2"));
    }
}
