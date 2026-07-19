package com.tms.aula9.currency;

import java.util.Locale;

public enum Currency {
    USD("Dolar Americano"),
    EUR("Euro"),
    BRL("Real Brasileiro"),
    GBP("Libra Esterlina"),
    JPY("Iene Japones"),
    AUD("Dolar Australiano"),
    CAD("Dolar Canadense"),
    CHF("Franco Suico"),
    ARS("Peso Argentino"),
    CLP("Peso Chileno");

    private final String displayName;

    Currency(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

    public static Currency fromUserInput(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Currency must not be blank");
        }
        String normalized = normalize(input);
        for (Currency currency : values()) {
            if (currency.name().equalsIgnoreCase(input) || normalize(currency.displayName).equals(normalized)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Unknown currency: " + input);
    }

    private static String normalize(String value) {
        return value.toLowerCase(Locale.ROOT).replace("ó", "o").replace("í", "i").trim();
    }
}
