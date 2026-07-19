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
