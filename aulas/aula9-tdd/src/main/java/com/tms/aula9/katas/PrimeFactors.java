package com.tms.aula9.katas;

import java.util.ArrayList;
import java.util.List;

public class PrimeFactors {
    public List<Integer> generate(int number) {
        if (number < 2) {
            return List.of();
        }
        List<Integer> factors = new ArrayList<>();
        int remaining = number;
        for (int divisor = 2; divisor <= remaining; divisor++) {
            while (remaining % divisor == 0) {
                factors.add(divisor);
                remaining /= divisor;
            }
        }
        return factors;
    }
}
