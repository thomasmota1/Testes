package com.tms.aula9.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @ParameterizedTest
    @CsvSource({"5, 2, 7", "-5, 2, -3", "1.5, 2.25, 3.75", "1000000, 0, 1000000"})
    void shouldAddPositiveNegativeExtremeAndDecimalValues(double a, double b, double expected) {
        assertEquals(expected, calculator.add(a, b), 0.001);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1000001, 1000001})
    void shouldRejectValuesOutsideValidRange(double value) {
        assertThrows(IllegalArgumentException.class, () -> calculator.add(value, 1));
    }

    @RepeatedTest(3)
    void shouldKeepSameResultAcrossRepeatedExecutions() {
        assertEquals(12, calculator.add(7, 5), 0.001);
    }

    @Test
    void shouldDivideAndRejectDivisionByZero() {
        assertThat(calculator.divide(10, 4), is(closeTo(2.5, 0.001)));
        assertThrows(ArithmeticException.class, () -> calculator.divide(10, 0));
    }

    @Test
    void shouldCalculateSquareRootAndRejectNegativeNumbers() {
        assertThat(calculator.squareRoot(2), is(closeTo(1.414, 0.001)));
        assertThrows(IllegalArgumentException.class, () -> calculator.squareRoot(-1));
    }
}
