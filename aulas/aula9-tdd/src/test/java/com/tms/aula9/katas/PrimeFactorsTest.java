package com.tms.aula9.katas;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimeFactorsTest {
    @Test
    void shouldGeneratePrimeFactors() {
        PrimeFactors factors = new PrimeFactors();

        assertEquals(List.of(), factors.generate(1));
        assertEquals(List.of(2), factors.generate(2));
        assertEquals(List.of(2, 2, 3), factors.generate(12));
        assertEquals(List.of(3, 5, 7), factors.generate(105));
    }
}
