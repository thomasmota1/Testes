package com.tms.aula9.currency;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CurrencyConverterTest {
    @Test
    void shouldConvertUsingExchangeRateMock() {
        ExchangeRateService service = mock(ExchangeRateService.class);
        when(service.rate(Currency.USD, Currency.BRL)).thenReturn(5.0);
        CurrencyConverter converter = new CurrencyConverter(service);

        assertEquals(50.0, converter.convert(10, "USD", "BRL"), 0.001);
        verify(service).rate(Currency.USD, Currency.BRL);
    }

    @Test
    void shouldAcceptFriendlyCurrencyNames() {
        ExchangeRateService service = mock(ExchangeRateService.class);
        when(service.rate(Currency.AUD, Currency.CLP)).thenReturn(610.0);
        CurrencyConverter converter = new CurrencyConverter(service);

        assertEquals(1220.0, converter.convert(2, "Dolar Australiano", "Peso Chileno"), 0.001);
    }

    @Test
    void shouldReturnSameAmountForSameCurrencyAndRejectInvalidInput() {
        CurrencyConverter converter = new CurrencyConverter(mock(ExchangeRateService.class));

        assertEquals(100, converter.convert(100, "EUR", "Euro"), 0.001);
        assertThrows(IllegalArgumentException.class, () -> converter.convert(-1, "USD", "BRL"));
        assertThrows(IllegalArgumentException.class, () -> converter.convert(1, "XYZ", "BRL"));
    }
}
