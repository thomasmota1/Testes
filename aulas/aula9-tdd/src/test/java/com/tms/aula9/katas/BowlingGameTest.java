package com.tms.aula9.katas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BowlingGameTest {
    @Test
    void shouldScoreGutterGame() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 0);

        assertEquals(0, game.score());
    }

    @Test
    void shouldScoreAllOnes() {
        BowlingGame game = new BowlingGame();
        rollMany(game, 20, 1);

        assertEquals(20, game.score());
    }

    @Test
    void shouldScoreOneSpare() {
        BowlingGame game = new BowlingGame();
        game.roll(5);
        game.roll(5);
        game.roll(3);
        rollMany(game, 17, 0);

        assertEquals(16, game.score());
    }

    @Test
    void shouldScoreOneStrikeAndPerfectGame() {
        BowlingGame game = new BowlingGame();
        game.roll(10);
        game.roll(3);
        game.roll(4);
        rollMany(game, 16, 0);
        assertEquals(24, game.score());

        BowlingGame perfect = new BowlingGame();
        rollMany(perfect, 12, 10);
        assertEquals(300, perfect.score());
    }

    private static void rollMany(BowlingGame game, int rolls, int pins) {
        for (int i = 0; i < rolls; i++) {
            game.roll(pins);
        }
    }
}
