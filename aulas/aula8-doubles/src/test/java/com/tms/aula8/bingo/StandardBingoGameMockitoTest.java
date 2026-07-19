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
    void shouldResetCallerWhenGameStarts() {
        game.addCard("player1", card);

        game.startGame();

        verify(caller).reset();
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

    @Test
    void shouldKeepFirstWinnerWhenSeveralCardsGetBingo() {
        Card secondCard = mock(Card.class);
        game.addCard("player1", card);
        game.addCard("player2", secondCard);
        game.startGame();

        when(caller.callNumber()).thenReturn(42);
        when(card.hasBingo()).thenReturn(true);
        when(secondCard.hasBingo()).thenReturn(true);

        game.playRound();

        assertEquals("player1", game.getWinner());
    }
}
