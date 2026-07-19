package com.tms.aula9.currency;

public interface ExchangeRateService {
    double rate(Currency source, Currency target);
}
