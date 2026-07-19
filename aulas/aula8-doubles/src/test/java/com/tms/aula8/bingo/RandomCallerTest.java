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
