package com.tms.aula9.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TicTacToeTest {
    private TicTacToe game;

    @BeforeEach
    void setUp() {
        game = new TicTacToe();
    }

    @Test
    void shouldStartWithEmptyBoardAndPlayerX() {
        assertEquals('X', game.getCurrentPlayer());
        assertEquals(' ', game.getCell(0, 0));
    }

    @Test
    void shouldAlternatePlayersAndRejectOccupiedCell() {
        game.play(0, 0);

        assertEquals('O', game.getCurrentPlayer());
        assertThrows(IllegalStateException.class, () -> game.play(0, 0));
    }

    @Test
    void shouldDetectHorizontalWin() {
        game.play(0, 0);
        game.play(1, 0);
        game.play(0, 1);
        game.play(1, 1);
        game.play(0, 2);

        assertTrue(game.hasWinner());
        assertEquals('X', game.getWinner());
    }

    @Test
    @DisplayName("Detects vertical victory")
    void shouldDetectVerticalWin() {
        game.play(0, 0);
        game.play(0, 1);
        game.play(1, 0);
        game.play(1, 1);
        game.play(2, 0);

        assertTrue(game.hasWinner());
        assertEquals('X', game.getWinner());
    }

    @Test
    void shouldRejectMovesAfterWinner() {
        game.play(0, 0);
        game.play(1, 0);
        game.play(0, 1);
        game.play(1, 1);
        game.play(0, 2);

        assertThrows(IllegalStateException.class, () -> game.play(2, 2));
    }

    @Test
    void shouldDetectDiagonalWin() {
        game.play(0, 0);
        game.play(0, 1);
        game.play(1, 1);
        game.play(0, 2);
        game.play(2, 2);

        assertTrue(game.hasWinner());
        assertEquals('X', game.getWinner());
    }

    @Test
    void shouldDetectDraw() {
        game.play(0, 0);
        game.play(0, 1);
        game.play(0, 2);
        game.play(1, 1);
        game.play(1, 0);
        game.play(1, 2);
        game.play(2, 1);
        game.play(2, 0);
        game.play(2, 2);

        assertTrue(game.isDraw());
        assertFalse(game.hasWinner());
    }

    @Test
    void shouldRejectInvalidPosition() {
        assertThrows(IllegalArgumentException.class, () -> game.play(3, 0));
    }
}
