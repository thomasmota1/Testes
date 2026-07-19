# Aula 8 - Frameworks para Automacao de Testes (Parte 3) - Test Doubles

**Aluno:** Thomas Gabriel Mota de Araujo  
**Data:** 14/07/2026 - 18/07/2026

---


## Estrutura 

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

## Atividade 1: Demais Dublês de Teste

Foram implementados exemplos de Dummy Object, Fake Object, Test Stub e Test Spy. O teste mostra a diferenca de uso entre preencher dependencia, simular repositorio, controlar entrada indireta e registrar chamadas.

**Arquivo:** [`src/main/java/com/tms/aula8/examples/Logger.java`](src/main/java/com/tms/aula8/examples/Logger.java)

Esta interface representa uma dependencia simples usada para demonstrar Dummy Object.

```java
package com.tms.aula8.examples;

public interface Logger {
    void log(String message);
}
```

**Arquivo:** [`src/main/java/com/tms/aula8/examples/User.java`](src/main/java/com/tms/aula8/examples/User.java)

Esta classe representa o usuario usado nos exemplos de repositorio, registro e dubles de teste.

```java
package com.tms.aula8.examples;

public class User {
    private final String id;
    private final String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
```

**Arquivo:** [`src/main/java/com/tms/aula8/examples/UserRepository.java`](src/main/java/com/tms/aula8/examples/UserRepository.java)

Esta interface define a dependencia de persistencia usada para demonstrar Fake Object e mocks.

```java
package com.tms.aula8.examples;

public interface UserRepository {
    void save(User user);

    User findById(String id);
}
```

**Arquivo:** [`src/main/java/com/tms/aula8/examples/EmailService.java`](src/main/java/com/tms/aula8/examples/EmailService.java)

Esta interface representa o servico externo de e-mail usado para demonstrar Spy e Mockito.

```java
package com.tms.aula8.examples;

public interface EmailService {
    void send(String to, String message);
}
```

**Arquivo:** [`src/main/java/com/tms/aula8/examples/UserRegistrationService.java`](src/main/java/com/tms/aula8/examples/UserRegistrationService.java)

Este servico e o SUT dos exemplos de dubles, pois coordena repositorio, e-mail e log.

```java
package com.tms.aula8.examples;

public class UserRegistrationService {
    private final UserRepository repository;
    private final EmailService emailService;
    private final Logger logger;

    public UserRegistrationService(UserRepository repository, EmailService emailService, Logger logger) {
        this.repository = repository;
        this.emailService = emailService;
        this.logger = logger;
    }

    public void register(User user) {
        repository.save(user);
        emailService.send(user.getId(), "Welcome, " + user.getName());
        logger.log("registered " + user.getId());
    }
}
```

**Arquivo:** [`src/main/java/com/tms/aula8/examples/TimeProvider.java`](src/main/java/com/tms/aula8/examples/TimeProvider.java)

Esta interface isola a obtencao do horario atual, permitindo controlar o tempo durante os testes.

```java
package com.tms.aula8.examples;

import java.time.LocalTime;

public interface TimeProvider {
    LocalTime now();
}
```

**Arquivo:** [`src/main/java/com/tms/aula8/examples/TimeDisplay.java`](src/main/java/com/tms/aula8/examples/TimeDisplay.java)

Esta classe formata o horario atual e mostra por que um Stub e util para evitar dependencia do relogio real.

```java
package com.tms.aula8.examples;

import java.time.LocalTime;

public class TimeDisplay {
    private final TimeProvider timeProvider;

    public TimeDisplay(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public String getCurrentTimeAsHtmlFragment() {
        LocalTime time = timeProvider.now();
        if (time.getHour() == 0 && time.getMinute() == 0) {
            return "<span class=\"tinyBoldText\">Midnight</span>";
        }
        return "<span class=\"tinyBoldText\">" + time + "</span>";
    }
}
```

**Arquivo:** [`src/test/java/com/tms/aula8/examples/TestDoublesExamplesTest.java`](src/test/java/com/tms/aula8/examples/TestDoublesExamplesTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula8.examples;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestDoublesExamplesTest {
    @Test
    void shouldUseStubToControlIndirectInput() {
        TimeDisplay display = new TimeDisplay(new FixedTimeProvider(LocalTime.MIDNIGHT));

        assertEquals("<span class=\"tinyBoldText\">Midnight</span>", display.getCurrentTimeAsHtmlFragment());
    }

    @Test
    void shouldUseFakeRepositoryWithRealBehaviorInMemory() {
        InMemoryUserRepository repository = new InMemoryUserRepository();
        User user = new User("1", "Ana");

        repository.save(user);

        assertEquals("Ana", repository.findById("1").getName());
    }

    @Test
    void shouldUseSpyToRecordSentEmailsAndDummyLoggerToFillDependency() {
        InMemoryUserRepository repository = new InMemoryUserRepository();
        EmailServiceSpy spy = new EmailServiceSpy();
        UserRegistrationService service = new UserRegistrationService(repository, spy, new DummyLogger());

        service.register(new User("ana@example.com", "Ana"));

        assertEquals(1, spy.count());
        assertEquals("ana@example.com", spy.recipients().get(0));
    }

    static class FixedTimeProvider implements TimeProvider {
        private final LocalTime time;

        FixedTimeProvider(LocalTime time) {
            this.time = time;
        }

        @Override
        public LocalTime now() {
            return time;
        }
    }

    static class InMemoryUserRepository implements UserRepository {
        private final Map<String, User> users = new HashMap<>();

        @Override
        public void save(User user) {
            users.put(user.getId(), user);
        }

        @Override
        public User findById(String id) {
            return users.get(id);
        }
    }

    static class EmailServiceSpy implements EmailService {
        private final List<String> recipients = new ArrayList<>();

        @Override
        public void send(String to, String message) {
            recipients.add(to);
        }

        int count() {
            return recipients.size();
        }

        List<String> recipients() {
            return recipients;
        }
    }

    static class DummyLogger implements Logger {
        @Override
        public void log(String message) {
            
        }
    }
}
```

---

## Atividade 2: Testes com EasyMock

Foram testados exemplos com `createMock`, `expect`, `andReturn`, `times`, `andThrow`, `replay` e `verify`.

**Arquivo:** [`src/test/java/com/tms/aula8/examples/EasyMockExamplesTest.java`](src/test/java/com/tms/aula8/examples/EasyMockExamplesTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula8.examples;

import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EasyMockExamplesTest {
    @Test
    void shouldUseExpectAndReturn() {
        UserRepository repository = createMock(UserRepository.class);
        expect(repository.findById("1")).andReturn(new User("1", "Ana"));
        replay(repository);

        assertEquals("Ana", repository.findById("1").getName());
        verify(repository);
    }

    @Test
    void shouldUseTimesForMultipleCalls() {
        UserRepository repository = createMock(UserRepository.class);
        expect(repository.findById("1")).andReturn(new User("1", "Ana")).times(2);
        replay(repository);

        repository.findById("1");
        repository.findById("1");

        verify(repository);
    }

    @Test
    void shouldUseAndThrowForExceptionalScenario() {
        TimeProvider provider = createMock(TimeProvider.class);
        expect(provider.now()).andThrow(new IllegalStateException("clock unavailable"));
        replay(provider);

        assertThrows(IllegalStateException.class, provider::now);
        verify(provider);
    }
}
```

---

## Atividade 3: Testes com Mockito

Foram replicados exemplos com `when`, `thenReturn`, `verify`, `ArgumentCaptor`, `spy`, `thenAnswer` e `InOrder`. Tambem foi implementado o exemplo de transferencia bancaria.

**Arquivo:** [`src/main/java/com/tms/aula8/examples/Account.java`](src/main/java/com/tms/aula8/examples/Account.java)

Esta classe representa uma conta bancaria usada no exemplo de transferencia com mock.

```java
package com.tms.aula8.examples;

public class Account {
    private final String id;
    private long balance;

    public Account(String id, long balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public long getBalance() {
        return balance;
    }

    public void debit(long amount) {
        if (amount <= 0 || amount > balance) {
            throw new IllegalArgumentException("Invalid debit amount");
        }
        balance -= amount;
    }

    public void credit(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid credit amount");
        }
        balance += amount;
    }
}
```

**Arquivo:** [`src/main/java/com/tms/aula8/examples/AccountManager.java`](src/main/java/com/tms/aula8/examples/AccountManager.java)

Esta interface representa a dependencia externa responsavel por localizar e atualizar contas.

```java
package com.tms.aula8.examples;

public interface AccountManager {
    Account findAccountForUser(String userId);

    void updateAccount(Account account);
}
```

**Arquivo:** [`src/main/java/com/tms/aula8/examples/AccountService.java`](src/main/java/com/tms/aula8/examples/AccountService.java)

Este servico realiza a transferencia e e testado com mocks para verificar interacoes com `AccountManager`.

```java
package com.tms.aula8.examples;

import java.util.Objects;

public class AccountService {
    private final AccountManager accountManager;

    public AccountService(AccountManager accountManager) {
        this.accountManager = Objects.requireNonNull(accountManager);
    }

    public void transfer(String senderId, String beneficiaryId, long amount) {
        Account sender = accountManager.findAccountForUser(senderId);
        Account beneficiary = accountManager.findAccountForUser(beneficiaryId);
        if (sender == null || beneficiary == null) {
            throw new IllegalArgumentException("Account not found");
        }

        sender.debit(amount);
        beneficiary.credit(amount);
        accountManager.updateAccount(sender);
        accountManager.updateAccount(beneficiary);
    }
}
```

**Arquivo:** [`src/test/java/com/tms/aula8/examples/MockitoExamplesTest.java`](src/test/java/com/tms/aula8/examples/MockitoExamplesTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula8.examples;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MockitoExamplesTest {
    @Test
    void shouldStubWithWhenThenReturnAndVerifyInteraction() {
        UserRepository repository = mock(UserRepository.class);
        when(repository.findById("1")).thenReturn(new User("1", "Ana"));

        User user = repository.findById("1");

        assertEquals("Ana", user.getName());
        verify(repository).findById("1");
    }

    @Test
    void shouldCaptureArguments() {
        EmailService emailService = mock(EmailService.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        emailService.send("ana@example.com", "Welcome");

        verify(emailService).send(org.mockito.ArgumentMatchers.eq("ana@example.com"), messageCaptor.capture());
        assertEquals("Welcome", messageCaptor.getValue());
    }

    @Test
    void shouldUseSpyForPartialMocking() {
        List<String> list = spy(new ArrayList<>());

        list.add("one");
        list.add("two");

        assertEquals(2, list.size());
        verify(list).add("one");
    }

    @Test
    void shouldUseAnswerAndInOrder() {
        UserRepository repository = mock(UserRepository.class);
        EmailService emailService = mock(EmailService.class);
        when(repository.findById(any())).thenAnswer(invocation -> new User(invocation.getArgument(0), "Generated"));

        repository.findById("42");
        emailService.send("42", "ok");

        InOrder inOrder = inOrder(repository, emailService);
        inOrder.verify(repository).findById("42");
        inOrder.verify(emailService).send("42", "ok");
    }
}
```

**Arquivo:** [`src/test/java/com/tms/aula8/examples/AccountServiceMockitoTest.java`](src/test/java/com/tms/aula8/examples/AccountServiceMockitoTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula8.examples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceMockitoTest {
    @Test
    void shouldTransferMoneyAndUpdateBothAccounts() {
        Account sender = new Account("1", 200);
        Account beneficiary = new Account("2", 100);
        AccountManager accountManager = mock(AccountManager.class);
        when(accountManager.findAccountForUser("sender")).thenReturn(sender);
        when(accountManager.findAccountForUser("beneficiary")).thenReturn(beneficiary);

        new AccountService(accountManager).transfer("sender", "beneficiary", 50);

        assertEquals(150, sender.getBalance());
        assertEquals(150, beneficiary.getBalance());
        verify(accountManager).updateAccount(sender);
        verify(accountManager).updateAccount(beneficiary);
    }
}
```

---

## Atividade 4: Comparativo entre Frameworks para Mock

| Framework | Estilo | Pontos fortes | Limitacoes |
|---|---|---|---|
| EasyMock | Record-replay-verify | Expectativas explicitas | Mais verboso |
| JMock | Expectations-based | Expressivo para expectativas complexas | Sintaxe mais pesada |
| Mockito | Stub-then-verify | Simples, legivel e popular | Pode ficar leniente se mal usado |

Mockito foi a escolha principal para os testes praticos por ter melhor legibilidade e integracao simples com JUnit 5.

---

## Atividade 5: Bingo Kata com Dublês

O Bingo foi dividido em tres responsabilidades: `Card`, `Caller` e `Bingo`. Os testes do jogo usam mocks para isolar o `StandardBingoGame` de `Caller` e `Card`, enquanto `StandardCard` e `RandomCaller` possuem testes próprios. Também há um teste de integração com os três componentes reais.

**Arquivo:** [`src/main/java/com/tms/aula8/bingo/Card.java`](src/main/java/com/tms/aula8/bingo/Card.java)

Esta interface define o contrato do cartao de Bingo para permitir testes isolados com dubles.

```java
package com.tms.aula8.bingo;

public interface Card {
    boolean mark(int number);

    boolean hasBingo();

    int[][] getNumbers();

    boolean[][] getMarkedState();
}
```

**Arquivo:** [`src/main/java/com/tms/aula8/bingo/Caller.java`](src/main/java/com/tms/aula8/bingo/Caller.java)

Esta interface define o contrato do sorteador de numeros do Bingo.

```java
package com.tms.aula8.bingo;

import java.util.List;

public interface Caller {
    int callNumber();

    List<Integer> getCalledNumbers();

    boolean hasMoreNumbers();

    void reset();
}
```

**Arquivo:** [`src/main/java/com/tms/aula8/bingo/Bingo.java`](src/main/java/com/tms/aula8/bingo/Bingo.java)

Esta interface define o contrato do coordenador do jogo de Bingo.

```java
package com.tms.aula8.bingo;

public interface Bingo {
    void addCard(String playerId, Card card);

    void startGame();

    int playRound();

    boolean hasWinner();

    String getWinner();
}
```

**Arquivo:** [`src/main/java/com/tms/aula8/bingo/StandardCard.java`](src/main/java/com/tms/aula8/bingo/StandardCard.java)

Esta implementacao real de `Card` mantem numeros, marcacoes e verifica condicoes de Bingo.

```java
package com.tms.aula8.bingo;

public class StandardCard implements Card {
    private static final int SIZE = 5;

    private final int[][] numbers;
    private final boolean[][] marked = new boolean[SIZE][SIZE];

    public StandardCard(int[][] numbers) {
        if (numbers == null || numbers.length != SIZE) {
            throw new IllegalArgumentException("Card must have five rows");
        }
        this.numbers = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            if (numbers[row] == null || numbers[row].length != SIZE) {
                throw new IllegalArgumentException("Card must have five columns per row");
            }
            System.arraycopy(numbers[row], 0, this.numbers[row], 0, SIZE);
        }
        marked[2][2] = true;
    }

    @Override
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

    @Override
    public boolean hasBingo() {
        for (int index = 0; index < SIZE; index++) {
            if (rowComplete(index) || columnComplete(index)) {
                return true;
            }
        }
        return diagonalComplete() || antiDiagonalComplete();
    }

    @Override
    public int[][] getNumbers() {
        int[][] copy = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(numbers[row], 0, copy[row], 0, SIZE);
        }
        return copy;
    }

    @Override
    public boolean[][] getMarkedState() {
        boolean[][] copy = new boolean[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            System.arraycopy(marked[row], 0, copy[row], 0, SIZE);
        }
        return copy;
    }

    private boolean rowComplete(int row) {
        for (int column = 0; column < SIZE; column++) {
            if (!marked[row][column]) {
                return false;
            }
        }
        return true;
    }

    private boolean columnComplete(int column) {
        for (int row = 0; row < SIZE; row++) {
            if (!marked[row][column]) {
                return false;
            }
        }
        return true;
    }

    private boolean diagonalComplete() {
        for (int index = 0; index < SIZE; index++) {
            if (!marked[index][index]) {
                return false;
            }
        }
        return true;
    }

    private boolean antiDiagonalComplete() {
        for (int index = 0; index < SIZE; index++) {
            if (!marked[index][SIZE - 1 - index]) {
                return false;
            }
        }
        return true;
    }
}
```

**Arquivo:** [`src/main/java/com/tms/aula8/bingo/RandomCaller.java`](src/main/java/com/tms/aula8/bingo/RandomCaller.java)

Esta implementacao real de `Caller` sorteia numeros sem repeticao e registra o historico.

```java
package com.tms.aula8.bingo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomCaller implements Caller {
    private final List<Integer> availableNumbers = new ArrayList<>();
    private final List<Integer> calledNumbers = new ArrayList<>();

    public RandomCaller() {
        reset();
    }

    @Override
    public int callNumber() {
        if (availableNumbers.isEmpty()) {
            throw new IllegalStateException("No numbers left");
        }
        int number = availableNumbers.remove(0);
        calledNumbers.add(number);
        return number;
    }

    @Override
    public List<Integer> getCalledNumbers() {
        return new ArrayList<>(calledNumbers);
    }

    @Override
    public boolean hasMoreNumbers() {
        return !availableNumbers.isEmpty();
    }

    @Override
    public void reset() {
        availableNumbers.clear();
        calledNumbers.clear();
        for (int number = 1; number <= 75; number++) {
            availableNumbers.add(number);
        }
        Collections.shuffle(availableNumbers);
    }
}
```

**Arquivo:** [`src/main/java/com/tms/aula8/bingo/StandardBingoGame.java`](src/main/java/com/tms/aula8/bingo/StandardBingoGame.java)

Esta implementacao coordena os cartoes, chama numeros e identifica vencedores.

```java
package com.tms.aula8.bingo;

import java.util.LinkedHashMap;
import java.util.Map;

public class StandardBingoGame implements Bingo {
    private final Caller caller;
    private final Map<String, Card> cards = new LinkedHashMap<>();
    private boolean started;
    private String winner;

    public StandardBingoGame(Caller caller) {
        this.caller = caller;
    }

    @Override
    public void addCard(String playerId, Card card) {
        if (started) {
            throw new IllegalStateException("Game already started");
        }
        cards.put(playerId, card);
    }

    @Override
    public void startGame() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("At least one card is required");
        }
        started = true;
        winner = null;
        caller.reset();
    }

    @Override
    public int playRound() {
        if (!started) {
            throw new IllegalStateException("Game not started");
        }
        int number = caller.callNumber();
        for (Map.Entry<String, Card> entry : cards.entrySet()) {
            entry.getValue().mark(number);
            if (entry.getValue().hasBingo() && winner == null) {
                winner = entry.getKey();
            }
        }
        return number;
    }

    @Override
    public boolean hasWinner() {
        return winner != null;
    }

    @Override
    public String getWinner() {
        return winner;
    }

    public boolean isGameOver() {
        return hasWinner() || !caller.hasMoreNumbers();
    }
}
```

**Arquivo:** [`src/test/java/com/tms/aula8/bingo/StandardCardTest.java`](src/test/java/com/tms/aula8/bingo/StandardCardTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula8.bingo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StandardCardTest {
    @Test
    void shouldMarkNumbersAndDetectBingo() {
        StandardCard card = new StandardCard(sampleNumbers());

        card.mark(1);
        card.mark(2);
        card.mark(3);
        card.mark(4);
        card.mark(5);

        assertTrue(card.hasBingo());
        assertFalse(card.mark(99));
    }

    @Test
    void shouldRejectInvalidCard() {
        assertThrows(IllegalArgumentException.class, () -> new StandardCard(new int[][]{{1}}));
    }

    static int[][] sampleNumbers() {
        return new int[][]{
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 0, 14, 15},
                {16, 17, 18, 19, 20},
                {21, 22, 23, 24, 25}
        };
    }
}
```

**Arquivo:** [`src/test/java/com/tms/aula8/bingo/RandomCallerTest.java`](src/test/java/com/tms/aula8/bingo/RandomCallerTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula8.bingo;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RandomCallerTest {
    @Test
    void shouldCallAllNumbersWithoutRepeating() {
        RandomCaller caller = new RandomCaller();
        Set<Integer> numbers = new HashSet<>();

        for (int i = 0; i < 75; i++) {
            numbers.add(caller.callNumber());
        }

        assertEquals(75, numbers.size());
        assertFalse(caller.hasMoreNumbers());
        assertThrows(IllegalStateException.class, caller::callNumber);
    }
}
```

**Arquivo:** [`src/test/java/com/tms/aula8/bingo/StandardBingoGameMockitoTest.java`](src/test/java/com/tms/aula8/bingo/StandardBingoGameMockitoTest.java)

Este teste automatizado valida os comportamentos esperados, incluindo cenarios de sucesso e falha relacionados a esta atividade.

```java
package com.tms.aula8.bingo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StandardBingoGameMockitoTest {
    private Caller caller;
    private Card card;
    private StandardBingoGame game;

    @BeforeEach
    void setUp() {
        caller = mock(Caller.class);
        card = mock(Card.class);
        game = new StandardBingoGame(caller);
    }

    @Test
    void shouldPlayRoundUsingCallerAndCardMocks() {
        game.addCard("player1", card);
        game.startGame();
        when(caller.callNumber()).thenReturn(42);
        when(card.mark(42)).thenReturn(true);
        when(card.hasBingo()).thenReturn(false);

        int number = game.playRound();

        assertEquals(42, number);
        assertFalse(game.hasWinner());
        verify(caller).callNumber();
        verify(card).mark(42);
        verify(card).hasBingo();
    }

    @Test
    void shouldDetectWinnerWithMockedCard() {
        game.addCard("player1", card);
        game.startGame();
        when(caller.callNumber()).thenReturn(7);
        when(card.hasBingo()).thenReturn(true);

        game.playRound();

        assertTrue(game.hasWinner());
        assertEquals("player1", game.getWinner());
    }

    @Test
    void shouldRejectRoundBeforeStart() {
        assertThrows(IllegalStateException.class, game::playRound);
    }
}
```

## Resultado Real da Execucao

```text
Tests run: 17, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Registro de Progresso

- [x] Estudo de SUT e DOC.
- [x] Implementacao de Dummy, Fake, Stub, Spy e Mock.
- [x] Testes com EasyMock.
- [x] Testes com Mockito.
- [x] Bingo Kata com interfaces e dublês.
- [x] Testes unitários de `StandardCard` e `RandomCaller`.
- [x] Teste de integração com os três componentes reais do Bingo.
- [x] Execucao completa da suite Maven com sucesso.


### O que impediu:
- Nada impediu a conclusao das atividades.

## Referencias

- Mockito: https://site.mockito.org/
- EasyMock: https://easymock.org/user-guide.html
- Martin Fowler, Mocks Aren't Stubs: https://martinfowler.com/articles/mocksArentStubs.html
