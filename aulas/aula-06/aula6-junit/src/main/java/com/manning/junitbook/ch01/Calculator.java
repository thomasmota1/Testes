package com.manning.junitbook.ch01;

public class Calculator {
    public double add(double number1, double number2) {
        return number1 + number2;
    }

    public double subtract(double number1, double number2) {
        return number1 - number2;
    }

    public double multiply(double number1, double number2) {
        return number1 * number2;
    }

    public double divide(double number1, double number2) {
        if (number2 == 0.0) {
            throw new ArithmeticException("Divisão por zero não permitida");
        }
        return number1 / number2;
    }

    public double squareRoot(double number) {
        if (number < 0.0) {
            throw new IllegalArgumentException(
                    "Não é possível calcular raiz quadrada de número negativo");
        }
        return Math.sqrt(number);
    }

    public double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }
}
