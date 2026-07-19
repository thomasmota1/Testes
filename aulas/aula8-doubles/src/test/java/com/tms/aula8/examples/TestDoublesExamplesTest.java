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
            // Dummy object: intentionally unused.
        }
    }
}
