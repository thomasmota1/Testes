# Aula 9 - Estrategias e Boas Praticas para Testes - TDD

**Aluno:** Thomas Gabriel Mota de Araujo  
**Data:** 18/07/2026 - 

---


## Estrutura da Entrega

| Item | Caminho |
|---|---|
| README da aula | [`README.md`](README.md) |
| Configuracao Maven | [`pom.xml`](pom.xml) |
| Codigo principal | [`src/main/java`](src/main/java) |
| Testes automatizados | [`src/test/java`](src/test/java) |

Para executar:

```bash
mvn test
```

---

## Atividade 1: Revisitando a Calculadora

Foram implementadas soma, divisao e raiz quadrada com valores positivos, negativos, decimais, extremos e excecoes.

**Arquivo:** [`src/main/java/com/tms/aula9/calculator/Calculator.java`](src/main/java/com/tms/aula9/calculator/Calculator.java)

Esta classe implementa as operacoes revisitadas com TDD: soma, divisao e raiz quadrada.

```java
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
```

**Arquivo:** [`src/test/java/com/tms/aula9/calculator/CalculatorTest.java`](src/test/java/com/tms/aula9/calculator/CalculatorTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula9.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @ParameterizedTest
    @CsvSource({"5, 2, 7", "-5, 2, -3", "1.5, 2.25, 3.75", "1000000, 0, 1000000"})
    void shouldAddPositiveNegativeExtremeAndDecimalValues(double a, double b, double expected) {
        assertEquals(expected, calculator.add(a, b), 0.001);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1000001, 1000001})
    void shouldRejectValuesOutsideValidRange(double value) {
        assertThrows(IllegalArgumentException.class, () -> calculator.add(value, 1));
    }

    @RepeatedTest(3)
    void shouldKeepSameResultAcrossRepeatedExecutions() {
        assertEquals(12, calculator.add(7, 5), 0.001);
    }

    @Test
    void shouldDivideAndRejectDivisionByZero() {
        assertThat(calculator.divide(10, 4), is(closeTo(2.5, 0.001)));
        assertThrows(ArithmeticException.class, () -> calculator.divide(10, 0));
    }

    @Test
    void shouldCalculateSquareRootAndRejectNegativeNumbers() {
        assertThat(calculator.squareRoot(2), is(closeTo(1.414, 0.001)));
        assertThrows(IllegalArgumentException.class, () -> calculator.squareRoot(-1));
    }
}
```

---

## Atividade 2: Gerenciador de Voos com TDD

Foram implementadas regras de voo Economy e Premium, passageiro duplicado, limite de passageiros e um gerenciador de reservas.

**Arquivo:** [`src/main/java/com/tms/aula9/flight/Passenger.java`](src/main/java/com/tms/aula9/flight/Passenger.java)

Esta classe representa o passageiro e define sua identidade e se ele e VIP.

```java
package com.tms.aula9.flight;

import java.util.Objects;

public class Passenger {
    private final String name;
    private final boolean vip;

    public Passenger(String name, boolean vip) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Passenger name required");
        }
        this.name = name;
        this.vip = vip;
    }

    public boolean isVip() {
        return vip;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Passenger passenger)) {
            return false;
        }
        return Objects.equals(name, passenger.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
```

**Arquivo:** [`src/main/java/com/tms/aula9/flight/Flight.java`](src/main/java/com/tms/aula9/flight/Flight.java)

Esta classe abstrata concentra regras comuns dos voos, como limite e controle de passageiros.

```java
package com.tms.aula9.flight;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class Flight {
    private final String id;
    private final int maxPassengers;
    private final Set<Passenger> passengers = new HashSet<>();

    protected Flight(String id, int maxPassengers) {
        if (id == null || id.isBlank() || maxPassengers <= 0) {
            throw new IllegalArgumentException("Invalid flight");
        }
        this.id = id;
        this.maxPassengers = maxPassengers;
    }

    public String getId() {
        return id;
    }

    public int getMaxPassengers() {
        return maxPassengers;
    }

    public int getPassengersCount() {
        return passengers.size();
    }

    public Set<Passenger> getPassengers() {
        return Collections.unmodifiableSet(passengers);
    }

    protected boolean addPassengerInternal(Passenger passenger) {
        if (passenger == null || passengers.size() >= maxPassengers || passengers.contains(passenger)) {
            return false;
        }
        return passengers.add(passenger);
    }

    protected boolean removePassengerInternal(Passenger passenger) {
        return passengers.remove(passenger);
    }

    public abstract boolean addPassenger(Passenger passenger);

    public abstract boolean removePassenger(Passenger passenger);
}
```

**Arquivo:** [`src/main/java/com/tms/aula9/flight/EconomyFlight.java`](src/main/java/com/tms/aula9/flight/EconomyFlight.java)

Esta classe implementa as regras de um voo Economy, aceitando passageiros regulares e VIP.

```java
package com.tms.aula9.flight;

public class EconomyFlight extends Flight {
    public EconomyFlight(String id, int maxPassengers) {
        super(id, maxPassengers);
    }

    @Override
    public boolean addPassenger(Passenger passenger) {
        return addPassengerInternal(passenger);
    }

    @Override
    public boolean removePassenger(Passenger passenger) {
        return removePassengerInternal(passenger);
    }
}
```

**Arquivo:** [`src/main/java/com/tms/aula9/flight/PremiumFlight.java`](src/main/java/com/tms/aula9/flight/PremiumFlight.java)

Esta classe implementa as regras de um voo Premium, aceitando apenas VIP e impedindo remocao.

```java
package com.tms.aula9.flight;

public class PremiumFlight extends Flight {
    public PremiumFlight(String id, int maxPassengers) {
        super(id, maxPassengers);
    }

    @Override
    public boolean addPassenger(Passenger passenger) {
        return passenger != null && passenger.isVip() && addPassengerInternal(passenger);
    }

    @Override
    public boolean removePassenger(Passenger passenger) {
        return false;
    }
}
```

**Arquivo:** [`src/main/java/com/tms/aula9/flight/FlightManager.java`](src/main/java/com/tms/aula9/flight/FlightManager.java)

Esta classe coordena cadastro de voos, reservas, cancelamentos e assentos disponiveis.

```java
package com.tms.aula9.flight;

import java.util.HashMap;
import java.util.Map;

public class FlightManager {
    private final Map<String, Flight> flights = new HashMap<>();

    public void registerFlight(Flight flight) {
        flights.put(flight.getId(), flight);
    }

    public boolean bookPassenger(String flightId, Passenger passenger) {
        return findFlight(flightId).addPassenger(passenger);
    }

    public boolean cancelBooking(String flightId, Passenger passenger) {
        return findFlight(flightId).removePassenger(passenger);
    }

    public int getAvailableSeats(String flightId) {
        Flight flight = findFlight(flightId);
        return flight.getMaxPassengers() - flight.getPassengersCount();
    }

    private Flight findFlight(String flightId) {
        Flight flight = flights.get(flightId);
        if (flight == null) {
            throw new IllegalArgumentException("Flight not found: " + flightId);
        }
        return flight;
    }
}
```

**Arquivo:** [`src/test/java/com/tms/aula9/flight/FlightTest.java`](src/test/java/com/tms/aula9/flight/FlightTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula9.flight;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlightTest {
    @Nested
    class EconomyFlightTest {
        @Test
        void shouldAddAndRemoveRegularAndVipPassengers() {
            Flight flight = new EconomyFlight("EC001", 3);
            Passenger regular = new Passenger("John", false);
            Passenger vip = new Passenger("Jane", true);

            assertTrue(flight.addPassenger(regular));
            assertTrue(flight.addPassenger(vip));
            assertTrue(flight.removePassenger(regular));
            assertTrue(flight.removePassenger(vip));
        }

        @Test
        void shouldRejectDuplicateAndPassengerAboveLimit() {
            Flight flight = new EconomyFlight("EC001", 1);
            Passenger passenger = new Passenger("John", false);

            assertTrue(flight.addPassenger(passenger));
            assertFalse(flight.addPassenger(passenger));
            assertFalse(flight.addPassenger(new Passenger("Jane", false)));
            assertEquals(1, flight.getPassengersCount());
        }
    }

    @Nested
    class PremiumFlightTest {
        @Test
        void shouldAcceptOnlyVipAndNeverRemoveFromPremium() {
            Flight flight = new PremiumFlight("PR001", 2);
            Passenger regular = new Passenger("John", false);
            Passenger vip = new Passenger("Jane", true);

            assertFalse(flight.addPassenger(regular));
            assertTrue(flight.addPassenger(vip));
            assertFalse(flight.removePassenger(vip));
            assertEquals(1, flight.getPassengersCount());
        }
    }

    @ParameterizedTest
    @CsvSource({"EC001, false, true", "EC001, true, true", "PR001, false, false", "PR001, true, true"})
    void shouldApplyPassengerAdditionRules(String flightId, boolean vip, boolean expected) {
        Flight flight = flightId.startsWith("EC") ? new EconomyFlight(flightId, 10) : new PremiumFlight(flightId, 10);

        assertEquals(expected, flight.addPassenger(new Passenger("Test", vip)));
    }
}
```

**Arquivo:** [`src/test/java/com/tms/aula9/flight/FlightManagerTest.java`](src/test/java/com/tms/aula9/flight/FlightManagerTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula9.flight;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FlightManagerTest {
    private FlightManager manager;

    @BeforeEach
    void setUp() {
        manager = new FlightManager();
        manager.registerFlight(new EconomyFlight("EC001", 100));
        manager.registerFlight(new PremiumFlight("PR001", 20));
    }

    @Test
    void shouldBookPassengerAndShowAvailableSeats() {
        assertTrue(manager.bookPassenger("EC001", new Passenger("John", false)));
        assertEquals(99, manager.getAvailableSeats("EC001"));
    }

    @Test
    void shouldRejectUnknownFlight() {
        assertThrows(IllegalArgumentException.class, () -> manager.bookPassenger("INVALID", new Passenger("John", false)));
    }
}
```

---

## Atividade 3: Conversor de Moedas

Foi criado um conversor com codigos e nomes amigaveis de moedas. A taxa de cambio e dependencia externa simulada com Mockito.

**Arquivo:** [`src/main/java/com/tms/aula9/currency/Currency.java`](src/main/java/com/tms/aula9/currency/Currency.java)

Este enum padroniza as moedas aceitas e permite entrada por codigo ou nome amigavel.

```java
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
```

**Arquivo:** [`src/main/java/com/tms/aula9/currency/ExchangeRateService.java`](src/main/java/com/tms/aula9/currency/ExchangeRateService.java)

Esta interface representa a dependencia externa de taxa de cambio, substituida por mock nos testes.

```java
package com.tms.aula9.currency;

public interface ExchangeRateService {
    double rate(Currency source, Currency target);
}
```

**Arquivo:** [`src/main/java/com/tms/aula9/currency/CurrencyConverter.java`](src/main/java/com/tms/aula9/currency/CurrencyConverter.java)

Esta classe converte valores entre moedas usando a taxa fornecida pela dependencia externa.

```java
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
```

**Arquivo:** [`src/test/java/com/tms/aula9/currency/CurrencyConverterTest.java`](src/test/java/com/tms/aula9/currency/CurrencyConverterTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
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
```

---

## Atividade 4: Validador de Senhas

O validador verifica tamanho minimo, letras maiusculas/minusculas, numero, caractere especial e ausencia de espacos.

**Arquivo:** [`src/main/java/com/tms/aula9/password/ValidationResult.java`](src/main/java/com/tms/aula9/password/ValidationResult.java)

Esta classe encapsula o resultado da validacao de senha e suas mensagens de erro.

```java
package com.tms.aula9.password;

import java.util.List;

public class ValidationResult {
    private final boolean valid;
    private final List<String> errors;

    public ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = List.copyOf(errors);
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getErrors() {
        return errors;
    }
}
```

**Arquivo:** [`src/main/java/com/tms/aula9/password/PasswordValidator.java`](src/main/java/com/tms/aula9/password/PasswordValidator.java)

Esta classe implementa os criterios de seguranca definidos para senhas.

```java
package com.tms.aula9.password;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator {
    public boolean isValid(String password) {
        return validate(password).isValid();
    }

    public ValidationResult validate(String password) {
        List<String> errors = new ArrayList<>();
        if (password == null || password.length() < 8) {
            errors.add("Senha deve ter pelo menos 8 caracteres");
        }
        if (password != null && password.matches(".*\\s.*")) {
            errors.add("Senha nao pode conter espacos");
        }
        if (password == null || !password.matches(".*[A-Z].*")) {
            errors.add("Senha deve conter letra maiuscula");
        }
        if (password == null || !password.matches(".*[a-z].*")) {
            errors.add("Senha deve conter letra minuscula");
        }
        if (password == null || !password.matches(".*[0-9].*")) {
            errors.add("Senha deve conter numero");
        }
        if (password == null || !password.matches(".*[@#$%^&+=!].*")) {
            errors.add("Senha deve conter caractere especial");
        }
        return new ValidationResult(errors.isEmpty(), errors);
    }
}
```

**Arquivo:** [`src/test/java/com/tms/aula9/password/PasswordValidatorTest.java`](src/test/java/com/tms/aula9/password/PasswordValidatorTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula9.password;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordValidatorTest {
    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
    }

    @ParameterizedTest
    @CsvSource({
            "Senha@123, true",
            "Ab@1, false",
            "senha@123, false",
            "SENHA@123, false",
            "Senha@abc, false",
            "Senha1234, false",
            "'Senha @123', false"
    })
    void shouldValidatePasswordRules(String password, boolean expected) {
        if (expected) {
            assertTrue(validator.isValid(password));
        } else {
            assertFalse(validator.isValid(password));
        }
    }
}
```

---
## Progresso da Primeira Etapa

Nesta primeira etapa foram concluídas as atividades 1 a 4. As atividades restantes serão desenvolvidas em uma atualização posterior.

## Resultado da Primeira Etapa

A validação final de todas as atividades ficará para a próxima etapa, após a implementação das atividades pendentes.

## Registro de Progresso

- [x] Estudo do ciclo Red-Green-Refactor.
- [x] Calculadora revisitada.
- [x] Gerenciador de voos com TDD.
- [x] Conversor de moedas com mock.
- [x] Validador de senhas.
- [ ] Conversor de unidades.
- [ ] Jogo da velha.
- [ ] Três TDD Katas.

### O que ficou pendente

- Atividade 5: Conversão de números arábicos para romanos.
- Atividade 6: Conversor de unidades.
- Atividade 7: Jogo da velha.
- Atividade 8: TDD Katas.
- Execução e registro da suíte completa após a conclusão das atividades pendentes.

## Referencias

- Kent Beck, Test-Driven Development: By Example.
- JUnit 5 User Guide: https://junit.org/junit5/docs/current/user-guide/
- Mockito: https://site.mockito.org/
- Coding Dojo Katas: https://codingdojo.org/kata/
