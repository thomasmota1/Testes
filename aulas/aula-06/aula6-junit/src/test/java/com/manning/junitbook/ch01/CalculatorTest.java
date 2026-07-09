package com.manning.junitbook.ch01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void testBasicOperations() {
        assertEquals(60.0, calculator.add(10, 50), 0.0);
        assertEquals(40.0, calculator.subtract(50, 10), 0.0);
        assertEquals(500.0, calculator.multiply(10, 50), 0.0);
        assertEquals(5.0, calculator.divide(50, 10), 0.0);
    }

    @Test
    void testNegativeAndDecimalValues() {
        assertEquals(-15.0, calculator.add(-10, -5), 0.0);
        assertEquals(50.0, calculator.multiply(-10, -5), 0.0);
        assertEquals(3.333, calculator.divide(10, 3), 0.001);
    }

    @Test
    void testDivideByZero() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(10, 0));
    }

    @Test
    void testSquareRoot() {
        assertEquals(3.0, calculator.squareRoot(9), 0.0);
        assertEquals(1.414, calculator.squareRoot(2), 0.001);
        assertEquals(0.0, calculator.squareRoot(0), 0.0);
    }

    @Test
    void testSquareRootNegative() {
        assertThrows(IllegalArgumentException.class, () -> calculator.squareRoot(-4));
    }

    @Test
    void testSquareRootLimits() {
        assertEquals(Math.sqrt(Double.MAX_VALUE),
                calculator.squareRoot(Double.MAX_VALUE), 0.0);
        assertEquals(Math.sqrt(Double.MIN_VALUE),
                calculator.squareRoot(Double.MIN_VALUE), 0.0);
    }

    @ParameterizedTest
    @CsvSource({
            "2, 0, 1",
            "2, 3, 8",
            "2, -3, 0.125",
            "-2, 3, -8",
            "-2, 4, 16",
            "1.5, 2, 2.25"
    })
    @DisplayName("Potenciação com inteiros, decimais e negativos")
    void testPower(double base, double exponent, double expected) {
        assertEquals(expected, calculator.power(base, exponent), 0.001);
    }
}
