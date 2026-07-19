package com.tms.aula8.bingo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BingoIntegrationTest {
    @Test
    void shouldPlayCompleteGameWithRealComponents() {
        Caller caller = new RandomCaller();
        Card card = new StandardCard(new int[][]{
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 0, 14, 15},
                {16, 17, 18, 19, 20},
                {21, 22, 23, 24, 25}
        });
        StandardBingoGame game = new StandardBingoGame(caller);

        game.addCard("player1", card);
        game.startGame();

        int rounds = 0;
        while (!game.isGameOver()) {
            game.playRound();
            rounds++;
        }

        assertTrue(rounds > 0);
        assertTrue(rounds <= 75);
        assertEquals(rounds, caller.getCalledNumbers().size());
        assertTrue(game.hasWinner() || !caller.hasMoreNumbers());
    }
}
