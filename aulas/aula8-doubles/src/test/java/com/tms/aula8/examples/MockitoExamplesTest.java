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
