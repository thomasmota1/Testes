# Aula 7 - Frameworks para Automacao de Testes (Parte 2)

**Aluno:** Thomas Gabriel Mota de Araujo  
**Data:** 26/07/2026 - 29/28/2026

---


Para executar:

```bash
mvn test
```

---

## Atividade 1: Injecao de Dependencias no JUnit 5

Foi implementada a classe `Usuario` com validacoes de nome, e-mail, senha e autenticacao. Os testes usam `TestInfo`, `TestReporter`, `RepetitionInfo` e `@RepeatedTest`.

**Arquivo:** [src/main/java/com/tms/aula7/user/ExceededAttemptsException.java](src/main/java/com/tms/aula7/user/ExceededAttemptsException.java)

Esta excecao representa o bloqueio de autenticacao apos o excesso de tentativas sem sucesso.

```java
package com.tms.aula7.user;

public class ExceededAttemptsException extends Exception {
    public ExceededAttemptsException(String message) {
        super(message);
    }
}
```

**Arquivo:** [src/main/java/com/tms/aula7/user/HashAlgorithm.java](src/main/java/com/tms/aula7/user/HashAlgorithm.java)

Este enum padroniza os algoritmos de hash usados nos testes parametrizados.

```java
package com.tms.aula7.user;

public enum HashAlgorithm {
    SHA_256("SHA-256"),
    MD5("MD5"),
    SHA_1("SHA-1");

    private final String value;

    HashAlgorithm(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
```

**Arquivo:** [src/main/java/com/tms/aula7/user/Usuario.java](src/main/java/com/tms/aula7/user/Usuario.java)

Esta classe concentra as regras de usuario solicitadas na aula, incluindo validacao de campos, hash de senha e controle de tentativas de autenticacao.

```java
package com.tms.aula7.user;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Pattern;

public class Usuario {
    private static final Pattern NOME_PATTERN = Pattern.compile("^\\p{L}+(\\s\\p{L}+)*$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._]+@[A-Za-z0-9]+$");
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final Duration ATTEMPT_WINDOW = Duration.ofSeconds(30);
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(1);

    private final Clock clock;
    private String nome;
    private String email;
    private String senhaHash;
    private String senhaAlgorithm = HashAlgorithm.SHA_256.value();
    private int tentativasFalhas;
    private Instant primeiraTentativaFalha;
    private Instant bloqueadoAte;

    public Usuario() {
        this(Clock.systemUTC());
    }

    public Usuario(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome nao pode ser nulo ou vazio");
        }
        if (!NOME_PATTERN.matcher(nome).matches()) {
            throw new IllegalArgumentException("Nome deve conter apenas letras e espacos entre palavras");
        }
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email nao pode ser nulo ou vazio");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email deve seguir o padrao usuario@dominio");
        }
        this.email = email;
    }

    public void setSenha(String senha) throws NoSuchAlgorithmException {
        setSenha(senha, HashAlgorithm.SHA_256.value());
    }

    public void setSenha(String senha, String algorithm) throws NoSuchAlgorithmException {
        validateSenha(senha);
        validateAlgorithm(algorithm);
        this.senhaHash = hash(senha, algorithm);
        this.senhaAlgorithm = algorithm;
        resetTentativas();
    }

    public boolean autenticar(String senha) throws ExceededAttemptsException, NoSuchAlgorithmException {
        return autenticar(senha, senhaAlgorithm);
    }

    public boolean autenticar(String senha, String algorithm) throws ExceededAttemptsException, NoSuchAlgorithmException {
        validateSenha(senha);
        validateAlgorithm(algorithm);
        ensureNotBlocked();

        boolean autenticado = senhaHash != null && senhaHash.equals(hash(senha, algorithm));
        if (autenticado) {
            resetTentativas();
            return true;
        }

        registrarFalha();
        return false;
    }

    public boolean validarNome() {
        return nome != null && NOME_PATTERN.matcher(nome).matches();
    }

    public boolean validarEmail() {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isBloqueado() {
        return bloqueadoAte != null && now().isBefore(bloqueadoAte);
    }

    public static boolean isAlgoritmoValido(String algorithm) {
        if (algorithm == null || algorithm.isBlank()) {
            return false;
        }
        try {
            MessageDigest.getInstance(algorithm);
            return true;
        } catch (NoSuchAlgorithmException exception) {
            return false;
        }
    }

    private void ensureNotBlocked() throws ExceededAttemptsException {
        if (isBloqueado()) {
            throw new ExceededAttemptsException("Usuario bloqueado. Tente novamente apos 1 minuto.");
        }
        if (bloqueadoAte != null && !now().isBefore(bloqueadoAte)) {
            resetTentativas();
            bloqueadoAte = null;
        }
    }

    private void registrarFalha() throws ExceededAttemptsException {
        Instant agora = now();
        if (primeiraTentativaFalha == null || agora.isAfter(primeiraTentativaFalha.plus(ATTEMPT_WINDOW))) {
            primeiraTentativaFalha = agora;
            tentativasFalhas = 0;
        }

        tentativasFalhas++;
        if (tentativasFalhas > MAX_FAILED_ATTEMPTS) {
            bloqueadoAte = agora.plus(BLOCK_DURATION);
            throw new ExceededAttemptsException("Excedido numero de tentativas. Bloqueado por 1 minuto.");
        }
    }

    private void resetTentativas() {
        tentativasFalhas = 0;
        primeiraTentativaFalha = null;
        bloqueadoAte = null;
    }

    private Instant now() {
        return Instant.now(clock);
    }

    private static void validateSenha(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Senha nao pode ser nula ou vazia");
        }
    }

    private static void validateAlgorithm(String algorithm) throws NoSuchAlgorithmException {
        if (!isAlgoritmoValido(algorithm)) {
            throw new NoSuchAlgorithmException("Algoritmo desconhecido: " + algorithm);
        }
    }

    private static String hash(String senha, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes);
    }
}
```

**Arquivo:** [src/test/java/com/tms/aula7/user/UsuarioTest.java](src/test/java/com/tms/aula7/user/UsuarioTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula7.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioTest {
    private static Usuario usuarioRepetido;

    private Usuario usuario;

    @BeforeEach
    void setUp(TestInfo testInfo, TestReporter testReporter) {
        usuario = new Usuario();
        testReporter.publishEntry("teste", testInfo.getDisplayName());
    }

    @Test
    @DisplayName("Nome valido deve ser aceito")
    void shouldAcceptValidName() {
        usuario.setNome("Joao Silva");

        assertEquals("Joao Silva", usuario.getNome());
        assertTrue(usuario.validarNome());
    }

    @Test
    @DisplayName("Nome nao pode ser nulo, vazio, numerico ou especial")
    void shouldRejectInvalidNames() {
        assertThrows(IllegalArgumentException.class, () -> usuario.setNome(null));
        assertThrows(IllegalArgumentException.class, () -> usuario.setNome(""));
        assertThrows(IllegalArgumentException.class, () -> usuario.setNome("Joao123"));
        assertThrows(IllegalArgumentException.class, () -> usuario.setNome("Joao-Silva"));
    }

    @Test
    @DisplayName("Email deve seguir usuario@dominio")
    void shouldValidateEmailPattern() {
        usuario.setEmail("joao.silva_1@dominio123");

        assertEquals("joao.silva_1@dominio123", usuario.getEmail());
        assertTrue(usuario.validarEmail());
        assertThrows(IllegalArgumentException.class, () -> usuario.setEmail("joao@dominio.com"));
        assertThrows(IllegalArgumentException.class, () -> usuario.setEmail("joao-silva@dominio"));
        assertThrows(IllegalArgumentException.class, () -> usuario.setEmail("joao@dominio_1"));
    }

    @Test
    @DisplayName("Senha correta autentica e senha errada falha")
    void shouldAuthenticateWithPassword() throws Exception {
        usuario.setSenha("senha123");

        assertTrue(usuario.autenticar("senha123"));
        assertFalse(usuario.autenticar("senhaErrada"));
    }

    @RepeatedTest(value = 4, name = "Tentativa {currentRepetition} de {totalRepetitions}")
    @DisplayName("Autenticacao bloqueia na quarta falha em menos de 30 segundos")
    void shouldBlockAfterFourthFailedAttempt(RepetitionInfo repetitionInfo, TestReporter testReporter) throws NoSuchAlgorithmException {
        if (repetitionInfo.getCurrentRepetition() == 1) {
            usuarioRepetido = new Usuario();
            usuarioRepetido.setSenha("senha123");
        }

        testReporter.publishEntry("tentativa", String.valueOf(repetitionInfo.getCurrentRepetition()));

        if (repetitionInfo.getCurrentRepetition() <= 3) {
            assertDoesNotThrow(() -> assertFalse(usuarioRepetido.autenticar("senhaErrada")));
        } else {
            assertThrows(ExceededAttemptsException.class, () -> usuarioRepetido.autenticar("senhaErrada"));
            assertTrue(usuarioRepetido.isBloqueado());
        }
    }
}
```

---

## Atividade 2: Testes Parametrizados

Foram criados testes com `@ValueSource`, `@EnumSource`, `@CsvSource` e `@CsvFileSource` para validar algoritmos de hash e autenticacao.

**Arquivo:** [src/test/java/com/tms/aula7/user/UsuarioParameterizedTest.java](src/test/java/com/tms/aula7/user/UsuarioParameterizedTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula7.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioParameterizedTest {
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }

    @ParameterizedTest
    @ValueSource(strings = {"SHA-256", "MD5", "SHA-1"})
    void shouldAcceptValidAlgorithmsWithValueSource(String algorithm) {
        assertDoesNotThrow(() -> usuario.setSenha("senha123", algorithm));
    }

    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "SHA-999", ""})
    void shouldRejectInvalidAlgorithmsWithValueSource(String algorithm) {
        assertThrows(NoSuchAlgorithmException.class, () -> usuario.setSenha("senha123", algorithm));
    }

    @ParameterizedTest
    @EnumSource(HashAlgorithm.class)
    void shouldAuthenticateUsingAlgorithmsFromEnumSource(HashAlgorithm algorithm) throws Exception {
        usuario.setSenha("senha123", algorithm.value());

        assertTrue(usuario.autenticar("senha123", algorithm.value()));
        assertFalse(usuario.autenticar("senhaErrada", algorithm.value()));
    }

    @ParameterizedTest
    @CsvSource({
            "SHA-256, true",
            "MD5, true",
            "SHA-1, true",
            "INVALID, false"
    })
    void shouldValidateAlgorithmsWithCsvSource(String algorithm, boolean expected) {
        assertEquals(expected, Usuario.isAlgoritmoValido(algorithm));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/auth_algorithms.csv")
    void shouldValidateAlgorithmsWithCsvFileSource(String algorithm, boolean expected) {
        assertEquals(expected, Usuario.isAlgoritmoValido(algorithm));
    }
}
```

**Arquivo:** [src/test/resources/auth_algorithms.csv](src/test/resources/auth_algorithms.csv)

Este arquivo CSV fornece dados externos para os testes com `@CsvFileSource`.

```csv
SHA-256,true
MD5,true
SHA-1,true
INVALID,false
SHA-999,false
```

---

## Atividade 3: Hamcrest

Foram criadas versoes alternativas de testes usando matchers do Hamcrest para melhorar a legibilidade das verificacoes.

**Arquivo:** [src/test/java/com/tms/aula7/user/UsuarioHamcrestTest.java](src/test/java/com/tms/aula7/user/UsuarioHamcrestTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula7.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

class UsuarioHamcrestTest {
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }

    @Test
    void shouldAssertUserFieldsWithHamcrest() {
        usuario.setNome("Joao Silva");
        usuario.setEmail("joao.silva@dominio");

        assertThat(usuario.getNome(), is(notNullValue()));
        assertThat(usuario.getNome(), containsString("Silva"));
        assertThat(usuario.validarNome(), is(true));
        assertThat(usuario.getEmail(), startsWith("joao"));
        assertThat(usuario.validarEmail(), is(equalTo(true)));
    }
}
```

**Arquivo:** [src/test/java/com/tms/aula7/katas/KatasHamcrestTest.java](src/test/java/com/tms/aula7/katas/KatasHamcrestTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula7.katas;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

class KatasHamcrestTest {
    @Test
    void shouldAssertKataResultsWithHamcrest() {
        RomanNumerals romanNumerals = new RomanNumerals();
        FizzBuzz fizzBuzz = new FizzBuzz();
        Checkout checkout = new Checkout();
        checkout.setUnitPrice("A", 50);
        checkout.setSpecialPrice("A", 3, 130);

        checkout.scan("A");
        checkout.scan("A");
        checkout.scan("A");

        assertThat(romanNumerals.toRoman(1984), containsString("MCML"));
        assertThat(romanNumerals.toArabic("CMXLIV"), is(equalTo(944)));
        assertThat(fizzBuzz.convert(15), is(equalTo("FizzBuzz")));
        assertThat(checkout.getTotal(), is(greaterThan(100)));
    }
}
```

---

## Atividade 4: Code Katas do A07.1P

### Kata 1: Roman Numerals
Conversao de arabico para romano e de romano para arabico, com validacao de entradas invalidas.

**Arquivo:** [src/main/java/com/tms/aula7/katas/RomanNumerals.java](src/main/java/com/tms/aula7/katas/RomanNumerals.java)

Esta classe implementa a conversao entre numeros arabicos e romanos, incluindo validacao de entradas invalidas.

```java
package com.tms.aula7.katas;

import java.util.LinkedHashMap;
import java.util.Map;

public class RomanNumerals {
    private static final int MIN_VALUE = 1;
    private static final int MAX_VALUE = 3999;
    private static final LinkedHashMap<Integer, String> ROMAN_SYMBOLS = new LinkedHashMap<>();

    static {
        ROMAN_SYMBOLS.put(1000, "M");
        ROMAN_SYMBOLS.put(900, "CM");
        ROMAN_SYMBOLS.put(500, "D");
        ROMAN_SYMBOLS.put(400, "CD");
        ROMAN_SYMBOLS.put(100, "C");
        ROMAN_SYMBOLS.put(90, "XC");
        ROMAN_SYMBOLS.put(50, "L");
        ROMAN_SYMBOLS.put(40, "XL");
        ROMAN_SYMBOLS.put(10, "X");
        ROMAN_SYMBOLS.put(9, "IX");
        ROMAN_SYMBOLS.put(5, "V");
        ROMAN_SYMBOLS.put(4, "IV");
        ROMAN_SYMBOLS.put(1, "I");
    }

    public String toRoman(int number) {
        if (number < MIN_VALUE || number > MAX_VALUE) {
            throw new IllegalArgumentException("Number must be between 1 and 3999");
        }

        StringBuilder roman = new StringBuilder();
        int remaining = number;

        for (Map.Entry<Integer, String> entry : ROMAN_SYMBOLS.entrySet()) {
            while (remaining >= entry.getKey()) {
                roman.append(entry.getValue());
                remaining -= entry.getKey();
            }
        }

        return roman.toString();
    }

    public int toArabic(String roman) {
        if (roman == null || roman.isBlank()) {
            throw new IllegalArgumentException("Roman numeral must not be blank");
        }

        String normalized = roman.trim().toUpperCase();
        int result = 0;
        int index = 0;

        for (Map.Entry<Integer, String> entry : ROMAN_SYMBOLS.entrySet()) {
            String symbol = entry.getValue();
            while (normalized.startsWith(symbol, index)) {
                result += entry.getKey();
                index += symbol.length();
            }
        }

        if (index != normalized.length() || !toRoman(result).equals(normalized)) {
            throw new IllegalArgumentException("Invalid Roman numeral: " + roman);
        }

        return result;
    }
}
```

**Arquivo:** [src/test/java/com/tms/aula7/katas/RomanNumeralsTest.java](src/test/java/com/tms/aula7/katas/RomanNumeralsTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula7.katas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RomanNumeralsTest {
    private RomanNumerals romanNumerals;

    @BeforeEach
    void setUp() {
        romanNumerals = new RomanNumerals();
    }

    @ParameterizedTest
    @CsvSource({
            "1, I",
            "4, IV",
            "9, IX",
            "40, XL",
            "90, XC",
            "400, CD",
            "900, CM",
            "944, CMXLIV",
            "1984, MCMLXXXIV",
            "3999, MMMCMXCIX"
    })
    void shouldConvertArabicToRoman(int arabic, String roman) {
        assertEquals(roman, romanNumerals.toRoman(arabic));
    }

    @ParameterizedTest
    @CsvSource({
            "I, 1",
            "IV, 4",
            "IX, 9",
            "XL, 40",
            "XC, 90",
            "CD, 400",
            "CM, 900",
            "CMXLIV, 944",
            "MCMLXXXIV, 1984",
            "MMMCMXCIX, 3999"
    })
    void shouldConvertRomanToArabic(String roman, int arabic) {
        assertEquals(arabic, romanNumerals.toArabic(roman));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 4000})
    void shouldRejectArabicNumbersOutsideValidRange(int number) {
        assertThrows(IllegalArgumentException.class, () -> romanNumerals.toRoman(number));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "IIII", "VX", "ABC", "MMMM"})
    void shouldRejectInvalidRomanNumerals(String roman) {
        assertThrows(IllegalArgumentException.class, () -> romanNumerals.toArabic(roman));
    }

    @Test
    void shouldRejectNullRomanNumeral() {
        assertThrows(IllegalArgumentException.class, () -> romanNumerals.toArabic(null));
    }
}
```

### Kata 2: Fizz Buzz
Conversao de numeros de 1 a 100 para `Fizz`, `Buzz`, `FizzBuzz` ou o proprio numero.

**Arquivo:** [src/main/java/com/tms/aula7/katas/FizzBuzz.java](src/main/java/com/tms/aula7/katas/FizzBuzz.java)

Esta classe implementa a regra do FizzBuzz para numeros no intervalo definido pelo kata.

```java
package com.tms.aula7.katas;

public class FizzBuzz {
    public String convert(int number) {
        if (number < 1 || number > 100) {
            throw new IllegalArgumentException("Number must be between 1 and 100");
        }
        if (number % 15 == 0) {
            return "FizzBuzz";
        }
        if (number % 3 == 0) {
            return "Fizz";
        }
        if (number % 5 == 0) {
            return "Buzz";
        }
        return String.valueOf(number);
    }
}
```

**Arquivo:** [src/test/java/com/tms/aula7/katas/FizzBuzzTest.java](src/test/java/com/tms/aula7/katas/FizzBuzzTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula7.katas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FizzBuzzTest {
    private FizzBuzz fizzBuzz;

    @BeforeEach
    void setUp() {
        fizzBuzz = new FizzBuzz();
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1",
            "2, 2",
            "3, Fizz",
            "5, Buzz",
            "15, FizzBuzz",
            "30, FizzBuzz",
            "98, 98",
            "100, Buzz"
    })
    void shouldConvertNumber(int number, String expected) {
        assertEquals(expected, fizzBuzz.convert(number));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 101})
    void shouldRejectNumbersOutsideOneToOneHundred(int number) {
        assertThrows(IllegalArgumentException.class, () -> fizzBuzz.convert(number));
    }
}
```

### Kata 3: Rock Paper Scissors
Implementacao das regras de Pedra, Papel e Tesoura, incluindo empates e entradas nulas.

**Arquivo:** [src/main/java/com/tms/aula7/katas/Move.java](src/main/java/com/tms/aula7/katas/Move.java)

Este enum representa os movimentos possiveis do jogo Pedra, Papel e Tesoura.

```java
package com.tms.aula7.katas;

public enum Move {
    ROCK,
    PAPER,
    SCISSORS
}
```

**Arquivo:** [src/main/java/com/tms/aula7/katas/Result.java](src/main/java/com/tms/aula7/katas/Result.java)

Este enum representa os resultados possiveis de uma rodada: vitoria, derrota ou empate.

```java
package com.tms.aula7.katas;

public enum Result {
    WIN,
    LOSE,
    DRAW
}
```

**Arquivo:** [src/main/java/com/tms/aula7/katas/RockPaperScissors.java](src/main/java/com/tms/aula7/katas/RockPaperScissors.java)

Esta classe implementa as regras de comparacao entre os movimentos do jogo.

```java
package com.tms.aula7.katas;

import java.util.Objects;

public class RockPaperScissors {
    public Result play(Move player1, Move player2) {
        Objects.requireNonNull(player1, "player1 must not be null");
        Objects.requireNonNull(player2, "player2 must not be null");

        if (player1 == player2) {
            return Result.DRAW;
        }

        if ((player1 == Move.ROCK && player2 == Move.SCISSORS)
                || (player1 == Move.SCISSORS && player2 == Move.PAPER)
                || (player1 == Move.PAPER && player2 == Move.ROCK)) {
            return Result.WIN;
        }

        return Result.LOSE;
    }
}
```

**Arquivo:** [src/test/java/com/tms/aula7/katas/RockPaperScissorsTest.java](src/test/java/com/tms/aula7/katas/RockPaperScissorsTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula7.katas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RockPaperScissorsTest {
    private RockPaperScissors game;

    @BeforeEach
    void setUp() {
        game = new RockPaperScissors();
    }

    @Test
    void shouldApplyWinningRules() {
        assertEquals(Result.WIN, game.play(Move.ROCK, Move.SCISSORS));
        assertEquals(Result.WIN, game.play(Move.SCISSORS, Move.PAPER));
        assertEquals(Result.WIN, game.play(Move.PAPER, Move.ROCK));
    }

    @Test
    void shouldApplyLosingRules() {
        assertEquals(Result.LOSE, game.play(Move.SCISSORS, Move.ROCK));
        assertEquals(Result.LOSE, game.play(Move.PAPER, Move.SCISSORS));
        assertEquals(Result.LOSE, game.play(Move.ROCK, Move.PAPER));
    }

    @ParameterizedTest
    @EnumSource(Move.class)
    void shouldDrawWhenMovesAreEqual(Move move) {
        assertEquals(Result.DRAW, game.play(move, move));
    }

    @Test
    void shouldRejectNullMoves() {
        assertThrows(NullPointerException.class, () -> game.play(null, Move.ROCK));
        assertThrows(NullPointerException.class, () -> game.play(Move.ROCK, null));
    }
}
```

### Kata 4: Bingo
Cartao 5x5 com espaco central livre, marcacao de numeros e deteccao por linha, coluna e diagonais.

**Arquivo:** [src/main/java/com/tms/aula7/katas/BingoCard.java](src/main/java/com/tms/aula7/katas/BingoCard.java)

Esta classe representa um cartao de Bingo e contem a logica de marcacao e verificacao de linhas vencedoras.

```java
package com.tms.aula7.katas;

public class BingoCard {
    private static final int SIZE = 5;

    private final int[][] numbers;
    private final boolean[][] marked;

    public BingoCard(int[][] numbers) {
        validateCard(numbers);
        this.numbers = copy(numbers);
        this.marked = new boolean[SIZE][SIZE];
        this.marked[2][2] = true;
    }

    public boolean mark(int number) {
        for (int row = 0; row < SIZE; row++) {
            for (int column = 0; column < SIZE; column++) {
                if (numbers[row][column] == number) {
                    marked[row][column] = true;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasBingo() {
        for (int index = 0; index < SIZE; index++) {
            if (isRowComplete(index) || isColumnComplete(index)) {
                return true;
            }
        }
        return isMainDiagonalComplete() || isAntiDiagonalComplete();
    }

    private boolean isRowComplete(int row) {
        for (int column = 0; column < SIZE; column++) {
            if (!marked[row][column]) {
                return false;
            }
        }
        return true;
    }

    private boolean isColumnComplete(int column) {
        for (int row = 0; row < SIZE; row++) {
            if (!marked[row][column]) {
                return false;
            }
        }
        return true;
    }

    private boolean isMainDiagonalComplete() {
        for (int index = 0; index < SIZE; index++) {
            if (!marked[index][index]) {
                return false;
            }
        }
        return true;
    }

    private boolean isAntiDiagonalComplete() {
        for (int index = 0; index < SIZE; index++) {
            if (!marked[index][SIZE - 1 - index]) {
                return false;
            }
        }
        return true;
    }

    private static void validateCard(int[][] numbers) {
        if (numbers == null || numbers.length != SIZE) {
            throw new IllegalArgumentException("Card must have 5 rows");
        }
        for (int[] row : numbers) {
            if (row == null || row.length != SIZE) {
                throw new IllegalArgumentException("Each row must have 5 columns");
            }
        }
    }

    private static int[][] copy(int[][] source) {
        int[][] copy = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(source[row], 0, copy[row], 0, SIZE);
        }
        return copy;
    }
}
```

**Arquivo:** [src/test/java/com/tms/aula7/katas/BingoCardTest.java](src/test/java/com/tms/aula7/katas/BingoCardTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula7.katas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BingoCardTest {
    private int[][] numbers;

    @BeforeEach
    void setUp() {
        numbers = new int[][]{
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 0, 14, 15},
                {16, 17, 18, 19, 20},
                {21, 22, 23, 24, 25}
        };
    }

    @Test
    void shouldMarkExistingNumber() {
        BingoCard card = new BingoCard(numbers);

        assertTrue(card.mark(1));
        assertFalse(card.mark(99));
    }

    @Test
    void shouldDetectRowBingo() {
        BingoCard card = new BingoCard(numbers);

        card.mark(1);
        card.mark(2);
        card.mark(3);
        card.mark(4);
        card.mark(5);

        assertTrue(card.hasBingo());
    }

    @Test
    void shouldDetectColumnBingo() {
        BingoCard card = new BingoCard(numbers);

        card.mark(2);
        card.mark(7);
        card.mark(12);
        card.mark(17);
        card.mark(22);

        assertTrue(card.hasBingo());
    }

    @Test
    void shouldDetectDiagonalBingoWithFreeCenter() {
        BingoCard card = new BingoCard(numbers);

        card.mark(1);
        card.mark(7);
        card.mark(19);
        card.mark(25);

        assertTrue(card.hasBingo());
    }

    @Test
    void shouldDetectAntiDiagonalBingoWithFreeCenter() {
        BingoCard card = new BingoCard(numbers);

        card.mark(5);
        card.mark(9);
        card.mark(17);
        card.mark(21);

        assertTrue(card.hasBingo());
    }

    @Test
    void shouldReturnFalseWhenThereIsNoBingo() {
        BingoCard card = new BingoCard(numbers);

        card.mark(1);
        card.mark(7);
        card.mark(19);

        assertFalse(card.hasBingo());
    }

    @Test
    void shouldRejectInvalidCard() {
        assertThrows(IllegalArgumentException.class, () -> new BingoCard(null));
        assertThrows(IllegalArgumentException.class, () -> new BingoCard(new int[][]{{1, 2, 3, 4, 5}}));
        assertThrows(IllegalArgumentException.class, () -> new BingoCard(new int[][]{
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 0, 14, 15},
                {16, 17, 18, 19, 20},
                {21, 22, 23}
        }));
    }
}
```

### Kata 5: Berlin Clock
Conversao de horario para as cinco linhas do relogio de Berlim, com validacao de faixas.

**Arquivo:** [src/main/java/com/tms/aula7/katas/BerlinClock.java](src/main/java/com/tms/aula7/katas/BerlinClock.java)

Esta classe converte um horario comum para a representacao em linhas do relogio de Berlim.

```java
package com.tms.aula7.katas;

public class BerlinClock {
    public String getSeconds(int seconds) {
        validateRange(seconds, 0, 59, "seconds");
        return seconds % 2 == 0 ? "Y" : "O";
    }

    public String getFiveHourRow(int hours) {
        validateRange(hours, 0, 23, "hours");
        return lights("R", hours / 5, 4);
    }

    public String getOneHourRow(int hours) {
        validateRange(hours, 0, 23, "hours");
        return lights("R", hours % 5, 4);
    }

    public String getFiveMinuteRow(int minutes) {
        validateRange(minutes, 0, 59, "minutes");
        int lit = minutes / 5;
        StringBuilder row = new StringBuilder();

        for (int position = 1; position <= 11; position++) {
            if (position > lit) {
                row.append("O");
            } else {
                row.append(position % 3 == 0 ? "R" : "Y");
            }
        }

        return row.toString();
    }

    public String getOneMinuteRow(int minutes) {
        validateRange(minutes, 0, 59, "minutes");
        return lights("Y", minutes % 5, 4);
    }

    public String getTime(int hours, int minutes, int seconds) {
        return getSeconds(seconds) + "\n"
                + getFiveHourRow(hours) + "\n"
                + getOneHourRow(hours) + "\n"
                + getFiveMinuteRow(minutes) + "\n"
                + getOneMinuteRow(minutes);
    }

    private static String lights(String litSymbol, int litCount, int total) {
        return litSymbol.repeat(litCount) + "O".repeat(total - litCount);
    }

    private static void validateRange(int value, int min, int max, String field) {
        if (value < min || value > max) {
            throw new IllegalArgumentException(field + " must be between " + min + " and " + max);
        }
    }
}
```

**Arquivo:** [src/test/java/com/tms/aula7/katas/BerlinClockTest.java](src/test/java/com/tms/aula7/katas/BerlinClockTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula7.katas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BerlinClockTest {
    private BerlinClock clock;

    @BeforeEach
    void setUp() {
        clock = new BerlinClock();
    }

    @ParameterizedTest
    @CsvSource({"0, Y", "1, O", "2, Y", "59, O"})
    void shouldConvertSeconds(int seconds, String expected) {
        assertEquals(expected, clock.getSeconds(seconds));
    }

    @ParameterizedTest
    @CsvSource({
            "0, OOOO",
            "5, ROOO",
            "10, RROO",
            "15, RRRO",
            "20, RRRR",
            "23, RRRR"
    })
    void shouldConvertFiveHourRow(int hours, String expected) {
        assertEquals(expected, clock.getFiveHourRow(hours));
    }

    @ParameterizedTest
    @CsvSource({
            "0, OOOO",
            "1, ROOO",
            "2, RROO",
            "3, RRRO",
            "4, RRRR",
            "5, OOOO",
            "23, RRRO"
    })
    void shouldConvertOneHourRow(int hours, String expected) {
        assertEquals(expected, clock.getOneHourRow(hours));
    }

    @ParameterizedTest
    @CsvSource({
            "0, OOOOOOOOOOO",
            "5, YOOOOOOOOOO",
            "15, YYROOOOOOOO",
            "30, YYRYYROOOOO",
            "59, YYRYYRYYRYY"
    })
    void shouldConvertFiveMinuteRow(int minutes, String expected) {
        assertEquals(expected, clock.getFiveMinuteRow(minutes));
    }

    @ParameterizedTest
    @CsvSource({
            "0, OOOO",
            "1, YOOO",
            "4, YYYY",
            "5, OOOO",
            "59, YYYY"
    })
    void shouldConvertOneMinuteRow(int minutes, String expected) {
        assertEquals(expected, clock.getOneMinuteRow(minutes));
    }

    @Test
    void shouldConvertFullTime() {
        String expected = "Y\nRRRR\nRRRO\nYYRYYRYYRYY\nYYYY";
        assertEquals(expected, clock.getTime(23, 59, 0));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 24})
    void shouldRejectInvalidHours(int hours) {
        assertThrows(IllegalArgumentException.class, () -> clock.getTime(hours, 0, 0));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 60})
    void shouldRejectInvalidMinutes(int minutes) {
        assertThrows(IllegalArgumentException.class, () -> clock.getTime(0, minutes, 0));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 60})
    void shouldRejectInvalidSeconds(int seconds) {
        assertThrows(IllegalArgumentException.class, () -> clock.getTime(0, 0, seconds));
    }
}
```

### Kata 6: Back to the Checkout
Checkout com precos unitarios, precos especiais, carrinho e validacoes.

**Arquivo:** [src/main/java/com/tms/aula7/katas/Checkout.java](src/main/java/com/tms/aula7/katas/Checkout.java)

Esta classe implementa o carrinho do kata Checkout, com precos unitarios e promocoes por quantidade.

```java
package com.tms.aula7.katas;

import java.util.HashMap;
import java.util.Map;

public class Checkout {
    private final Map<String, Integer> unitPrices = new HashMap<>();
    private final Map<String, SpecialPrice> specialPrices = new HashMap<>();
    private final Map<String, Integer> cart = new HashMap<>();

    public void setUnitPrice(String item, int price) {
        validateItem(item);
        validatePositive(price, "price");
        unitPrices.put(item, price);
    }

    public void setSpecialPrice(String item, int quantity, int price) {
        validateItem(item);
        validatePositive(quantity, "quantity");
        validatePositive(price, "price");
        if (!unitPrices.containsKey(item)) {
            throw new IllegalArgumentException("Unit price must be configured before special price");
        }
        specialPrices.put(item, new SpecialPrice(quantity, price));
    }

    public void scan(String item) {
        validateItem(item);
        if (!unitPrices.containsKey(item)) {
            throw new IllegalArgumentException("Unknown item: " + item);
        }
        cart.merge(item, 1, Integer::sum);
    }

    public int getTotal() {
        int total = 0;
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            total += calculateItemTotal(entry.getKey(), entry.getValue());
        }
        return total;
    }

    public void clear() {
        cart.clear();
    }

    private int calculateItemTotal(String item, int quantity) {
        int unitPrice = unitPrices.get(item);
        SpecialPrice specialPrice = specialPrices.get(item);

        if (specialPrice == null) {
            return unitPrice * quantity;
        }

        int specialSets = quantity / specialPrice.quantity;
        int remainingItems = quantity % specialPrice.quantity;
        return (specialSets * specialPrice.price) + (remainingItems * unitPrice);
    }

    private static void validateItem(String item) {
        if (item == null || item.isBlank()) {
            throw new IllegalArgumentException("Item must not be blank");
        }
    }

    private static void validatePositive(int value, String field) {
        if (value <= 0) {
            throw new IllegalArgumentException(field + " must be positive");
        }
    }

    private static class SpecialPrice {
        private final int quantity;
        private final int price;

        private SpecialPrice(int quantity, int price) {
            this.quantity = quantity;
            this.price = price;
        }
    }
}
```

**Arquivo:** [src/test/java/com/tms/aula7/katas/CheckoutTest.java](src/test/java/com/tms/aula7/katas/CheckoutTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula7.katas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CheckoutTest {
    private Checkout checkout;

    @BeforeEach
    void setUp() {
        checkout = new Checkout();
        checkout.setUnitPrice("A", 50);
        checkout.setUnitPrice("B", 30);
        checkout.setUnitPrice("C", 20);
        checkout.setUnitPrice("D", 15);
        checkout.setSpecialPrice("A", 3, 130);
        checkout.setSpecialPrice("B", 2, 45);
    }

    @Test
    void shouldReturnZeroForEmptyCart() {
        assertEquals(0, checkout.getTotal());
    }

    @Test
    void shouldCalculateSingleItem() {
        checkout.scan("A");

        assertEquals(50, checkout.getTotal());
    }

    @Test
    void shouldCalculateMultipleItemsWithoutSpecialPrice() {
        checkout.scan("A");
        checkout.scan("B");
        checkout.scan("C");

        assertEquals(100, checkout.getTotal());
    }

    @Test
    void shouldApplySpecialPriceWhenQuantityIsExact() {
        checkout.scan("A");
        checkout.scan("A");
        checkout.scan("A");

        assertEquals(130, checkout.getTotal());
    }

    @Test
    void shouldApplySpecialPriceWithRemainder() {
        checkout.scan("A");
        checkout.scan("A");
        checkout.scan("A");
        checkout.scan("A");

        assertEquals(180, checkout.getTotal());
    }

    @Test
    void shouldCalculateMixedCart() {
        checkout.scan("A");
        checkout.scan("A");
        checkout.scan("A");
        checkout.scan("B");
        checkout.scan("B");
        checkout.scan("C");

        assertEquals(195, checkout.getTotal());
    }

    @Test
    void shouldClearCart() {
        checkout.scan("A");
        checkout.clear();

        assertEquals(0, checkout.getTotal());
    }

    @Test
    void shouldRejectUnknownItem() {
        assertThrows(IllegalArgumentException.class, () -> checkout.scan("X"));
    }

    @Test
    void shouldRejectInvalidPricesAndItems() {
        assertThrows(IllegalArgumentException.class, () -> checkout.setUnitPrice("", 10));
        assertThrows(IllegalArgumentException.class, () -> checkout.setUnitPrice("E", 0));
        assertThrows(IllegalArgumentException.class, () -> checkout.setSpecialPrice("E", 2, 10));
        assertThrows(IllegalArgumentException.class, () -> checkout.setSpecialPrice("A", 0, 10));
        assertThrows(IllegalArgumentException.class, () -> checkout.scan(null));
    }
}
```

---

## Comparativo de Fontes para Testes Parametrizados

| Fonte | Melhor uso | Vantagem | Limitacao |
|---|---|---|---|
| `@ValueSource` | Um parametro simples | Direto e legivel | Nao suporta multiplas colunas |
| `@EnumSource` | Valores baseados em enum | Type-safe | Limitado a enums |
| `@CsvSource` | Entrada e saida inline | Bom para tabelas pequenas | Pode ficar confuso com muitos dados |
| `@CsvFileSource` | Dados externos | Separa dados do codigo | Exige arquivo externo |

## Resultado Real da Execucao

```text
Tests run: 124, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Registro de Progresso

### O que foi feito:
- [x] Estudo de injecao de dependencias no JUnit 5.
- [x] Uso de TestInfo, TestReporter e RepetitionInfo.
- [x] Testes parametrizados com ValueSource, EnumSource, CsvSource e CsvFileSource.
- [x] Uso de Hamcrest em testes alternativos.
- [x] Implementacao completa dos seis Code Katas do A07.1P.
- [x] Execucao completa da suite Maven com sucesso.

### O que impediu:
- Nada impediu a conclusao das atividades.


## Referencias

- JUnit 5 User Guide. Disponivel em: https://junit.org/junit5/docs/current/user-guide/
- Hamcrest Tutorial. Disponivel em: http://hamcrest.org/JavaHamcrest/tutorial
- Coding Dojo Katas. Disponivel em: https://codingdojo.org/kata/
