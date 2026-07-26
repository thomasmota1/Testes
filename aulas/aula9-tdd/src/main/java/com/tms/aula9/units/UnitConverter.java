package com.tms.aula9.units;

import java.util.Map;

public class UnitConverter {
    private static final Map<String, Double> LENGTH_TO_METER = Map.of(
            "m", 1.0, "km", 1000.0, "cm", 0.01, "mi", 1609.34, "in", 0.0254);
    private static final Map<String, Double> WEIGHT_TO_GRAM = Map.of(
            "g", 1.0, "kg", 1000.0, "lb", 453.59237, "oz", 28.349523125);

    public double convert(double value, String source, String target) {
        if (!Double.isFinite(value) || source == null || target == null
                || source.isBlank() || target.isBlank()) {
            throw new IllegalArgumentException("Value and units are required");
        }
        if (LENGTH_TO_METER.containsKey(source) && LENGTH_TO_METER.containsKey(target)) {
            return value * LENGTH_TO_METER.get(source) / LENGTH_TO_METER.get(target);
        }
        if (WEIGHT_TO_GRAM.containsKey(source) && WEIGHT_TO_GRAM.containsKey(target)) {
            return value * WEIGHT_TO_GRAM.get(source) / WEIGHT_TO_GRAM.get(target);
        }
        if (isTemperature(source) && isTemperature(target)) {
            return fromCelsius(toCelsius(value, source), target);
        }
        throw new IllegalArgumentException("Incompatible or unsupported units");
    }

    private static boolean isTemperature(String unit) {
        return "C".equals(unit) || "F".equals(unit) || "K".equals(unit);
    }

    private static double toCelsius(double value, String unit) {
        return switch (unit) {
            case "C" -> value;
            case "F" -> (value - 32) * 5 / 9;
            case "K" -> value - 273.15;
            default -> throw new IllegalArgumentException("Unsupported temperature");
        };
    }

    private static double fromCelsius(double value, String unit) {
        return switch (unit) {
            case "C" -> value;
            case "F" -> value * 9 / 5 + 32;
            case "K" -> value + 273.15;
            default -> throw new IllegalArgumentException("Unsupported temperature");
        };
    }
}
