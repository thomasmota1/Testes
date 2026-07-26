package com.tms.aula9.units;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UnitConverterTest {
    private UnitConverter converter;

    @BeforeEach
    void setUp() {
        converter = new UnitConverter();
    }

    @Test
    void shouldConvertLength() {
        assertEquals(1.0, converter.convert(1000, "m", "km"), 0.001);
        assertEquals(1.60934, converter.convert(1, "mi", "km"), 0.001);
        assertEquals(2.54, converter.convert(1, "in", "cm"), 0.001);
    }

    @Test
    void shouldConvertWeight() {
        assertEquals(2.20462, converter.convert(1, "kg", "lb"), 0.001);
        assertEquals(16.0, converter.convert(1, "lb", "oz"), 0.001);
    }

    @Test
    void shouldConvertTemperature() {
        assertEquals(32.0, converter.convert(0, "C", "F"), 0.001);
        assertEquals(273.15, converter.convert(0, "C", "K"), 0.001);
        assertEquals(0.0, converter.convert(32, "F", "C"), 0.001);
    }

    @Test
    @DisplayName("Rejects incompatible, missing, and non-finite inputs")
    void shouldRejectIncompatibleUnits() {
        assertThrows(IllegalArgumentException.class, () -> converter.convert(1, "kg", "m"));
        assertThrows(IllegalArgumentException.class, () -> converter.convert(1, null, "m"));
        assertThrows(IllegalArgumentException.class, () -> converter.convert(1, "m", " "));
        assertThrows(IllegalArgumentException.class, () -> converter.convert(Double.NaN, "m", "km"));
    }
}
