package com.tms.aula9.currency;

public class CurrencyConverter {
    private final ExchangeRateService exchangeRateService;

    public CurrencyConverter(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    public double convert(double amount, String source, String target) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }
        Currency sourceCurrency = Currency.fromUserInput(source);
        Currency targetCurrency = Currency.fromUserInput(target);
        if (sourceCurrency == targetCurrency) {
            return amount;
        }
        return amount * exchangeRateService.rate(sourceCurrency, targetCurrency);
    }
}
