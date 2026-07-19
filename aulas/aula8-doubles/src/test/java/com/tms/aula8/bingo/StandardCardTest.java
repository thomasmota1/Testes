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
