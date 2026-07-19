package com.tms.aula9.calculator;

public class Calculator {
    private static final double MIN = -1_000_000;
    private static final double MAX = 1_000_000;

    public double add(double a, double b) {
        validateRange(a);
        validateRange(b);
        double result = a + b;
        validateRange(result);
        return result;
    }

    public double divide(double dividend, double divisor) {
        validateRange(dividend);
        validateRange(divisor);
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return dividend / divisor;
    }

    public double squareRoot(double value) {
        validateRange(value);
        if (value < 0) {
            throw new IllegalArgumentException("Square root requires non-negative value");
        }
        return Math.sqrt(value);
    }

    private static void validateRange(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value) || value < MIN || value > MAX) {
            throw new IllegalArgumentException("Value outside valid range");
        }
    }
}
